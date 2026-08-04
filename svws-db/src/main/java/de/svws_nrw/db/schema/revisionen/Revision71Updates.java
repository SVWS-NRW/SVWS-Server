package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates auf Revision 71.
 */
public final class Revision71Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 71.
	 */
	public Revision71Updates() {
		super(SchemaRevisionen.REV_71);
		add("Bereinigt verwaiste Betreuungslehrer_ID-Referenzen in Schueler_AllgAdr vor dem Anlegen des Fremdschlüssels.",
				"""
				UPDATE Schueler_AllgAdr
				SET Betreuungslehrer_ID = NULL
				WHERE Betreuungslehrer_ID IS NOT NULL
				  AND Betreuungslehrer_ID NOT IN (SELECT ID FROM K_Lehrer);
				""",
				Schema.tab_Schueler_AllgAdr);
	}
}
