package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle FachKatalog.
 */
public class Tabelle_FachKatalog extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Faches");

	/** Die Definition der Tabellenspalte KuerzelASD */
	public SchemaTabelleSpalte col_KuerzelASD = add("KuerzelASD", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die texttuelle Beschreibung des Faches");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik");

	/** Die Definition der Tabellenspalte Aufgabenfeld */
	public SchemaTabelleSpalte col_Aufgabenfeld = add("Aufgabenfeld", SchemaDatentypen.INT, false)
		.setJavaComment("Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)");

	/** Die Definition der Tabellenspalte Fachgruppe */
	public SchemaTabelleSpalte col_Fachgruppe = add("Fachgruppe", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Das Kürzel der zugeordneten Fachgruppe");

	/** Die Definition der Tabellenspalte JahrgangAb */
	public SchemaTabelleSpalte col_JahrgangAb = add("JahrgangAb", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen)");

	/** Die Definition der Tabellenspalte IstFremdsprache */
	public SchemaTabelleSpalte col_IstFremdsprache = add("IstFremdsprache", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache)");

	/** Die Definition der Tabellenspalte IstHKFS */
	public SchemaTabelleSpalte col_IstHKFS = add("IstHKFS", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache)");

	/** Die Definition der Tabellenspalte IstAusRegUFach */
	public SchemaTabelleSpalte col_IstAusRegUFach = add("IstAusRegUFach", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird");

	/** Die Definition der Tabellenspalte IstErsatzPflichtFS */
	public SchemaTabelleSpalte col_IstErsatzPflichtFS = add("IstErsatzPflichtFS", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS)");

	/** Die Definition der Tabellenspalte IstKonfKoop */
	public SchemaTabelleSpalte col_IstKonfKoop = add("IstKonfKoop", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik");

	/** Die Definition der Tabellenspalte NurSII */
	public SchemaTabelleSpalte col_NurSII = add("NurSII", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird");

	/** Die Definition der Tabellenspalte ExportASD */
	public SchemaTabelleSpalte col_ExportASD = add("ExportASD", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle FachKatalog.
	 */
	public Tabelle_FachKatalog() {
		super("FachKatalog", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.faecher");
		setJavaClassName("DTOFaecherKatalog");
		setJavaComment("Eine Tabelle mit den zulässigen Fächern");
	}

}
