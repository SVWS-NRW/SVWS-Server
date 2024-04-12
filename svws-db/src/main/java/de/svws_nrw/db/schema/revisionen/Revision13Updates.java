package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 13.
 */
public final class Revision13Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 13.
	 */
	public Revision13Updates() {
		super(SchemaRevisionen.REV_13);
		add("Entferne ggf. eine fehlerhafte Unique-Constraint, sofern sie überhaupt erstellt wurde",
			"ALTER TABLE " + Schema.tab_SchuelerSprachpruefungen.name() + " DROP INDEX IF EXISTS " + Schema.tab_SchuelerSprachpruefungen.name() + "_UC1;",
			Schema.tab_SchuelerSprachpruefungen
		);
	}

}
