package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.PersonTypConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Datenschutz.
 */
public class Tabelle_K_Datenschutz extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaName("id")
			.setJavaComment("Eindeutige ID für den Datensatz");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public final SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(250)
			.setNotNull()
			.setJavaName("bezeichnung")
			.setJavaComment("Eine kurze Bezeichnung der Einwilligungsart");

	/** Die Definition der Tabellenspalte Sichtbar */
	public final SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("+")
			.setNotNull()
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaName("istSichtbar")
			.setJavaComment("Regelt die Sichtbarkeit der Einwilligungsart bei der Ansicht der Schülertabelle ");

	/** Die Definition der Tabellenspalte Schluessel */
	public final SchemaTabelleSpalte col_Schluessel = add("Schluessel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
			.setJavaName("schluessel")
			.setJavaComment("Fest vorgegebene Werte, die es in Schild-NRW später ermöglichen, die Einwilligungsart zu erkennen");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public final SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setNotNull()
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment(
					"Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Sortierung */
	public final SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
			.setDefault("32000")
			.setNotNull()
			.setJavaName("sortierung")
			.setJavaComment("Gibt die Reihenfolge der Einwilligungsarten bei der Darstellung an.");

	/** Die Definition der Tabellenspalte Beschreibung */
	public final SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.TEXT, false)
			.setJavaName("beschreibung")
			.setJavaComment("Eine ausführliche Beschreibung der Einwilligungsart");

	/** Die Definition der Tabellenspalte PersonArt */
	public final SchemaTabelleSpalte col_PersonArt = add("PersonArt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setConverter(PersonTypConverter.class)
			.setConverterRevision(SchemaRevisionen.REV_17)
			.setJavaName("personTyp")
			.setJavaComment("Personentyp der Einwilligungsart (S=Schueler L=Lehrer E=Erzieher)");

	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Datenschutz.
	 */
	public Tabelle_K_Datenschutz() {
		super("K_Datenschutz", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOEinwilligungsart");
		setJavaComment("Tabelle für die Einwilligungsarten");
	}

}
