package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 20.
 */
public final class Revision20Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 20.
	 */
	public Revision20Updates() {
		super(SchemaRevisionen.REV_20);
		add("Primärschlüssel entfernen - Tabelle Fach_Gliederungen",
				"ALTER TABLE %s DROP PRIMARY KEY".formatted(Schema.tab_Fach_Gliederungen.name()),
				Schema.tab_Fach_Gliederungen
		);
		add("Primärschlüssel neu setzen - Tabelle Fach_Gliederungen",
				"ALTER TABLE %s ADD CONSTRAINT pk_%s PRIMARY KEY(%s, %s, %s)".formatted(Schema.tab_Fach_Gliederungen.name(),
						Schema.tab_Fach_Gliederungen.name(), Schema.tab_Fach_Gliederungen.col_Fach_ID.name(),
						Schema.tab_Fach_Gliederungen.col_Gliederung.name(), Schema.tab_Fach_Gliederungen.col_Fachklasse_ID.name()),
				Schema.tab_Fach_Gliederungen
		);
	}

}
