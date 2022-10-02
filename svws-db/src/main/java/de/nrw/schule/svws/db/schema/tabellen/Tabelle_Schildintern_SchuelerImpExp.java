package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_SchuelerImpExp.
 */
public class Tabelle_Schildintern_SchuelerImpExp extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Tabelle */
	public SchemaTabelleSpalte col_Tabelle = add("Tabelle", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Tabelle für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte TabellenAnzeige */
	public SchemaTabelleSpalte col_TabellenAnzeige = add("TabellenAnzeige", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte MasterTable */
	public SchemaTabelleSpalte col_MasterTable = add("MasterTable", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte ExpCmd */
	public SchemaTabelleSpalte col_ExpCmd = add("ExpCmd", SchemaDatentypen.VARCHAR, false).setDatenlaenge(250)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte SrcGetFieldsSQL */
	public SchemaTabelleSpalte col_SrcGetFieldsSQL = add("SrcGetFieldsSQL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(250)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte DeleteSQL */
	public SchemaTabelleSpalte col_DeleteSQL = add("DeleteSQL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(250)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte DstGetIDSQL */
	public SchemaTabelleSpalte col_DstGetIDSQL = add("DstGetIDSQL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(250)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte HauptFeld */
	public SchemaTabelleSpalte col_HauptFeld = add("HauptFeld", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte DetailFeld */
	public SchemaTabelleSpalte col_DetailFeld = add("DetailFeld", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");

	/** Die Definition der Tabellenspalte Reihenfolge */
	public SchemaTabelleSpalte col_Reihenfolge = add("Reihenfolge", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_SchuelerImpExp.
	 */
	public Tabelle_Schildintern_SchuelerImpExp() {
		super("Schildintern_SchuelerImpExp", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternSchuelerImportExport");
		setJavaComment("Wird in SchILD2.0 beim \"Katalog-Export\" und (\"Datenaustausch>>Kataloge exportieren\") verwendet. Frage: Wird das in Zukunft noch benötigt?");
	}

}
