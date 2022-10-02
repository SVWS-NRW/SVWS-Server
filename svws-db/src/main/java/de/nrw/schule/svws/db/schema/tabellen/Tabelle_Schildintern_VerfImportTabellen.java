package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_VerfImportTabellen.
 */
public class Tabelle_Schildintern_VerfImportTabellen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte TableName */
	public SchemaTabelleSpalte col_TableName = add("TableName", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstRequiredFields */
	public SchemaTabelleSpalte col_DstRequiredFields = add("DstRequiredFields", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstIDFieldName */
	public SchemaTabelleSpalte col_DstIDFieldName = add("DstIDFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte Sequence */
	public SchemaTabelleSpalte col_Sequence = add("Sequence", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte LookupTable */
	public SchemaTabelleSpalte col_LookupTable = add("LookupTable", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte LookupFields */
	public SchemaTabelleSpalte col_LookupFields = add("LookupFields", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte LookupFieldTypes */
	public SchemaTabelleSpalte col_LookupFieldTypes = add("LookupFieldTypes", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte LookupResultField */
	public SchemaTabelleSpalte col_LookupResultField = add("LookupResultField", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte LookupResultFieldType */
	public SchemaTabelleSpalte col_LookupResultFieldType = add("LookupResultFieldType", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte LookupKeyField */
	public SchemaTabelleSpalte col_LookupKeyField = add("LookupKeyField", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstDefaultFieldName */
	public SchemaTabelleSpalte col_DstDefaultFieldName = add("DstDefaultFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstDefaultFieldValue */
	public SchemaTabelleSpalte col_DstDefaultFieldValue = add("DstDefaultFieldValue", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstCreateID */
	public SchemaTabelleSpalte col_DstCreateID = add("DstCreateID", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte GU_ID_Field */
	public SchemaTabelleSpalte col_GU_ID_Field = add("GU_ID_Field", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_VerfImportTabellen.
	 */
	public Tabelle_Schildintern_VerfImportTabellen() {
		super("Schildintern_VerfImportTabellen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternVerfImportTabellen");
		setJavaComment("Stellt die verfügbaren Tabellen zur Verfügung, die beim \"Import aus Textdateien\" zur Verfügung stehen");
	}

}
