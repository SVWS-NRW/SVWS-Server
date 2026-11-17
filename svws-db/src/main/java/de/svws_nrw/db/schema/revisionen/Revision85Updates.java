package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 85. Die Angabe zu den internationalen Kontakten der Schule
 * wird aus der Tabelle EigeneSchule für den aktuellen Schuljahresabschnitt der
 * Schule in die neue Tabelle Statistik_Zusatzerhebungen übernommen. Ein
 * Datensatz wird dabei nur angelegt, wenn der Wert vom Standardwert abweicht. Die
 * Spalte hatInternationaleKontakte in EigeneSchule wird anschließend vom DBUpdater
 * entfernt.
 */
public final class Revision85Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 85.
	 */
	public Revision85Updates() {
		super(SchemaRevisionen.REV_85);
		add("Übernehme die Angabe zu den internationalen Kontakten aus EigeneSchule in die Tabelle Statistik_Zusatzerhebungen",
				"INSERT INTO %1$s(%2$s, %3$s) SELECT e.%4$s, e.%5$s FROM %6$s e JOIN %7$s s ON s.%8$s = e.%4$s WHERE e.%5$s = 1"
						.formatted(Schema.tab_Statistik_Zusatzerhebungen.name(),
								Schema.tab_Statistik_Zusatzerhebungen.col_idSchuljahresabschnitt.name(),
								Schema.tab_Statistik_Zusatzerhebungen.col_hatInternationaleKontakte.name(),
								Schema.tab_EigeneSchule.col_Schuljahresabschnitts_ID.name(),
								Schema.tab_EigeneSchule.col_hatInternationaleKontakte.name(),
								Schema.tab_EigeneSchule.name(),
								Schema.tab_Schuljahresabschnitte.name(),
								Schema.tab_Schuljahresabschnitte.col_ID.name()),
				Schema.tab_Statistik_Zusatzerhebungen, Schema.tab_EigeneSchule, Schema.tab_Schuljahresabschnitte);
	}

}
