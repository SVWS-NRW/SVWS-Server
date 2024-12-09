package de.svws_nrw.data.gost.klausurplan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanRaum;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link GostKlausurraum}.
 */
public final class DataGostKlausurenRaum extends DataManagerRevised<Long, DTOGostKlausurenRaeume, GostKlausurraum> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link GostKlausurraum}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenRaum(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("idTermin");
		super.setAttributesRequiredOnCreation("idTermin");
	}

	/**
	 * Gibt die Daten eines {@link GostKlausurraum}s zu deren ID zurück.
	 *
	 * @param id   Die ID des {@link GostKlausurraum}s.
	 *
	 * @return die Daten des {@link GostKlausurraum}s zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public GostKlausurraum getById(final Long id) throws ApiOperationException {
		final DTOGostKlausurenRaeume klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOGostKlausurenRaeume} Objekt zur angegebenen ID.
	 *
	 * @param id ID des {@link DTOGostKlausurenRaeume} Objekts.
	 *
	 * @return Ein {@link DTOGostKlausurenRaeume} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOGostKlausurenRaeume getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den GostKlausurraum darf nicht null sein.");

		final DTOGostKlausurenRaeume klasseDto = conn.queryByKey(DTOGostKlausurenRaeume.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein GostKlausurraum zur ID " + id + " gefunden.");

		return klasseDto;
	}

	@Override
	protected void initDTO(final DTOGostKlausurenRaeume dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected GostKlausurraum map(final DTOGostKlausurenRaeume dto) throws ApiOperationException {
		final GostKlausurraum daten = new GostKlausurraum();
		daten.id = dto.ID;
		daten.idTermin = dto.Termin_ID;
		daten.idStundenplanRaum = dto.Stundenplan_Raum_ID;
		daten.bemerkung = dto.Bemerkungen;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOGostKlausurenRaeume dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idTermin" -> {
				dto.Termin_ID = JSONMapper.convertToLong(value, false);
				if (conn.queryByKey(DTOGostKlausurenTermine.class, dto.Termin_ID) == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Klausurtermin mit ID %d existiert nicht.".formatted(dto.Termin_ID));
			}
			case "idStundenplanRaum" -> {
				dto.Stundenplan_Raum_ID = JSONMapper.convertToLong(value, true);
				if (dto.Stundenplan_Raum_ID != null && conn.queryByKey(DTOStundenplanRaum.class, dto.Stundenplan_Raum_ID) == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Stundenplanraum nicht gefunden, ID: " + dto.Stundenplan_Raum_ID);
			}
			case "bemerkung" -> dto.Bemerkungen =
					DataGostKlausuren.convertEmptyStringToNull(JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Raeume.col_Bemerkungen.datenlaenge()));
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param terminIds die IDs dee Klausurtermine
	 *
	 * @return die Liste der Klausurräume
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurraum> getKlausurraeumeZuTerminen(final List<Long> terminIds) throws ApiOperationException {
		if (terminIds.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenRaeume> raeume = conn.queryList(DTOGostKlausurenRaeume.QUERY_LIST_BY_TERMIN_ID, DTOGostKlausurenRaeume.class, terminIds);
		final List<GostKlausurraum> daten = new ArrayList<>();
		for (final DTOGostKlausurenRaeume r : raeume)
			daten.add(map(r));
		return daten;
	}

}
