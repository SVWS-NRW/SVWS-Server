package de.svws_nrw.service.lehrer.personaldaten;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.types.lehrer.LehrerAbgangsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.personaldaten.LehrerPersonaldatenMapper;
import de.svws_nrw.mapper.lehrer.personaldaten.LehrerPersonaldatenMappingContext;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtService;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenService;
import jakarta.ws.rs.core.Response;

/**
 * Service für den Zugriff und die Änderung von Personaldaten von Lehrkräften.
 *
 * <p>Der Service übernimmt insbesondere:</p>
 * <ul>
 *     <li>das Laden der Personaldaten einer Lehrkraft,</li>
 *     <li>das partielle Aktualisieren der Personaldaten einer Lehrkraft,</li>
 *     <li>das partielle Aktualisieren der Personaldaten mehrerer Lehrkräfte,</li>
 *     <li>die Validierung von Datumswerten sowie Zugangs- und Abgangsgründen.</li>
 * </ul>
 *
 * <p>Die eigentlichen Abbildungen zwischen Datenbankobjekten und API-Modellen
 * werden durch den {@link LehrerPersonaldatenMapper} durchgeführt.</p>
 */
public class LehrerPersonaldatenService {

	private final LehrerRepository repository;
	private final LehrerPersonaldatenMapper mapper;
	private final LehrerPersonalabschnittsdatenService personalabschnittsdatenService;
	private final LehrerLehramtService lehramtService;


	/**
	 * Erstellt einen neuen Service für Lehrkräfte-Personaldaten.
	 *
	 * @param repository Repository für den Zugriff auf Lehrkräfte
	 * @param mapper Mapper für die Abbildung der Personaldaten
	 * @param personalabschnittsdatenService Service für Personalabschnittsdaten
	 * @param lehramtService Service für Lehrämter
	 */
	public LehrerPersonaldatenService(
			final LehrerRepository repository,
			final LehrerPersonaldatenMapper mapper,
			final LehrerPersonalabschnittsdatenService personalabschnittsdatenService,
			final LehrerLehramtService lehramtService
	) {
		this.repository = repository;
		this.mapper = mapper;
		this.personalabschnittsdatenService = personalabschnittsdatenService;
		this.lehramtService = lehramtService;
	}

	/**
	 * Lädt die Personaldaten einer Lehrkraft anhand ihrer ID.
	 *
	 * <p>Neben den direkten Personaldaten werden auch die zugehörigen
	 * Personalabschnittsdaten und Lehrämter geladen.</p>
	 *
	 * @param id ID der Lehrkraft
	 * @return die Personaldaten der Lehrkraft
	 * @throws ApiOperationException wenn keine Lehrkraft mit der angegebenen
	 *                               ID gefunden wurde
	 */
	public LehrerPersonaldaten getById(final Long id) {
		final var lehrer = repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, "Kein Lehrer zur ID %d gefunden".formatted(id)));
		final var personalabschnittsdaten = personalabschnittsdatenService.getByIdLehrer(lehrer.ID);
		final var lehraemter = lehramtService.getByIdLehrer(lehrer.ID);
		return toApi(lehrer, personalabschnittsdaten, lehraemter);
	}

	/**
	 * Aktualisiert die Personaldaten einer Lehrkraft partiell.
	 *
	 * <p>Nicht im Patch-Request angegebene Werte bleiben unverändert.
	 * Die Aktualisierung wird innerhalb einer Transaktion ausgeführt.</p>
	 *
	 * @param id ID der zu aktualisierenden Lehrkraft
	 * @param dto Patch-Request mit den zu aktualisierenden Werten
	 * @return die aktualisierten Personaldaten der Lehrkraft
	 * @throws ApiOperationException wenn die Lehrkraft nicht gefunden wurde
	 *                               oder die übermittelten Werte ungültig sind
	 */
	public LehrerPersonaldaten patch(final long id, final LehrerPersonaldatenPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var lehrer = repository.getById(id);
			validatePatch(dto);
			final var personalabschnittsdaten = personalabschnittsdatenService.getByIdLehrer(lehrer.ID);
			final var lehraemter = lehramtService.getByIdLehrer(lehrer.ID);
			mapper.patch(dto, lehrer);
			return toApi(lehrer, personalabschnittsdaten, lehraemter);
		});
	}

	/**
	 * Aktualisiert die Personaldaten mehrerer Lehrkräfte partiell.
	 *
	 * <p>Alle Patch-Requests werden zunächst validiert. Anschließend werden
	 * die betroffenen Lehrkräfte und deren abhängige Daten geladen. Die
	 * Änderungen werden innerhalb einer gemeinsamen Transaktion ausgeführt.</p>
	 *
	 * @param dtos Liste der Patch-Requests mit den IDs der zu aktualisierenden
	 *             Lehrkräfte
	 * @return Liste der aktualisierten Personaldaten in der Reihenfolge der
	 *         übergebenen Patch-Requests
	 * @throws ApiOperationException wenn mindestens eine Lehrkraft nicht
	 *                               gefunden wurde oder ein Patch-Request
	 *                               ungültige Werte enthält
	 */
	public List<LehrerPersonaldaten> patchMultiple(final List<LehrerPersonaldatenBatchPatchRequest> dtos) {
		return TransactionSupport.transactional(() -> {
			dtos.forEach(this::validatePatch);

			final var idsToPatch = dtos.stream()
					.map(dto -> dto.id)
					.toList();

			final var toPatchById = repository.findMapByIds(idsToPatch);

			final var missingIds = idsToPatch.stream()
					.filter(id -> !toPatchById.containsKey(id))
					.distinct()
					.toList();

			if (!missingIds.isEmpty()) {
				throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein Lehrer zu folgenden IDs gefunden: %s".formatted(missingIds));
			}

			final var lehrerIds = toPatchById.values().stream()
					.map(lehrer -> lehrer.ID)
					.distinct()
					.toList();

			final var personalabschnittsdatenByIdLehrer = personalabschnittsdatenService.getMapByIdLehrer(lehrerIds);
			final var lehraemterByIdLehrer = lehramtService.getMapByIdLehrer(lehrerIds);

			return dtos.stream()
					.map(dto -> {
						final var lehrer = toPatchById.get(dto.id);
						mapper.patch(dto, lehrer);
						final var personalabschnittsdaten = personalabschnittsdatenByIdLehrer.getOrDefault(lehrer.ID, List.of());
						final var lehraemter = lehraemterByIdLehrer.getOrDefault(lehrer.ID, List.of());
						return toApi(lehrer, personalabschnittsdaten, lehraemter);
					})
					.toList();
		});
	}

	private void validatePatch(final LehrerPersonaldatenPatchRequest dto) {
		dto.zugangsgrund.ifPresent(this::validateZugangsgrund);
		dto.abgangsgrund.ifPresent(this::validateAbgangsgrund);
	}

	private void validateZugangsgrund(final String kuerzel) {
		if (kuerzel == null) {
			return;
		}
		if (LehrerZugangsgrund.data().getWertByKuerzel(kuerzel) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein LehrerZugangsgrund mit dem Kuerzel %s gefunden".formatted(kuerzel));
		}
	}

	private void validateAbgangsgrund(final String kuerzel) {
		if (kuerzel == null) {
			return;
		}
		if (LehrerAbgangsgrund.data().getWertByKuerzel(kuerzel) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein LehrerAbgangsgrund mit dem Kuerzel %s gefunden".formatted(kuerzel));
		}
	}

	private LehrerPersonaldaten toApi(
			final DTOLehrer lehrer,
			final List<LehrerPersonalabschnittsdaten> personalabschnittsdaten,
			final List<LehrerLehramtEintrag> lehraemter
	) {
		return mapper.toApi(
				lehrer,
				new LehrerPersonaldatenMappingContext(personalabschnittsdaten, lehraemter)
		);
	}

}
