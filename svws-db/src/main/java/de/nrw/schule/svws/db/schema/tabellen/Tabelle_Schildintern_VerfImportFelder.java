package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_VerfImportFelder.
 */
public class Tabelle_Schildintern_VerfImportFelder extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Notwendiges Tabelle für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte TableDescription */
	public SchemaTabelleSpalte col_TableDescription = add("TableDescription", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte FieldDescription */
	public SchemaTabelleSpalte col_FieldDescription = add("FieldDescription", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstTable */
	public SchemaTabelleSpalte col_DstTable = add("DstTable", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstFieldName */
	public SchemaTabelleSpalte col_DstFieldName = add("DstFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstFieldType */
	public SchemaTabelleSpalte col_DstFieldType = add("DstFieldType", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstRequiredState */
	public SchemaTabelleSpalte col_DstRequiredState = add("DstRequiredState", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstLookupTable */
	public SchemaTabelleSpalte col_DstLookupTable = add("DstLookupTable", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstLookupTableIDFieldName */
	public SchemaTabelleSpalte col_DstLookupTableIDFieldName = add("DstLookupTableIDFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstLookupFieldName */
	public SchemaTabelleSpalte col_DstLookupFieldName = add("DstLookupFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstResultFieldName */
	public SchemaTabelleSpalte col_DstResultFieldName = add("DstResultFieldName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstKeyLookupInsert */
	public SchemaTabelleSpalte col_DstKeyLookupInsert = add("DstKeyLookupInsert", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstKeyLookupNameCreateID */
	public SchemaTabelleSpalte col_DstKeyLookupNameCreateID = add("DstKeyLookupNameCreateID", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte DstForceNumeric */
	public SchemaTabelleSpalte col_DstForceNumeric = add("DstForceNumeric", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_VerfImportFelder.
	 */
	public Tabelle_Schildintern_VerfImportFelder() {
		super("Schildintern_VerfImportFelder", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternVerfImportFelder");
		setJavaComment("Stellt die verfügbaren Felder zur Verfügung, die beim \"Import aus Textdateien\" zur Verfügung stehen");
	}

}
