package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Tabelle_Notenmodul_Konfiguration_Server.
 */
public class Tabelle_Notenmodul_Konfiguration_Server extends SchemaTabelle {

	/** Die Definition der Tabellenspalte schluessel */
	public final SchemaTabelleSpalte col_schluessel = add("schluessel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(255)
			.setNotNull()
			.setJavaComment("Der Schlüssel des Konfigurationseintrags");

	/** Die Definition der Tabellenspalte wert */
	public final SchemaTabelleSpalte col_wert = add("wert", SchemaDatentypen.TEXT, false)
			.setNotNull()
			.setJavaComment("Der Wert des Konfigurationseintrags");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Notenmodul_Konfiguration_Server.
	 */
	public Tabelle_Notenmodul_Konfiguration_Server() {
		super("Notenmodul_Konfiguration_Server", SchemaRevisionen.REV_53);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("notenmodul");
		setJavaClassName("DTONotenmodulKonfigurationServer");
		setJavaComment("Tabelle für die Informationen zu der Server-Konfiguration des lokalen Notenmoduls, welche auch als Vorlage für die externen Notenmodule verwendet werden kann.");
	}

}
