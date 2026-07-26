package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf GOSt-Schülerklausuren.
 */
public final class GostKlausurenSchuelerklausurService {

	private final GostKlausurenSchuelerklausurRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Schülerklausuren
	 */
	public GostKlausurenSchuelerklausurService(final GostKlausurenSchuelerklausurRepository repository) {
		this.repository = repository;
	}

	/**
	 * Wandelt ein Datenbank-DTO in ein API-DTO um.
	 *
	 * @param dto das Datenbank-DTO
	 *
	 * @return das API-DTO
	 */
	public static GostSchuelerklausur toApi(final DTOGostKlausurenSchuelerklausuren dto) {
		final GostSchuelerklausur daten = new GostSchuelerklausur();
		daten.id = dto.ID;
		daten.idKursklausur = dto.Kursklausur_ID;
		daten.idSchueler = dto.Schueler_ID;
		daten.bemerkung = dto.Bemerkungen;
		daten.aktiv = dto.Aktiv;
		return daten;
	}

	/**
	 * Ermittelt eine Schülerklausur anhand der ID.
	 *
	 * @param id die ID
	 *
	 * @return die Schülerklausur
	 */
	public GostSchuelerklausur get(final long id) {
		return toApi(repository.getById(id));
	}

	/**
	 * Ermittelt Schülerklausuren zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Schülerklausuren
	 */
	public List<GostSchuelerklausur> getListByIds(final Collection<Long> ids) {
		return repository.findListByIds(ids).stream().map(GostKlausurenSchuelerklausurService::toApi).toList();
	}

	/**
	 * Ermittelt Schülerklausuren zu den angegebenen Kursklausuren.
	 *
	 * @param kursklausurIds die IDs der Kursklausuren
	 *
	 * @return die Schülerklausuren
	 */
	public List<GostSchuelerklausur> getListByKursklausurIds(final Collection<Long> kursklausurIds) {
		return repository.getListByKursklausurIds(kursklausurIds).stream().map(GostKlausurenSchuelerklausurService::toApi).toList();
	}

	/**
	 * Patcht eine Schülerklausur.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die gepatchte Schülerklausur
	 */
	public GostSchuelerklausur patch(final GostKlausurenSchuelerklausurPatchRequest patchRequest) {
		return transactional(() -> {
			final DTOGostKlausurenSchuelerklausuren dto = repository.getById(patchRequest.id);
			if (patchRequest.bemerkung.isPresent()) {
				dto.Bemerkungen = JSONMapper.convertToString(patchRequest.bemerkung.get(), true, true,
						Schema.tab_Gost_Klausuren_Schuelerklausuren.col_Bemerkungen.datenlaenge(), "bemerkung");
			}
			if (patchRequest.aktiv.isPresent()) {
				dto.Aktiv = JSONMapper.convertToBoolean(patchRequest.aktiv.get(), false, "aktiv");
			}
			repository.update(dto);
			repository.flush();
			return toApi(dto);
		});
	}

	/**
	 * Löscht mehrere Schülerklausuren.
	 *
	 * @param ids die IDs
	 *
	 * @return die gelöschten Schülerklausuren
	 */
	public List<GostSchuelerklausur> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<GostSchuelerklausur> result = new ArrayList<>();
			for (final Long id : ids) {
				final DTOGostKlausurenSchuelerklausuren dto = repository.getById(id);
				result.add(toApi(dto));
				repository.delete(dto);
			}
			return result;
		});
	}

	/**
	 * Löscht eine Schülerklausur.
	 *
	 * @param id die ID
	 *
	 * @return die gelöschte Schülerklausur
	 */
	public GostSchuelerklausur delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

}
