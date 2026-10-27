package de.svws_nrw.service.lehrer.einwilligung;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.lehrer.LehrerEinwilligung;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutzPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.einwilligung.LehrerEinwilligungMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.einwilligung.LehrerEinwilligungRepository;
import de.svws_nrw.repo.schule.kataloge.einwilligungsart.EinwilligungsartRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dieser Service bündelt die Fachlogik für die Einwilligungen von Lehrern zu den Einwilligungsarten.
 */
public final class LehrerEinwilligungService {

	private final LehrerEinwilligungRepository repository;
	private final LehrerRepository lehrerRepository;
	private final EinwilligungsartRepository einwilligungsartRepository;
	private final LehrerEinwilligungMapper mapper;

	/**
	 * Erstellt einen neuen {@link LehrerEinwilligungService}.
	 *
	 * @param repository                   das Repository für die Einwilligungen von Lehrern
	 * @param lehrerRepository           das Repository für die Lehrer
	 * @param einwilligungsartRepository   das Repository für den Einwilligungsarten-Katalog
	 * @param mapper                       der Mapper zwischen Datenbank-DTO und Core-DTO
	 */
	public LehrerEinwilligungService(final LehrerEinwilligungRepository repository, final LehrerRepository lehrerRepository,
			final EinwilligungsartRepository einwilligungsartRepository, final LehrerEinwilligungMapper mapper) {
		this.repository = repository;
		this.lehrerRepository = lehrerRepository;
		this.einwilligungsartRepository = einwilligungsartRepository;
		this.mapper = mapper;
	}

	/**
	 * Bestimmt alle Einwilligungen eines Lehrers.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Einwilligungen des Lehrers, leer wenn keine vorhanden sind
	 */
	public List<LehrerEinwilligung> getListByIdLehrer(final long idLehrer) {
		return repository.getListByIdLehrer(idLehrer).stream().map(mapper::toApi).toList();
	}

	/**
	 * Aktualisiert eine Einwilligung teilweise (PATCH). Nur die im Request gesetzten Felder werden übernommen.
	 * Die Operation läuft in einer Transaktion.
	 *
	 * @param idLehrer           die ID des Lehrers
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 * @param request              die zu ändernden Felder
	 *
	 * @return die aktualisierte Einwilligung
	 *
	 * @throws ApiOperationException wenn keine Einwilligung für die Kombination aus Lehrer und Einwilligungsart existiert
	 */
	public LehrerEinwilligung patch(final long idLehrer, final long idEinwilligungsart, final LehrerEinwilligungPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final DTOLehrerDatenschutz entity = getEntityOrThrow(idLehrer, idEinwilligungsart);
			mapper.patch(request, entity);
			return mapper.toApi(entity);
		});
	}

	/**
	 * Legt für einen Lehrer zu jeder Einwilligungsart für Lehrer eine Einwilligung an, die weder erteilt noch abgefragt ist.
	 * Bereits vorhandene Einwilligungen bleiben unverändert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die neu angelegten Einwilligungen, leer wenn der Lehrer bereits zu allen Einwilligungsarten eine Einwilligung hat
	 *
	 * @throws ApiOperationException wenn der Lehrer nicht existiert
	 */
	public List<LehrerEinwilligung> addForLehrer(final long idLehrer) {
		return TransactionSupport.transactional(() -> {
			assertLehrerExists(idLehrer);
			final List<Long> idsMissing = filterMissing(einwilligungsartRepository.getAllIdsByPersonTyp(PersonTyp.LEHRER),
					getIdsEinwilligungsartenOfLehrer(idLehrer));
			return createAll(idsMissing.stream().map(idEinwilligungsart -> mapper.toDomain(idLehrer, idEinwilligungsart)).toList());
		});
	}

	/**
	 * Legt für eine Einwilligungsart zu jedem Lehrer eine Einwilligung an, die weder erteilt noch abgefragt ist.
	 * Bereits vorhandene Einwilligungen bleiben unverändert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 *
	 * @return die neu angelegten Einwilligungen, leer wenn alle Lehrer bereits eine Einwilligung zu der Einwilligungsart haben
	 *
	 * @throws ApiOperationException wenn die Einwilligungsart nicht existiert oder nicht für Lehrer gilt
	 */
	public List<LehrerEinwilligung> addForEinwilligungsart(final long idEinwilligungsart) {
		return TransactionSupport.transactional(() -> {
			assertEinwilligungsartExists(idEinwilligungsart);
			final List<Long> idsMissing = filterMissing(lehrerRepository.getAllIds(), getIdsLehrerOfEinwilligungsart(idEinwilligungsart));
			return createAll(idsMissing.stream().map(idLehrer -> mapper.toDomain(idLehrer, idEinwilligungsart)).toList());
		});
	}

	private DTOLehrerDatenschutz getEntityOrThrow(final long idLehrer, final long idEinwilligungsart) {
		return repository.findById(new DTOLehrerDatenschutzPK(idLehrer, idEinwilligungsart))
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
						"Eine Einwilligung mit LehrerID %d und der EinwilligungsartID %d wurde nicht gefunden."
								.formatted(idLehrer, idEinwilligungsart)));
	}

	private void assertLehrerExists(final long idLehrer) {
		if (!lehrerRepository.existsById(idLehrer)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d wurde nicht gefunden.".formatted(idLehrer));
		}
	}

	private void assertEinwilligungsartExists(final long idEinwilligungsart) {
		if (!einwilligungsartRepository.existsByIdAndPersonTyp(idEinwilligungsart, PersonTyp.LEHRER)) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Einwilligungsart mit der ID %d wurde für Lehrer nicht gefunden.".formatted(idEinwilligungsart));
		}
	}

	private Set<Long> getIdsEinwilligungsartenOfLehrer(final long idLehrer) {
		return repository.getListByIdLehrer(idLehrer).stream().map(e -> e.DatenschutzID).collect(Collectors.toSet());
	}

	private Set<Long> getIdsLehrerOfEinwilligungsart(final long idEinwilligungsart) {
		return repository.getListByIdEinwilligungsart(idEinwilligungsart).stream().map(e -> e.LehrerID).collect(Collectors.toSet());
	}

	private static List<Long> filterMissing(final Collection<Long> idsAll, final Set<Long> idsExisting) {
		return idsAll.stream().filter(id -> !idsExisting.contains(id)).toList();
	}

	private List<LehrerEinwilligung> createAll(final List<DTOLehrerDatenschutz> entities) {
		if (entities.isEmpty()) {
			return List.of();
		}
		repository.create(entities);
		return entities.stream().map(mapper::toApi).toList();
	}

}
