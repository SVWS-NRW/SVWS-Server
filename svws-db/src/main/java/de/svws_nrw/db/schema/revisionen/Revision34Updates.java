package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 34.
 */
public final class Revision34Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 34.
	 */
	public Revision34Updates() {
		super(SchemaRevisionen.REV_34);
		add("Reinitialisierung Kurs_Schueler: Entfernen von Einträgen (sollte keiner vorhanden sein...)",
				"DELETE FROM Kurs_Schueler",
				Schema.tab_Kurs_Schueler
		);
		add("Ggf. nochmals Entfernen fehlerhafter Kurs-Einträge in den Leistungsdaten (Zuordnung zu Lernabschnitten)",
				"""
				UPDATE SchuelerLeistungsdaten
				SET Kurs_ID = NULL
				WHERE (SELECT Schuljahresabschnitts_ID FROM Kurse WHERE Kurse.ID = SchuelerLeistungsdaten.Kurs_ID)
				    <> (SELECT Schuljahresabschnitts_ID FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = SchuelerLeistungsdaten.Abschnitt_ID)
				""",
				Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_SchuelerLeistungsdaten
		);
		add("Ggf. nochmals Entfernen fehlerhafter Kurs-Einträge in den Leistungsdaten (Kurs mit nicht passenden Fächern)",
				"UPDATE SchuelerLeistungsdaten JOIN Kurse ON SchuelerLeistungsdaten.Kurs_ID = Kurse.ID "
						+ "SET SchuelerLeistungsdaten.Kurs_ID = NULL "
						+ "WHERE SchuelerLeistungsdaten.Fach_ID != Kurse.Fach_ID;",
				Schema.tab_SchuelerLeistungsdaten, Schema.tab_Kurse
		);
		add("Reinitialisierung Kurs_Schueler: Befüllen mit Daten",
				"""
				INSERT INTO Kurs_Schueler
				SELECT DISTINCT
				    Kurse.ID AS Kurs_ID,
				    Schueler.ID AS Schueler_ID,
				    SchuelerLernabschnittsdaten.WechselNr AS LernabschnittWechselNr,
				    SchuelerLeistungsdaten.ID AS Leistung_ID
				FROM
				    Kurse JOIN SchuelerLeistungsdaten ON Kurse.ID = SchuelerLeistungsdaten.Kurs_ID
				        JOIN SchuelerLernabschnittsdaten ON SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID
				        JOIN Schueler ON SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
				""",
				Schema.tab_Kurs_Schueler, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_SchuelerLeistungsdaten
		);
	}

}
