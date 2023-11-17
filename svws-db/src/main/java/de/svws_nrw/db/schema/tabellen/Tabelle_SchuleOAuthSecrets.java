package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuleOAuthSecrets.
 */
public class Tabelle_SchuleOAuthSecrets extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des OAuth-Datensatzes");

	/** Die Definition der Tabellenspalte AuthServer */
	public SchemaTabelleSpalte col_AuthServer = add("AuthServer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Der Authorization Server");

	/** Die Definition der Tabellenspalte ClientID */
	public SchemaTabelleSpalte col_ClientID = add("ClientID", SchemaDatentypen.TEXT, false)
		.setNotNull()
		.setJavaComment("Die ID des Clients");

	/** Die Definition der Tabellenspalte ClientSecret */
	public SchemaTabelleSpalte col_ClientSecret = add("ClientSecret", SchemaDatentypen.TEXT, false)
		.setNotNull()
		.setJavaComment("Das Secret des Clients");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuleOAuthSecrets.
	 */
	public Tabelle_SchuleOAuthSecrets() {
		super("SchuleOAuthSecrets", SchemaRevisionen.REV_14);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.auth");
		setJavaClassName("DTOSchuleOAuthSecrets");
		setJavaComment("Tabelle für die Credentials bei der OAuth-Authentifizierung durch die Schule");
	}

}
