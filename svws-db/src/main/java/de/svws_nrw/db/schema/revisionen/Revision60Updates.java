package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 60.
 */
public class Revision60Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 60.
	 */
	public Revision60Updates() {
		super(SchemaRevisionen.REV_60);
		updateZeitstempel();
	}

	private void updateZeitstempel() {
		add("Erstelle die Zeitstempel für die Tabelle SchuelerZP10",
				"""
				INSERT INTO %s(ID, tsVornote, tsNoteSchriftlichePruefung, tsMuendlichePruefung, tsMuendlichePruefungFreiwillig, tsNoteMuendlichePruefung, tsAbschlussnote)
				SELECT ID, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3) FROM %s;
				"""
				.formatted(Schema.tab_TimestampsSchuelerZP10.name(), Schema.tab_SchuelerZP10.name()),
				Schema.tab_TimestampsSchuelerZP10, Schema.tab_SchuelerZP10
		);
	}

}
