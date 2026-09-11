package de.svws_nrw.service.uv.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerUnterrichtsfach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lehrer.UvLehrerUnterrichtsfachRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die Unterrichtsfächer der UV-Lehrkräfte
 * (Tabelle UV_LehrerUnterrichtsfaecher).
 */
public final class UvLehrerUnterrichtsfachService {

	private final UvLehrerUnterrichtsfachRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvLehrerUnterrichtsfaecherRepository   das Repository für die Unterrichtsfächer der UV-Lehrkräfte
	 */
	public UvLehrerUnterrichtsfachService(final UvLehrerUnterrichtsfachRepository uvLehrerUnterrichtsfaecherRepository) {
		this.repository = uvLehrerUnterrichtsfaecherRepository;
	}

	private static LehrerUnterrichtsfach toApi(final DTOUvLehrerUnterrichtsfach dto) {
		final var daten = new LehrerUnterrichtsfach();
		daten.istKLehrer = false;
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
	 * Ermittelt das Unterrichtsfach eines UV-Lehrers anhand der übergebenen ID.
	 * @param id   die ID des Eintrags
	 * @return das Unterrichtsfach
	 */
	public LehrerUnterrichtsfach get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurde kein UvLehrerUnterrichtsfach mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Unterrichtsfächer der UV-Lehrkräfte anhand der übergebenen IDs.
	 * @param ids   die IDs der Einträge
	 * @return die Liste der Unterrichtsfächer
	 */
	public List<LehrerUnterrichtsfach> getList(final Collection<Long> ids) {
		final List<DTOUvLehrerUnterrichtsfach> entities = repository.findListByIds(new ArrayList<>(ids));
		if (entities.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvLehrer zu den IDs gefunden (%d von %d).".formatted(entities.size(), ids.size()));
		}
		return entities.stream().map(UvLehrerUnterrichtsfachService::toApi).toList();
	}

	/**
	 * Liefert alle Unterrichtsfächer eines bestimmten UV-Lehrers.
	 *
	 * @param idLehrer   die ID des UV-Lehrers
	 * @return die Liste der Unterrichtsfächer
	 */
	public List<LehrerUnterrichtsfach> getListByLehrerId(final long idLehrer) {
		final var dtos = repository.getListByLehrerId(idLehrer);
		return dtos.stream().map(UvLehrerUnterrichtsfachService::toApi).toList();
	}

	/**
	 * Liefert alle Unterrichtsfächer für die übergebenen UV-Lehrer-IDs.
	 *
	 * @param idsLehrer   die IDs der UV-Lehrer
	 * @return die Liste der Unterrichtsfächer
	 */
	public List<LehrerUnterrichtsfach> getListByLehrerIds(final Collection<Long> idsLehrer) {
		final var dtos = repository.getListByLehrerIds(idsLehrer);
		return dtos.stream().map(UvLehrerUnterrichtsfachService::toApi).toList();
	}

	/**
	 * Liefert die Unterrichtsfächer für die übergebenen UV-Lehrer-IDs gruppiert nach UV-Lehrer-ID.
	 *
	 * @param idsLehrer   die IDs der UV-Lehrer
	 * @return die Map mit den Unterrichtsfächern, gruppiert nach UV-Lehrer-ID
	 */
	public Map<Long, List<LehrerUnterrichtsfach>> getMapByLehrerIds(final Collection<Long> idsLehrer) {
		final var map = repository.getMapByLehrerIds(idsLehrer);
		final Map<Long, List<LehrerUnterrichtsfach>> result = new java.util.HashMap<>();
		for (final var entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().stream().map(UvLehrerUnterrichtsfachService::toApi).toList());
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
	public LehrerUnterrichtsfach create(final UvLehrerUnterrichtsfachCreateRequest createRequest) {
		return transactional(() -> {
			final var dto = new DTOUvLehrerUnterrichtsfach(0, createRequest.idLehrer, createRequest.idFach,
					createRequest.istSek1, createRequest.istSek2);
			dto.Bemerkung = createRequest.bemerkung;
			dto.GueltigVon = createRequest.gueltigVon;
			dto.GueltigBis = createRequest.gueltigBis;
			checkBeforePersist(dto);
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
	public LehrerUnterrichtsfach patch(final long id, final UvLehrerUnterrichtsfachPatchRequest patch) {
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
			checkBeforePersist(entity);
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

	private void checkBeforePersist(final DTOUvLehrerUnterrichtsfach dto) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
	}

}
