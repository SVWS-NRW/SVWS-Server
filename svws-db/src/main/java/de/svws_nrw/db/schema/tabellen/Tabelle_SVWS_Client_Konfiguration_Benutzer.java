package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SVWS_Client_Konfiguration_Benutzer.
 */
public class Tabelle_SVWS_Client_Konfiguration_Benutzer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Benutzer_ID */
	public SchemaTabelleSpalte col_Benutzer_ID = add("Benutzer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID des Datenbankbenutzers, für den der Client-Konfigurationsdatensatz gespeichert ist");

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


	/** Die Definition des Fremdschlüssels SVWSClientKonfigurationBenutzer_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_SVWSClientKonfigurationBenutzer_Benutzer_FK = addForeignKey(
			"SVWSClientKonfigurationBenutzer_Benutzer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Benutzer_ID, Schema.tab_Benutzer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SVWS_Client_Konfiguration_Benutzer.
	 */
	public Tabelle_SVWS_Client_Konfiguration_Benutzer() {
		super("SVWS_Client_Konfiguration_Benutzer", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.client");
		setJavaClassName("DTOClientKonfigurationBenutzer");
		setJavaComment("Tabelle für das Speichern von Client-Konfigurationen als Key-Value-Paare. Dabei werden über das Feld App unterschiedliche Client-Anwendungen unterstützt und über das Feld Benutzer eine Benutzerspezifische Speicherung.");
	}

}
