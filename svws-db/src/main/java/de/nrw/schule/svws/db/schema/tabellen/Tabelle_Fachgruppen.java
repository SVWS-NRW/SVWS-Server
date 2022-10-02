package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Fachgruppen.
 */
public class Tabelle_Fachgruppen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Fachgruppen-Core-Type, welcher auch ein Mapping zu den Fachgruppen von SchildNRW und Lupo bereitstellt");

	/** Die Definition der Tabellenspalte Fachbereich */
	public SchemaTabelleSpalte col_Fachbereich = add("Fachbereich", SchemaDatentypen.INT, false)
		.setJavaComment("Fachbereich (Nr) der Fachgruppe aus Lupo");

	/** Die Definition der Tabellenspalte SchildFgID */
	public SchemaTabelleSpalte col_SchildFgID = add("SchildFgID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Fachgruppen ID aus SchildNRW");

	/** Die Definition der Tabellenspalte FG_Bezeichnung */
	public SchemaTabelleSpalte col_FG_Bezeichnung = add("FG_Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setNotNull()
		.setJavaComment("Bezeichnung der Fachgruppe");

	/** Die Definition der Tabellenspalte FG_Kuerzel */
	public SchemaTabelleSpalte col_FG_Kuerzel = add("FG_Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kürzel der Fachgruppe");

	/** Die Definition der Tabellenspalte Schulformen */
	public SchemaTabelleSpalte col_Schulformen = add("Schulformen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Gibt an in welchen Schulformen die Fachgruppe zulässig ist");

	/** Die Definition der Tabellenspalte FarbeR */
	public SchemaTabelleSpalte col_FarbeR = add("FarbeR", SchemaDatentypen.INT, false)
		.setJavaComment("Default-Fachgruppenfarbe (Rot-Wert)");

	/** Die Definition der Tabellenspalte FarbeG */
	public SchemaTabelleSpalte col_FarbeG = add("FarbeG", SchemaDatentypen.INT, false)
		.setJavaComment("Default-Fachgruppenfarbe (Grün-Wert)");

	/** Die Definition der Tabellenspalte FarbeB */
	public SchemaTabelleSpalte col_FarbeB = add("FarbeB", SchemaDatentypen.INT, false)
		.setJavaComment("Default-Fachgruppenfarbe (Blau-Wert)");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Eine Standard-Sortierreihenfolge für die Fachgruppen");

	/** Die Definition der Tabellenspalte FuerZeugnis */
	public SchemaTabelleSpalte col_FuerZeugnis = add("FuerZeugnis", SchemaDatentypen.INT, false)
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, ob die Fachgruppe für Unterteilungen auf Zeugnissen verwendet wird");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Das Schuljahr, ab dem die Fachgruppe eingeführt wurde");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Das Schuljahr, bis wann die Fachgruppe gültig ist");


	/** Die Definition des Unique-Index Fachgruppen_UC1 */
	public SchemaTabelleUniqueIndex unique_Fachgruppen_UC1 = addUniqueIndex("Fachgruppen_UC1", 
			col_SchildFgID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Fachgruppen.
	 */
	public Tabelle_Fachgruppen() {
		super("Fachgruppen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.faecher");
		setJavaClassName("DTOFachgruppen");
		setJavaComment("Tabelle für den Core-Type der Fachgruppen");
	}

}
