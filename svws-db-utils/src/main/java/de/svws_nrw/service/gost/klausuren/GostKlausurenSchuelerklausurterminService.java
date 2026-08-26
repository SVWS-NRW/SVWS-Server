package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurterminRepository;
import org.apache.commons.lang3.StringUtils;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf GOSt-Schülerklausurtermine.
 */
public final class GostKlausurenSchuelerklausurterminService {

	private final GostKlausurenSchuelerklausurterminRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Schülerklausurtermine
	 */
	public GostKlausurenSchuelerklausurterminService(final GostKlausurenSchuelerklausurterminRepository repository) {
		this.repository = repository;
	}

	/**
	 * Wandelt ein Datenbank-DTO in ein API-DTO um.
	 *
	 * @param dto das Datenbank-DTO
	 *
	 * @return das API-DTO
	 */
	public static GostSchuelerklausurtermin toApi(final DTOGostKlausurenSchuelerklausurenTermine dto) {
		final GostSchuelerklausurtermin daten = new GostSchuelerklausurtermin();
		daten.id = dto.ID;
		daten.idSchuelerklausur = dto.Schuelerklausur_ID;
		daten.folgeNr = dto.Folge_Nr;
		daten.idTermin = dto.Termin_ID;
		daten.startzeit = dto.Startzeit;
		daten.bemerkung = dto.Bemerkungen;
		return daten;
	}

	/**
	 * Ermittelt einen Schülerklausurtermin anhand der ID.
	 *
	 * @param id die ID
	 *
	 * @return der Schülerklausurtermin
	 */
	public GostSchuelerklausurtermin get(final long id) {
		return toApi(repository.getById(id));
	}

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Schülerklausurtermine
	 */
	public List<GostSchuelerklausurtermin> getListByIds(final Collection<Long> ids) {
		return repository.findListByIds(ids).stream().map(GostKlausurenSchuelerklausurterminService::toApi).toList();
	}

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen Schülerklausuren.
	 *
	 * @param schuelerklausurIds die IDs der Schülerklausuren
	 *
	 * @return die Schülerklausurtermine
	 */
	public List<GostSchuelerklausurtermin> getListBySchuelerklausurIds(final Collection<Long> schuelerklausurIds) {
		return repository.getListBySchuelerklausurIds(schuelerklausurIds).stream()
				.map(GostKlausurenSchuelerklausurterminService::toApi)
				.toList();
	}

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen Klausurterminen.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Schülerklausurtermine
	 */
	public List<GostSchuelerklausurtermin> getListByTerminIds(final Collection<Long> terminIds) {
		return repository.getListByTerminIds(terminIds).stream()
				.map(GostKlausurenSchuelerklausurterminService::toApi)
				.toList();
	}

	List<GostSchuelerklausurtermin> removeTerminFromNachschreiberByTerminId(final long idTermin) {
		final List<DTOGostKlausurenSchuelerklausurenTermine> dtos = repository.getListByTerminIds(List.of(idTermin)).stream()
				.filter(dto -> dto.Folge_Nr > 0)
				.toList();
		for (final DTOGostKlausurenSchuelerklausurenTermine dto : dtos) {
			dto.Termin_ID = null;
		}
		repository.update(dtos);
		repository.flush();
		return dtos.stream().map(GostKlausurenSchuelerklausurterminService::toApi).toList();
	}

	/**
	 * Erstellt einen Schülerklausurtermin.
	 *
	 * @param createRequest die Erstell-Daten
	 *
	 * @return der neue Schülerklausurtermin
	 */
	public GostSchuelerklausurtermin create(final GostKlausurenSchuelerklausurterminCreateRequest createRequest) {
		return transactional(() -> {
			final List<DTOGostKlausurenSchuelerklausurenTermine> vorhandeneTermine = getListBySchuelerklausurId(createRequest.idSchuelerklausur);
			return create(createRequest, getNaechsteFolgeNr(vorhandeneTermine));
		});
	}

	GostSchuelerklausurtermin create(final GostKlausurenSchuelerklausurterminCreateRequest createRequest, final int folgeNr) {
		final DTOGostKlausurenSchuelerklausurenTermine dto =
				new DTOGostKlausurenSchuelerklausurenTermine(-1L, createRequest.idSchuelerklausur, folgeNr);
		applyCreateAttributes(dto, createRequest);
		repository.create(dto);
		repository.flush();
		return toApi(dto);
	}

	/**
	 * Patcht ausschließlich den Schülerklausurtermin selbst ohne fachliche Querprüfungen und ohne Raumdaten-Seiteneffekte.
	 *
	 * Package-private, damit REST-Workflows den höherwertigen {@link GostKlausurenSchuelerklausurterminPatchService} verwenden und diese Seiteneffekte
	 * nicht umgehen.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return der gepatchte Schülerklausurtermin
	 */
	GostSchuelerklausurtermin patch(final GostKlausurenSchuelerklausurterminPatchRequest patchRequest) {
		return patchMultiple(List.of(patchRequest)).getFirst();
	}

	/**
	 * Patcht ausschließlich mehrere Schülerklausurtermine selbst ohne fachliche Querprüfungen und ohne Raumdaten-Seiteneffekte.
	 *
	 * Package-private, damit REST-Workflows den höherwertigen {@link GostKlausurenSchuelerklausurterminPatchService} verwenden und diese Seiteneffekte
	 * nicht umgehen.
	 *
	 * @param patchRequests die Patch-Daten
	 *
	 * @return die gepatchten Schülerklausurtermine
	 */
	List<GostSchuelerklausurtermin> patchMultiple(final Collection<GostKlausurenSchuelerklausurterminPatchRequest> patchRequests) {
		final List<DTOGostKlausurenSchuelerklausurenTermine> dtos = new ArrayList<>();
		for (final GostKlausurenSchuelerklausurterminPatchRequest patchRequest : patchRequests) {
			final DTOGostKlausurenSchuelerklausurenTermine dto = repository.getById(patchRequest.id);
			applyPatchAttributes(dto, patchRequest);
			dtos.add(dto);
		}
		repository.update(dtos);
		repository.flush();
		return dtos.stream().map(GostKlausurenSchuelerklausurterminService::toApi).toList();
	}

	private static void applyPatchAttributes(final DTOGostKlausurenSchuelerklausurenTermine dto,
			final GostKlausurenSchuelerklausurterminPatchRequest patchRequest) {
		if (patchRequest.idTermin.isPresent()) {
			dto.Termin_ID = JSONMapper.convertToLong(patchRequest.idTermin.get(), true, "idTermin");
		}
		if (patchRequest.startzeit.isPresent()) {
			dto.Startzeit = JSONMapper.convertToIntegerInRange(patchRequest.startzeit.get(), true, 0, 1440, "startzeit");
		}
		if (patchRequest.bemerkung.isPresent()) {
			dto.Bemerkungen = StringUtils.trimToNull(JSONMapper.convertToString(patchRequest.bemerkung.get(), true, true,
					Schema.tab_Gost_Klausuren_Schuelerklausuren_Termine.col_Bemerkungen.datenlaenge(), "bemerkung"));
		}
	}

	/**
	 * Löscht einen Schülerklausurtermin.
	 *
	 * @param id die ID
	 *
	 * @return der gelöschte Schülerklausurtermin
	 */
	public GostSchuelerklausurtermin delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	List<DTOGostKlausurenSchuelerklausurenTermine> getListBySchuelerklausurId(final Long idSchuelerklausur) {
		return repository.getListBySchuelerklausurIds(List.of(idSchuelerklausur));
	}

	static int getNaechsteFolgeNr(final List<DTOGostKlausurenSchuelerklausurenTermine> vorhandeneTermine) {
		return vorhandeneTermine.stream()
				.mapToInt(dto -> dto.Folge_Nr)
				.max()
				.orElse(-1) + 1;
	}

	private static void applyCreateAttributes(final DTOGostKlausurenSchuelerklausurenTermine dto,
			final GostKlausurenSchuelerklausurterminCreateRequest createRequest) {
		if (createRequest.idTermin.isPresent()) {
			dto.Termin_ID = JSONMapper.convertToLong(createRequest.idTermin.get(), true, "idTermin");
		}
		if (createRequest.startzeit.isPresent()) {
			dto.Startzeit = JSONMapper.convertToIntegerInRange(createRequest.startzeit.get(), true, 0, 1440, "startzeit");
		}
		if (createRequest.bemerkung.isPresent()) {
			dto.Bemerkungen = StringUtils.trimToNull(JSONMapper.convertToString(createRequest.bemerkung.get(), true, true,
					Schema.tab_Gost_Klausuren_Schuelerklausuren_Termine.col_Bemerkungen.datenlaenge(), "bemerkung"));
		}
	}

	/**
	 * Löscht mehrere Schülerklausurtermine.
	 *
	 * @param ids die IDs
	 *
	 * @return die gelöschten Schülerklausurtermine
	 */
	public List<GostSchuelerklausurtermin> deleteMultiple(final Collection<Long> ids) {
		return transactional(() -> {
			final List<GostSchuelerklausurtermin> result = new ArrayList<>();
			for (final Long id : ids) {
				final DTOGostKlausurenSchuelerklausurenTermine dto = repository.getById(id);
				result.add(toApi(dto));
				repository.delete(dto);
			}
			return result;
		});
	}

}
