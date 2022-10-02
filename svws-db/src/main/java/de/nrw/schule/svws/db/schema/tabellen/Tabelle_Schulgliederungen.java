package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulgliederungen.
 */
public class Tabelle_Schulgliederungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Schulgliederung");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Kürzel der Schulgliederung");

	/** Die Definition der Tabellenspalte IstBKBildungsgang */
	public SchemaTabelleSpalte col_IstBKBildungsgang = add("IstBKBildungsgang", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, ob die Schulgliederung einen Bildungsgang am Berufskollegs darstellt (1) oder nicht (0)");

	/** Die Definition der Tabellenspalte IstAuslaufend */
	public SchemaTabelleSpalte col_IstAuslaufend = add("IstAuslaufend", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, ob die Schulgliederung auslaufend ist (1) oder nicht (0)");

	/** Die Definition der Tabellenspalte IstAusgelaufen */
	public SchemaTabelleSpalte col_IstAusgelaufen = add("IstAusgelaufen", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, ob die Schulgliederung ausgelaufen ist (1) oder nicht (0)");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Bezeichnung der Schulgliederung");

	/** Die Definition der Tabellenspalte BKAnlage */
	public SchemaTabelleSpalte col_BKAnlage = add("BKAnlage", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Die Anlage, falls es sich um einen Bildungsgang am Berufskolleg handelt");

	/** Die Definition der Tabellenspalte BKTyp */
	public SchemaTabelleSpalte col_BKTyp = add("BKTyp", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setJavaComment("Der Typ der Anlage, falls es sich um einen Bildungsgang am Berufskolleg handelt");

	/** Die Definition der Tabellenspalte BKIndex */
	public SchemaTabelleSpalte col_BKIndex = add("BKIndex", SchemaDatentypen.INT, false)
		.setJavaComment("Der Index für den Zugriff auf die Fachklassen, falls es sich um einen Bildungsgang am Berufskolleg handelt");

	/** Die Definition der Tabellenspalte IstVZ */
	public SchemaTabelleSpalte col_IstVZ = add("IstVZ", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt (1) oder nicht (0)");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an, ab welchem Schuljahr die Schulgliederung gültig ist");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an, bis zu welchem Schuljahr die Schulgliederungen gültig ist");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulgliederungen.
	 */
	public Tabelle_Schulgliederungen() {
		super("Schulgliederungen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAlleSchulgliederungen");
		setJavaComment("Tabelle Schulgliederungen");
	}

}
