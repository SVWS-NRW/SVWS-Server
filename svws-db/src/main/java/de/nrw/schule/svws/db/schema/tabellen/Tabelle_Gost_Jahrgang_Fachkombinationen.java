package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.gost.GostLaufbahnplanungFachkombinationTypConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Jahrgang_Fachkombinationen.
 */
public class Tabelle_Gost_Jahrgang_Fachkombinationen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datensatzes");

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, false)
		.setNotNull()
		.setDefault("-1")
		.setJavaComment("Schuljahr, in welchem der Jahrgang das Abitur macht");

	/** Die Definition der Tabellenspalte Fach1_ID */
	public SchemaTabelleSpalte col_Fach1_ID = add("Fach1_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des ersten Faches der nicht möglichen Fächerkombination / eines Fächerprofils");

	/** Die Definition der Tabellenspalte Fach2_ID */
	public SchemaTabelleSpalte col_Fach2_ID = add("Fach2_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des zweiten Faches der nicht möglichen Fächerkombination / eines Fächerprofils");

	/** Die Definition der Tabellenspalte Kursart1 */
	public SchemaTabelleSpalte col_Kursart1 = add("Kursart1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Die Kursart des ersten Faches der nicht möglichen Fächerkombination / eines Fächerprofils");

	/** Die Definition der Tabellenspalte Kursart2 */
	public SchemaTabelleSpalte col_Kursart2 = add("Kursart2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Die Kursart des zweiten Faches der nicht möglichen Fächerkombination / eines Fächerprofils");

	/** Die Definition der Tabellenspalte EF1 */
	public SchemaTabelleSpalte col_EF1 = add("EF1", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setNotNull()
			.setDefault("1")
			.setJavaComment("Gibt an, ob die Regel in EF.1 angewendet werden soll.");
	
	/** Die Definition der Tabellenspalte EF2 */
	public SchemaTabelleSpalte col_EF2 = add("EF2", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setNotNull()
			.setDefault("1")
			.setJavaComment("Gibt an, ob die Regel in EF.2 angewendet werden soll.");
	
	/** Die Definition der Tabellenspalte Q11 */
	public SchemaTabelleSpalte col_Q11 = add("Q11", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setNotNull()
			.setDefault("1")
			.setJavaComment("Gibt an, ob die Regel in Q1.1 angewendet werden soll.");
	
	
	/** Die Definition der Tabellenspalte Q12 */
	public SchemaTabelleSpalte col_Q12 = add("Q12", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setNotNull()
			.setDefault("1")
			.setJavaComment("Gibt an, ob die Regel in Q1.2 angewendet werden soll.");
	
	/** Die Definition der Tabellenspalte Q21 */
	public SchemaTabelleSpalte col_Q21 = add("Q21", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setNotNull()
			.setDefault("1")
			.setJavaComment("Gibt an, ob die Regel in Q2.1 angewendet werden soll.");
	
	/** Die Definition der Tabellenspalte Q22 */
	public SchemaTabelleSpalte col_Q22 = add("Q22", SchemaDatentypen.INT, false)
			.setConverter(Boolean01Converter.class)
			.setNotNull()
			.setDefault("1")
			.setJavaComment("Gibt an, ob die Regel in Q2.2 angewendet werden soll.");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.INT, false)
		.setConverter(GostLaufbahnplanungFachkombinationTypConverter.class)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Gibt an, ob es sich um eine nicht mögliche Fächerkombination (0) oder ein Fächerprofil handelt (1)");

	/** Ein Kurzer Hinweistext zu der Fachkombination */
	public SchemaTabelleSpalte col_Hinweistext = add("Hinweistext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setDefault("")
			.setNotNull()
			.setJavaComment("Hinweistext, der ausgegeben wird, wenn das Fachprofil / die nicht mögliche Fächerkombination nicht erfüllt wird.");


	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Fachkombinationen_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Fachkombinationen_Abi_Jahrgang_FK = addForeignKey(
			"Gost_Jahrgang_Fachkombinationen_Abi_Jahrgang_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
		);

	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Fachkombinationen_Fach1_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Fachkombinationen_Fach1_ID_FK = addForeignKey(
			"Gost_Jahrgang_Fachkombinationen_Fach1_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach1_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Fachkombinationen_Fach2_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Fachkombinationen_Fach2_ID_FK = addForeignKey(
			"Gost_Jahrgang_Fachkombinationen_Fach2_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach2_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Jahrgang_Fachkombinationen.
	 */
	public Tabelle_Gost_Jahrgang_Fachkombinationen() {
		super("Gost_Jahrgang_Fachkombinationen", SchemaRevisionen.REV_4);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostJahrgangFachkombinationen");
		setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Zu erzwingende Fachkombinationen oder nicht mögliche Fachkombinationen in einem Jahrgang");
	}

}
