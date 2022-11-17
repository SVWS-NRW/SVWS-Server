package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Schulformen.
 */
public class Tabelle_EigeneSchule_Schulformen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Schulgliederung");

	/** Die Definition der Tabellenspalte SGL */
	public SchemaTabelleSpalte col_SGL = add("SGL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulgliedererung die an der Schule vorkommt");

	/** Die Definition der Tabellenspalte SF_SGL */
	public SchemaTabelleSpalte col_SF_SGL = add("SF_SGL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Statistikkürzel SchulformSchulgliederung");

	/** Die Definition der Tabellenspalte Schulform */
	public SchemaTabelleSpalte col_Schulform = add("Schulform", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schulform der SGL");

	/** Die Definition der Tabellenspalte DoppelQualifikation */
	public SchemaTabelleSpalte col_DoppelQualifikation = add("DoppelQualifikation", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an, ob am Berufskolleg die SGL mit Doppelqualifikation abgeschlossen werden kann");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung der SGL");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Sichtbarkeit der SGL");

	/** Die Definition der Tabellenspalte BKIndex */
	public SchemaTabelleSpalte col_BKIndex = add("BKIndex", SchemaDatentypen.INT, false)
		.setJavaComment("BKIndex zur SGL (IT.NRW)");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Schulform2 */
	public SchemaTabelleSpalte col_Schulform2 = add("Schulform2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schulform2 zur SGL");


	/** Die Definition des Unique-Index EigeneSchule_Schulformen_UC1 */
	public SchemaTabelleUniqueIndex unique_EigeneSchule_Schulformen_UC1 = addUniqueIndex("EigeneSchule_Schulformen_UC1", 
			col_SGL
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Schulformen.
	 */
	public Tabelle_EigeneSchule_Schulformen() {
		super("EigeneSchule_Schulformen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOSchulformen");
		setJavaComment("Liste der Schulgliederungen (SGL) Schule bearbeiten > Kann dann den Schülern unter Indv-Daten I zugeordnet werden (IT.NRW)");
	}

}
