package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenKursklausurRepository;
import org.apache.commons.lang3.StringUtils;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf GOSt-Kursklausuren.
 */
public final class GostKlausurenKursklausurService {

	private final GostKlausurenKursklausurRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Kursklausuren
	 */
	public GostKlausurenKursklausurService(final GostKlausurenKursklausurRepository repository) {
		this.repository = repository;
	}

	/**
	 * Wandelt ein Datenbank-DTO in ein API-DTO um.
	 *
	 * @param dto das Datenbank-DTO
	 *
	 * @return das API-DTO
	 */
	public static GostKursklausur toApi(final DTOGostKlausurenKursklausuren dto) {
		final GostKursklausur daten = new GostKursklausur();
		daten.id = dto.ID;
		daten.idVorgabe = dto.Vorgabe_ID;
		daten.idKurs = dto.Kurs_ID;
		daten.idTermin = dto.Termin_ID;
		daten.startzeit = dto.Startzeit;
		daten.bemerkung = dto.Bemerkungen;
		return daten;
	}

	/**
	 * Ermittelt eine Kursklausur anhand der ID.
	 *
	 * @param id die ID
	 *
	 * @return die Kursklausur
	 */
	public GostKursklausur get(final long id) {
		return toApi(repository.getById(id));
	}

	/**
	 * Patcht ausschließlich die Kursklausur selbst ohne fachliche Querprüfungen und ohne Raumdaten-Seiteneffekte.
	 *
	 * Package-private, damit REST-Workflows den höherwertigen {@link GostKlausurenKursklausurPatchService} verwenden und diese Prüfungen und
	 * Seiteneffekte nicht umgehen.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die gepatchte Kursklausur
	 */
	GostKursklausur patch(final GostKlausurenKursklausurPatchRequest patchRequest) {
		final DTOGostKlausurenKursklausuren dto = repository.getById(patchRequest.id);
		if (patchRequest.idTermin.isPresent()) {
			applyTermin(dto, patchRequest.idTermin.get());
		}
		if (patchRequest.startzeit.isPresent()) {
			dto.Startzeit = de.svws_nrw.data.JSONMapper.convertToIntegerInRange(patchRequest.startzeit.get(), true, 0, 1440, "startzeit");
		}
		if (patchRequest.bemerkung.isPresent()) {
			dto.Bemerkungen = StringUtils.trimToNull(de.svws_nrw.data.JSONMapper.convertToString(patchRequest.bemerkung.get(), true, true,
					Schema.tab_Gost_Klausuren_Kursklausuren.col_Bemerkungen.datenlaenge(), "bemerkung"));
		}
		repository.update(dto);
		repository.flush();
		return toApi(dto);
	}

	private static void applyTermin(final DTOGostKlausurenKursklausuren dto, final Long newTerminId) {
		if (!Objects.equals(newTerminId, dto.Termin_ID)) {
			dto.Startzeit = null;
		}
		dto.Termin_ID = newTerminId;
	}

	/**
	 * Ermittelt Kursklausuren zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Kursklausuren
	 */
	public List<GostKursklausur> getListByIds(final Collection<Long> ids) {
		return repository.findListByIds(ids).stream().map(GostKlausurenKursklausurService::toApi).toList();
	}

	/**
	 * Ermittelt Kursklausuren zu den angegebenen Klausurvorgaben.
	 *
	 * @param vorgabeIds die IDs der Klausurvorgaben
	 *
	 * @return die Kursklausuren
	 */
	public List<GostKursklausur> getListByVorgabeIds(final Collection<Long> vorgabeIds) {
		return repository.getListByVorgabeIds(vorgabeIds).stream().map(GostKlausurenKursklausurService::toApi).toList();
	}

	/**
	 * Ermittelt Kursklausuren zu den angegebenen Klausurterminen.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Kursklausuren
	 */
	public List<GostKursklausur> getListByTerminIds(final Collection<Long> terminIds) {
		return repository.getListByTerminIds(terminIds).stream().map(GostKlausurenKursklausurService::toApi).toList();
	}

	/**
	 * Löscht eine Kursklausur.
	 *
	 * @param id die ID
	 *
	 * @return die gelöschte Kursklausur
	 */
	public GostKursklausur delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Kursklausuren.
	 *
	 * @param ids die IDs
	 *
	 * @return die gelöschten Kursklausuren
	 */
	public List<GostKursklausur> deleteMultiple(final Collection<Long> ids) {
		return transactional(() -> {
			final List<GostKursklausur> result = new ArrayList<>();
			for (final Long id : ids) {
				final DTOGostKlausurenKursklausuren dto = repository.getById(id);
				result.add(toApi(dto));
				repository.delete(dto);
			}
			return result;
		});
	}

}
