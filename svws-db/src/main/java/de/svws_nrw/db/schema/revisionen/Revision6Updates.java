package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 3.
 */
public class Revision6Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates 
	 * für Revision 6.
	 */
	public Revision6Updates() {
		super(SchemaRevisionen.REV_6);
		updateENMZeitstempel();
	}

	private void updateENMZeitstempel() {
		add("Aktualisisere die Zeitstempel für die ENM-Leistungsdaten", 
			"INSERT INTO " + Schema.tab_EnmLeistungsdaten.name() + "(ID, tsNotenKrz, tsFehlStd, tsuFehlStd, tsLernentw, tsWarnung)"+
			"SELECT id, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3) FROM " +
			Schema.tab_SchuelerLeistungsdaten.name() + " WHERE ID NOT IN (SELECT ID FROM " + Schema.tab_EnmLeistungsdaten.name() + ");",
			Schema.tab_EnmLeistungsdaten, Schema.tab_SchuelerLeistungsdaten
		);
		add("Aktualisisere die Zeitstempel für die ENM-Lernabschnittsdaten", 
			"INSERT INTO " + Schema.tab_EnmLernabschnittsdaten.name() + "(ID, tsSumFehlStd, tsSumFehlStdU, tsZeugnisBem, tsASV, tsAUE, tsBemerkungVersetzung)"+
			"SELECT id, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3) FROM " +
			Schema.tab_SchuelerLernabschnittsdaten.name() + " WHERE ID NOT IN (SELECT ID FROM " + Schema.tab_EnmLernabschnittsdaten.name() + ");",
			Schema.tab_EnmLernabschnittsdaten, Schema.tab_SchuelerLernabschnittsdaten
		);			
	}
	
}
