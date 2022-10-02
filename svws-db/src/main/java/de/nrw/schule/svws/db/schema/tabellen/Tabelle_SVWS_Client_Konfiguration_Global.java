package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SVWS_Client_Konfiguration_Global.
 */
public class Tabelle_SVWS_Client_Konfiguration_Global extends SchemaTabelle {

	/** Die Definition der Tabellenspalte AppName */
	public SchemaTabelleSpalte col_AppName = add("AppName", SchemaDatentypen.VARCHAR, true).setDatenlaenge(100)
		.setNotNull()
		.setJavaComment("Der Name der Client-Anwendung, für die der Konfigurationsdatensatz gespeichert ist");

	/** Die Definition der Tabellenspalte Schluessel */
	public SchemaTabelleSpalte col_Schluessel = add("Schluessel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Der Schlüsselname des Konfigurationsdatensatzes");

	/** Die Definition der Tabellenspalte Wert */
	public SchemaTabelleSpalte col_Wert = add("Wert", SchemaDatentypen.TEXT, false)
		.setNotNull()
		.setJavaComment("Der Wert des Konfigurationsdatensatzes");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SVWS_Client_Konfiguration_Global.
	 */
	public Tabelle_SVWS_Client_Konfiguration_Global() {
		super("SVWS_Client_Konfiguration_Global", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.client");
		setJavaClassName("DTOClientKonfigurationGlobal");
		setJavaComment("Tabelle für das Speichern von Client-Konfigurationen als Key-Value-Paare. Dabei werden über das Feld App unterschiedliche Client-Anwendungen unterstützt. Die Konfigurationen in dieser Tabelle gelten global für den Client.");
	}

}
