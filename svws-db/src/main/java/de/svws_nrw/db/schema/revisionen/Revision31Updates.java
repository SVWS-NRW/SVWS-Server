package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 31.
 */
public class Revision31Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 31.
	 */
	public Revision31Updates() {
		super(SchemaRevisionen.REV_31);
		add("Nachtragen der Schulgliederung in der Tabelle %s anhand von Fachklassen.".formatted(Schema.tab_Klassen.name()),
				"""
				UPDATE %1$s
				SET %2$s = (SELECT %6$s FROM %4$s WHERE %5$s = %1$s.%3$s)
				WHERE %1$s.%2$s IS NULL AND %1$s.%3$s IS NOT NULL
				""".formatted(Schema.tab_Klassen.name(), Schema.tab_Klassen.col_ASDSchulformNr.name(),
						Schema.tab_Klassen.col_Fachklasse_ID.name(),
						Schema.tab_EigeneSchule_Fachklassen.name(),
						Schema.tab_EigeneSchule_Fachklassen.col_ID.name(),
						Schema.tab_EigeneSchule_Fachklassen.col_BKIndexTyp.name()),
				Schema.tab_Klassen, Schema.tab_EigeneSchule_Fachklassen
		);
		add("Nachtragen der Schulgliederung in der Tabelle %s anhand der Vorgängerklasse im gleichen Schuljahresabschnitt.".formatted(Schema.tab_Klassen.name()),
				"""
				UPDATE %1$s k1
				SET k1.%2$s = (SELECT %2$s FROM %1$s k2 WHERE k1.%5$s = k2.%3$s AND k2.%2$s IS NOT NULL AND k1.%4$s = k2.%4$s)
				WHERE k1.%2$s IS NULL AND k1.%5$s IS NOT NULL
				""".formatted(Schema.tab_Klassen.name(), Schema.tab_Klassen.col_ASDSchulformNr.name(),
						Schema.tab_Klassen.col_Klasse.name(),
						Schema.tab_Klassen.col_Schuljahresabschnitts_ID.name(),
						Schema.tab_Klassen.col_VKlasse.name()),
				Schema.tab_Klassen
		);
		add("Nachtragen der Schulgliederung in der Tabelle %s anhand der Nachfolgerklasse im gleichen Schuljahresabschnitt.".formatted(Schema.tab_Klassen.name()),
				"""
				UPDATE %1$s k1
				SET k1.%2$s = (SELECT %2$s FROM %1$s k2 WHERE k1.%5$s = k2.%3$s AND k2.%2$s IS NOT NULL AND k1.%4$s = k2.%4$s)
				WHERE k1.%2$s IS NULL AND k1.%5$s IS NOT NULL
				""".formatted(Schema.tab_Klassen.name(), Schema.tab_Klassen.col_ASDSchulformNr.name(),
						Schema.tab_Klassen.col_Klasse.name(),
						Schema.tab_Klassen.col_Schuljahresabschnitts_ID.name(),
						Schema.tab_Klassen.col_FKlasse.name()),
				Schema.tab_Klassen
		);
		add("Nachtragen der Organisationsform in der Tabelle %s anhand der Vorgängerklasse im gleichen Schuljahresabschnitt.".formatted(Schema.tab_Klassen.name()),
				"""
				UPDATE %1$s k1
				SET k1.%2$s = (SELECT %2$s FROM %1$s k2 WHERE k1.%5$s = k2.%3$s AND k2.%2$s IS NOT NULL AND k1.%4$s = k2.%4$s)
				WHERE k1.%2$s IS NULL AND k1.%5$s IS NOT NULL
				""".formatted(Schema.tab_Klassen.name(), Schema.tab_Klassen.col_OrgFormKrz.name(),
						Schema.tab_Klassen.col_Klasse.name(),
						Schema.tab_Klassen.col_Schuljahresabschnitts_ID.name(),
						Schema.tab_Klassen.col_VKlasse.name()),
				Schema.tab_Klassen
		);
		add("Nachtragen der Organisationsform in der Tabelle %s anhand der Nachfolgerklasse im gleichen Schuljahresabschnitt.".formatted(Schema.tab_Klassen.name()),
				"""
				UPDATE %1$s k1
				SET k1.%2$s = (SELECT %2$s FROM %1$s k2 WHERE k1.%5$s = k2.%3$s AND k2.%2$s IS NOT NULL AND k1.%4$s = k2.%4$s)
				WHERE k1.%2$s IS NULL AND k1.%5$s IS NOT NULL
				""".formatted(Schema.tab_Klassen.name(), Schema.tab_Klassen.col_OrgFormKrz.name(),
						Schema.tab_Klassen.col_Klasse.name(),
						Schema.tab_Klassen.col_Schuljahresabschnitts_ID.name(),
						Schema.tab_Klassen.col_FKlasse.name()),
				Schema.tab_Klassen
		);
	}

}
