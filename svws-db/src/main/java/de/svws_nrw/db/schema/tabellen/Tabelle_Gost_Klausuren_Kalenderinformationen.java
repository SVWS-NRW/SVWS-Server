package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Kalenderinformationen.
 */
public class Tabelle_Gost_Klausuren_Kalenderinformationen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Kalenderinformation (generiert)");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Bezeichnung der Kalenderinformation");

	/** Die Definition der Tabellenspalte Startdatum */
	public SchemaTabelleSpalte col_Startdatum = add("Startdatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Startdatum für den Kalendereintrag");

	/** Die Definition der Tabellenspalte Startzeit */
	public SchemaTabelleSpalte col_Startzeit = add("Startzeit", SchemaDatentypen.TIME, false)
		.setConverter(UhrzeitConverter.class)
		.setJavaComment("Startzeit für den Kalendereintrag");

	/** Die Definition der Tabellenspalte Enddatum */
	public SchemaTabelleSpalte col_Enddatum = add("Enddatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Enddatum für den Kalendereintrag");

	/** Die Definition der Tabellenspalte Endzeit */
	public SchemaTabelleSpalte col_Endzeit = add("Endzeit", SchemaDatentypen.TIME, false)
		.setConverter(UhrzeitConverter.class)
		.setJavaComment("Endzeit für den Kalendereintrag");

	/** Die Definition der Tabellenspalte IstSperrtermin */
	public SchemaTabelleSpalte col_IstSperrtermin = add("IstSperrtermin", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich um einen Sperrtermin handelt oder nicht: 1 - true, 0 - false.");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Bemerkungen zur Kalenderinformation");

	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Kalenderinformationen.
	 */
	public Tabelle_Gost_Klausuren_Kalenderinformationen() {
		super("Gost_Klausuren_Kalenderinformationen", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenKalenderinformationen");
		setJavaComment("Tabelle für die Definition von Kalenderinformationen");
	}

}
