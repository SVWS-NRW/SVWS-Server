package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle ImpExp_EigeneImporte_Tabellen.
 */
public class Tabelle_ImpExp_EigeneImporte_Tabellen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Import_ID */
	public SchemaTabelleSpalte col_Import_ID = add("Import_ID", SchemaDatentypen.INT, true)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte TableName */
	public SchemaTabelleSpalte col_TableName = add("TableName", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte DstCreateID */
	public SchemaTabelleSpalte col_DstCreateID = add("DstCreateID", SchemaDatentypen.INT, false)
		.setConverter(Boolean01Converter.class)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte DstIDFieldName */
	public SchemaTabelleSpalte col_DstIDFieldName = add("DstIDFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte Sequence */
	public SchemaTabelleSpalte col_Sequence = add("Sequence", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte LookupTable */
	public SchemaTabelleSpalte col_LookupTable = add("LookupTable", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte LookupFields */
	public SchemaTabelleSpalte col_LookupFields = add("LookupFields", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte LookupFieldTypes */
	public SchemaTabelleSpalte col_LookupFieldTypes = add("LookupFieldTypes", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte LookupFieldPos */
	public SchemaTabelleSpalte col_LookupFieldPos = add("LookupFieldPos", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte LookupKeyField */
	public SchemaTabelleSpalte col_LookupKeyField = add("LookupKeyField", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte LookupResultField */
	public SchemaTabelleSpalte col_LookupResultField = add("LookupResultField", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte LookupResultFieldType */
	public SchemaTabelleSpalte col_LookupResultFieldType = add("LookupResultFieldType", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte DstDefaultFieldName */
	public SchemaTabelleSpalte col_DstDefaultFieldName = add("DstDefaultFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte DstDefaultFieldValue */
	public SchemaTabelleSpalte col_DstDefaultFieldValue = add("DstDefaultFieldValue", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("externen Textimport Tabellen");

	/** Die Definition der Tabellenspalte GU_ID_Field */
	public SchemaTabelleSpalte col_GU_ID_Field = add("GU_ID_Field", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("externen Textimport Tabellen");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle ImpExp_EigeneImporte_Tabellen.
	 */
	public Tabelle_ImpExp_EigeneImporte_Tabellen() {
		super("ImpExp_EigeneImporte_Tabellen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.impexp");
		setJavaClassName("DTOEigeneImporteTabellen");
		setJavaComment("Importschemata, die im Programm \"Externimport.exe\" definiert werden - Hier die Tabellendefinitionen");
	}

}
