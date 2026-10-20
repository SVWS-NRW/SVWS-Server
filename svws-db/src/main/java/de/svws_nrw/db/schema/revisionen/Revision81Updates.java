package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 81.
 */
public class Revision81Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 81.
	 */
	public Revision81Updates() {
		super(SchemaRevisionen.REV_81);
		updateSchuelerStatus();
	}

	private void updateSchuelerStatus() {
		add("Tabelle %s: Setze den Status von Schülern ohne Status auf 0 (Neuaufnahme)".formatted(Schema.tab_Schueler.name()),
				"UPDATE %s SET Status = 0 WHERE Status IS NULL".formatted(Schema.tab_Schueler.name()),
				Schema.tab_Schueler
		);
	}

}
