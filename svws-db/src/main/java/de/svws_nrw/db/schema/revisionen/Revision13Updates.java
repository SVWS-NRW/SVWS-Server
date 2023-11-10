package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 13.
 */
public class Revision13Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 13.
	 */
	public Revision13Updates() {
		super(SchemaRevisionen.REV_10);
		updateSchuelerNachnameZusatz();
	}

	private void updateSchuelerNachnameZusatz() {
	    final String sql = "UPDATE %s la JOIN %s l ON la.%s = l.%s SET la.%s = l.%s";
		add("Übertrage die Information zur Stammschule in die Abschnittsdaten des Lehrers",
		    sql.formatted(Schema.tab_LehrerAbschnittsdaten.name(),
		    		Schema.tab_K_Lehrer.name(),
			        Schema.tab_LehrerAbschnittsdaten.col_Lehrer_ID.name(),
			        Schema.tab_K_Lehrer.col_ID.name(),
			        Schema.tab_LehrerAbschnittsdaten.col_StammschulNr.name(),
			        Schema.tab_K_Lehrer.col_StammschulNr.name()),
			Schema.tab_K_Lehrer, Schema.tab_LehrerAbschnittsdaten
		);
	}

}
