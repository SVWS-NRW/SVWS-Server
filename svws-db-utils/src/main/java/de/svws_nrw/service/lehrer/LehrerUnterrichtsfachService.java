package de.svws_nrw.service.lehrer;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerUnterrichtsfach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.unterrichtsfach.LehrerUnterrichtsfachRepository;
import jakarta.ws.rs.core.Response.Status;


/**
 * Ein Service für den Zugriff auf die Unterrichtsfächer von Lehrern
 */
public final class LehrerUnterrichtsfachService {

	private final LehrerUnterrichtsfachRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository für den Datenbank-Zugriff
	 */
	public LehrerUnterrichtsfachService(final LehrerUnterrichtsfachRepository repository) {
		this.repository = repository;
	}


	private static LehrerUnterrichtsfach toApi(final DTOLehrerUnterrichtsfach dto) {
		final var daten = new LehrerUnterrichtsfach();
		daten.id = dto.ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.idFach = dto.Fach_ID;
		daten.istSek1 = Boolean.TRUE.equals(dto.IstSek1);
		daten.istSek2 = Boolean.TRUE.equals(dto.IstSek2);
		daten.bemerkung = dto.Bemerkung;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}


	/**
	 * Ermittelt das Unterrichtsfach anhand der übergebenen ID.
	 *
	 * @param id   die ID des Unterrichtsfach-Eintrags
	 *
	 * @return das Unterrichtsfach
	 */
	public LehrerUnterrichtsfach get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}


	/**
	 * Ermittelt die Unterrichtsfächer anhand der übergebenen IDs.
	 *
	 * @param ids   die IDs der Unterrichtsfach-Einträge
	 *
	 * @return die Unterrichtsfächer
	 */
	public List<LehrerUnterrichtsfach> getList(final Collection<Long> ids) {
		final List<DTOLehrerUnterrichtsfach> entities = repository.findListByIds(new ArrayList<>(ids));
		return entities.stream().map(LehrerUnterrichtsfachService::toApi).toList();
	}


	/**
	 * Ermittelt die Unterrichtsfächer für den Lehrer mit der übergebenen ID.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Unterrichtsfächer des Lehrers
	 */
	public List<LehrerUnterrichtsfach> getListByLehrerId(final long idLehrer) {
		final var dtos = repository.getListByLehrerId(idLehrer);
		return dtos.stream().map(LehrerUnterrichtsfachService::toApi).toList();
	}


	/**
	 * Ermittelt die Unterrichtsfächer für die Lehrer mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Unterrichtsfächer der Lehrer
	 */
	public List<LehrerUnterrichtsfach> getListByLehrerIds(final Collection<Long> idsLehrer) {
		final var dtos = repository.getListByLehrerIds(idsLehrer);
		return dtos.stream().map(LehrerUnterrichtsfachService::toApi).toList();
	}


	/**
	 * Ermittelt die Unterrichtsfächer für die Lehrer mit den übergebenen IDs, gruppiert nach Lehrer-ID.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Zuordnung der Lehrer-IDs zu deren Unterrichtsfächern
	 */
	public Map<Long, List<LehrerUnterrichtsfach>> getMapByLehrerIds(final Collection<Long> idsLehrer) {
		final var map = repository.getMapByLehrerIds(idsLehrer);
		final Map<Long, List<LehrerUnterrichtsfach>> result = new java.util.HashMap<>();
		for (final var entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().stream().map(LehrerUnterrichtsfachService::toApi).toList());
		}
		return result;
	}


	/**
	 * Erstellt ein neues Unterrichtsfach.
	 *
	 * @param createRequest   die Daten für den neuen Eintrag
	 *
	 * @return das neue Unterrichtsfach
	 */
	public LehrerUnterrichtsfach create(final LehrerUnterrichtsfachCreateRequest createRequest) {
		return transactional(() -> {
			final var dto = new DTOLehrerUnterrichtsfach(0, createRequest.idLehrer, createRequest.idFach,
					createRequest.istSek1, createRequest.istSek2);
			dto.Bemerkung = createRequest.bemerkung;
			dto.GueltigVon = createRequest.gueltigVon;
			dto.GueltigBis = createRequest.gueltigBis;
			final var created = repository.create(dto);
			return toApi(created);
		});
	}


	/**
	 * Führt einen Patch auf dem Unterrichtsfach mit der angegebenen ID aus.
	 *
	 * @param id      die ID des Unterrichtsfach-Eintrags
	 * @param patch   der Patch
	 *
	 * @return das gepatchte Unterrichtsfach
	 */
	public LehrerUnterrichtsfach patch(final long id, final LehrerUnterrichtsfachPatchRequest patch) {
		return transactional(() -> {
			final var entities = repository.findListByIds(List.of(id));
			if (entities.isEmpty()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag mit der ID %d gefunden.".formatted(id));
			}
			final var entity = entities.getFirst();
			patch.istSek1.ifPresent(val -> entity.IstSek1 = val);
			patch.istSek2.ifPresent(val -> entity.IstSek2 = val);
			patch.bemerkung.ifPresent(val -> entity.Bemerkung = val);
			patch.gueltigVon.ifPresent(val -> entity.GueltigVon = val);
			patch.gueltigBis.ifPresent(val -> entity.GueltigBis = val);
			repository.update(entity);
			repository.flush();
			return toApi(entity);
		});
	}


	/**
	 * Löscht das Unterrichtsfach mit der angegebenen ID.
	 *
	 * @param id   die ID des Unterrichtsfach-Eintrags
	 *
	 * @return das gelöschte Unterrichtsfach
	 */
	public LehrerUnterrichtsfach delete(final long id) {
		return transactional(() -> {
			final var entities = repository.findListByIds(List.of(id));
			if (entities.isEmpty()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag mit der ID %d gefunden.".formatted(id));
			}
			final var entity = entities.getFirst();
			final var result = toApi(entity);
			repository.delete(List.of(entity));
			return result;
		});
	}

}
