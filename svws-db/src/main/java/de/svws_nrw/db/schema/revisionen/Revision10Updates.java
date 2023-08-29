package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 10.
 */
public class Revision10Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 10.
	 */
	public Revision10Updates() {
		super(SchemaRevisionen.REV_10);
		updateSchuelerNachnameZusatz();
	}

	private void updateSchuelerNachnameZusatz() {
	    final String sql = "UPDATE %s SET %s = concat(%s, ' ', %s) WHERE %s IS NOT NULL";
		add("Kopiere den Namenszusatz an den Anfang des Nachnamens",
		    sql.formatted(Schema.tab_Schueler.name(),
			        Schema.tab_Schueler.col_Name.name(),
			        Schema.tab_Schueler.col_ZusatzNachname.name(),
			        Schema.tab_Schueler.col_Name.name(),
			        Schema.tab_Schueler.col_ZusatzNachname.name()),
			Schema.tab_Schueler
		);
        add("Erzieher 1 - Kopiere den Namenszusatz an den Anfang des Nachnamens",
            sql.formatted(Schema.tab_SchuelerErzAdr.name(),
                    Schema.tab_SchuelerErzAdr.col_Name1.name(),
                    Schema.tab_SchuelerErzAdr.col_Erz1ZusatzNachname.name(),
                    Schema.tab_SchuelerErzAdr.col_Name1.name(),
                    Schema.tab_SchuelerErzAdr.col_Erz1ZusatzNachname.name()),
            Schema.tab_SchuelerErzAdr
        );
        add("Erzieher 2 - Kopiere den Namenszusatz an den Anfang des Nachnamens",
            sql.formatted(Schema.tab_SchuelerErzAdr.name(),
                    Schema.tab_SchuelerErzAdr.col_Name2.name(),
                    Schema.tab_SchuelerErzAdr.col_Erz2ZusatzNachname.name(),
                    Schema.tab_SchuelerErzAdr.col_Name2.name(),
                    Schema.tab_SchuelerErzAdr.col_Erz2ZusatzNachname.name()),
            Schema.tab_SchuelerErzAdr
        );
	}

}
