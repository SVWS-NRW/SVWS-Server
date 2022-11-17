package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Fach_Gliederungen.
 */
public class Tabelle_Fach_Gliederungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte Gliederung */
	public SchemaTabelleSpalte col_Gliederung = add("Gliederung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("SGL für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Faechergruppe */
	public SchemaTabelleSpalte col_Faechergruppe = add("Faechergruppe", SchemaDatentypen.INT, false)
		.setJavaComment("Fächergruppe für gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte GewichtungAB */
	public SchemaTabelleSpalte col_GewichtungAB = add("GewichtungAB", SchemaDatentypen.INT, false)
		.setJavaComment("Gewichtung Allgemeinbildend für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte GewichtungBB */
	public SchemaTabelleSpalte col_GewichtungBB = add("GewichtungBB", SchemaDatentypen.INT, false)
		.setJavaComment("Gewichtung Berufsbezogen für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte SchriftlichAB */
	public SchemaTabelleSpalte col_SchriftlichAB = add("SchriftlichAB", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Ist schriftliches Fach Allgemeinbildend für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte SchriftlichBB */
	public SchemaTabelleSpalte col_SchriftlichBB = add("SchriftlichBB", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Ist schriftliches Fach Berufsbildend für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte GymOSFach */
	public SchemaTabelleSpalte col_GymOSFach = add("GymOSFach", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Ist Fach der GymOB für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte ZeugnisBez */
	public SchemaTabelleSpalte col_ZeugnisBez = add("ZeugnisBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(130)
		.setJavaComment("Zeugnisbezeihnung für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte Lernfelder */
	public SchemaTabelleSpalte col_Lernfelder = add("Lernfelder", SchemaDatentypen.TEXT, false)
		.setJavaComment("Lernfelder für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte Fachklasse_ID */
	public SchemaTabelleSpalte col_Fachklasse_ID = add("Fachklasse_ID", SchemaDatentypen.BIGINT, true)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Fachklassen ID für die gliederungsbezogenen Einstellungen zum Fach (BK)");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung dfür die gliederungsbezogenen Einstellungen zum Fach (BK)");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Fach_Gliederungen.
	 */
	public Tabelle_Fach_Gliederungen() {
		super("Fach_Gliederungen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.berufskolleg");
		setJavaClassName("DTOFachgliederungen");
		setJavaComment("Gliederungsspezifische Einstellungen zu den Unterrichtsfächern (nur BK)");
	}

}
