package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 55.
 */
public final class Revision56Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 55.
	 */
	public Revision56Updates() {
		super(SchemaRevisionen.REV_56);
		add("Setzt in der Tabelle Katalog_Floskeln die Spalte Fach_ID auf NULL, wenn aktuell ein Wert gesetzt ist, dieser aber in der Tabelle"
						+ " EigeneSchule_Faecher nicht (mehr) als gültige ID existiert. Damit werden ungültige bzw. verwaiste Fremdschlüsselbezüge entfernt.",
				"""
				UPDATE Katalog_Floskeln
				SET Fach_ID = NULL
				WHERE Fach_ID IS NOT NULL
					AND Fach_ID NOT IN (
						SELECT ID
						FROM EigeneSchule_Faecher
						WHERE ID IS NOT NULL
				);
				""",
				Schema.tab_Katalog_Floskeln, Schema.tab_EigeneSchule_Faecher);
	}
}
