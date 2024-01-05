package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Jahrgang_Fachwahlen.
 */
public class Tabelle_Gost_Jahrgang_Fachwahlen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Schuljahr, in welchem der Jahrgang das Abitur macht");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: ID des Faches in der Fächertabelle");

	/** Die Definition der Tabellenspalte EF1_Kursart */
	public SchemaTabelleSpalte col_EF1_Kursart = add("EF1_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in EF.1");

	/** Die Definition der Tabellenspalte EF2_Kursart */
	public SchemaTabelleSpalte col_EF2_Kursart = add("EF2_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in EF.2");

	/** Die Definition der Tabellenspalte Q11_Kursart */
	public SchemaTabelleSpalte col_Q11_Kursart = add("Q11_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in Q1.1");

	/** Die Definition der Tabellenspalte Q12_Kursart */
	public SchemaTabelleSpalte col_Q12_Kursart = add("Q12_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in Q1.2");

	/** Die Definition der Tabellenspalte Q21_Kursart */
	public SchemaTabelleSpalte col_Q21_Kursart = add("Q21_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in Q2.1");

	/** Die Definition der Tabellenspalte Q22_Kursart */
	public SchemaTabelleSpalte col_Q22_Kursart = add("Q22_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in Q2.2");

	/** Die Definition der Tabellenspalte AbiturFach */
	public SchemaTabelleSpalte col_AbiturFach = add("AbiturFach", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Abiturfach 1 bis 4 oder null");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Bemerkungen zum belegten Fach");



	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Fachwahlen_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Fachwahlen_Abi_Jahrgang_FK = addForeignKey(
			"Gost_Jahrgang_Fachwahlen_Abi_Jahrgang_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
		);

	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Fachwahlen_Fach_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Fachwahlen_Fach_ID_FK = addForeignKey(
			"Gost_Jahrgang_Fachwahlen_Fach_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);


	/** Die Definition des Non-Unique-Index Gost_Jahrgang_Fachwahlen_IDX_Abi_Jahrgang */
	public SchemaTabelleIndex index_Gost_Jahrgang_Fachwahlen_IDX_Abi_Jahrgang = addIndex("Gost_Jahrgang_Fachwahlen_IDX_Abi_Jahrgang",
		col_Abi_Jahrgang
	);

	/** Die Definition des Non-Unique-Index Gost_Jahrgang_Fachwahlen_IDX_Abi_Jahrgang_Fach_ID */
	public SchemaTabelleIndex index_Gost_Jahrgang_Fachwahlen_IDX_Abi_Jahrgang_Fach_ID = addIndex("Gost_Jahrgang_Fachwahlen_IDX_Abi_Jahrgang_Fach_ID",
		col_Abi_Jahrgang,
		col_Fach_ID
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Jahrgang_Fachwahlen.
	 */
	public Tabelle_Gost_Jahrgang_Fachwahlen() {
		super("Gost_Jahrgang_Fachwahlen", SchemaRevisionen.REV_4);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostJahrgangFachbelegungen");
		setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Informationen zu der jahrgangsspezifischen Vorlage zu von Schülern normalerweise gewählten Fächern");
	}

}
