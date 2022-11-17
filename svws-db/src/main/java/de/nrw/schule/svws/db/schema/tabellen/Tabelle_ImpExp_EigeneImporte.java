package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle ImpExp_EigeneImporte.
 */
public class Tabelle_ImpExp_EigeneImporte extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.INT, true)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("ID des Importschemas für den externen Textimport");

	/** Die Definition der Tabellenspalte Title */
	public SchemaTabelleSpalte col_Title = add("Title", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Bezeichnung des Schemas");

	/** Die Definition der Tabellenspalte DelimiterChar */
	public SchemaTabelleSpalte col_DelimiterChar = add("DelimiterChar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Enthält das Trennzeichen für den Import");

	/** Die Definition der Tabellenspalte TextQuote */
	public SchemaTabelleSpalte col_TextQuote = add("TextQuote", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Texttrennzeichen");

	/** Die Definition der Tabellenspalte SkipLines */
	public SchemaTabelleSpalte col_SkipLines = add("SkipLines", SchemaDatentypen.SMALLINT, false)
		.setDefault("0")
		.setJavaComment("externen Textimport");

	/** Die Definition der Tabellenspalte DateFormat */
	public SchemaTabelleSpalte col_DateFormat = add("DateFormat", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Format der Datumswerte");

	/** Die Definition der Tabellenspalte BooleanTrue */
	public SchemaTabelleSpalte col_BooleanTrue = add("BooleanTrue", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("externen Textimport");

	/** Die Definition der Tabellenspalte AbkWeiblich */
	public SchemaTabelleSpalte col_AbkWeiblich = add("AbkWeiblich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Abkürzung für weiblich");

	/** Die Definition der Tabellenspalte AbkMaennlich */
	public SchemaTabelleSpalte col_AbkMaennlich = add("AbkMaennlich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Abkürzung für männlich");

	/** Die Definition der Tabellenspalte MainTable */
	public SchemaTabelleSpalte col_MainTable = add("MainTable", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport");

	/** Die Definition der Tabellenspalte InsertMode */
	public SchemaTabelleSpalte col_InsertMode = add("InsertMode", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("externen Textimport");

	/** Die Definition der Tabellenspalte LookupTableDir */
	public SchemaTabelleSpalte col_LookupTableDir = add("LookupTableDir", SchemaDatentypen.VARCHAR, false).setDatenlaenge(250)
		.setJavaComment("externen Textimport");

	/** Die Definition der Tabellenspalte SchuelerIDMode */
	public SchemaTabelleSpalte col_SchuelerIDMode = add("SchuelerIDMode", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setJavaComment("externen Textimport");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle ImpExp_EigeneImporte.
	 */
	public Tabelle_ImpExp_EigeneImporte() {
		super("ImpExp_EigeneImporte", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.impexp");
		setJavaClassName("DTOEigeneImporte");
		setJavaComment("Importschemata, die im Programm \"Externimport.exe\" definiert werden");
	}

}
