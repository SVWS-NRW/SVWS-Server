package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchulleitungFunktion.
 */
public class Tabelle_SchulleitungFunktion extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Schulleitungsfunktion");

	/** Die Definition der Tabellenspalte Funktionstext */
	public SchemaTabelleSpalte col_Funktionstext = add("Funktionstext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Beschreibung der Funktion");

	/** Die Definition der Tabellenspalte AbSchuljahr */
	public SchemaTabelleSpalte col_AbSchuljahr = add("AbSchuljahr", SchemaDatentypen.INT, false)
		.setJavaComment("Schulleitungsfunktion ist gültig ab Schuljahr");

	/** Die Definition der Tabellenspalte BisSchuljahr */
	public SchemaTabelleSpalte col_BisSchuljahr = add("BisSchuljahr", SchemaDatentypen.INT, false)
		.setJavaComment("Schulleitungsfunktion ist gültig bis Schuljahr");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchulleitungFunktion.
	 */
	public Tabelle_SchulleitungFunktion() {
		super("SchulleitungFunktion", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOSchulleitungFunktion");
		setJavaComment("Tabelle für die Definition von Schulleitungsfunktionen");
	}

}
