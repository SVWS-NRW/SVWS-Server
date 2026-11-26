package de.svws_nrw.oauth.config;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import de.svws_nrw.oauth.internal.OAuthDomain;

/**
 * Die Konfiguration der Resource Server, die über OAuth angebunden werden.
 * Je Domäne, Umgebung und Ressourcentyp gibt es höchstens einen Eintrag.
 */
public enum OAuthResourceServerConfig {

	/** Der Signierdienst der Test-Umgebung von IT.NRW */
	IT_NRW_SIGNIERDIENST_TEST(
			OAuthDomain.IT_NRW,
			OAuthEnvironment.TEST,
			OAuthResourceType.IT_NRW_SIGNIERDIENST,
			"https://bipo.reengineering-ref-neu.nrw.de/BiPo/signierdienst/api/crypto/signatures",
			"svwp:signierdienst"),

	/** Der Abruf erfolgreicher Bewerbungen in der Test-Umgebung von schulbewerbung.de */
	SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN_TEST(
			OAuthDomain.SCHULBEWERBUNG,
			OAuthEnvironment.TEST,
			OAuthResourceType.SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN,
			"https://schulen-anbindungen.demo.schulbewerbung.de/v1/erfolgreiche-bewerbungen",
			"erfolgreiche-bewerbungen");


	private final OAuthDomain domain;

	private final OAuthEnvironment environment;

	private final OAuthResourceType resourceType;

	private final URI url;

	private final String requestScope;


	/**
	 * Erstellt die Konfiguration eines Resource Servers.
	 *
	 * @param domain         die OAuth-Domäne
	 * @param environment    die Umgebung der externen Systeme
	 * @param resourceType   die Art des Resource Servers
	 * @param url            die vollständige URL des Resource Servers
	 * @param requestScope   der beim Token-Request anzufordernde Scope
	 */
	OAuthResourceServerConfig(
			final OAuthDomain domain,
			final OAuthEnvironment environment,
			final OAuthResourceType resourceType,
			final String url,
			final String requestScope) {
		this.domain = domain;
		this.environment = environment;
		this.resourceType = resourceType;
		this.url = URI.create(url);
		this.requestScope = requestScope;
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
	 * Gibt die Art des Resource Servers zurück.
	 *
	 * @return die Art des Resource Servers
	 */
	public OAuthResourceType getResourceType() {
		return resourceType;
	}

	/**
	 * Gibt die vollständige URL des Resource Servers zurück.
	 *
	 * @return die URL des Resource Servers
	 */
	public URI getUrl() {
		return url;
	}

	/**
	 * Gibt den beim Token-Request anzufordernden Scope zurück. Er ist die einzige Quelle für den Scope.
	 *
	 * @return der Scope
	 */
	public String getRequestScope() {
		return requestScope;
	}

	/**
	 * Sucht die Konfiguration des Resource Servers zur Domäne, zur Umgebung und zum Ressourcentyp.
	 *
	 * @param domain         die OAuth-Domäne
	 * @param environment    die Umgebung der externen Systeme
	 * @param resourceType   die Art des Resource Servers
	 *
	 * @return die Konfiguration oder ein leeres Optional, falls keine vorhanden ist
	 */
	public static Optional<OAuthResourceServerConfig> find(final OAuthDomain domain, final OAuthEnvironment environment,
			final OAuthResourceType resourceType) {
		return Arrays.stream(values())
				.filter(config -> (config.domain == domain) && (config.environment == environment) && (config.resourceType == resourceType))
				.findFirst();
	}

}
