package de.svws_nrw.service.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenRaeume;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRaumRepository;
import org.apache.commons.lang3.StringUtils;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf GOSt-Klausurräume.
 */
public final class GostKlausurenRaumService {

	private final GostKlausurenRaumRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Klausurräume
	 */
	public GostKlausurenRaumService(final GostKlausurenRaumRepository repository) {
		this.repository = repository;
	}

	/**
	 * Wandelt ein Datenbank-DTO in ein API-DTO um.
	 *
	 * @param dto das Datenbank-DTO
	 *
	 * @return das API-DTO
	 */
	public static GostKlausurraum toApi(final DTOGostKlausurenRaeume dto) {
		final GostKlausurraum daten = new GostKlausurraum();
		daten.id = dto.ID;
		daten.idTermin = dto.Termin_ID;
		daten.idStundenplanRaum = dto.Stundenplan_Raum_ID;
		daten.bemerkung = dto.Bemerkungen;
		return daten;
	}

	/**
	 * Ermittelt einen Klausurraum anhand der ID.
	 *
	 * @param id die ID
	 *
	 * @return der Klausurraum
	 */
	public GostKlausurraum get(final long id) {
		return toApi(repository.getById(id));
	}

	/**
	 * Ermittelt Klausurräume zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Klausurräume
	 */
	public List<GostKlausurraum> getListByIds(final Collection<Long> ids) {
		return repository.findListByIds(ids).stream().map(GostKlausurenRaumService::toApi).toList();
	}

	/**
	 * Ermittelt Klausurräume zu den angegebenen Klausurterminen.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Klausurräume
	 */
	public List<GostKlausurraum> getListByTerminIds(final Collection<Long> terminIds) {
		return repository.getListByTerminIds(terminIds).stream().map(GostKlausurenRaumService::toApi).toList();
	}

	/**
	 * Erstellt einen Klausurraum.
	 *
	 * @param createRequest die Erstell-Daten
	 *
	 * @return der neue Klausurraum
	 */
	public GostKlausurraum create(final GostKlausurenRaumCreateRequest createRequest) {
		return transactional(() -> {
			final DTOGostKlausurenRaeume dto = new DTOGostKlausurenRaeume(-1L, createRequest.idTermin);
			applyCreateAttributes(dto, createRequest);
			repository.create(dto);
			repository.flush();
			return toApi(dto);
		});
	}

	/**
	 * Patcht einen Klausurraum.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return der gepatchte Klausurraum
	 */
	public GostKlausurraum patch(final GostKlausurenRaumPatchRequest patchRequest) {
		return transactional(() -> {
			final DTOGostKlausurenRaeume dto = repository.getById(patchRequest.id);
			applyPatch(dto, patchRequest);
			repository.update(dto);
			repository.flush();
			return toApi(dto);
		});
	}

	/**
	 * Löscht einen Klausurraum.
	 *
	 * @param id die ID
	 *
	 * @return der gelöschte Klausurraum
	 */
	public GostKlausurraum delete(final long id) {
		return transactional(() -> {
			final DTOGostKlausurenRaeume dto = repository.getById(id);
			final GostKlausurraum result = toApi(dto);
			repository.delete(dto);
			return result;
		});
	}

	private static void applyCreateAttributes(final DTOGostKlausurenRaeume dto, final GostKlausurenRaumCreateRequest createRequest) {
		if (createRequest.idStundenplanRaum.isPresent()) {
			dto.Stundenplan_Raum_ID = JSONMapper.convertToLong(createRequest.idStundenplanRaum.get(), true, "idStundenplanRaum");
			dto.Stundenplan_Raum_Kuerzel = null;
		}
		if (createRequest.bemerkung.isPresent()) {
			dto.Bemerkungen = StringUtils.trimToNull(JSONMapper.convertToString(createRequest.bemerkung.get(), true, true,
					Schema.tab_Gost_Klausuren_Raeume.col_Bemerkungen.datenlaenge(), "bemerkung"));
		}
	}

	private static void applyPatch(final DTOGostKlausurenRaeume dto, final GostKlausurenRaumPatchRequest patchRequest) {
		if (patchRequest.idStundenplanRaum.isPresent()) {
			dto.Stundenplan_Raum_ID = JSONMapper.convertToLong(patchRequest.idStundenplanRaum.get(), true, "idStundenplanRaum");
			dto.Stundenplan_Raum_Kuerzel = null;
		}
		if (patchRequest.bemerkung.isPresent()) {
			dto.Bemerkungen = StringUtils.trimToNull(JSONMapper.convertToString(patchRequest.bemerkung.get(), true, true,
					Schema.tab_Gost_Klausuren_Raeume.col_Bemerkungen.datenlaenge(), "bemerkung"));
		}
	}

}
