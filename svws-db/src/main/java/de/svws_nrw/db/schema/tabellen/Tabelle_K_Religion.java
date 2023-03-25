package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Religion.
 */
public class Tabelle_K_Religion extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Religion");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setNotNull()
		.setJavaComment("Bezeichnung der Religion");

	/** Die Definition der Tabellenspalte StatistikKrz */
	public SchemaTabelleSpalte col_StatistikKrz = add("StatistikKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Statistikkürzel der Religion");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung der Religion");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Sichbarkeit der Religion");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Änderbarkeit der Religion");

	/** Die Definition der Tabellenspalte ExportBez */
	public SchemaTabelleSpalte col_ExportBez = add("ExportBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Exportbezeichnung der Religion");

	/** Die Definition der Tabellenspalte ZeugnisBezeichnung */
	public SchemaTabelleSpalte col_ZeugnisBezeichnung = add("ZeugnisBezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Zeugnisbezeichnung der Religion");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Unique-Index K_Religion_UC1 */
	public SchemaTabelleUniqueIndex unique_K_Religion_UC1 = addUniqueIndex("K_Religion_UC1", 
			col_Bezeichnung
		)
		.setRevision(SchemaRevisionen.REV_1);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Religion.
	 */
	public Tabelle_K_Religion() {
		super("K_Religion", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOKonfession");
		setJavaComment("Liste der verwendeten Konfessionen mit interner Bezeichnung und ASD-Kürzel (IT.NRW)");
	}

}
