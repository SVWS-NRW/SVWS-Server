package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 22.
 */
public final class Revision22Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 22.
	 */
	public Revision22Updates() {
		super(SchemaRevisionen.REV_22);
		final var tab = Schema.tab_BenutzerKompetenzen;
		add("Ergänzen der Benutzerkompetenz UNTERRICHTSVERTEILUNG_ANSEHEN, falls die Benutzerkompetenz KATALOG_EINTRAEGE_ANSEHEN vorliegt - Tabelle "
				+ tab.name(),
				"INSERT INTO " + tab.name() + "(" + tab.col_Benutzer_ID.name() + "," + tab.col_Kompetenz_ID.name() + ")"
						+ " SELECT " + tab.col_Benutzer_ID.name() + ", 105 AS " + tab.col_Kompetenz_ID.name() + " FROM " + tab.name() + " WHERE "
						+ tab.col_Kompetenz_ID.name() + " = 81",
				tab
		);
		add("Ergänzen der Benutzerkompetenz UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN, falls die Benutzerkompetenz KATALOG_EINTRAEGE_ANSEHEN vorliegt - Tabelle "
				+ tab.name(),
				"INSERT INTO " + tab.name() + "(" + tab.col_Benutzer_ID.name() + "," + tab.col_Kompetenz_ID.name() + ")"
						+ " SELECT " + tab.col_Benutzer_ID.name() + ", 106 AS " + tab.col_Kompetenz_ID.name() + " FROM " + tab.name() + " WHERE "
						+ tab.col_Kompetenz_ID.name() + " = 81",
				tab
		);
		add("Ergänzen der Benutzerkompetenz UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN, falls die Benutzerkompetenz KATALOG_EINTRAEGE_AENDERN vorliegt - Tabelle "
				+ tab.name(),
				"INSERT INTO " + tab.name() + "(" + tab.col_Benutzer_ID.name() + "," + tab.col_Kompetenz_ID.name() + ")"
						+ " SELECT " + tab.col_Benutzer_ID.name() + ", 107 AS " + tab.col_Kompetenz_ID.name() + " FROM " + tab.name() + " WHERE "
						+ tab.col_Kompetenz_ID.name() + " = 82",
				tab
		);
		final var tab2 = Schema.tab_BenutzergruppenKompetenzen;
		add("Ergänzen der Benutzerkompetenz UNTERRICHTSVERTEILUNG_ANSEHEN, falls die Benutzerkompetenz KATALOG_EINTRAEGE_ANSEHEN vorliegt - Tabelle "
				+ tab2.name(),
				"INSERT INTO " + tab2.name() + "(" + tab2.col_Gruppe_ID.name() + "," + tab2.col_Kompetenz_ID.name() + ")"
						+ " SELECT " + tab2.col_Gruppe_ID.name() + ", 105 AS " + tab2.col_Kompetenz_ID.name() + " FROM " + tab2.name() + " WHERE "
						+ tab2.col_Kompetenz_ID.name() + " = 81",
				tab2
		);
		add("Ergänzen der Benutzerkompetenz UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN, falls die Benutzerkompetenz KATALOG_EINTRAEGE_ANSEHEN vorliegt - Tabelle "
				+ tab2.name(),
				"INSERT INTO " + tab2.name() + "(" + tab2.col_Gruppe_ID.name() + "," + tab2.col_Kompetenz_ID.name() + ")"
						+ " SELECT " + tab2.col_Gruppe_ID.name() + ", 106 AS " + tab2.col_Kompetenz_ID.name() + " FROM " + tab2.name() + " WHERE "
						+ tab2.col_Kompetenz_ID.name() + " = 81",
				tab2
		);
		add("Ergänzen der Benutzerkompetenz UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN, falls die Benutzerkompetenz KATALOG_EINTRAEGE_AENDERN vorliegt - Tabelle "
				+ tab2.name(),
				"INSERT INTO " + tab2.name() + "(" + tab2.col_Gruppe_ID.name() + "," + tab2.col_Kompetenz_ID.name() + ")"
						+ " SELECT " + tab2.col_Gruppe_ID.name() + ", 107 AS " + tab2.col_Kompetenz_ID.name() + " FROM " + tab2.name() + " WHERE "
						+ tab2.col_Kompetenz_ID.name() + " = 82",
				tab2
		);
	}

}
