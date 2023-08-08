package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Client_Konfiguration_Global.
 */
public class Tabelle_Client_Konfiguration_Global extends SchemaTabelle {

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
	 * Erstellt die Schema-Defintion für die Tabelle Client_Konfiguration_Global.
	 */
	public Tabelle_Client_Konfiguration_Global() {
		super("Client_Konfiguration_Global", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("client");
		setJavaClassName("DTOClientKonfigurationGlobal");
		setJavaComment("Tabelle für das Speichern von Client-Konfigurationen als Key-Value-Paare. Dabei werden über das Feld App unterschiedliche Client-Anwendungen unterstützt. Die Konfigurationen in dieser Tabelle gelten global für den Client.");
	}

}
