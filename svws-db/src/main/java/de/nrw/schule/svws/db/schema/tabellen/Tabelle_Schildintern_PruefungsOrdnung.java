package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_PruefungsOrdnung.
 */
public class Tabelle_Schildintern_PruefungsOrdnung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte PO_Schulform */
	public SchemaTabelleSpalte col_PO_Schulform = add("PO_Schulform", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: zulässige Schulformen der Prüfungsordnungen");

	/** Die Definition der Tabellenspalte PO_Krz */
	public SchemaTabelleSpalte col_PO_Krz = add("PO_Krz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(30)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: erstes Kürzel");

	/** Die Definition der Tabellenspalte PO_Name */
	public SchemaTabelleSpalte col_PO_Name = add("PO_Name", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: zweites Kürzel");

	/** Die Definition der Tabellenspalte PO_SGL */
	public SchemaTabelleSpalte col_PO_SGL = add("PO_SGL", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: zulässige Gliederungen");

	/** Die Definition der Tabellenspalte PO_MinJahrgang */
	public SchemaTabelleSpalte col_PO_MinJahrgang = add("PO_MinJahrgang", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: DEPRECATED: ");

	/** Die Definition der Tabellenspalte PO_MaxJahrgang */
	public SchemaTabelleSpalte col_PO_MaxJahrgang = add("PO_MaxJahrgang", SchemaDatentypen.INT, false)
		.setDefault("20")
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: DEPRECATED: ");

	/** Die Definition der Tabellenspalte PO_Jahrgaenge */
	public SchemaTabelleSpalte col_PO_Jahrgaenge = add("PO_Jahrgaenge", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: zulässige Jahrgänge");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig ab Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig bis Schuljahr");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_PruefungsOrdnung.
	 */
	public Tabelle_Schildintern_PruefungsOrdnung() {
		super("Schildintern_PruefungsOrdnung", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternPruefungsOrdnung");
		setJavaComment("Auflistung der Prüfungsordnungen zu Schulformen und Jahrgängen");
	}

}
