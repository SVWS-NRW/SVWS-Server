package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
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

	/** Die Definition der Tabellenspalte TokenType */
	public SchemaTabelleSpalte col_TokenType = add("TokenType", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setRevision(SchemaRevisionen.REV_32)
			.setJavaComment("Der Typ des Tokens");

	/** Die Definition der Tabellenspalte TokenTimestamp */
	public SchemaTabelleSpalte col_TokenTimestamp = add("TokenTimestamp", SchemaDatentypen.BIGINT, false)
			.setRevision(SchemaRevisionen.REV_32)
			.setJavaComment("Ankunftzeitpunkt des Tokens als Zeitstempel in Millisekungen");

	/** Die Definition der Tabellenspalte TokenExpiresIn */
	public SchemaTabelleSpalte col_TokenExpiresIn = add("TokenExpiresIn", SchemaDatentypen.BIGINT, false)
			.setRevision(SchemaRevisionen.REV_32)
			.setJavaComment("Lebensdauer des Tokens in Sekunden");

	/** Die Definition der Tabellenspalte TokenScope */
	public SchemaTabelleSpalte col_TokenScope = add("TokenScope", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setRevision(SchemaRevisionen.REV_32)
			.setJavaComment("Der Gültigkeitsbereich des Tokens");

	/** Die Definition der Tabellenspalte Token */
	public SchemaTabelleSpalte col_Token = add("Token", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setRevision(SchemaRevisionen.REV_32)
			.setJavaComment("Das Token");

	/** Die Definition der Tabellenspalte TLSCert */
	public SchemaTabelleSpalte col_TLSCert = add("TLSCert", SchemaDatentypen.TEXT, false)
			.setRevision(SchemaRevisionen.REV_32)
			.setJavaComment("Das TLS Zertifikat des OAuth2-Servers");

	/** Die Definition der Tabellenspalte TLSCertIsKnown */
	public SchemaTabelleSpalte col_TLSCertIsKnown = add("TLSCertIsKnown", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setConverter(Boolean01Converter.class)
			.setRevision(SchemaRevisionen.REV_32)
			.setJavaComment("Gibt an, ob das Zertifikat über den Keystore validiert werden kann.");

	/** Die Definition der Tabellenspalte TLSCertIsTrusted */
	public SchemaTabelleSpalte col_TLSCertIsTrusted = add("TLSCertIsTrusted", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setConverter(Boolean01Converter.class)
			.setRevision(SchemaRevisionen.REV_32)
			.setJavaComment("Gibt an, ob dem TLS-Zertifikat vertraut werden darf, entweder weil es bekannt ist oder weil der Benutzer zugestimmt hat.");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuleOAuthSecrets.
	 */
	public Tabelle_SchuleOAuthSecrets() {
		super("SchuleOAuthSecrets", SchemaRevisionen.REV_1);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.auth");
		setJavaClassName("DTOSchuleOAuthSecrets");
		setJavaComment("Tabelle für die Credentials bei der OAuth-Authentifizierung durch die Schule");
	}

}
