package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SVWS_DB_Version.
 */
public class Tabelle_SVWS_DB_Version extends SchemaTabelle {

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
	 * Erstellt die Schema-Defintion für die Tabelle SVWS_DB_Version.
	 */
	public Tabelle_SVWS_DB_Version() {
		super("SVWS_DB_Version", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("svws.db");
		setJavaClassName("DTODBVersion");
		setJavaComment("Diese Tabelle enthält die Versionsinformationen zu der SVWS-DB");
	}

}
