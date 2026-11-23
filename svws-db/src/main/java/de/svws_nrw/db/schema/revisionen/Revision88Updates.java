package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 88. Die neu angelegte Spalte Status (NOT NULL, Default 0) wird
 * aus der Hilfsspalte StatusTmp befüllt. Die Hilfsspalte wird anschließend
 * vom DBUpdater entfernt.
 */
public final class Revision88Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 88.
	 */
	public Revision88Updates() {
		super(SchemaRevisionen.REV_88);
		add("Übernehme den Status der Schüler aus der Hilfsspalte StatusTmp in die neu angelegte Spalte Status",
				"UPDATE %1$s SET %2$s = %3$s"
						.formatted(Schema.tab_Schueler.name(),
								Schema.tab_Schueler.col_Status.name(),
								Schema.tab_Schueler.col_StatusTmp.name()),
				Schema.tab_Schueler);
	}

}
