package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 29.
 */
public class Revision29Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 29.
	 */
	public Revision29Updates() {
		super(SchemaRevisionen.REV_29);
		updateZeitstempel();
	}

	private void updateZeitstempel() {
		add("Setze Zeitstempel für neue Spalte in %s.".formatted(Schema.tab_TimestampsSchuelerLernabschnittsdaten.name()),
				"UPDATE %s SET %s = CURTIME(3), %s = CURTIME(3), %s = CURTIME(3)"
						.formatted(Schema.tab_TimestampsSchuelerLernabschnittsdaten.name(),
								Schema.tab_TimestampsSchuelerLernabschnittsdaten.col_tsLELS.name(),
								Schema.tab_TimestampsSchuelerLernabschnittsdaten.col_tsESF.name(),
								Schema.tab_TimestampsSchuelerLernabschnittsdaten.col_tsBemerkungFSP.name()),
				Schema.tab_TimestampsSchuelerLernabschnittsdaten
		);
	}

}
