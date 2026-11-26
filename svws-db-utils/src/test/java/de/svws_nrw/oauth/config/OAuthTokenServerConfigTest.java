package de.svws_nrw.oauth.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.svws_nrw.oauth.internal.OAuthDomain;

/**
 * Tests für {@link OAuthTokenServerConfig}.
 */
@DisplayName("Tests für OAuthTokenServerConfig")
class OAuthTokenServerConfigTest {

	@Test
	@DisplayName("IT.NRW TEST: Token-URL der Test-Umgebung")
	void itNrwDev() {
		final var config = OAuthTokenServerConfig.find(OAuthDomain.IT_NRW, OAuthEnvironment.TEST).orElseThrow();
		assertEquals(OAuthTokenServerConfig.IT_NRW_TEST, config);
		assertEquals(URI.create("https://authservice-bipo.reengineering-ref-neu.nrw.de/oauth2/token"), config.getUrl());
	}

	@Test
	@DisplayName("schulbewerbung.de TEST: Token-URL der Test-Umgebung")
	void schulbewerbungDev() {
		final var config = OAuthTokenServerConfig.find(OAuthDomain.SCHULBEWERBUNG, OAuthEnvironment.TEST).orElseThrow();
		assertEquals(OAuthTokenServerConfig.SCHULBEWERBUNG_TEST, config);
		assertEquals(URI.create("https://schulen-anbindungen.demo.schulbewerbung.de/v1/oauth/token"), config.getUrl());
	}

	@ParameterizedTest
	@EnumSource(OAuthDomain.class)
	@DisplayName("find: für PROD ist noch keine Konfiguration vorhanden")
	void find_prod_leer(final OAuthDomain domain) {
		assertTrue(OAuthTokenServerConfig.find(domain, OAuthEnvironment.PROD).isEmpty());
	}

	@Test
	@DisplayName("find: für WENOM ist keine Konfiguration vorhanden")
	void find_wenom_leer() {
		assertTrue(OAuthTokenServerConfig.find(OAuthDomain.WENOM, OAuthEnvironment.TEST).isEmpty());
	}

	@ParameterizedTest
	@EnumSource(OAuthTokenServerConfig.class)
	@DisplayName("find: jeder Eintrag wird über Domäne und Umgebung gefunden")
	void find_jederEintrag(final OAuthTokenServerConfig config) {
		assertEquals(config, OAuthTokenServerConfig.find(config.getDomain(), config.getEnvironment()).orElseThrow());
	}

}
