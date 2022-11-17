package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Schueler_Fachwahlen.
 */
public class Tabelle_Gost_Schueler_Fachwahlen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Schülers in der Schuelertabelle");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Faches in der Fächertabelle");

	/** Die Definition der Tabellenspalte EF1_Kursart */
	public SchemaTabelleSpalte col_EF1_Kursart = add("EF1_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in EF.1");

	/** Die Definition der Tabellenspalte EF1_Punkte */
	public SchemaTabelleSpalte col_EF1_Punkte = add("EF1_Punkte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in EF.1");

	/** Die Definition der Tabellenspalte EF2_Kursart */
	public SchemaTabelleSpalte col_EF2_Kursart = add("EF2_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in EF.2");

	/** Die Definition der Tabellenspalte EF2_Punkte */
	public SchemaTabelleSpalte col_EF2_Punkte = add("EF2_Punkte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in EF.2");

	/** Die Definition der Tabellenspalte Q11_Kursart */
	public SchemaTabelleSpalte col_Q11_Kursart = add("Q11_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in Q1.1");

	/** Die Definition der Tabellenspalte Q11_Punkte */
	public SchemaTabelleSpalte col_Q11_Punkte = add("Q11_Punkte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in Q1.1");

	/** Die Definition der Tabellenspalte Q12_Kursart */
	public SchemaTabelleSpalte col_Q12_Kursart = add("Q12_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in Q1.2");

	/** Die Definition der Tabellenspalte Q12_Punkte */
	public SchemaTabelleSpalte col_Q12_Punkte = add("Q12_Punkte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in Q1.2");

	/** Die Definition der Tabellenspalte Q21_Kursart */
	public SchemaTabelleSpalte col_Q21_Kursart = add("Q21_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in Q2.1");

	/** Die Definition der Tabellenspalte Q21_Punkte */
	public SchemaTabelleSpalte col_Q21_Punkte = add("Q21_Punkte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in Q2.1");

	/** Die Definition der Tabellenspalte Q22_Kursart */
	public SchemaTabelleSpalte col_Q22_Kursart = add("Q22_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in Q2.2");

	/** Die Definition der Tabellenspalte Q22_Punkte */
	public SchemaTabelleSpalte col_Q22_Punkte = add("Q22_Punkte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in Q2.2");

	/** Die Definition der Tabellenspalte AbiturFach */
	public SchemaTabelleSpalte col_AbiturFach = add("AbiturFach", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Abiturfach 1 bis 4 oder null");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Bemerkungen zum belegten Fach");

	/** Die Definition der Tabellenspalte Markiert_Q1 */
	public SchemaTabelleSpalte col_Markiert_Q1 = add("Markiert_Q1", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q1.1 für die Einbringung in das Abitur markiert wurde");

	/** Die Definition der Tabellenspalte Markiert_Q2 */
	public SchemaTabelleSpalte col_Markiert_Q2 = add("Markiert_Q2", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q1.2 für die Einbringung in das Abitur markiert wurde");

	/** Die Definition der Tabellenspalte Markiert_Q3 */
	public SchemaTabelleSpalte col_Markiert_Q3 = add("Markiert_Q3", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q2.1 für die Einbringung in das Abitur markiert wurde");

	/** Die Definition der Tabellenspalte Markiert_Q4 */
	public SchemaTabelleSpalte col_Markiert_Q4 = add("Markiert_Q4", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q2.2 für die Einbringung in das Abitur markiert wurde");

	/** Die Definition der Tabellenspalte ergebnisAbiturpruefung */
	public SchemaTabelleSpalte col_ergebnisAbiturpruefung = add("ergebnisAbiturpruefung", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Das Ergebnis der Abiturprüfung in einem Abiturfach (1. - 4. Fach)");

	/** Die Definition der Tabellenspalte hatMuendlichePflichtpruefung */
	public SchemaTabelleSpalte col_hatMuendlichePflichtpruefung = add("hatMuendlichePflichtpruefung", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob eine mündliche Pflichtprüfung im 1. - 3. Fach angesetzt werden muss (null - unbekannt, 0 - Nein, 1 - Ja)  ");

	/** Die Definition der Tabellenspalte ergebnisMuendlichePruefung */
	public SchemaTabelleSpalte col_ergebnisMuendlichePruefung = add("ergebnisMuendlichePruefung", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Ergebnis der mündlichen Prüfung im 1. - 3. Fach");


	/** Die Definition des Fremdschlüssels Gost_Schueler_Fachwahlen_Schueler_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Schueler_Fachwahlen_Schueler_ID_FK = addForeignKey(
			"Gost_Schueler_Fachwahlen_Schueler_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Fachwahlen_Fach_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Schueler_Fachwahlen_Fach_ID_FK = addForeignKey(
			"Gost_Schueler_Fachwahlen_Fach_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Schueler_Fachwahlen.
	 */
	public Tabelle_Gost_Schueler_Fachwahlen() {
		super("Gost_Schueler_Fachwahlen", SchemaRevisionen.REV_4);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostSchuelerFachbelegungen");
		setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Informationen zu den vom Schüler gewählten Fächern");
	}

}
