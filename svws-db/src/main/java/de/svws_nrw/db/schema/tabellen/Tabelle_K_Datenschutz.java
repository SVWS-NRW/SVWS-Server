package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Datenschutz.
 */
public class Tabelle_K_Datenschutz extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eindeutige ID für den Datensatz");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(250)
		.setNotNull()
		.setJavaComment("Eine kurze Bezeichnung des DSGVO-Merkmals");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Regelt die Sichtbarkeit des Merkmals bei der Ansicht der Schülertabelle ");

	/** Die Definition der Tabellenspalte Schluessel */
	public SchemaTabelleSpalte col_Schluessel = add("Schluessel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Fest vorgegebene Werte, die es in Schild-NRW später ermöglichen, die DSGVO-Merkmale zu erkennen");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setNotNull()
		.setJavaComment("Gibt die Reihenfolge der Merkmale bei der Darstellung an.");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.TEXT, false)
		.setJavaComment("Eine ausführliche Beschreibung des DSGCO-Merkmals");

	/** Die Definition der Tabellenspalte PersonArt */
	public SchemaTabelleSpalte col_PersonArt = add("PersonArt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("PersonenArt des Datenschutz-Eintrags (S=Schueler L=Lehrer)");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Datenschutz.
	 */
	public Tabelle_K_Datenschutz() {
		super("K_Datenschutz", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOKatalogDatenschutz");
		setJavaComment("Tabelle für die DSGVO Einwilligungsarten");
	}

}
