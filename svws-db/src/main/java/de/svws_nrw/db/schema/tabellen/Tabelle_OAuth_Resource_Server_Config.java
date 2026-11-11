package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.OAuthResourceTypeConverter;
import de.svws_nrw.db.converter.current.OAuthServerModeConverter;
import de.svws_nrw.db.converter.current.OAuthServiceDomainConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle OAuth_Resource_Server_Config.
 */
public class Tabelle_OAuth_Resource_Server_Config extends SchemaTabelle {

	/** Die Definition der Tabellenspalte id */
	public final SchemaTabelleSpalte col_id = add("id", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaName("id")
			.setJavaComment("ID der Konfiguration des Resource Servers");

	/** Die Definition der Tabellenspalte url */
	public final SchemaTabelleSpalte col_url = add("url", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2048)
			.setNotNull()
			.setJavaName("url")
			.setJavaComment("Die vollständige URL des Resource Servers");

	/** Die Definition der Tabellenspalte domain */
	public final SchemaTabelleSpalte col_domain = add("domain", SchemaDatentypen.VARCHAR, false).setDatenlaenge(32)
			.setNotNull()
			.setConverter(OAuthServiceDomainConverter.class)
			.setConverterRevision(SchemaRevisionen.REV_84)
			.setJavaName("domain")
			.setJavaComment("Die zugehörige fachliche/technische Service Domäne");

	/** Die Definition der Tabellenspalte server_mode */
	public final SchemaTabelleSpalte col_server_mode = add("server_mode", SchemaDatentypen.VARCHAR, false).setDatenlaenge(16)
			.setNotNull()
			.setConverter(OAuthServerModeConverter.class)
			.setConverterRevision(SchemaRevisionen.REV_84)
			.setJavaName("serverMode")
			.setJavaComment("Der Modus der externen Systeme (DEVELOPMENT oder PRODUCTION)");

	/** Die Definition der Tabellenspalte resource_type */
	public final SchemaTabelleSpalte col_resource_type = add("resource_type", SchemaDatentypen.VARCHAR, false).setDatenlaenge(32)
			.setNotNull()
			.setConverter(OAuthResourceTypeConverter.class)
			.setConverterRevision(SchemaRevisionen.REV_84)
			.setJavaName("resourceType")
			.setJavaComment("Die Art des Resource Servers, z.B. SIGNIERDIENST");

	/** Die Definition der Tabellenspalte request_scope */
	public final SchemaTabelleSpalte col_request_scope = add("request_scope", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setNotNull()
			.setJavaName("requestScope")
			.setJavaComment("Der beim Token-Request anzufordernde Scope. Er ist die einzige Quelle für den Scope.");

	/** Die Definition des Unique-Index OAuth_Resource_Server_Config_UC1 */
	public final SchemaTabelleUniqueIndex unique_OAuth_Resource_Server_Config_UC1 = addUniqueIndex("OAuth_Resource_Server_Config_UC1",
			col_domain, col_server_mode, col_resource_type);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle OAuth_Resource_Server_Config.
	 */
	public Tabelle_OAuth_Resource_Server_Config() {
		super("OAuth_Resource_Server_Config", SchemaRevisionen.REV_84);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("svws.auth");
		setJavaClassName("DTOOAuthResourceServerConfig");
		setJavaComment("Konfiguration der Resource Server, die über OAuth angebunden werden (URL und Scope je Domäne, Modus und Ressourcentyp)");
	}

}
