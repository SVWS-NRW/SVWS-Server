package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 52.
 */
public final class Revision52Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 52.
	 */
	public Revision52Updates() {
		super(SchemaRevisionen.REV_52);
		add("Katalog_Floskeln_Gruppen: Kopieren der Daten aus der Tabelle Floskelgruppen und ergänzen einer ID.",
				"""
				INSERT INTO Katalog_Floskeln_Gruppen(ID, Kuerzel, Hauptgruppe_ID, Bezeichnung, Farbe)
				SELECT ROW_NUMBER() OVER() AS ID, Kuerzel,
				CASE Hauptgruppe WHEN 'ASV' THEN 1 WHEN 'AUE' THEN 2 WHEN 'FACH' THEN 3 WHEN 'FSP' THEN 4 WHEN 'FOERD' THEN 5
				    WHEN 'FÖRD' THEN 5 WHEN 'VERM' THEN 6 WHEN 'VERS' THEN 7 WHEN 'ZB' THEN 8
				    WHEN 'LELS' THEN 9 WHEN 'ÜG45' THEN 10 ELSE 0 END AS Hauptgruppe_ID,
				Bezeichnung, Farbe FROM Floskelgruppen
				""",
				Schema.tab_Floskelgruppen, Schema.tab_Katalog_Floskeln_Gruppen);
		add("Katalog_Floskeln: Kopieren der Daten aus der Tabelle Floskeln und ergänzen einer ID.",
				"""
				INSERT INTO Katalog_Floskeln(ID, Kuerzel, Text, Gruppe_ID, Fach_ID, Niveau)
				SELECT ROW_NUMBER() OVER() AS ID,
				fl.Kuerzel,
				fl.FloskelText as Text,
				g.ID AS Gruppe_ID,
				esf.ID AS Fach_ID,
				CASE WHEN (fl.FloskelNiveau REGEXP '^[0-9]+$') THEN CAST(fl.FloskelNiveau AS int) ELSE NULL END AS Niveau
				FROM Floskeln fl LEFT JOIN Katalog_Floskeln_Gruppen g ON fl.FloskelGruppe = g.Kuerzel
				LEFT JOIN EigeneSchule_Faecher esf ON fl.FloskelFach = esf.FachKrz
				""",
				Schema.tab_Floskeln, Schema.tab_Katalog_Floskeln_Gruppen, Schema.tab_Katalog_Floskeln, Schema.tab_EigeneSchule_Faecher);
		add("Katalog_Floskeln_Jahrgaenge: Erzeugen der Tabelle mit den Jahrgangszuordnungen der Floskeln.",
				"""
				INSERT INTO Katalog_Floskeln_Jahrgaenge(ID, Floskel_ID, Jahrgang_ID)
				SELECT ROW_NUMBER() OVER() AS ID,
				kfl.ID AS Floskel_ID,
				esj.ID AS Jahrgang_ID
				FROM Katalog_Floskeln kfl JOIN Floskeln fl ON kfl.Kuerzel = fl.Kuerzel AND fl.FloskelJahrgang IS NOT NULL
				JOIN EigeneSchule_Jahrgaenge esj ON fl.FloskelJahrgang = esj.ASDJahrgang
				""",
				Schema.tab_Floskeln, Schema.tab_Katalog_Floskeln_Gruppen, Schema.tab_Katalog_Floskeln, Schema.tab_Katalog_Floskeln_Jahrgaenge, Schema.tab_EigeneSchule_Jahrgaenge);
	}
}
