package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulformen.
 */
public class Tabelle_Schulformen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Schulform");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Kürzel der Schulform");

	/** Die Definition der Tabellenspalte Nummer */
	public SchemaTabelleSpalte col_Nummer = add("Nummer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Nummer der Schulform für die amtliche Schulstatistik");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Bezeichnung der Schulform");

	/** Die Definition der Tabellenspalte HatGymOb */
	public SchemaTabelleSpalte col_HatGymOb = add("HatGymOb", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob eine Schule der Schulform eine gymnasiale Oberstufe haben kann (1) oder nicht (0)");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an, ab welchem Schuljahr die Schulform gültig ist");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an, bis zu welchem Schuljahr die Schulform gültig ist");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulformen.
	 */
	public Tabelle_Schulformen() {
		super("Schulformen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAlleSchulformen");
		setJavaComment("Tabelle mit Schulformen");
	}

}
