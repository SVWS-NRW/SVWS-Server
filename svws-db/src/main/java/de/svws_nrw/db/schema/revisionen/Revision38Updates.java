package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 38.
 */
public final class Revision38Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 38.
	 */
	public Revision38Updates() {
		super(SchemaRevisionen.REV_38);
		add("Tabelle Stundenplan: Setze das Flag aktiv, wenn keine Überschneidungen bei den Datumsangaben vorliegen",
				"""
				UPDATE Stundenplan sp1
				SET Aktiv = 1
				WHERE NOT EXISTS (
				    SELECT 1
				    FROM Stundenplan sp2
				    WHERE sp1.ID <> sp2.ID AND sp1.Schuljahresabschnitts_ID = sp2.Schuljahresabschnitts_ID AND sp1.Beginn <= sp2.Ende AND sp1.Ende >= sp2.Beginn
				)
				""",
				Schema.tab_Stundenplan
		);
	}

}
