package de.svws_nrw.service.uv.kurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvKursCreateRequest;
import de.svws_nrw.db.dto.current.uv.DTOUvKurs;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.kurse.UvKursRepository;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppeRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Kurse (Tabelle UV_Kurse).
 */
public final class UvKursService {

	private final UvKursRepository repository;
	private final UvLerngruppeRepository lerngruppeRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvKursRepository das Kurs-Repository
	 * @param lerngruppeRepository das Lerngruppen-Repository
	 */
	public UvKursService(final UvKursRepository uvKursRepository, final UvLerngruppeRepository lerngruppeRepository) {
		this.repository = uvKursRepository;
		this.lerngruppeRepository = lerngruppeRepository;
	}


	private static UvKurs toApi(final DTOUvKurs dto) {
		final var daten = new UvKurs();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		daten.idFach = dto.Fach_ID;
		daten.kursart = dto.Kursart;
		daten.kursnummer = dto.Kursnummer;
		daten.idSchuelergruppe = dto.Schuelergruppe_ID;
		return daten;
	}

	/**
	 * Ermittelt den Kurs anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return der Kurs
	 */
	public UvKurs get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein UvKurs mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Kurse anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste
	 */
	public List<UvKurs> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvKurse zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvKursService::toApi).toList();
	}

	/**
	 * Liefert alle Kurse eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Kurse
	 */
	public List<UvKurs> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvKursService::toApi).toList();
	}

	/**
	 * Erstellt einen neuen Kurs.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return der neue Kurs
	 */
	public UvKurs create(final UvKursCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Kurse.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Kurse
	 */
	public List<UvKurs> createMultiple(final Collection<UvKursCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvKurs> entities = new ArrayList<>();
			for (final UvKursCreateRequest request : createRequests) {
				entities.add(buildNewDto(nextId++, request));
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	private static DTOUvKurs buildNewDto(final long id, final UvKursCreateRequest request) {
		return new DTOUvKurs(id, request.idPlanungsabschnitt, request.idSchuljahresabschnitt,
				request.idFach, request.kursart, request.kursnummer, request.idSchuelergruppe);
	}

	/**
	 * Führt einen Patch auf einem Kurs aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return der gepatchte Kurs
	 */
	public UvKurs patch(final UvKursPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Kursen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten Kurse
	 */
	public List<UvKurs> patchMultiple(final Collection<UvKursPatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final var ids = patches.stream().map(p -> p.id).toList();
			final var entities = repository.findListByIds(ids);
			if (entities.size() != patches.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle angefragten Datensätze konnten gefunden werden.");
			}
			for (final var p : patches) {
				final var entity = repository.getById(p.id);
				applyPatch(entity, p);
			}
			repository.update(entities);
			repository.flush();
			return getList(ids);
		});
	}

	private static void applyPatch(final DTOUvKurs dto, final UvKursPatchRequest patch) {
		patch.idFach.ifPresent(val -> dto.Fach_ID = val);
		patch.kursart.ifPresent(val -> dto.Kursart = val);
		patch.kursnummer.ifPresent(val -> dto.Kursnummer = val);
		patch.idSchuelergruppe.ifPresent(val -> dto.Schuelergruppe_ID = val);
	}

	/**
	 * Löscht einen Kurs.
	 *
	 * @param id   die ID
	 *
	 * @return der gelöschte Kurs
	 */
	public UvKurs delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Kurse.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Kurse
	 */
	public List<UvKurs> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvKursService::toApi).toList();
			lerngruppeRepository.delete(lerngruppeRepository.getListByKursIds(ids));
			repository.delete(entities);
			return result;
		});
	}

}
