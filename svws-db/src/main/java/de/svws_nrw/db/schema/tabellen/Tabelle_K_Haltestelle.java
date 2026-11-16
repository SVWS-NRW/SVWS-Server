package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Haltestelle.
 */
public class Tabelle_K_Haltestelle extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setJavaName("id")
			.setNotNull()
			.setJavaComment("ID der Haltestelle");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public final SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
			.setJavaName("bezeichnung")
			.setNotNull()
			.setJavaComment("Bezeichnung der Haltestelle");

	/** Die Definition der Tabellenspalte Sortierung */
	public final SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
			.setJavaName("sortierung")
			.setDefault("32000")
			.setJavaComment("Sortierung der Haltestelle");

	/** Die Definition der Tabellenspalte Sichtbar */
	public final SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setJavaName("istSichtbar")
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Sichbarkeit der Haltestelle");

	/** Die Definition der Tabellenspalte Aenderbar */
	public final SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setJavaName("istAenderbar")
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Änderbarkeit der Haltestelle");

	/** Die Definition der Tabellenspalte EntfernungSchule */
	public final SchemaTabelleSpalte col_EntfernungSchule = add("EntfernungSchule", SchemaDatentypen.FLOAT, false)
			.setJavaName("entfernungSchule")
			.setJavaComment("Enterfung zur Schule von der Haltestelle");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public final SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank"
					+ " gespeichert werden");


	/** Die Definition des Unique-Index K_Haltestelle_UC1 */
	public final SchemaTabelleUniqueIndex unique_K_Haltestelle_UC1 = addUniqueIndex("K_Haltestelle_UC1",
			col_Bezeichnung
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Haltestelle.
	 */
	public Tabelle_K_Haltestelle() {
		super("K_Haltestelle", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOHaltestellen");
		setJavaComment("Liste der Haltestellen Individualdaten I - kann selbst definiert werden");
	}

}
