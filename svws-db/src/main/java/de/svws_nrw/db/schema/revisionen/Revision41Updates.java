package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 41.
 */
public final class Revision41Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 41.
	 */
	public Revision41Updates() {
		super(SchemaRevisionen.REV_41);
		add("Korrektur der Werte in der Spalte EinschulungsartASD der Tabelle Schueler",
				"""
				UPDATE Schueler
				SET EinschulungsartASD = null
				WHERE EinschulungsartASD NOT IN (
				    SELECT Kuerzel
				    FROM EinschulungsartKatalog_Keys
				)
				""",
				Schema.tab_EinschulungsartKatalog_Keys, Schema.tab_Schueler
		);
	}

}
