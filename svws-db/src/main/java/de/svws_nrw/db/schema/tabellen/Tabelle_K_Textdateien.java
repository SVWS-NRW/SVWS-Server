package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Textdateien.
 */
public class Tabelle_K_Textdateien extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Textes der zum Ersatz für txt-Dateien genutzt wird");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(32)
		.setJavaComment("Bezeichnungdes Textes der zum Ersatz für txt-Dateien genutzt wird");

	/** Die Definition der Tabellenspalte Text_ID */
	public SchemaTabelleSpalte col_Text_ID = add("Text_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("TextID des Textes der zum Ersatz für txt-Dateien genutzt wird");

	/** Die Definition der Tabellenspalte Text_Body */
	public SchemaTabelleSpalte col_Text_Body = add("Text_Body", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text-Body des Textes der zum Ersatz für txt-Dateien genutzt wird");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Sichbarkeit des Textes der zum Ersatz für txt-Dateien genutzt wird");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Sortierung des Textes der zum Ersatz für txt-Dateien genutzt wird");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Textdateien.
	 */
	public Tabelle_K_Textdateien() {
		super("K_Textdateien", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOTextDateien");
		setJavaComment("Katalog für TXT-Dateien, diese werden ab Schild3.0 intern verwaltet (z.B für Email-Texte)");
	}

}
