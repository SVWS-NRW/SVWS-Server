package de.svws_nrw.service.lehrer.fachrichtung;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtungAnerkennung;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.fachrichtung.LehrerFachrichtungMapper;
import de.svws_nrw.repo.lehrer.fachrichtung.LehrerLehramtFachrichtungRepository;
import de.svws_nrw.repo.lehrer.lehramt.LehrerLehramtRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

/**
 * Ein Service für den Zugriff auf die Fachrichtungen von Lehrern
 */
public final class LehrerFachrichtungService {

	private final LehrerLehramtFachrichtungRepository repo;
	private final LehrerLehramtRepository lehrerLehramtRepository;
	private final LehrerFachrichtungMapper mapper;

	/**
	 * constructor
	 * @param repo {@link LehrerLehramtFachrichtungRepository}
	 * @param lehrerLehramtRepository {@link LehrerLehramtRepository}
	 * @param mapper {@link LehrerFachrichtungMapper}
	 */
	public LehrerFachrichtungService(
			final LehrerLehramtFachrichtungRepository repo,
			final LehrerLehramtRepository lehrerLehramtRepository,
			final LehrerFachrichtungMapper mapper) {
		this.repo = repo;
		this.lehrerLehramtRepository = lehrerLehramtRepository;
		this.mapper = mapper;
	}

	/**
	 * Gibt die Liste aller Fachrichtungseinträge zurück.
	 *
	 * @return Liste aller Fachrichtungseinträge.
	 */
	public List<LehrerFachrichtungEintrag> getAll() {
		final var entities = repo.getAll();
		return entities.stream()
				.map(mapper::toApi)
				.toList();
	}

	/**
	 * Gibt eine Map mit der Zuordnung der Fachrichtungen zu den Lehrämtern mit den übergebenen IDs zurück.
	 *
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Zuordnung
	 */
	public Map<Long, List<LehrerFachrichtungEintrag>> getLehrerFachrichtungenByIdLehramt(final Collection<Long> idsLehraemter) {
		return repo.getLehrerFachrichtungenByIdLehramt(idsLehraemter)
				.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> entry.getValue().stream().map(mapper::toApi).toList()
				));
	}

	/**
	 * Gibt alle Fachrichtungen zum Lehramt mit der angegebenen ID zurück.
	 *
	 * @param idLehramt   die ID des Lehramtes
	 * @return Liste der zugehörigen {@link LehrerFachrichtungEintrag}-Objekte, leer wenn keine vorhanden
	 */
	public List<LehrerFachrichtungEintrag> getByIdLehramt(final long idLehramt) {
		return repo.getByLehramtId(idLehramt).stream()
				.map(mapper::toApi)
				.toList();
	}

	/**
	 * Legt eine neue LehrerFachrichtung an.
	 * Validiert vor dem Anlegen die Existenz der referenzierten Ids.
	 *
	 * @param dto der {@link LehrerFachrichtungCreateRequest} mit den Pflichtfeldern
	 * @return der {@link LehrerFachrichtungEintrag} der neu angelegten LehrerFachrichtung
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn die Ids unbekannt sind
	 */
	public LehrerFachrichtungEintrag create(final LehrerFachrichtungCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var lehrerfachrichtung = mapper.toDomain(dto);
			final var created = repo.create(lehrerfachrichtung);
			return mapper.toApi(created);
		});
	}

	/**
	 * Aktualisiert eine bestehende LehrerFachrichtung partiell anhand der im {@link LehrerFachrichtungPatchRequest}
	 * gesetzten Felder. Felder mit {@code undefined}-Wert bleiben unverändert.
	 *
	 * @param id  die ID
	 * @param dto der {@link LehrerFachrichtungPatchRequest}
	 * @return der aktualisierte {@link LehrerFachrichtungEintrag}
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND} wenn kein Ort zur {@code id} existiert,
	 *                               mit {@code 400 BAD_REQUEST} wenn die Ids unbekannt sind
	 */
	public LehrerFachrichtungEintrag patch(final long id, final LehrerFachrichtungPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repo.getById(id);
			validatePatch(dto);
			mapper.patch(dto, entity);
			return this.mapper.toApi(entity);
		});
	}

	/**
	 * Löscht die LehrerFachrichtung mit den angegebenen IDs.
	 * Nicht gefundene IDs werden stillschweigend ignoriert.
	 * Jeder Eintrag in der Rückgabeliste enthält die ID und ob die Löschung erfolgreich war.
	 *
	 * @param idsToDelete Liste der zu löschenden LehrerFachrichtung-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() -> {
			final var entitiesToDelete = repo.findListByIds(idsToDelete);

			return repo.delete(entitiesToDelete)
					.stream()
					.map(merkmal -> SimpleOperationResponse.ofSuccess(merkmal.id))
					.sorted(Comparator.comparingLong(response -> response.id))
					.toList();
		});
	}

	private void validatePatch(final LehrerFachrichtungPatchRequest dto) {
		dto.idLehramt.ifPresent(this::validateIdLehramt);
		dto.idFachrichtung.ifPresent(this::validateIdFachrichtung);
		dto.idAnerkennungsgrund.ifPresent(this::validateIdAnerkennungsgrund);
	}

	private void validateCreate(final LehrerFachrichtungCreateRequest dto) {
		validateIdLehramt(dto.idLehramt);
		validateIdFachrichtung(dto.idFachrichtung);
		if (dto.idAnerkennungsgrund != null) {
			validateIdAnerkennungsgrund(dto.idAnerkennungsgrund);
		}
	}

	private void validateIdAnerkennungsgrund(final @NotNull Long idAnerkennungsgrund) {
		if (LehrerFachrichtungAnerkennung.data().getEintragByID(idAnerkennungsgrund) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Anerkennungsgrund für die ID %d gefunden.".formatted(idAnerkennungsgrund));
		}
	}

	private void validateIdFachrichtung(final @NotNull Long idFachrichtung) {
		if (LehrerFachrichtung.data().getEintragByID(idFachrichtung) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Keine LehrerFachrichtung für die ID %d gefunden.".formatted(idFachrichtung));
		}
	}

	private void validateIdLehramt(final @NotNull Long idLehramt) {
		if (!lehrerLehramtRepository.existsById(idLehramt)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Lehramt für die ID %d gefunden.".formatted(idLehramt));
		}
	}


}
