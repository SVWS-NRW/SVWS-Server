package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 40.
 */
public final class Revision40Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 39.
	 */
	public Revision40Updates() {
		super(SchemaRevisionen.REV_40);
		add("Tabelle Gost_Klausuren_Termine: Setzen des korrekten Schuljahresabschnitts",
				"""
				UPDATE Gost_Klausuren_Termine gkt
				SET Schuljahresabschnitt_ID = (
				    SELECT s.ID
				    FROM Schuljahresabschnitte s
				    WHERE s.Jahr = gkt.Abi_Jahrgang -
				        CASE
				            WHEN gkt.Halbjahr IN (4, 5) THEN 1
				            WHEN gkt.Halbjahr IN (2, 3) THEN 2
				            WHEN gkt.Halbjahr IN (0, 1) THEN 3
				        END
				      AND s.Abschnitt = (gkt.Halbjahr % 2) + 1
				)
				WHERE gkt.Halbjahr IN (0, 1, 2, 3, 4, 5)
				  AND Schuljahresabschnitt_ID = 0
				""",
				Schema.tab_Gost_Klausuren_Termine
		);
		add("Tabelle Gost_Klausuren_Raeume: Speichere das Kürzel des Stundenplan-Raums, falls der zugehörige Stundenplan für den Termin nicht gültig ist",
				"""
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
				      AND (s.Aktiv=FALSE OR gkt.Datum < s.Beginn OR gkt.Datum > s.Ende)
				)
				""",
				Schema.tab_Gost_Klausuren_Raeume
		);
		add("Tabelle Gost_Klausuren_Raeume: Stelle die Stundenplanraum-ID wieder her, falls es einen neuen zugehörigen Stundenplan für den Termin gibt",
				"""
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
				      AND gkt.Datum BETWEEN s.Beginn AND s.Ende
				      AND gkt.ID = gkrU.Termin_ID
				      AND s.Aktiv = TRUE
				) AS sub ON sub.gkrU_ID = gkrU.ID
				SET
				    gkrU.Stundenplan_Raum_ID = sub.neuer_Raum_ID,
				    gkrU.Stundenplan_Raum_Kuerzel = NULL
				""",
				Schema.tab_Gost_Klausuren_Raeume
		);
		add("Tabelle Gost_Klausuren_Raumstunden: Nullwerte für Zeitraster_ID zulassen",
				"""
				ALTER TABLE Gost_Klausuren_Raumstunden
				MODIFY COLUMN Zeitraster_ID BIGINT(20) NULL
				""",
				Schema.tab_Gost_Klausuren_Raumstunden
		);
		add("Tabelle Gost_Klausuren_Raumstunden: Speichere die Stunde des Stundenplan-Zeitrasters, falls der zugehörige Stundenplan für den Termin nicht gültig ist",
				"""
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
				          s.Aktiv = FALSE OR
				          gkt.Datum < s.Beginn OR
				          gkt.Datum > s.Ende
				      )
				)
				""",
				Schema.tab_Gost_Klausuren_Raumstunden
		);
		add("Tabelle Gost_Klausuren_Raumstunden: Stelle die Zeitraster-ID wieder her, falls es einen neuen zugehörigen Stundenplan für den Termin gibt",
				"""
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
				      AND gkt.Datum BETWEEN s.Beginn AND s.Ende
				      AND sz.Tag = DAYOFWEEK(gkt.Datum) - 1
				      AND gkt.ID = gkr.Termin_ID
				      AND s.Aktiv = TRUE
				) AS sub ON sub.gkrsU_ID = gkrsU.ID
				SET
				    gkrsU.Zeitraster_ID = sub.neue_Zeitraster_ID,
				    gkrsU.Zeitraster_Stunde = NULL
				""",
				Schema.tab_Gost_Klausuren_Raumstunden
		);
	}

}
