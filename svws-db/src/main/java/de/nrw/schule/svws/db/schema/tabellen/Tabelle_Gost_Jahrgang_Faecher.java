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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Jahrgang_Faecher.
 */
public class Tabelle_Gost_Jahrgang_Faecher extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: ID des Faches in der Fächertabelle");

	/** Die Definition der Tabellenspalte WaehlbarEF1 */
	public SchemaTabelleSpalte col_WaehlbarEF1 = add("WaehlbarEF1", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Belegung des Faches in der EF.1 möglich: 1 - true, 0 - false ");

	/** Die Definition der Tabellenspalte WaehlbarEF2 */
	public SchemaTabelleSpalte col_WaehlbarEF2 = add("WaehlbarEF2", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Belegung des Faches in der EF.2 möglich: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte WaehlbarQ11 */
	public SchemaTabelleSpalte col_WaehlbarQ11 = add("WaehlbarQ11", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Belegung des Faches in der Q1.1 möglich: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte WaehlbarQ12 */
	public SchemaTabelleSpalte col_WaehlbarQ12 = add("WaehlbarQ12", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Belegung des Faches in der Q1.2 möglich: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte WaehlbarQ21 */
	public SchemaTabelleSpalte col_WaehlbarQ21 = add("WaehlbarQ21", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Belegung des Faches in der Q2.1 möglich: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte WaehlbarQ22 */
	public SchemaTabelleSpalte col_WaehlbarQ22 = add("WaehlbarQ22", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Belegung des Faches in der Q2.2 möglich: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte WaehlbarAbiGK */
	public SchemaTabelleSpalte col_WaehlbarAbiGK = add("WaehlbarAbiGK", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Eintrag, ob das Fach als 3. oder 4. Abiturfach gewählt werden kann: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte WaehlbarAbiLK */
	public SchemaTabelleSpalte col_WaehlbarAbiLK = add("WaehlbarAbiLK", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Eintrag, ob das Fach als 1. oder 2. Abiturfach (LK) gewählt werden kann: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte WochenstundenEF1 */
	public SchemaTabelleSpalte col_WochenstundenEF1 = add("WochenstundenEF1", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Anzahl der Unterrichtsstunden in 45-minuten-Einheiten des Faches in der EF.1");

	/** Die Definition der Tabellenspalte WochenstundenEF2 */
	public SchemaTabelleSpalte col_WochenstundenEF2 = add("WochenstundenEF2", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Anzahl der Unterrichtsstunden in 45-minuten-Einheiten des Faches in der EF.2");

	/** Die Definition der Tabellenspalte WochenstundenQPhase */
	public SchemaTabelleSpalte col_WochenstundenQPhase = add("WochenstundenQPhase", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Anzahl der Unterrichtsstunden in 45-minuten-Einheiten des Faches in der Qualifikationsphase");

	/** Die Definition der Tabellenspalte SchiftlichkeitEF1 */
	public SchemaTabelleSpalte col_SchiftlichkeitEF1 = add("SchiftlichkeitEF1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Mündlich oder Schriftlich bei dem Fach in der EF1");

	/** Die Definition der Tabellenspalte SchiftlichkeitEF2 */
	public SchemaTabelleSpalte col_SchiftlichkeitEF2 = add("SchiftlichkeitEF2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Mündlich oder Schriftlich dei dem Fach in der EF2");

	/** Die Definition der Tabellenspalte NurMuendlich */
	public SchemaTabelleSpalte col_NurMuendlich = add("NurMuendlich", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Gibt an, ob das Fach nur mündlich belegt werden kann: 1 - true, 0 - false");


	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Faecher_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Faecher_Abi_Jahrgang_FK = addForeignKey(
			"Gost_Jahrgang_Faecher_Abi_Jahrgang_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
		);

	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Faecher_Fach_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Faecher_Fach_ID_FK = addForeignKey(
			"Gost_Jahrgang_Faecher_Fach_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Jahrgang_Faecher.
	 */
	public Tabelle_Gost_Jahrgang_Faecher() {
		super("Gost_Jahrgang_Faecher", SchemaRevisionen.REV_4);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostJahrgangFaecher");
		setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Informationen zu den wählbaren Fächern der einzelnen Jahrgänge");
	}

}
