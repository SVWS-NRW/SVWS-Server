package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 26.
 */
public class Revision26Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 26.
	 */
	public Revision26Updates() {
		super(SchemaRevisionen.REV_26);
		updateZeitstempel();
	}

	private void updateZeitstempel() {
		add("Setze/Aktualisisere die Zeitstempel für die Nodenmodul-Credentials der Lehrer",
				"INSERT INTO %1$s(Lehrer_ID, tsPasswordHash) SELECT Lehrer_ID, CURTIME(3) FROM %2$s WHERE Lehrer_ID NOT IN (SELECT Lehrer_ID FROM %1$s);"
					.formatted(Schema.tab_TimestampsLehrerNotenmodulCredentials.name(), Schema.tab_LehrerNotenmodulCredentials.name()),
				Schema.tab_TimestampsLehrerNotenmodulCredentials, Schema.tab_LehrerNotenmodulCredentials
		);
	}

}
