package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SVWS_Core_Type_Versionen.
 */
public class Tabelle_SVWS_Core_Type_Versionen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte NameTabelle */
	public SchemaTabelleSpalte col_NameTabelle = add("NameTabelle", SchemaDatentypen.VARCHAR, true).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Gibt den Namen der Tabelle an, wo die Daten des Core-Types hinterlegt werden.");

	/** Die Definition der Tabellenspalte Name */
	public SchemaTabelleSpalte col_Name = add("Name", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1023)
		.setNotNull()
		.setJavaComment("Gibt den Namen des Core-Types an.");

	/** Die Definition der Tabellenspalte Version */
	public SchemaTabelleSpalte col_Version = add("Version", SchemaDatentypen.BIGINT, false)
		.setDefault("1")
		.setNotNull()
		.setJavaComment("Die Version, in welcher der Core-Type in der DB vorliegt");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SVWS_Core_Type_Versionen.
	 */
	public Tabelle_SVWS_Core_Type_Versionen() {
		super("SVWS_Core_Type_Versionen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("svws.db");
		setJavaClassName("DTOCoreTypeVersion");
		setJavaComment("Tabelle für das Speichern, in welcher Version die Core-Type-Daten in den Datenbank-Tabellen vorliegen");
	}

}
