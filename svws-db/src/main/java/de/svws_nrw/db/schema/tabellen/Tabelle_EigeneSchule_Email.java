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
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Eintrags");

	/** Die Definition der Tabellenspalte Domain */
	public final SchemaTabelleSpalte col_Domain = add("Domain", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Die Default Email-Domain der Schule");

	/** Die Definition der Tabellenspalte SMTPServer */
	public final SchemaTabelleSpalte col_SMTPServer = add("SMTPServer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setNotNull()
			.setJavaComment("Die Adresse des SMTP-Servers ");

	/** Die Definition der Tabellenspalte SMTPPort */
	public final SchemaTabelleSpalte col_SMTPPort = add("SMTPPort", SchemaDatentypen.INT, false)
			.setDefault("25")
			.setNotNull()
			.setJavaComment("Die Port-Adresse des SMTP-Servers");

	/** Die Definition der Tabellenspalte SMTPStartTLS */
	public final SchemaTabelleSpalte col_SMTPStartTLS = add("SMTPStartTLS", SchemaDatentypen.INT, false)
			.setDefault("1")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob StartTLS für die SMTP-Verbindung genutzt wird (1) oder nicht (0)");

	/** Die Definition der Tabellenspalte SMTPUseTLS */
	public final SchemaTabelleSpalte col_SMTPUseTLS = add("SMTPUseTLS", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment(
					"Gibt an, ob TLS für die SMTP-Verbindung genutzt wird (1) oder nicht (0). Ist TLS gesetzt, muss das Zertifikat des SMTP-Servers vertrauenswürdig sein oder zum Fingerabdruck in der Spalte SMTPZertifikatFingerabdruck passen");

	/** Die Definition der Tabellenspalte SMTPTrustTLSHost */
	public final SchemaTabelleSpalte col_SMTPTrustTLSHost = add("SMTPTrustTLSHost", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Gibt an, falls gesetzt, welchem Host - unabhängig von Zertifikaten - vertraut werden kann, '*' für jeden beliebigen Host. Die Spalte nutzen ausschließlich externe Programme, der SVWS-Server wertet sie nicht aus.");

	/** Die Definition der Tabellenspalte SMTPZertifikatFingerabdruck */
	public final SchemaTabelleSpalte col_SMTPZertifikatFingerabdruck = add("SMTPZertifikatFingerabdruck", SchemaDatentypen.VARCHAR, false).setDatenlaenge(95)
			.setJavaComment("Der SHA-256-Fingerabdruck eines Zertifikats des SMTP-Servers. Ist er gesetzt, vertraut der SVWS-Server nur noch diesem einen Zertifikat, z. B. auch einem selbst erstellten")
			.setRevision(SchemaRevisionen.REV_86);


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
