package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Katalog_Raeume.
 */
public class Tabelle_Katalog_Raeume extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID identifiziert einen Raumeintrag eindeutig");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Das Kürzel des Raums - auch eindeutig");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
		.setNotNull()
		.setJavaComment("Gegebenenfalls eine ausführlichere Beschreibung des Raumes");

	/** Die Definition der Tabellenspalte Groesse */
	public SchemaTabelleSpalte col_Groesse = add("Groesse", SchemaDatentypen.INT, false)
		.setDefault("40")
		.setNotNull()
		.setJavaComment("Die Größe des Raumes, d.h. wie viele Schüler hier max. Platz haben");


	/** Die Definition des Unique-Index Katalog_Raeume_UC1 */
	public SchemaTabelleUniqueIndex unique_Katalog_Raeume_UC1 = addUniqueIndex("Katalog_Raeume_UC1", 
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Katalog_Raeume.
	 */
	public Tabelle_Katalog_Raeume() {
		super("Katalog_Raeume", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOKatalogRaum");
		setJavaComment("Enthält eine aktuelle Liste von Räumen, die an der Schule verfügbar sind.");
	}

}
