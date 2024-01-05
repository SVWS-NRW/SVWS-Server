package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schema_Status.
 */
public class Tabelle_Schema_Status extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Revision */
	public SchemaTabelleSpalte col_Revision = add("Revision", SchemaDatentypen.BIGINT, true)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Die Revision des Datenbankschemas der SVWS-DB");

	/** Die Definition der Tabellenspalte IsTainted */
	public SchemaTabelleSpalte col_IsTainted = add("IsTainted", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Datenbank noch für einen Produktivbetrieb zugelassen ist oder durch ein Update auf eine Entwicklerversion eventuell in einem ungültigen Zustand ist");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schema_Status.
	 */
	public Tabelle_Schema_Status() {
		super("Schema_Status", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schema");
		setJavaClassName("DTOSchemaStatus");
		setJavaComment("Diese Tabelle enthält Informationen zum Status der SVWS-DB");
	}

}
