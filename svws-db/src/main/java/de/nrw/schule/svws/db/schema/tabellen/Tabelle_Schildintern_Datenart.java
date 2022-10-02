package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_Datenart.
 */
public class Tabelle_Schildintern_Datenart extends SchemaTabelle {

	/** Die Definition der Tabellenspalte DatenartKrz */
	public SchemaTabelleSpalte col_DatenartKrz = add("DatenartKrz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: ");

	/** Die Definition der Tabellenspalte Datenart */
	public SchemaTabelleSpalte col_Datenart = add("Datenart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: ");

	/** Die Definition der Tabellenspalte Tabellenname */
	public SchemaTabelleSpalte col_Tabellenname = add("Tabellenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Schildintern Tabelle: ");

	/** Die Definition der Tabellenspalte Reihenfolge */
	public SchemaTabelleSpalte col_Reihenfolge = add("Reihenfolge", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: ");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig ab Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig bis Schuljahr");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_Datenart.
	 */
	public Tabelle_Schildintern_Datenart() {
		super("Schildintern_Datenart", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternDatenart");
		setJavaComment("Wird beim Export in Text/Excel-Dateien verwendet. Definiert die für den Export zur Verfügung stehenden Datenarten");
	}

}
