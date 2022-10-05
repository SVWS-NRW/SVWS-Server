package de.nrw.schule.svws.db.schema.revisionen;

import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaRevisionUpdateSQL;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 2.
 */
public class Revision2Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates 
	 * für Revision 2.
	 */
	public Revision2Updates() {
		super(SchemaRevisionen.REV_2);
		add("Initialisierung Kurs_Schueler: Entfernen von Einträgen (sollte keiner vorhanden sein...)", 
			"DELETE FROM Kurs_Schueler",
			Schema.tab_Kurs_Schueler
		);
		add("Entfernen fehlerhafter Kurs-Einträge in den Leistungsdaten (Zuordnung zu Lernabschnitten)", 
			"""
			UPDATE SchuelerLeistungsdaten a 
			SET Kurs_ID = NULL 
			WHERE (SELECT Schuljahresabschnitts_ID FROM Kurse WHERE ID = a.Kurs_ID)
				<> (SELECT Schuljahresabschnitts_ID FROM SchuelerLernabschnittsdaten WHERE ID = a.Abschnitt_ID)
			""",
			DBDriver.SQLITE,
			"""
			UPDATE SchuelerLeistungsdaten 
			SET Kurs_ID = NULL 
			WHERE (SELECT Schuljahresabschnitts_ID FROM Kurse WHERE Kurse.ID = SchuelerLeistungsdaten.Kurs_ID) 
				<> (SELECT Schuljahresabschnitts_ID FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = SchuelerLeistungsdaten.Abschnitt_ID)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_SchuelerLeistungsdaten
		);
		add("Initialisierung Kurs_Schueler: Befüllen mit Daten", 
			"""
			INSERT INTO Kurs_Schueler
			SELECT DISTINCT
				Kurse.ID AS Kurs_ID,
				Schueler.ID AS Schueler_ID
			FROM 
				Kurse JOIN SchuelerLeistungsdaten ON Kurse.ID = SchuelerLeistungsdaten.Kurs_ID
					JOIN SchuelerLernabschnittsdaten ON SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID
					JOIN Schueler ON SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
			""",
			Schema.tab_Kurs_Schueler, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_SchuelerLeistungsdaten
		);
	}

}
