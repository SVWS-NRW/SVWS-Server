package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die SMTP-Server-Konfiguration für den Email-Dienstanbieter der Schule.
 * Die einzelnen Email-Zugänge werden über die Tabelle BenutzerEmail konfiguriert.
 */
public class Tabelle_EigeneSchule_Email extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID des Eintrags");

	/** Die Definition der Tabellenspalte Domain */
	public SchemaTabelleSpalte col_Domain = add("Domain", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Die Default Email-Domain der Schule");

	/** Die Definition der Tabellenspalte SMTPServer */
	public SchemaTabelleSpalte col_SMTPServer = add("SMTPServer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die Adresse des SMTP-Servers ");

	/** Die Definition der Tabellenspalte SMTPPort */
	public SchemaTabelleSpalte col_SMTPPort = add("SMTPPort", SchemaDatentypen.INT, false)
		.setDefault("25")
		.setNotNull()
		.setJavaComment("Die Port-Adresse des SMTP-Servers");

	/** Die Definition der Tabellenspalte SMTPStartTLS */
	public SchemaTabelleSpalte col_SMTPStartTLS = add("SMTPStartTLS", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob StartTLS für die SMTP-Verbindung genutzt wird (1) oder nicht (0)");

	/** Die Definition der Tabellenspalte SMTPUseTLS */
	public SchemaTabelleSpalte col_SMTPUseTLS = add("SMTPUseTLS", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob TLS für die SMTP-Verbindung genutzt wird (1) oder nicht (0). Ist TLS gesetzt, so wird entweder ein Zertifikat im Key-Store des Servers benötigt oder es muss einem Host vertraut werden (siehe Spalte SMTPTrustTLSHost) ");

	/** Die Definition der Tabellenspalte SMTPTrustTLSHost */
	public SchemaTabelleSpalte col_SMTPTrustTLSHost = add("SMTPTrustTLSHost", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Gibt an, falls gesetzt, welchem Host - unabhängig von Zertifikaten - vertraut werden kann, '*' für jeden beliebigen Host");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_SMTPKonfiguration.
	 */
	public Tabelle_EigeneSchule_Email() {
		super("EigeneSchule_Email", SchemaRevisionen.REV_11);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schule");
		setJavaClassName("DTOSchuleEmail");
		setJavaComment("Die Informationen zu der SMTP-Server-Konfiguration eines Providers für Dienst-Email-Adressen der Schule.");
	}

}
