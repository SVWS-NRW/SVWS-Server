package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schema_AutoInkremente.
 */
public class Tabelle_Schema_AutoInkremente extends SchemaTabelle {

	/** Die Definition der Tabellenspalte NameTabelle */
	public SchemaTabelleSpalte col_NameTabelle = add("NameTabelle", SchemaDatentypen.VARCHAR, true).setDatenlaenge(200)
		.setNotNull()
		.setJavaComment("Gibt den Tabellennamen an, für dessen Auto-Inkrement der ID-Wert verwendet werden soll.");

	/** Die Definition der Tabellenspalte MaxID */
	public SchemaTabelleSpalte col_MaxID = add("MaxID", SchemaDatentypen.BIGINT, false)
		.setDefault("1")
		.setNotNull()
		.setJavaComment("Die ID des höchsten jemals in die DB geschriebenen ID-Wertes bei der zugehörigen Tabelle");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SVWS_DB_AutoInkremente.
	 */
	public Tabelle_Schema_AutoInkremente() {
		super("Schema_AutoInkremente", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schema");
		setJavaClassName("DTOSchemaAutoInkremente");
		setJavaComment("Tabelle für das Zwischenspeichern der bisherigen Maximalwerte beim Einfügen, damit eine DBMS-unabhängige Auto-Inkrement-Funktion realisiert werden kann.");
	}

}
