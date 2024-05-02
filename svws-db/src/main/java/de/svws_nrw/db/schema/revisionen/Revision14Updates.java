package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 14.
 */
public class Revision14Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 14.
	 */
	public Revision14Updates() {
		super(SchemaRevisionen.REV_14);
		updateENMZeitstempel();
	}

	private void updateENMZeitstempel() {
		add("Aktualisisere die Zeitstempel für die ENM-Teilleistungen",
			"INSERT INTO " + Schema.tab_EnmTeilleistungen.name() + "(ID, tsDatum, tsLehrer_ID, tsArt_ID, tsBemerkung, tsNotenKrz)"
			+ "SELECT id, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3) FROM "
			+ Schema.tab_SchuelerEinzelleistungen.name() + " WHERE ID NOT IN (SELECT ID FROM " + Schema.tab_EnmTeilleistungen.name() + ");",
			Schema.tab_EnmTeilleistungen, Schema.tab_SchuelerEinzelleistungen
		);
	}

}
