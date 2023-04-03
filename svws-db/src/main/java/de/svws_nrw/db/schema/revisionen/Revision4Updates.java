package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 3.
 */
public final class Revision4Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 4.
	 */
	public Revision4Updates() {
		super(SchemaRevisionen.REV_4);
		updateFachkombinationen();
	}

	private void updateFachkombinationen() {
		// Erzeuge einen Default-Eintrag für die Vorlage von Abiturjahrgängen
		add("Erzeuge einen Default-Eintrag für die Vorlage von Abiturjahrgängen",
				"""
				INSERT INTO Gost_Jahrgangsdaten(Abi_Jahrgang,ZusatzkursGEVorhanden,ZusatzkursGEErstesHalbjahr,ZusatzkursSWVorhanden,ZusatzkursSWErstesHalbjahr,TextBeratungsbogen,TextMailversand)
				VALUES (-1,true,'Q2.1',true,'Q2.1','','')
				""", Schema.tab_Gost_Jahrgangsdaten
		);

		// Übertrage die Daten aus der Tabelle NichtMoeglAbiFachKombi in die Tabelle Gost_Jahrgang_Fachkombinationen (zuerst eine Richtung der Regel)
		add("Übertrage Regeln aus NichtMoeglAbiFachKombi nach Gost_Jahrgang_Fachkombinationen (Richtung 1)",
			"""
			INSERT INTO Gost_Jahrgang_Fachkombinationen(Abi_Jahrgang,Fach1_ID,Fach2_ID,Kursart1,Kursart2,EF1,EF2,Q11,Q12,Q21,Q22,Typ,Hinweistext)
			SELECT
			  -1,
			  nmk.Fach1_ID,
			  nmk.Fach2_ID,
			  nmk.Kursart1,
			  nmk.Kursart2,
			  nmk.Phase <> 'Q1Q4' AS EF1,
			  nmk.Phase <> 'Q1Q4' AS EF2,
			  1 AS Q11,
			  1 AS Q12,
			  1 AS Q21,
			  1 AS Q22,
			  CASE WHEN nmk.Typ = '+' THEN 1 ELSE 0 END AS Typ,
			  CONCAT(f1.FachKrz,
			    CASE WHEN nmk.Kursart1 IS NULL THEN '' ELSE CONCAT(' als ', nmk.Kursart1) END,
			    CASE WHEN nmk.Typ = '+' THEN ' erfordert ' ELSE ' erlaubt kein ' END,
			    f2.FachKrz,
			    CASE WHEN nmk.Kursart2 IS NULL THEN '' ELSE CONCAT(' als ', nmk.Kursart2) END
			  ) AS Hinweistext
			FROM
			  NichtMoeglAbiFachKombi nmk JOIN EigeneSchule_Faecher f1 ON nmk.Fach1_ID = f1.ID
			                             JOIN EigeneSchule_Faecher f2 ON nmk.Fach2_ID = f2.ID;
			""",
			Schema.tab_Gost_Jahrgang_Fachkombinationen, Schema.tab_NichtMoeglAbiFachKombi, Schema.tab_EigeneSchule_Faecher
		);

		// Übertrage die Daten aus der Tabelle NichtMoeglAbiFachKombi in die Tabelle Gost_Jahrgang_Fachkombinationen (dann die andere Richtung der Regel)
		add("Übertrage Regeln aus NichtMoeglAbiFachKombi nach Gost_Jahrgang_Fachkombinationen (Richtung 2)",
				"""
				INSERT INTO Gost_Jahrgang_Fachkombinationen(Abi_Jahrgang,Fach1_ID,Fach2_ID,Kursart1,Kursart2,EF1,EF2,Q11,Q12,Q21,Q22,Typ,Hinweistext)
				SELECT
				  -1,
				  nmk.Fach2_ID,
				  nmk.Fach1_ID,
				  nmk.Kursart2,
				  nmk.Kursart1,
				  nmk.Phase <> 'Q1Q4' AS EF1,
				  nmk.Phase <> 'Q1Q4' AS EF2,
				  1 AS Q11,
				  1 AS Q12,
				  1 AS Q21,
				  1 AS Q22,
				  CASE WHEN nmk.Typ = '+' THEN 1 ELSE 0 END AS Typ,
				  CONCAT(f2.FachKrz,
				    CASE WHEN nmk.Kursart2 IS NULL THEN '' ELSE CONCAT(' als ', nmk.Kursart2) END,
				    CASE WHEN nmk.Typ = '+' THEN ' erfordert ' ELSE ' erlaubt kein ' END,
				    f1.FachKrz,
				    CASE WHEN nmk.Kursart1 IS NULL THEN '' ELSE CONCAT(' als ', nmk.Kursart1) END
				  ) AS Hinweistext
				FROM
				  NichtMoeglAbiFachKombi nmk JOIN EigeneSchule_Faecher f1 ON nmk.Fach1_ID = f1.ID
				                             JOIN EigeneSchule_Faecher f2 ON nmk.Fach2_ID = f2.ID;
				""",
				Schema.tab_Gost_Jahrgang_Fachkombinationen, Schema.tab_NichtMoeglAbiFachKombi, Schema.tab_EigeneSchule_Faecher
			);
	}

}
