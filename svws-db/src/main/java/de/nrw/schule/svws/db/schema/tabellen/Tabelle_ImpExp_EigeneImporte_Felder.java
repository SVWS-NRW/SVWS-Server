package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle ImpExp_EigeneImporte_Felder.
 */
public class Tabelle_ImpExp_EigeneImporte_Felder extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Import_ID */
	public SchemaTabelleSpalte col_Import_ID = add("Import_ID", SchemaDatentypen.INT, true)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte Field_ID */
	public SchemaTabelleSpalte col_Field_ID = add("Field_ID", SchemaDatentypen.INT, true)
		.setDefault("0")
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte TableDescription */
	public SchemaTabelleSpalte col_TableDescription = add("TableDescription", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte FieldDescription */
	public SchemaTabelleSpalte col_FieldDescription = add("FieldDescription", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte SrcPosition */
	public SchemaTabelleSpalte col_SrcPosition = add("SrcPosition", SchemaDatentypen.SMALLINT, false)
		.setDefault("0")
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstTable */
	public SchemaTabelleSpalte col_DstTable = add("DstTable", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstFieldName */
	public SchemaTabelleSpalte col_DstFieldName = add("DstFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstFieldType */
	public SchemaTabelleSpalte col_DstFieldType = add("DstFieldType", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstFieldIsIdentifier */
	public SchemaTabelleSpalte col_DstFieldIsIdentifier = add("DstFieldIsIdentifier", SchemaDatentypen.INT, false)
		.setConverter("Boolean01Converter")
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstLookupDir */
	public SchemaTabelleSpalte col_DstLookupDir = add("DstLookupDir", SchemaDatentypen.VARCHAR, false).setDatenlaenge(250)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstLookupTable */
	public SchemaTabelleSpalte col_DstLookupTable = add("DstLookupTable", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstLookupFieldName */
	public SchemaTabelleSpalte col_DstLookupFieldName = add("DstLookupFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstLookupTableIDFieldName */
	public SchemaTabelleSpalte col_DstLookupTableIDFieldName = add("DstLookupTableIDFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstResultFieldName */
	public SchemaTabelleSpalte col_DstResultFieldName = add("DstResultFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstKeyLookupInsert */
	public SchemaTabelleSpalte col_DstKeyLookupInsert = add("DstKeyLookupInsert", SchemaDatentypen.INT, false)
		.setConverter("Boolean01Converter")
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstKeyLookupNameCreateID */
	public SchemaTabelleSpalte col_DstKeyLookupNameCreateID = add("DstKeyLookupNameCreateID", SchemaDatentypen.INT, false)
		.setConverter("Boolean01Converter")
		.setJavaComment("externen Textimport Felder");

	/** Die Definition der Tabellenspalte DstForceNumeric */
	public SchemaTabelleSpalte col_DstForceNumeric = add("DstForceNumeric", SchemaDatentypen.INT, false)
		.setConverter("Boolean01Converter")
		.setJavaComment("externen Textimport Felder");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle ImpExp_EigeneImporte_Felder.
	 */
	public Tabelle_ImpExp_EigeneImporte_Felder() {
		super("ImpExp_EigeneImporte_Felder", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.impexp");
		setJavaClassName("DTOEigeneImporteFelder");
		setJavaComment("Importschemata, die im Programm \"Externimport.exe\" definiert werden - Hier die Spaltendefinitionen");
	}

}
