package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 58.
 */
public final class Revision58Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 58.
	 */
	public Revision58Updates() {
		super(SchemaRevisionen.REV_58);
		add("Setzt in der Tabelle Klassen die Spalte AdrMerkmal auf NULL, wenn aktuell ein Wert gesetzt ist, "
						+ "dieser aber in der Tabelle EigeneSchule_Teilstandorte nicht als gültiges Adressmerkmal existiert. "
						+ "Dies bereitet die Aktivierung der Fremdschlüsselbeziehung vor.",
				"""
				UPDATE Klassen
				SET AdrMerkmal = NULL
				WHERE AdrMerkmal IS NOT NULL
				   AND AdrMerkmal NOT IN (
					  SELECT AdrMerkmal
					  FROM EigeneSchule_Teilstandorte
				   );
				""",
				Schema.tab_Klassen, Schema.tab_EigeneSchule_Teilstandorte);
	}
}
