package de.svws_nrw.service.uv.klassen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.db.dto.current.uv.DTOUvKlasse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.klassen.UvKlasseRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Klassen (Tabelle UV_Klassen).
 */
public final class UvKlasseService {

	private final UvKlasseRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository
	 */
	public UvKlasseService(final UvKlasseRepository repository) {
		this.repository = repository;
	}

	private static UvKlasse toApi(final DTOUvKlasse dto) {
		final UvKlasse k = new UvKlasse();
		k.id = dto.ID;
		k.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		k.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		k.bezeichnung = dto.Bezeichnung;
		k.kuerzel = dto.Kuerzel;
		k.parallelitaet = dto.Parallelitaet;
		k.idStundentafel = dto.Stundentafel_ID;
		k.idSchuelergruppe = dto.Schuelergruppe_ID;
		k.orgFormKrz = dto.OrgFormKrz;
		k.idFachklasse = dto.Fachklasse_ID;
		k.asdSchulformNr = dto.ASDSchulformNr;
		return k;
	}

	/**
	 * Ermittelt die Klasse anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Klasse
	 */
	public UvKlasse get(final long id) {
		final var dto = repository.findById(id);
		if (dto.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvKlasse mit der ID %d gefunden.".formatted(id));
		}
		return toApi(dto.get());
	}

	/**
	 * Ermittelt die Klassen anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste der Klassen
	 */
	public List<UvKlasse> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvKlassen zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvKlasseService::toApi).toList();
	}

	/**
	 * Liefert alle Klassen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Klassen
	 */
	public List<UvKlasse> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvKlasseService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Klasse.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Klasse
	 */
	public UvKlasse create(final UvKlasseCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Klassen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Klassen
	 */
	public List<UvKlasse> createMultiple(final Collection<UvKlasseCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvKlasse> entities = new ArrayList<>();
			for (final UvKlasseCreateRequest request : createRequests) {
				entities.add(buildNewDto(nextId++, request));
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	private static DTOUvKlasse buildNewDto(final long id, final UvKlasseCreateRequest request) {
		final DTOUvKlasse dto = new DTOUvKlasse(id, request.idPlanungsabschnitt, request.idSchuljahresabschnitt,
				request.kuerzel, request.parallelitaet,
				request.idSchuelergruppe);
		dto.Bezeichnung = request.bezeichnung;
		dto.Stundentafel_ID = request.idStundentafel;
		dto.OrgFormKrz = request.orgFormKrz;
		dto.Fachklasse_ID = request.idFachklasse;
		dto.ASDSchulformNr = request.asdSchulformNr;
		return dto;
	}

	/**
	 * Führt einen Patch auf einer Klasse aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Klasse
	 */
	public UvKlasse patch(final UvKlassePatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Klassen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten Klassen
	 */
	public List<UvKlasse> patchMultiple(final Collection<UvKlassePatchRequest> patches) {
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

	private static void applyPatch(final DTOUvKlasse dto, final UvKlassePatchRequest patch) {
		patch.bezeichnung.ifPresent(val -> dto.Bezeichnung = val);
		patch.kuerzel.ifPresent(val -> dto.Kuerzel = val);
		patch.parallelitaet.ifPresent(val -> dto.Parallelitaet = val);
		patch.idStundentafel.ifPresent(val -> dto.Stundentafel_ID = val);
		patch.idSchuelergruppe.ifPresent(val -> dto.Schuelergruppe_ID = val);
		patch.orgFormKrz.ifPresent(val -> dto.OrgFormKrz = val);
		patch.idFachklasse.ifPresent(val -> dto.Fachklasse_ID = val);
		patch.asdSchulformNr.ifPresent(val -> dto.ASDSchulformNr = val);
	}

	/**
	 * Löscht eine Klasse.
	 *
	 * @param id   die ID
	 *
	 * @return die gelöschte Klasse
	 */
	public UvKlasse delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Klassen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Klassen
	 */
	public List<UvKlasse> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvKlasseService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
