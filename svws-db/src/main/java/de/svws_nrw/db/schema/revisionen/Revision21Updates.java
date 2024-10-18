package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 21.
 */
public final class Revision21Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 21.
	 */
	public Revision21Updates() {
		super(SchemaRevisionen.REV_21);
		add("Übertrage Daten aus der alten Tabelle SchuelerWiedervorlage - Tabelle Wiedervorlage",
				"""
				INSERT INTO %s
				SELECT
				  sw.ID AS ID,
				  sw.Typ AS PersonTyp,
				  CASE WHEN sw.Typ = 'L' THEN sw.Schueler_ID ELSE NULL END AS Lehrer_ID,
				  CASE WHEN sw.Typ = 'S' THEN sw.Schueler_ID ELSE NULL END AS Schueler_ID,
				  NULL AS Erzieher_ID,
				  sw.Bemerkung AS Bemerkung,
				  sw.AngelegtAm AS tsAngelegt,
				  sw.WiedervorlageAm AS tsWiedervorlage,
				  sw.ErledigtAm AS tsErledigt,
				  sw.User_ID AS Benutzer_ID,
				  NULL AS Benutzer_ID_Erledigt,
				  bg.ID AS Benutzergruppe_ID,
				  CASE WHEN sw.NichtLoeschen = '-' OR sw.NichtLoeschen IS NULL THEN 1 ELSE 0 END AS AutomatischErledigt
				FROM %s sw
				LEFT JOIN %s bg ON sw.Sekretariat = '+' AND bg.Bezeichnung = 'Sekretariat'
				WHERE Schueler_ID IS NOT NULL AND (((sw.Typ = 'L') AND sw.Schueler_ID IN (SELECT ID FROM K_Lehrer)) OR ((sw.Typ = 'S') AND sw.Schueler_ID IN (SELECT ID FROM Schueler)))
				""".formatted(Schema.tab_Wiedervorlage.name(), Schema.tab_SchuelerWiedervorlage.name(), Schema.tab_Benutzergruppen.name()),
				Schema.tab_Wiedervorlage, Schema.tab_SchuelerWiedervorlage, Schema.tab_Benutzergruppen
		);
	}

}
