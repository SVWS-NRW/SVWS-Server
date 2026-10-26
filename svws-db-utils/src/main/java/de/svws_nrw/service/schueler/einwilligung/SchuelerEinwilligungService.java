package de.svws_nrw.service.schueler.einwilligung;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerEinwilligung;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutzPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.einwilligung.SchuelerEinwilligungMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.einwilligung.SchuelerEinwilligungRepository;
import de.svws_nrw.repo.schule.kataloge.einwilligungsart.EinwilligungsartRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dieser Service bündelt die Fachlogik für die Einwilligungen von Schülern zu den Einwilligungsarten.
 */
public final class SchuelerEinwilligungService {

	private final SchuelerEinwilligungRepository repository;
	private final SchuelerRepository schuelerRepository;
	private final EinwilligungsartRepository einwilligungsartRepository;
	private final SchuelerEinwilligungMapper mapper;

	/**
	 * Erstellt einen neuen {@link SchuelerEinwilligungService}.
	 *
	 * @param repository                   das Repository für die Einwilligungen von Schülern
	 * @param schuelerRepository           das Repository für die Schüler
	 * @param einwilligungsartRepository   das Repository für den Einwilligungsarten-Katalog
	 * @param mapper                       der Mapper zwischen Datenbank-DTO und Core-DTO
	 */
	public SchuelerEinwilligungService(final SchuelerEinwilligungRepository repository, final SchuelerRepository schuelerRepository,
			final EinwilligungsartRepository einwilligungsartRepository, final SchuelerEinwilligungMapper mapper) {
		this.repository = repository;
		this.schuelerRepository = schuelerRepository;
		this.einwilligungsartRepository = einwilligungsartRepository;
		this.mapper = mapper;
	}

	/**
	 * Bestimmt alle Einwilligungen eines Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Einwilligungen des Schülers, leer wenn keine vorhanden sind
	 */
	public List<SchuelerEinwilligung> getListByIdSchueler(final long idSchueler) {
		return repository.getListByIdSchueler(idSchueler).stream().map(mapper::toApi).toList();
	}

	/**
	 * Aktualisiert eine Einwilligung teilweise (PATCH). Nur die im Request gesetzten Felder werden übernommen.
	 * Die Operation läuft in einer Transaktion.
	 *
	 * @param idSchueler           die ID des Schülers
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 * @param request              die zu ändernden Felder
	 *
	 * @return die aktualisierte Einwilligung
	 *
	 * @throws ApiOperationException wenn keine Einwilligung für die Kombination aus Schüler und Einwilligungsart existiert
	 */
	public SchuelerEinwilligung patch(final long idSchueler, final long idEinwilligungsart, final SchuelerEinwilligungPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final DTOSchuelerDatenschutz entity = getEntityOrThrow(idSchueler, idEinwilligungsart);
			mapper.patch(request, entity);
			return mapper.toApi(entity);
		});
	}

	/**
	 * Legt für einen Schüler zu jeder Einwilligungsart für Schüler eine Einwilligung an, die weder erteilt noch abgefragt ist.
	 * Bereits vorhandene Einwilligungen bleiben unverändert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die neu angelegten Einwilligungen, leer wenn der Schüler bereits zu allen Einwilligungsarten eine Einwilligung hat
	 *
	 * @throws ApiOperationException wenn der Schüler nicht existiert
	 */
	public List<SchuelerEinwilligung> addForSchueler(final long idSchueler) {
		return TransactionSupport.transactional(() -> {
			assertSchuelerExists(idSchueler);
			final List<Long> idsMissing = filterMissing(einwilligungsartRepository.getAllIdsByPersonTyp(PersonTyp.SCHUELER),
					getIdsEinwilligungsartenOfSchueler(idSchueler));
			return createAll(idsMissing.stream().map(idEinwilligungsart -> mapper.toDomain(idSchueler, idEinwilligungsart)).toList());
		});
	}

	/**
	 * Legt für eine Einwilligungsart zu jedem Schüler eine Einwilligung an, die weder erteilt noch abgefragt ist.
	 * Bereits vorhandene Einwilligungen bleiben unverändert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 *
	 * @return die neu angelegten Einwilligungen, leer wenn alle Schüler bereits eine Einwilligung zu der Einwilligungsart haben
	 *
	 * @throws ApiOperationException wenn die Einwilligungsart nicht existiert oder nicht für Schüler gilt
	 */
	public List<SchuelerEinwilligung> addForEinwilligungsart(final long idEinwilligungsart) {
		return TransactionSupport.transactional(() -> {
			assertEinwilligungsartExists(idEinwilligungsart);
			final List<Long> idsMissing = filterMissing(schuelerRepository.getAllIds(), getIdsSchuelerOfEinwilligungsart(idEinwilligungsart));
			return createAll(idsMissing.stream().map(idSchueler -> mapper.toDomain(idSchueler, idEinwilligungsart)).toList());
		});
	}

	private DTOSchuelerDatenschutz getEntityOrThrow(final long idSchueler, final long idEinwilligungsart) {
		return repository.findById(new DTOSchuelerDatenschutzPK(idSchueler, idEinwilligungsart))
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
						"Eine Einwilligung mit SchuelerID %d und der EinwilligungsartID %d wurde nicht gefunden."
								.formatted(idSchueler, idEinwilligungsart)));
	}

	private void assertSchuelerExists(final long idSchueler) {
		if (!schuelerRepository.existsById(idSchueler)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Schüler mit der ID %d wurde nicht gefunden.".formatted(idSchueler));
		}
	}

	private void assertEinwilligungsartExists(final long idEinwilligungsart) {
		if (!einwilligungsartRepository.existsByIdAndPersonTyp(idEinwilligungsart, PersonTyp.SCHUELER)) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Einwilligungsart mit der ID %d wurde für Schüler nicht gefunden.".formatted(idEinwilligungsart));
		}
	}

	private Set<Long> getIdsEinwilligungsartenOfSchueler(final long idSchueler) {
		return repository.getListByIdSchueler(idSchueler).stream().map(e -> e.Datenschutz_ID).collect(Collectors.toSet());
	}

	private Set<Long> getIdsSchuelerOfEinwilligungsart(final long idEinwilligungsart) {
		return repository.getListByIdEinwilligungsart(idEinwilligungsart).stream().map(e -> e.Schueler_ID).collect(Collectors.toSet());
	}

	private static List<Long> filterMissing(final Collection<Long> idsAll, final Set<Long> idsExisting) {
		return idsAll.stream().filter(id -> !idsExisting.contains(id)).toList();
	}

	private List<SchuelerEinwilligung> createAll(final List<DTOSchuelerDatenschutz> entities) {
		if (entities.isEmpty()) {
			return List.of();
		}
		repository.create(entities);
		return entities.stream().map(mapper::toApi).toList();
	}

}
