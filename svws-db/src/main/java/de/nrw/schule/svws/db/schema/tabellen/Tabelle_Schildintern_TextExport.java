package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_TextExport.
 */
public class Tabelle_Schildintern_TextExport extends SchemaTabelle {

	/** Die Definition der Tabellenspalte DatenartKrz */
	public SchemaTabelleSpalte col_DatenartKrz = add("DatenartKrz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");

	/** Die Definition der Tabellenspalte Feldname */
	public SchemaTabelleSpalte col_Feldname = add("Feldname", SchemaDatentypen.VARCHAR, true).setDatenlaenge(30)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");

	/** Die Definition der Tabellenspalte AnzeigeText */
	public SchemaTabelleSpalte col_AnzeigeText = add("AnzeigeText", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");

	/** Die Definition der Tabellenspalte Feldtyp */
	public SchemaTabelleSpalte col_Feldtyp = add("Feldtyp", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");

	/** Die Definition der Tabellenspalte Feldwerte */
	public SchemaTabelleSpalte col_Feldwerte = add("Feldwerte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");

	/** Die Definition der Tabellenspalte ErgebnisWerte */
	public SchemaTabelleSpalte col_ErgebnisWerte = add("ErgebnisWerte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");

	/** Die Definition der Tabellenspalte LookupFeldname */
	public SchemaTabelleSpalte col_LookupFeldname = add("LookupFeldname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");

	/** Die Definition der Tabellenspalte LookupSQLText */
	public SchemaTabelleSpalte col_LookupSQLText = add("LookupSQLText", SchemaDatentypen.TEXT, false)
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");

	/** Die Definition der Tabellenspalte DBFormat */
	public SchemaTabelleSpalte col_DBFormat = add("DBFormat", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setDefault("ALLE")
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Excel_CSV_Export");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_TextExport.
	 */
	public Tabelle_Schildintern_TextExport() {
		super("Schildintern_TextExport", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternTextExport");
		setJavaComment("Felder, die im Text-/Excel-Export zur Verfügung stehen");
	}

}
