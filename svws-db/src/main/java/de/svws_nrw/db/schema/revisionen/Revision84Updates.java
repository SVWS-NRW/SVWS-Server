package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 84. Sie befüllt die neuen Konfigurationstabellen für externe
 * OAuth-Systeme mit den Test-Werten (Modus DEV) für IT.NRW (Signierdienst) und
 * schulbewerbung.de (Abruf erfolgreicher Bewerbungen). Die Spalten
 * AuthServer und RequestedScope in SchuleOAuthSecrets werden ohne Datenübernahme
 * vom DBUpdater entfernt.
 */
public final class Revision84Updates extends SchemaRevisionUpdateSQL {

	/** Der DB-Wert der Domäne IT.NRW. Bewusst fest eingetragen, damit die Revision unabhängig von späteren Änderungen am Core-Enum bleibt. */
	private static final String DOMAIN_IT_NRW = "IT_NRW";

	/** Der DB-Wert der Domäne schulbewerbung.de. Bewusst fest eingetragen, damit die Revision unabhängig von späteren Änderungen am Core-Enum bleibt. */
	private static final String DOMAIN_SCHULBEWERBUNG = "SCHULBEWERBUNG";

	/** Der DB-Wert des Server-Modus für die Test-Umgebungen */
	private static final String SERVER_MODE_DEV = "DEVELOPMENT";

	/** Der DB-Wert des Ressourcentyps für den Signierdienst von IT.NRW */
	private static final String RESOURCE_TYPE_SIGNIERDIENST = "SIGNIERDIENST";

	/** Der DB-Wert des Ressourcentyps für den Abruf erfolgreicher Bewerbungen bei schulbewerbung.de */
	private static final String RESOURCE_TYPE_ERFOLGREICHE_BEWERBUNGEN = "ERFOLGREICHE_BEWERBUNGEN";

	/** Die Basis-URL der Test-Umgebung von IT.NRW */
	private static final String IT_NRW_BASIS_URL = "https://bipo.reengineering-ref-neu.nrw.de";

	/** Die URL des Token-Endpunktes der Test-Umgebung von IT.NRW */
	private static final String IT_NRW_TOKEN_URL = IT_NRW_BASIS_URL + "/oauth2/token";

	/** Die URL des Signierdienstes der Test-Umgebung von IT.NRW */
	private static final String IT_NRW_SIGNIERDIENST_URL = IT_NRW_BASIS_URL + "/BiPo/signierdienst/api/crypto/signatures";

	/** Der Scope für den Signierdienst */
	private static final String IT_NRW_SIGNIERDIENST_SCOPE = "svwp:signierdienst";

	/** Die Basis-URL der Test-Umgebung von schulbewerbung.de */
	private static final String SCHULBEWERBUNG_BASIS_URL = "https://schulen-anbindungen.demo.schulbewerbung.de/v1";

	/** Die URL des Token-Endpunktes der Test-Umgebung von schulbewerbung.de */
	private static final String SCHULBEWERBUNG_TOKEN_URL = SCHULBEWERBUNG_BASIS_URL + "/oauth/token";

	/** Die URL des Endpunktes für erfolgreiche Bewerbungen bei schulbewerbung.de */
	private static final String ERFOLGREICHE_BEWERBUNGEN_URL = SCHULBEWERBUNG_BASIS_URL + "/erfolgreiche-bewerbungen";

	/** Der Scope für den Abruf erfolgreicher Bewerbungen bei schulbewerbung.de */
	private static final String ERFOLGREICHE_BEWERBUNGEN_SCOPE = "erfolgreiche-bewerbungen";

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 84.
	 */
	public Revision84Updates() {
		super(SchemaRevisionen.REV_84);
		seedTokenServer(1, DOMAIN_IT_NRW, IT_NRW_TOKEN_URL);
		seedTokenServer(2, DOMAIN_SCHULBEWERBUNG, SCHULBEWERBUNG_TOKEN_URL);
		seedResourceServer(1, DOMAIN_IT_NRW, IT_NRW_SIGNIERDIENST_URL, RESOURCE_TYPE_SIGNIERDIENST, IT_NRW_SIGNIERDIENST_SCOPE);
		seedResourceServer(2, DOMAIN_SCHULBEWERBUNG, ERFOLGREICHE_BEWERBUNGEN_URL, RESOURCE_TYPE_ERFOLGREICHE_BEWERBUNGEN,
				ERFOLGREICHE_BEWERBUNGEN_SCOPE);
	}

	private void seedTokenServer(final long id, final String domain, final String url) {
		add("Ergänze die Konfiguration des Token Servers für %s im Modus DEV".formatted(domain),
				"INSERT INTO %1$s(id, url, domain, server_mode) VALUES (%2$d, '%3$s', '%4$s', '%5$s')"
						.formatted(Schema.tab_OAuth_Token_Server_Config.name(), id, url, domain, SERVER_MODE_DEV),
				Schema.tab_OAuth_Token_Server_Config);
	}

	private void seedResourceServer(final long id, final String domain, final String url, final String resourceType,
			final String scope) {
		add("Ergänze die Konfiguration des Resource Servers %s für %s im Modus DEV".formatted(resourceType, domain),
				"INSERT INTO %1$s(id, url, domain, server_mode, resource_type, request_scope) VALUES (%2$d, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s')"
						.formatted(Schema.tab_OAuth_Resource_Server_Config.name(), id, url, domain, SERVER_MODE_DEV, resourceType, scope),
				Schema.tab_OAuth_Resource_Server_Config);
	}

}
