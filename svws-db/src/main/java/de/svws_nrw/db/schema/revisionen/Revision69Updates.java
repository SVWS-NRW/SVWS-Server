package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 55.
 */
public final class Revision69Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 55.
	 */
	public Revision69Updates() {
		super(SchemaRevisionen.REV_69);
		add("Setzt in der Tabelle Schueler die Spalte LSSchulform auf 'S', wenn der aktuelle Wert 'FE' oder 'SK' ist.",
				"""
				UPDATE Schueler
				SET LSSchulform = 'S'
				WHERE LSSchulform IN ('FE', 'SK');
				""",
				Schema.tab_Schueler);
	}
}
