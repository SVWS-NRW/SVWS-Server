package de.svws_nrw.oauth.config;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import de.svws_nrw.oauth.internal.OAuthDomain;

/**
 * Die Konfiguration der Token Server, über die OAuth-Tokens bezogen werden.
 * Je Domäne und Umgebung gibt es höchstens einen Eintrag.
 */
public enum OAuthTokenServerConfig {

	/** Der Token Server der Test-Umgebung von IT.NRW */
	IT_NRW_TEST(OAuthDomain.IT_NRW, OAuthEnvironment.TEST, "https://authservice-bipo.reengineering-ref-neu.nrw.de/oauth2/token"),

	/** Der Token Server der Test-Umgebung von schulbewerbung.de */
	SCHULBEWERBUNG_TEST(OAuthDomain.SCHULBEWERBUNG, OAuthEnvironment.TEST, "https://schulen-anbindungen.demo.schulbewerbung.de/v1/oauth/token");


	private final OAuthDomain domain;

	private final OAuthEnvironment environment;

	private final URI url;


	/**
	 * Erstellt die Konfiguration eines Token Servers.
	 *
	 * @param domain       die OAuth-Domäne
	 * @param environment  die Umgebung der externen Systeme
	 * @param url          die vollständige URL des Token-Endpunktes
	 */
	OAuthTokenServerConfig(final OAuthDomain domain, final OAuthEnvironment environment, final String url) {
		this.domain = domain;
		this.environment = environment;
		this.url = URI.create(url);
	}

	/**
	 * Gibt die OAuth-Domäne zurück.
	 *
	 * @return die OAuth-Domäne
	 */
	public OAuthDomain getDomain() {
		return domain;
	}

	/**
	 * Gibt die Umgebung der externen Systeme zurück.
	 *
	 * @return die Umgebung der externen Systeme
	 */
	public OAuthEnvironment getEnvironment() {
		return environment;
	}

	/**
	 * Gibt die vollständige URL des Token-Endpunktes zurück.
	 *
	 * @return die URL des Token-Endpunktes
	 */
	public URI getUrl() {
		return url;
	}

	/**
	 * Sucht die Konfiguration des Token Servers zur Domäne und zur Umgebung.
	 *
	 * @param domain       die OAuth-Domäne
	 * @param environment  die Umgebung der externen Systeme
	 *
	 * @return die Konfiguration oder ein leeres Optional, falls keine vorhanden ist
	 */
	public static Optional<OAuthTokenServerConfig> find(final OAuthDomain domain, final OAuthEnvironment environment) {
		return Arrays.stream(values())
				.filter(config -> (config.domain == domain) && (config.environment == environment))
				.findFirst();
	}

}
