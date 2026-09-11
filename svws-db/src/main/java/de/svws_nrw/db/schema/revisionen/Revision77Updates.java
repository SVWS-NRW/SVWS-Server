package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 77.
 */
public class Revision77Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 77.
	 */
	public Revision77Updates() {
		super(SchemaRevisionen.REV_77);
		updateZeitstempel();
	}

	private void updateZeitstempel() {
		add("Setze die Zeitstempel für die Kursart-Zuweisungen bei Schüler-Lernabschnitten",
				"INSERT INTO %1$s(Abschnitt_ID, Fach_ID, tsKursart) SELECT Abschnitt_ID, Fach_ID, CURTIME(3) FROM %2$s;"
					.formatted(Schema.tab_TimestampsSchuelerZuweisungen.name(), Schema.tab_SchuelerZuweisungen.name()),
				Schema.tab_TimestampsSchuelerZuweisungen, Schema.tab_SchuelerZuweisungen
		);
	}

}
