package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Adressart.
 */
public class Tabelle_K_Adressart extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Adressart");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
			.setNotNull()
			.setJavaComment("Bezeichnung der Adressart (Betrieb Kammer usw)");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
			.setDefault("32000")
			.setJavaComment("Sortierung der Adressart");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Sichtbarkeit der Adressart");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Adressart ist änderbar Ja Nein");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment(
					"Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Unique-Index K_Adressart_UC1 */
	public SchemaTabelleUniqueIndex unique_K_Adressart_UC1 = addUniqueIndex("K_Adressart_UC1",
			col_Bezeichnung
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Adressart.
	 */
	public Tabelle_K_Adressart() {
		super("K_Adressart", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOKatalogAdressart");
		setJavaComment("Katalog der Arten, die den weiteren Adressen/Betrieben zugeordnet werden können");
	}

}
