package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Katalog_Aufsichtsbereich.
 */
public class Tabelle_Katalog_Aufsichtsbereich extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID identifiziert einen Aufsichtsbereich eindeutig");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Die Kurzbezeichnung des Aufsichtsbereichs");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
		.setNotNull()
		.setJavaComment("Gegebenenfalls eine ausführlichere Beschreibung des Aufsichtsbereichs");


	/** Die Definition des Unique-Index Katalog_Aufsichtsbereich_UC1 */
	public SchemaTabelleUniqueIndex unique_Katalog_Aufsichtsbereich_UC1 = addUniqueIndex("Katalog_Aufsichtsbereich_UC1", 
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Katalog_Aufsichtsbereich.
	 */
	public Tabelle_Katalog_Aufsichtsbereich() {
		super("Katalog_Aufsichtsbereich", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOKatalogAufsichtsbereich");
		setJavaComment("Enthält eine aktuelle Liste von Aufsichtsbereichen, die an der Schule für Pausenzeiten vereinbart sind.");
	}

}
