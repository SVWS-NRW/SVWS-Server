package de.svws_nrw.repo.gost.klausuren;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;

/**
 * Repository für DB-nahe Seiteneffekte zwischen Stundenplan-Gültigkeiten und Klausurraumdaten.
 */
public final class GostKlausurenStundenplanHookRepository {

	private final DBEntityManager conn;

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenStundenplanHookRepository(final DBEntityManager conn) {
		this.conn = conn;
	}

	/**
	 * Speichert bei einer Verkürzung der Stundenplangültigkeit das Kürzel des Stundenplanraums
	 * bzw. die Stunde des Zeitrasters zur späteren Wiederherstellung.
	 *
	 * @param stundenplan das geänderte Stundenplan-DTO
	 */
	public void handleStundenplangueltigkeitMinus(final DTOStundenplan stundenplan) {
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
	 * Stellt bei einer Erweiterung der Stundenplangültigkeit passende Stundenplanräume und Zeitraster wieder her.
	 *
	 * @param stundenplan das geänderte Stundenplan-DTO
	 */
	public void handleStundenplangueltigkeitPlus(final DTOStundenplan stundenplan) {
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
