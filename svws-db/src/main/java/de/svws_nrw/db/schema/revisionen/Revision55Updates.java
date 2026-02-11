package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 55.
 */
public final class Revision55Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 55.
	 */
	public Revision55Updates() {
		super(SchemaRevisionen.REV_55);
		add("Kopieren der Daten aus K_Ankreuzfloskel und EigeneSchule_Jahrgaenge und Anlegen der Zuordnung in Ankreuzkompetenz_Jahrgaenge.",
				"""
				INSERT INTO Ankreuzkompetenz_Jahrgang
				(
					ID,
					ID_Ankreuzkompetenz,
					ID_Jahrgang
				)
				SELECT
					ROW_NUMBER() OVER (ORDER BY af.ID, ej.ID) AS ID,
					af.ID                                     AS IDAnkreuzkompetenz,
					ej.ID                                     AS IDJahrgang
					FROM K_Ankreuzfloskeln af
						INNER JOIN EigeneSchule_Jahrgaenge ej
							ON af.Jahrgang = ej.ASDJahrgang;
				""",
				Schema.tab_K_Ankreuzfloskeln, Schema.tab_EigeneSchule_Jahrgaenge, Schema.tab_Ankreuzkompetenz_Jahrgang);
	}
}
