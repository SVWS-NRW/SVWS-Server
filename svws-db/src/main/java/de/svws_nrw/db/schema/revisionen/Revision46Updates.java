package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 46.
 */
public final class Revision46Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 46.
	 */
	public Revision46Updates() {
		super(SchemaRevisionen.REV_46);
		add("Klassen: Korrektur von Einträgen - Undefiniert/Default auf Regelklasse setzen, sofern es sich nicht um die Schulform WB handelt.",
				"""
				UPDATE Klassen SET Klassenart = 'RK' WHERE Klassenart = '**' AND 'WB' <> (SELECT SchulformKrz FROM EigeneSchule LIMIT 1)
				""",
				Schema.tab_Klassen, Schema.tab_EigeneSchule
		);
		add("Klassen: Korrektur von Einträgen - Undefiniert/Default auf Regelklasse setzen, sofern es sich um die Schulform WB handelt und das Schuljahr nach 2023 ist.",
				"""
				UPDATE Klassen kl JOIN Schuljahresabschnitte s ON kl.Schuljahresabschnitts_ID = s.ID
				SET kl.Klassenart = 'RK'
				WHERE kl.Klassenart = '**' AND 'WB' = (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) AND s.Jahr >= 2024
				""",
				Schema.tab_Klassen, Schema.tab_Schuljahresabschnitte, Schema.tab_EigeneSchule
		);
	}

}
