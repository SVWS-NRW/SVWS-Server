package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Notenmodul_Verbindungen.
 */
public class Tabelle_Notenmodul_Verbindungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte id */
	public final SchemaTabelleSpalte col_id = add("id", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID der Verbindung");

	/** Die Definition der Tabellenspalte bezeichnung */
	public final SchemaTabelleSpalte col_bezeichnung = add("bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Die Bezeichnung der Verbindung für die Darstellung in der Verbindungsliste (null, wenn einfach die URL dargestellt werden soll)");

	/** Die Definition der Tabellenspalte url */
	public final SchemaTabelleSpalte col_url = add("url", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setNotNull()
			.setJavaComment("Die URL des Servers des externen Notenmoduls");

	/** Die Definition der Tabellenspalte clientID */
	public final SchemaTabelleSpalte col_clientID = add("clientID", SchemaDatentypen.TEXT, false)
			.setNotNull()
			.setJavaComment("Die Client-ID, welche zusammen mit dem Client-Secret, zur für die OAuth2-Verbindung zu dem externen Server genutzt wird.");

	/** Die Definition der Tabellenspalte clientSecret */
	public final SchemaTabelleSpalte col_clientSecret  = add("clientSecret", SchemaDatentypen.TEXT, false)
			.setNotNull()
			.setJavaComment("Das Client-Secret, welches zusammen mit der Client-ID, zur für die OAuth2-Verbindung zu dem externen Server genutzt wird.");

	/** Die Definition der Tabellenspalte tokenTimestamp */
	public final SchemaTabelleSpalte col_tokenTimestamp = add("tokenTimestamp", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Verbindungs-Token: Ankunftzeitpunkt des Tokens als Zeitstempel in Millisekungen");

	/** Die Definition der Tabellenspalte tokenExpiresIn */
	public final SchemaTabelleSpalte col_tokenExpiresIn = add("tokenExpiresIn", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Verbindungs-Token: Lebensdauer des Tokens in Sekunden");

	/** Die Definition der Tabellenspalte token */
	public final SchemaTabelleSpalte col_token = add("token", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Verbindungs-Token: Das Token");

	/** Die Definition der Tabellenspalte serverTLSCert */
	public final SchemaTabelleSpalte col_serverTLSCert = add("serverTLSCert", SchemaDatentypen.TEXT, false)
			.setJavaComment("Externener Server: Das TLS Zertifikat des OAuth2-Servers");

	/** Die Definition der Tabellenspalte serverTLSCertIsKnown */
	public final SchemaTabelleSpalte col_serverTLSCertIsKnown = add("serverTLSCertIsKnown", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Externener Server: Gibt an, ob das Zertifikat über den Keystore validiert werden kann.");

	/** Die Definition der Tabellenspalte serverTLSCertIsTrusted */
	public final SchemaTabelleSpalte col_serverTLSCertIsTrusted = add("serverTLSCertIsTrusted", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Externener Server: Gibt an, ob dem TLS-Zertifikat vertraut werden darf, entweder weil es bekannt ist oder weil der Benutzer zugestimmt hat.");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuleOAuthSecrets.
	 */
	public Tabelle_Notenmodul_Verbindungen() {
		super("Notenmodul_Verbindungen", SchemaRevisionen.REV_53);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("notenmodul");
		setJavaClassName("DTONotenmodulVerbindungen");
		setJavaComment("Tabelle für die Informationen von Notenmodul-Verbindungen u.a. mit den OAuth2-Verbindungsinformationen zu externen Servern");
	}

}
