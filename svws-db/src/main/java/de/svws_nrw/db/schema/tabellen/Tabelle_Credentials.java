package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Credentials.
 */
public class Tabelle_Credentials extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datensatzes für die SVWS internen Account-Credentials");

	/** Die Definition der Tabellenspalte Benutzername */
	public SchemaTabelleSpalte col_Benutzername = add("Benutzername", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Benutzername für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte BenutzernamePseudonym */
	public SchemaTabelleSpalte col_BenutzernamePseudonym = add("BenutzernamePseudonym", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Der pseudonymisierte Benutzername für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte Initialkennwort */
	public SchemaTabelleSpalte col_Initialkennwort = add("Initialkennwort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Initialkennwort für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte PasswordHash */
	public SchemaTabelleSpalte col_PasswordHash = add("PasswordHash", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Passwordhash für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte RSAPublicKey */
	public SchemaTabelleSpalte col_RSAPublicKey = add("RSAPublicKey", SchemaDatentypen.TEXT, false)
		.setJavaComment("RSAPublicKey für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte RSAPrivateKey */
	public SchemaTabelleSpalte col_RSAPrivateKey = add("RSAPrivateKey", SchemaDatentypen.TEXT, false)
		.setJavaComment("RSAPrivateKey für den Credential-Datensatz");

	/** Die Definition der Tabellenspalte AES */
	public SchemaTabelleSpalte col_AES = add("AES", SchemaDatentypen.TEXT, false)
		.setJavaComment("AES-Schlüssel für den Credential-Datensatz");


	/** Die Definition des Unique-Index Credentials_UC1 */
	public SchemaTabelleUniqueIndex unique_Credentials_UC1 = addUniqueIndex("Credentials_UC1", 
			col_Benutzername
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Credentials.
	 */
	public Tabelle_Credentials() {
		super("Credentials", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("svws.auth");
		setJavaClassName("DTOCredentials");
		setJavaComment("Daten für die Zugänge zu SVWS internen Systemen für SingleSignOn-Lösungen");
	}

}
