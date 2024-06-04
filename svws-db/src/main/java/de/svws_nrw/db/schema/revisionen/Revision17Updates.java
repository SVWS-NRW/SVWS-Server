package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 17.
 */
public final class Revision17Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 17.
	 */
	public Revision17Updates() {
		super(SchemaRevisionen.REV_17);
		add("Stundenplan: Verschieben der Informationen zu den Wochentypen bei Pausenaufsichten in die Zuordnung zu den Aufsichtsbereichen.",
			"UPDATE %s b JOIN %s a ON b.Pausenaufsicht_ID = a.ID SET b.Wochentyp = a.Wochentyp"
				.formatted(Schema.tab_Stundenplan_PausenaufsichtenBereich.name(), Schema.tab_Stundenplan_Pausenaufsichten.name()),
			Schema.tab_Stundenplan_Pausenaufsichten, Schema.tab_Stundenplan_PausenaufsichtenBereich
		);
	}

}
