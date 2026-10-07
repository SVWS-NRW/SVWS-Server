package de.svws_nrw.service.schule.katalog.jahrgang;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Bildungsstufe;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.jahrgang.JahrgangMapper;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

public class JahrgangService {
	private final JahrgangRepository repository;
	private final JahrgangMapper mapper;
	private final EigeneSchuleService eigeneSchuleService;

	private static final String KUERZEL_WIRD_BEREITS_VERWENDET = "Das Kürzel %s wird bereits verwendet";
	private static final String BEZEICHNUNG_WIRD_BEREITS_VERWENDET = "Die Bezeichnung %s wird bereits verwendet";
	private static final String ID_JAHRGANG_FEHLT = "Die ID des Jahrgangs muss angegeben werden.";
	private static final String KEIN_JAHRGANG_GEFUNDEN = "Kein Jahrgang mit der ID %d gefunden.";
	private static final String KEINE_BILDUNGSSTUFE_GEFUNDEN = "Keine Bildungsstufe zur ID %d gefunden.";
	private static final String KEINE_SCHULGLIEDERUNG_GEFUNDEN = "Keine Schulgliederung mit der ID %d gefunden.";
	private static final String SCHULGLIEDERUNG_NICHT_GUELTIG = "Die Schulgliederung ist für diese Schulform nicht gültig.";
	private static final String KEIN_FOLGEJAHRGANG_GEFUNDEN = "Ein Folgejahrgang mit der ID %d wurde nicht gefunden.";
	private static final String KEIN_JAHRGANG_ZUR_ID_GEFUNDEN = "Kein Jahrgang zur ID %d gefunden.";

	/**
	 * constructor
	 *
	 * @param repository {@link JahrgangRepository}
	 * @param mapper {@link JahrgangMapper}
	 * @param eigeneSchuleService {@link EigeneSchuleService}
	 */
	public JahrgangService(final JahrgangRepository repository, final JahrgangMapper mapper, final EigeneSchuleService eigeneSchuleService) {
		this.repository = repository;
		this.mapper = mapper;
		this.eigeneSchuleService = eigeneSchuleService;
	}

	/**
	 * Gibt einen Jahrgang zur ID zurück
	 *
	 * @param id id
	 *
	 * @return ein Jahrgang
	 */
	public JahrgangsDaten getById(final long id) {
		final var schuljahr = eigeneSchuleService.getSchuljahr();
		final var schulform = eigeneSchuleService.getSchulform();
		return repository.findById(id)
				.map(entity -> mapper.toApi(entity, schuljahr, schulform))
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, KEIN_JAHRGANG_ZUR_ID_GEFUNDEN.formatted(id)));
	}

	/**
	 * Gibt die Liste aller Jahrgänge aus dem Schulkatalog zurück.
	 *
	 * @return Liste aller Jahrgänge
	 */
	public List<JahrgangsDaten> getAll() {
		final var schuljahr = eigeneSchuleService.getSchuljahr();
		final var schulform = eigeneSchuleService.getSchulform();
		final var entities = repository.getAll();
		final var referencedIds = repository.getReferencedIds(
				entities.stream()
						.map(j -> j.ID)
						.toList()
		);

		return entities.stream()
				.map(e -> map(e, schuljahr, schulform, referencedIds))
				.sorted(Comparator.comparingLong(j -> j.id))
				.toList();
	}

	/**
	 * Legt einen neuen Jahrgang im schulinternen Katalog an.
	 * Validiert vor dem Anlegen die Eindeutigkeit von Kürzel und Bezeichnung, die Existenz der
	 * referenzierten CoreType-Einträge sowie die Existenz des angegebenen Folgejahrgangs.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param request der {@link JahrgangCreateRequest} mit den Daten des neuen Jahrgangs
	 *
	 * @return die {@link JahrgangsDaten} des neu angelegten Jahrgangs
	 *
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST}, wenn Kürzel oder Bezeichnung bereits
	 *                               vergeben sind, ein referenzierter CoreType-Eintrag unbekannt ist,
	 *                               die Schulgliederung nicht zur Schulform passt oder der Folgejahrgang nicht existiert
	 */
	public JahrgangsDaten create(final JahrgangCreateRequest request) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(request);
			final var jahrgang = mapper.toDomain(request);
			final var created = repository.create(jahrgang);
			final var schuljahr = eigeneSchuleService.getSchuljahr();
			final var schulform = eigeneSchuleService.getSchulform();
			return mapper.toApi(created, schuljahr, schulform);
		});
	}

	/**
	 * Aktualisiert den Jahrgang mit der angegebenen ID. Es werden ausschließlich die im Request
	 * gesetzten Felder verändert. Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param id      die ID des zu aktualisierenden Jahrgangs
	 * @param request der {@link JahrgangPatchRequest} mit den zu ändernden Feldern
	 *
	 * @return die aktualisierten {@link JahrgangsDaten}
	 *
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND}, wenn kein Jahrgang zu der ID existiert, und mit
	 *                               {@code 400 BAD_REQUEST}, wenn Kürzel oder Bezeichnung bereits von einem
	 *                               anderen Jahrgang verwendet werden, ein referenzierter CoreType-Eintrag
	 *                               unbekannt ist, die Schulgliederung nicht zur Schulform passt oder der
	 *                               Folgejahrgang nicht existiert
	 */
	public JahrgangsDaten patch(final long id, final JahrgangPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final var entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, KEIN_JAHRGANG_ZUR_ID_GEFUNDEN.formatted(id)));
			this.validatePatch(request, id);
			mapper.patch(request, entity);
			final var schuljahr = eigeneSchuleService.getSchuljahr();
			final var schulform = eigeneSchuleService.getSchulform();
			return mapper.toApi(entity, schuljahr, schulform);
		});
	}

	/**
	 * Löscht mehrere Jahrgänge anhand ihrer IDs.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param idsToDelete die Liste der IDs der zu löschenden Jahrgänge
	 * @return eine Liste von Antworten mit dem Status jeder Löschoperation, sortiert nach ID
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.deleteWithReferenceCheck(
						idsToDelete,
						repository,
						j -> j.ID,
						"Jahrgang"
				)
		);
	}

	private JahrgangsDaten map(final DTOJahrgang entity, final int schuljahr, final Schulform schulform, final Set<Long> referencedIds) {
		final var eintrag = mapper.toApi(entity, schuljahr, schulform);
		eintrag.referenziertInAnderenTabellen = referencedIds.contains(entity.ID);
		return eintrag;
	}

	private void validateCreate(final JahrgangCreateRequest request) {
		validateUniqueKuerzel(request.kuerzel, null);
		validateUniqueBezeichnung(request.bezeichnung, null);
		validateIdJahrgang(request.idJahrgang);
		validateIdSchulgliederung(request.idSchulgliederung);
		validateIdBildungsstufe(request.idBildungsstufe);
		validateIdFolgejahrgang(request.idFolgejahrgang);
	}

	private void validatePatch(final JahrgangPatchRequest request, final long id) {
		request.kuerzel.ifPresent(kuerzel -> validateUniqueKuerzel(kuerzel, id));
		request.bezeichnung.ifPresent(bezeichnung -> validateUniqueBezeichnung(bezeichnung, id));
		request.idJahrgang.ifPresent(this::validateIdJahrgang);
		request.idSchulgliederung.ifPresent(this::validateIdSchulgliederung);
		request.idBildungsstufe.ifPresent(this::validateIdBildungsstufe);
		request.idFolgejahrgang.ifPresent(this::validateIdFolgejahrgang);
	}

	private void validateUniqueKuerzel(final String kuerzel, final Long idToExclude) {
		final var exists = (idToExclude == null)
				? repository.kuerzelIsAlreadyUsedCreate(kuerzel)
				: repository.kuerzelIsAlreadyUsedPatch(kuerzel, idToExclude);

		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KUERZEL_WIRD_BEREITS_VERWENDET.formatted(kuerzel));
		}
	}

	private void validateUniqueBezeichnung(final String bezeichnung, final Long idToExclude) {
		final var exists = (idToExclude == null)
				? repository.bezeichnungIsAlreadyUsedCreate(bezeichnung)
				: repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, idToExclude);

		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, BEZEICHNUNG_WIRD_BEREITS_VERWENDET.formatted(bezeichnung));
		}
	}

	private void validateIdJahrgang(final Long idJahrgang) {
		if (idJahrgang == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ID_JAHRGANG_FEHLT);
		}

		if (Jahrgaenge.data().getEintragByID(idJahrgang) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KEIN_JAHRGANG_GEFUNDEN.formatted(idJahrgang));
		}
	}

	private void validateIdBildungsstufe(final Long idBildungsstufe) {
		if (idBildungsstufe == null) {
			return;
		}

		if (Bildungsstufe.data().getEintragByID(idBildungsstufe) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KEINE_BILDUNGSSTUFE_GEFUNDEN.formatted(idBildungsstufe));
		}
	}

	private void validateIdSchulgliederung(final Long idSchulgliederung) {
		if (idSchulgliederung == null) {
			return;
		}

		if (Schulgliederung.data().getEintragByID(idSchulgliederung) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KEINE_SCHULGLIEDERUNG_GEFUNDEN.formatted(idSchulgliederung));
		}

		final var schuljahr = eigeneSchuleService.getSchuljahr();
		final var schulform = eigeneSchuleService.getSchulform();
		if (!Schulgliederung.data().getWertByID(idSchulgliederung).hatSchulform(schuljahr, schulform)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, SCHULGLIEDERUNG_NICHT_GUELTIG);
		}
	}


	private void validateIdFolgejahrgang(final Long idFolgejahrgang) {
		if (idFolgejahrgang == null) {
			return;
		}

		if (!repository.existsById(idFolgejahrgang)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KEIN_FOLGEJAHRGANG_GEFUNDEN.formatted(idFolgejahrgang));
		}
	}
}
