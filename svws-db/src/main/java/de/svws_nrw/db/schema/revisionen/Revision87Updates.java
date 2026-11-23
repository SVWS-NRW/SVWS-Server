package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 87. Der Status der Schüler wird in die Hilfsspalte StatusTmp
 * übernommen. Ein fehlender Status (NULL) wird dabei auf 0 (Neuaufnahme)
 * gesetzt. Die Spalte Status wird anschließend vom DBUpdater entfernt und
 * mit Revision 88 als NOT NULL neu angelegt.
 */
public final class Revision87Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 87.
	 */
	public Revision87Updates() {
		super(SchemaRevisionen.REV_87);
		add("Übernehme den Status der Schüler in die Hilfsspalte StatusTmp und setze einen fehlenden Status auf 0 (Neuaufnahme)",
				"UPDATE %1$s SET %2$s = COALESCE(%3$s, 0)"
						.formatted(Schema.tab_Schueler.name(),
								Schema.tab_Schueler.col_StatusTmp.name(),
								Schema.tab_Schueler.col_Status_Deprecated_Revision_87.name()),
				Schema.tab_Schueler);
	}

}
