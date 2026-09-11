package de.svws_nrw.service.uv.lerngruppen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.uv.UvManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppe;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppeRepository;
import de.svws_nrw.service.uv.kurse.UvKursService;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Lerngruppen (Tabelle UV_Lerngruppen).
 */
public final class UvLerngruppeService {

	private final UvLerngruppeRepository repository;
	private final UvKursService uvKursService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository
	 * @param uvKursService der Service für Kurse
	 */
	public UvLerngruppeService(final UvLerngruppeRepository repository, final UvKursService uvKursService) {
		this.repository = repository;
		this.uvKursService = uvKursService;
	}

	private static UvLerngruppe toApi(final DTOUvLerngruppe dto) {
		final var daten = new UvLerngruppe();
		daten.id = dto.ID;
		daten.idKlasse = dto.Klasse_ID;
		daten.idFach = dto.Fach_ID;
		daten.idKurs = dto.Kurs_ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.wochenstunden = dto.Wochenstunden;
		daten.wochenstundenUnterrichtet = dto.WochenstundenUnterrichtet;
		daten.koopSchulNr = dto.KoopSchulNr;
		daten.koopAnzahlExterne = dto.KoopAnzahlExterne;
		return daten;
	}

	/**
	 * Ermittelt die Lerngruppe anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Lerngruppe
	 */
	public UvLerngruppe get(final long id) {
		final var dto = repository.findById(id);
		if (dto.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvLerngruppe mit der ID %d gefunden.".formatted(id));
		}
		return toApi(dto.get());
	}

	/**
	 * Ermittelt die Lerngruppen anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste
	 */
	public List<UvLerngruppe> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvLerngruppen zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvLerngruppeService::toApi).toList();
	}

	/**
	 * Liefert alle Lerngruppen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Lerngruppen
	 */
	public List<UvLerngruppe> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvLerngruppeService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Lerngruppe.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Lerngruppe
	 */
	public UvLerngruppe create(final UvLerngruppeCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Lerngruppen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Lerngruppen
	 */
	public List<UvLerngruppe> createMultiple(final Collection<UvLerngruppeCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvLerngruppe> entities = new ArrayList<>();
			for (final UvLerngruppeCreateRequest request : createRequests) {
				entities.add(buildNewDto(nextId++, request));
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	private static DTOUvLerngruppe buildNewDto(final long id, final UvLerngruppeCreateRequest request) {
		final DTOUvLerngruppe dto = new DTOUvLerngruppe(id, request.idPlanungsabschnitt, request.wochenstunden,
				request.wochenstundenUnterrichtet, request.koopAnzahlExterne);
		dto.Klasse_ID = request.idKlasse;
		dto.Fach_ID = request.idFach;
		dto.Kurs_ID = request.idKurs;
		dto.KoopSchulNr = request.koopSchulNr;
		validateAssignment(toApi(dto));
		return dto;
	}

	/**
	 * Führt einen Patch auf einer Lerngruppe aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Lerngruppe
	 */
	public UvLerngruppe patch(final UvLerngruppePatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Lerngruppen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten Lerngruppen
	 */
	public List<UvLerngruppe> patchMultiple(final Collection<UvLerngruppePatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final var ids = patches.stream().map(p -> p.id).toList();
			final var entities = repository.findListByIds(ids);
			if (entities.size() != patches.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle angefragten Datensätze konnten gefunden werden.");
			}
			for (final var patch : patches) {
				final var entity = repository.getById(patch.id);
				applyPatch(entity, patch);
			}
			repository.update(entities);
			repository.flush();
			return getList(ids);
		});
	}

	private static void applyPatch(final DTOUvLerngruppe dto, final UvLerngruppePatchRequest patch) {
		// Erst die resultierende Zuordnung prüfen, bevor die persistierte Entität verändert wird.
		final UvLerngruppe result = toApi(dto);
		patch.idKlasse.ifPresent(val -> result.idKlasse = val);
		patch.idFach.ifPresent(val -> result.idFach = val);
		patch.idKurs.ifPresent(val -> result.idKurs = val);
		validateAssignment(result);
		patch.idKlasse.ifPresent(val -> dto.Klasse_ID = val);
		patch.idFach.ifPresent(val -> dto.Fach_ID = val);
		patch.idKurs.ifPresent(val -> dto.Kurs_ID = val);
		patch.wochenstunden.ifPresent(val -> dto.Wochenstunden = val);
		patch.wochenstundenUnterrichtet.ifPresent(val -> dto.WochenstundenUnterrichtet = val);
		patch.koopSchulNr.ifPresent(val -> dto.KoopSchulNr = val);
		patch.koopAnzahlExterne.ifPresent(val -> dto.KoopAnzahlExterne = val);
	}

	private static void validateAssignment(final UvLerngruppe lerngruppe) {
		try {
			UvManager.lerngruppeCheckZuordnung(lerngruppe);
		} catch (final DeveloperNotificationException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * Löscht eine Lerngruppe.
	 *
	 * @param id   die ID
	 *
	 * @return die gelöschte Lerngruppe
	 */
	public UvLerngruppe delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Lerngruppen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Lerngruppen
	 */
	public List<UvLerngruppe> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvLerngruppeService::toApi).toList();
			final List<Long> kursIds = entities.stream()
					.map(lerngruppe -> lerngruppe.Kurs_ID)
					.filter(idKurs -> idKurs != null)
					.toList();
			if (!kursIds.isEmpty()) {
				uvKursService.deleteMultiple(kursIds);
			}
			repository.delete(entities.stream().filter(lerngruppe -> lerngruppe.Kurs_ID == null).toList());
			return result;
		});
	}

}
