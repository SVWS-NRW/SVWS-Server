package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
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

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht");

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.VARCHAR, true).setDatenlaenge(30)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: ID des Datensatzes der nicht möglichen Fächerkombination");

	/** Die Definition der Tabellenspalte Fach1_ID */
	public SchemaTabelleSpalte col_Fach1_ID = add("Fach1_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Die ID des ersten Faches der nicht möglichen Fächerkombination / eines Fächerprofils");

	/** Die Definition der Tabellenspalte Fach2_ID */
	public SchemaTabelleSpalte col_Fach2_ID = add("Fach2_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Die ID des zweiten Faches der nicht möglichen Fächerkombination / eines Fächerprofils");

	/** Die Definition der Tabellenspalte Kursart1 */
	public SchemaTabelleSpalte col_Kursart1 = add("Kursart1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Die Kursart des ersten Faches der nicht möglichen Fächerkombination / eines Fächerprofils");

	/** Die Definition der Tabellenspalte Kursart2 */
	public SchemaTabelleSpalte col_Kursart2 = add("Kursart2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Die Kursart des zweiten Faches der nicht möglichen Fächerkombination / eines Fächerprofils");

	/** Die Definition der Tabellenspalte Phase */
	public SchemaTabelleSpalte col_Phase = add("Phase", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setDefault("-")
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Die Gültigkeit der nicht möglichen Fächerkombination EF.1 bis Q2.2 oder Q1.1 bis Q2.2");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Gibt an, ob es sich um eine nicht mögliche Fächerkombination (0) oder ein Fächerprofil handelt (1)");


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
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostJahrgangFachkombinationen");
		setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Zu erzwingende Fachkombinationen oder nicht mögliche Fachkombinationen in einem Jahrgang");
	}

}
