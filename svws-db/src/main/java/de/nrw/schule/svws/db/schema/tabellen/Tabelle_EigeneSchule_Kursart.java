package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Kursart.
 */
public class Tabelle_EigeneSchule_Kursart extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Kursarteneintrag");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setJavaComment("Bezeichnung des Kursarteneintrags IT.NRW");

	/** Die Definition der Tabellenspalte InternBez */
	public SchemaTabelleSpalte col_InternBez = add("InternBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Interne Bezeichnung Kursarteneintrag");

	/** Die Definition der Tabellenspalte Kursart */
	public SchemaTabelleSpalte col_Kursart = add("Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kürzel Kursart");

	/** Die Definition der Tabellenspalte KursartAllg */
	public SchemaTabelleSpalte col_KursartAllg = add("KursartAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Allgemeine Bezeichnung Kursart (zB GK bei GKM)");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung der Kursart");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Sichtbarkeit der Kursart");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Kursart änderbar Ja Nein");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Unique-Index EigeneSchule_Kursart_UC1 */
	public SchemaTabelleUniqueIndex unique_EigeneSchule_Kursart_UC1 = addUniqueIndex("EigeneSchule_Kursart_UC1", 
			col_KursartAllg, 
			col_Kursart
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Kursart.
	 */
	public Tabelle_EigeneSchule_Kursart() {
		super("EigeneSchule_Kursart", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOKursarten");
		setJavaComment("vorhandene Kursarten die dem Schüler zugewiesen werden können in den Leistungsdaten / Kursen");
	}

}
