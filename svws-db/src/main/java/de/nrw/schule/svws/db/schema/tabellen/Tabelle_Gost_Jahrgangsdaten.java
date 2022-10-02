package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Jahrgangsdaten.
 */
public class Tabelle_Gost_Jahrgangsdaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht");

	/** Die Definition der Tabellenspalte ZusatzkursGEVorhanden */
	public SchemaTabelleSpalte col_ZusatzkursGEVorhanden = add("ZusatzkursGEVorhanden", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setConverter("Boolean01Converter")
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Legt fest, ob ein Zusatzkurs in Geschichte angeboten wird");

	/** Die Definition der Tabellenspalte ZusatzkursGEErstesHalbjahr */
	public SchemaTabelleSpalte col_ZusatzkursGEErstesHalbjahr = add("ZusatzkursGEErstesHalbjahr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setDefault("Q2.1")
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Halbjahr, in welchem ein Zusatzkurs in Geschichte beginnt (z.B. Q2.1)");

	/** Die Definition der Tabellenspalte ZusatzkursSWVorhanden */
	public SchemaTabelleSpalte col_ZusatzkursSWVorhanden = add("ZusatzkursSWVorhanden", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setConverter("Boolean01Converter")
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Legt fest, ob ein Zusatzkurs in Sozialwissenschaften angeboten wird");

	/** Die Definition der Tabellenspalte ZusatzkursSWErstesHalbjahr */
	public SchemaTabelleSpalte col_ZusatzkursSWErstesHalbjahr = add("ZusatzkursSWErstesHalbjahr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setDefault("Q2.1")
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Halbjahr, in welchem ein Zusatzkurs in Sozialwissenschaften beginnt (z.B. Q2.1)");

	/** Die Definition der Tabellenspalte TextBeratungsbogen */
	public SchemaTabelleSpalte col_TextBeratungsbogen = add("TextBeratungsbogen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2000)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Text, welcher auf dem Ausdruck eines Beratungsbogens gedruckt wird");

	/** Die Definition der Tabellenspalte TextMailversand */
	public SchemaTabelleSpalte col_TextMailversand = add("TextMailversand", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2000)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Text, welcher in einer Mail beim Versenden von Beratungsdateien verwendet wird");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Jahrgangsdaten.
	 */
	public Tabelle_Gost_Jahrgangsdaten() {
		super("Gost_Jahrgangsdaten", SchemaRevisionen.REV_4);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostJahrgangsdaten");
		setJavaComment("Gymnasiale Oberstufe - Allgemeine Informationen zu den Jahrgängen");
	}

}
