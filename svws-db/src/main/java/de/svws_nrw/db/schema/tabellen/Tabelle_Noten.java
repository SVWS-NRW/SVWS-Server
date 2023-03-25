package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Noten.
 */
public class Tabelle_Noten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Noten");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Das Kürzel der Note");

	/** Die Definition der Tabellenspalte IstTendenznote */
	public SchemaTabelleSpalte col_IstTendenznote = add("IstTendenznote", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich um eine Tendenznote (plus) oder (minus) handelt");

	/** Die Definition der Tabellenspalte Text */
	public SchemaTabelleSpalte col_Text = add("Text", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Die Bezeichnung der Note");

	/** Die Definition der Tabellenspalte AufZeugnis */
	public SchemaTabelleSpalte col_AufZeugnis = add("AufZeugnis", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Note auf einem Zeugnis als erteilte Note gedruckt wird oder nicht.");

	/** Die Definition der Tabellenspalte Notenpunkte */
	public SchemaTabelleSpalte col_Notenpunkte = add("Notenpunkte", SchemaDatentypen.INT, false)
		.setJavaComment("Die Notenpunkte der Note in der Sekundarstufe II");

	/** Die Definition der Tabellenspalte TextLaufbahnSII */
	public SchemaTabelleSpalte col_TextLaufbahnSII = add("TextLaufbahnSII", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Die Bezeichnung der Note in der Sekundarstufe II, die für die Laufbahn zum Abitur verwendet wird");

	/** Die Definition der Tabellenspalte AufLaufbahnSII */
	public SchemaTabelleSpalte col_AufLaufbahnSII = add("AufLaufbahnSII", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Note bei der Laufbahn in der Sekundarstufe II gedruckt wird oder nicht.");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Eine Default-Sortierung der Noten");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Der Datensatz ist gültig ab dem Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Der Datensatz ist gültig bis zu dem Schuljahr");


	/** Die Definition des Unique-Index Noten_UC1 */
	public SchemaTabelleUniqueIndex unique_Noten_UC1 = addUniqueIndex("Noten_UC1", 
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Noten.
	 */
	public Tabelle_Noten() {
		super("Noten", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("coretypes");
		setJavaClassName("DTONote");
		setJavaComment("Tabelle für den Core-Type der Noten");
	}

}
