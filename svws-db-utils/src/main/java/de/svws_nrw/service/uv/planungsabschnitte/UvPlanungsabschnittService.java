package de.svws_nrw.service.uv.planungsabschnitte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnitt;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.planungsabschnitte.UvPlanungsabschnittRepository;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Planungsabschnitte (Tabelle UV_Planungsabschnitte).
 */
public final class UvPlanungsabschnittService {

	private final UvPlanungsabschnittRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvPlanungsabschnittRepository   das Repository
	 */
	public UvPlanungsabschnittService(final UvPlanungsabschnittRepository uvPlanungsabschnittRepository) {
		this.repository = uvPlanungsabschnittRepository;
	}

	private static UvPlanungsabschnitt toApi(final DTOUvPlanungsabschnitt dto) {
		final var daten = new UvPlanungsabschnitt();
		daten.id = dto.ID;
		daten.schuljahr = dto.Schuljahr;
		daten.aktiv = dto.Aktiv;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		daten.beschreibung = dto.Beschreibung;
		return daten;
	}

	/**
	 * Ermittelt den UV-Planungsabschnitt anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return der UV-Planungsabschnitt
	 */
	public UvPlanungsabschnitt get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein UvPlanungsabschnitt mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die UV-Planungsabschnitte anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste
	 */
	public List<UvPlanungsabschnitt> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvPlanungsabschnitte zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvPlanungsabschnittService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Planungsabschnitte.
	 *
	 * @return die Liste aller UV-Planungsabschnitte
	 */
	public List<UvPlanungsabschnitt> getAll() {
		return repository.getAll().stream().map(UvPlanungsabschnittService::toApi).toList();
	}

	/**
	 * Liefert die UV-Planungsabschnitte eines Schuljahres. Ist das Schuljahr {@code null},
	 * so werden alle UV-Planungsabschnitte zurückgegeben.
	 *
	 * @param schuljahr   das Schuljahr oder {@code null} für alle
	 *
	 * @return die Liste der UV-Planungsabschnitte
	 */
	public List<UvPlanungsabschnitt> getListBySchuljahr(final Integer schuljahr) {
		return repository.getListBySchuljahr(schuljahr).stream().map(UvPlanungsabschnittService::toApi).toList();
	}

	/**
	 * Erstellt einen neuen UV-Planungsabschnitt.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return der neue UV-Planungsabschnitt
	 */
	public UvPlanungsabschnitt create(final UvPlanungsabschnittCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue UV-Planungsabschnitte.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen UV-Planungsabschnitte
	 */
	public List<UvPlanungsabschnitt> createMultiple(final Collection<UvPlanungsabschnittCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvPlanungsabschnitt> entities = new ArrayList<>();
			for (final UvPlanungsabschnittCreateRequest request : createRequests) {
				final var neu = buildNewDto(nextId++, request);
				checkBeforePersist(neu);
				entities.add(neu);
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	private static DTOUvPlanungsabschnitt buildNewDto(final long id, final UvPlanungsabschnittCreateRequest request) {
		if ((request.gueltigVon == null) || !DateUtils.isValidDate(request.gueltigVon)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Attribut gueltigVon hat einen ungültigen Datumswert: %s".formatted(request.gueltigVon));
		}
		if ((request.gueltigBis != null) && !DateUtils.isValidDate(request.gueltigBis)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Attribut gueltigBis hat einen ungültigen Datumswert: %s".formatted(request.gueltigBis));
		}
		final var neu = new DTOUvPlanungsabschnitt(id, request.schuljahr, (request.aktiv != null) && request.aktiv, request.gueltigVon);
		neu.GueltigBis = request.gueltigBis;
		neu.Beschreibung = StringUtils.truncate(StringUtils.trimToNull(request.beschreibung), Schema.tab_UV_Planungsabschnitte.col_Beschreibung.datenlaenge());
		return neu;
	}

	/**
	 * Führt einen Patch auf dem UV-Planungsabschnitt aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return der gepatchte UV-Planungsabschnitt
	 */
	public UvPlanungsabschnitt patch(final UvPlanungsabschnittPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf UV-Planungsabschnitten aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten UV-Planungsabschnitte
	 */
	public List<UvPlanungsabschnitt> patchMultiple(final Collection<UvPlanungsabschnittPatchRequest> patches) {
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
				final boolean touchedGueltigkeit = applyPatch(entity, p);
				checkBeforePersist(entity, touchedGueltigkeit);
			}
			repository.update(entities);
			repository.flush();
			return getList(ids);
		});
	}

	private static boolean applyPatch(final DTOUvPlanungsabschnitt dto, final UvPlanungsabschnittPatchRequest patch) {
		final boolean[] touchedGueltigkeit = { false };
		patch.aktiv.ifPresent(val -> {
			dto.Aktiv = val;
			touchedGueltigkeit[0] = true;
		});
		patch.gueltigVon.ifPresent(val -> {
			if ((val == null) || !DateUtils.isValidDate(val)) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Das Attribut gueltigVon hat einen ungültigen Datumswert: %s".formatted(val));
			}
			dto.GueltigVon = val;
			touchedGueltigkeit[0] = true;
		});
		patch.gueltigBis.ifPresent(val -> {
			if ((val != null) && !DateUtils.isValidDate(val)) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Das Attribut gueltigBis hat einen ungültigen Datumswert: %s".formatted(val));
			}
			dto.GueltigBis = val;
			touchedGueltigkeit[0] = true;
		});
		patch.beschreibung.ifPresent(val -> dto.Beschreibung = StringUtils.truncate(StringUtils.trimToNull(val), Schema.tab_UV_Planungsabschnitte.col_Beschreibung.datenlaenge()));
		return touchedGueltigkeit[0];
	}

	/**
	 * Löscht den UV-Planungsabschnitt.
	 *
	 * @param id   die ID
	 *
	 * @return der gelöschte UV-Planungsabschnitt
	 */
	public UvPlanungsabschnitt delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere UV-Planungsabschnitte.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten UV-Planungsabschnitte
	 */
	public List<UvPlanungsabschnitt> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			if (entities.size() != ids.size()) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Es wurden nicht alle angegebenen IDs in der Datenbank gefunden.");
			}
			final var result = entities.stream().map(UvPlanungsabschnittService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private void checkBeforePersist(final DTOUvPlanungsabschnitt dto) {
		checkBeforePersist(dto, true);
	}

	private void checkBeforePersist(final DTOUvPlanungsabschnitt dto, final boolean gueltigkeitGeaendert) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
		if (dto.Aktiv && gueltigkeitGeaendert) {
			final List<DTOUvPlanungsabschnitt> plaene = repository.getListBySchuljahr(dto.Schuljahr);
			for (final DTOUvPlanungsabschnitt abschnitt : plaene) {
				if ((abschnitt.ID == dto.ID) || !abschnitt.Aktiv) {
					continue;
				}
				if (DateUtils.berechneGemeinsameTage(abschnitt.GueltigVon, abschnitt.GueltigBis, dto.GueltigVon, dto.GueltigBis).length > 0) {
					throw new ApiOperationException(Status.CONFLICT,
							"Der Gültigkeit des UV-Planungsabschnitts steht in Konflikt zum Planungsabschnitt mit der ID %d.".formatted(abschnitt.ID));
				}
			}
		}
	}

}
