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
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
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
		super.setAttributesNotPatchable("id", "idTermin");
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
				dto.Stundenplan_Raum_Kuerzel = null;
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

	/**
	 * Speichert bei einer Verkürzung der Stundenplangültigkeit das Kürzel des Stundenplanraums zur späteren Wiederherstellung
	 *
	 * @param conn            die Datenbankverbindung
	 * @param stundenplan   das geänderte Stundenplan-DTO
	 */
	public static void dbHookStundenplangueltigkeitMinus(final DBEntityManager conn, final DTOStundenplan stundenplan) {
		conn.transactionNativeUpdate(
			String.format("""
			UPDATE Gost_Klausuren_Raeume gkrU
			SET
			    Stundenplan_Raum_Kuerzel = (
			        SELECT srU.Kuerzel
			        FROM Stundenplan_Raeume srU
			        WHERE srU.ID = gkrU.Stundenplan_Raum_ID
			    ),
			    Stundenplan_Raum_ID = NULL
			WHERE gkrU.ID IN (
			    SELECT gkr.ID
			    FROM Gost_Klausuren_Raeume gkr
			    JOIN Gost_Klausuren_Termine gkt ON gkr.Termin_ID = gkt.ID
			    JOIN Stundenplan_Raeume sr ON gkr.Stundenplan_Raum_ID = sr.ID
			    JOIN Stundenplan s ON s.ID = sr.Stundenplan_ID
			    WHERE gkt.Datum IS NOT NULL
			      AND (%d = 0 OR gkt.Datum < '%s' OR gkt.Datum > '%s')
			      AND s.ID = %d
			)
			""", (stundenplan.Aktiv) ? 1 : 0, stundenplan.Beginn, stundenplan.Ende, stundenplan.ID)
		);
		conn.transactionNativeUpdate(
			String.format("""
			UPDATE Gost_Klausuren_Raumstunden gkrsU
			SET
			    Zeitraster_Stunde = (
			        SELECT zrU.Stunde
			        FROM Stundenplan_Zeitraster zrU
			        WHERE zrU.ID = gkrsU.Zeitraster_ID
			    ),
			    Zeitraster_ID = NULL
			WHERE gkrsU.ID IN (
			    SELECT gkrs.ID
			    FROM Gost_Klausuren_Raumstunden gkrs
			    JOIN Gost_Klausuren_Raeume gkr ON gkrs.Klausurraum_ID = gkr.ID
			    JOIN Gost_Klausuren_Termine gkt ON gkr.Termin_ID = gkt.ID
			    JOIN Stundenplan_Zeitraster sz ON gkrs.Zeitraster_ID = sz.ID
			    JOIN Stundenplan s ON s.ID = sz.Stundenplan_ID
			    WHERE gkt.Datum IS NOT NULL
			      AND (
			          %d = 0 OR
			          gkt.Datum < '%s' OR
			          gkt.Datum > '%s'
			      ) AND s.ID = %d
			)
			""", (stundenplan.Aktiv) ? 1 : 0, stundenplan.Beginn, stundenplan.Ende, stundenplan.ID)
		);
	}

	/**
	 * Speichert bei einer Verkürzung der Stundenplangültigkeit das Kürzel des Stundenplanraums zur späteren Wiederherstellung
	 *
	 * @param conn            die Datenbankverbindung
	 * @param stundenplan   das geänderte Stundenplan-DTO
	 */
	public static void dbHookStundenplangueltigkeitPlus(final DBEntityManager conn, final DTOStundenplan stundenplan) {
		conn.transactionNativeUpdate(
			String.format("""
			UPDATE Gost_Klausuren_Raeume gkrU
			JOIN (
			    SELECT
			        gkrU.ID AS gkrU_ID,
			        sr.ID AS neuer_Raum_ID
			    FROM Gost_Klausuren_Raeume gkrU
			    JOIN Stundenplan_Raeume sr ON sr.Kuerzel = gkrU.Stundenplan_Raum_Kuerzel
			    JOIN Stundenplan s ON sr.Stundenplan_ID = s.ID
			    JOIN Gost_Klausuren_Termine gkt ON s.Schuljahresabschnitts_ID = gkt.Schuljahresabschnitt_ID
			    WHERE gkrU.Stundenplan_Raum_Kuerzel IS NOT NULL
			      AND gkt.Schuljahresabschnitt_ID = %d
			      AND gkt.Datum BETWEEN '%s' AND '%s'
			      AND gkt.ID = gkrU.Termin_ID
			      AND s.ID = %d
			      AND s.Aktiv = TRUE
			) AS sub ON sub.gkrU_ID = gkrU.ID
			SET
			    gkrU.Stundenplan_Raum_ID = sub.neuer_Raum_ID,
			    gkrU.Stundenplan_Raum_Kuerzel = NULL
			""", stundenplan.Schuljahresabschnitts_ID, stundenplan.Beginn, stundenplan.Ende, stundenplan.ID)
		);
		conn.transactionNativeUpdate(
			String.format("""
			UPDATE Gost_Klausuren_Raumstunden gkrsU
			JOIN (
			    SELECT
			        gkrs.ID AS gkrsU_ID,
			        sz.ID AS neue_Zeitraster_ID
			    FROM Gost_Klausuren_Raumstunden gkrs
			    JOIN Gost_Klausuren_Raeume gkr ON gkrs.Klausurraum_ID = gkr.ID
			    JOIN Gost_Klausuren_Termine gkt ON gkr.Termin_ID = gkt.ID
			    JOIN Stundenplan s ON gkt.Schuljahresabschnitt_ID = s.Schuljahresabschnitts_ID
			    JOIN Stundenplan_Zeitraster sz ON gkrs.Zeitraster_Stunde = sz.Stunde AND sz.Stundenplan_ID = s.ID
			    WHERE gkrs.Zeitraster_Stunde IS NOT NULL
			      AND gkt.Schuljahresabschnitt_ID = %d
			      AND gkt.Datum BETWEEN '%s' AND '%s'
			      AND sz.Tag = DAYOFWEEK(gkt.Datum) - 1
			      AND gkt.ID = gkr.Termin_ID
			      AND s.ID = %d
			      AND s.Aktiv = TRUE
			) AS sub ON sub.gkrsU_ID = gkrsU.ID
			SET
			    gkrsU.Zeitraster_ID = sub.neue_Zeitraster_ID,
			    gkrsU.Zeitraster_Stunde = NULL
			""", stundenplan.Schuljahresabschnitts_ID, stundenplan.Beginn, stundenplan.Ende, stundenplan.ID)
		);
	}

}
