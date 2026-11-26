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
 * Tests für {@link OAuthResourceServerConfig}.
 */
@DisplayName("Tests für OAuthResourceServerConfig")
class OAuthResourceServerConfigTest {

	@Test
	@DisplayName("IT.NRW TEST: URL und Scope des Signierdienstes")
	void itNrwSignierdienstDev() {
		final var config = OAuthResourceServerConfig
				.find(OAuthDomain.IT_NRW, OAuthEnvironment.TEST, OAuthResourceType.IT_NRW_SIGNIERDIENST).orElseThrow();
		assertEquals(OAuthResourceServerConfig.IT_NRW_SIGNIERDIENST_TEST, config);
		assertEquals(URI.create("https://bipo.reengineering-ref-neu.nrw.de/BiPo/signierdienst/api/crypto/signatures"), config.getUrl());
		assertEquals("svwp:signierdienst", config.getRequestScope());
	}

	@Test
	@DisplayName("schulbewerbung.de TEST: URL und Scope für erfolgreiche Bewerbungen")
	void schulbewerbungErfolgreicheBewerbungenDev() {
		final var config = OAuthResourceServerConfig
				.find(OAuthDomain.SCHULBEWERBUNG, OAuthEnvironment.TEST, OAuthResourceType.SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN).orElseThrow();
		assertEquals(OAuthResourceServerConfig.SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN_TEST, config);
		assertEquals(URI.create("https://schulen-anbindungen.demo.schulbewerbung.de/v1/erfolgreiche-bewerbungen"), config.getUrl());
		assertEquals("erfolgreiche-bewerbungen", config.getRequestScope());
	}

	@ParameterizedTest
	@EnumSource(OAuthResourceType.class)
	@DisplayName("find: für PROD ist noch keine Konfiguration vorhanden")
	void find_prod_leer(final OAuthResourceType resourceType) {
		assertTrue(OAuthResourceServerConfig.find(OAuthDomain.IT_NRW, OAuthEnvironment.PROD, resourceType).isEmpty());
		assertTrue(OAuthResourceServerConfig.find(OAuthDomain.SCHULBEWERBUNG, OAuthEnvironment.PROD, resourceType).isEmpty());
	}

	@Test
	@DisplayName("find: Ressourcentyp einer anderen Domäne liefert keine Konfiguration")
	void find_falscheDomaene_leer() {
		assertTrue(OAuthResourceServerConfig.find(OAuthDomain.SCHULBEWERBUNG, OAuthEnvironment.TEST, OAuthResourceType.IT_NRW_SIGNIERDIENST).isEmpty());
	}

	@ParameterizedTest
	@EnumSource(OAuthResourceServerConfig.class)
	@DisplayName("find: jeder Eintrag wird über Domäne, Umgebung und Ressourcentyp gefunden")
	void find_jederEintrag(final OAuthResourceServerConfig config) {
		assertEquals(config, OAuthResourceServerConfig.find(config.getDomain(), config.getEnvironment(), config.getResourceType()).orElseThrow());
	}

}
