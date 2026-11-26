package de.svws_nrw.oauth.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.ServerMode;

/**
 * Tests für {@link OAuthEnvironment}.
 */
@DisplayName("Tests für OAuthEnvironment")
class OAuthEnvironmentTest {

	@Test
	@DisplayName("of: STABLE wird auf PROD abgebildet")
	void of_stable_prod() {
		assertEquals(OAuthEnvironment.PROD, OAuthEnvironment.of(ServerMode.STABLE));
	}

	@Test
	@DisplayName("of: BETA, ALPHA und DEV werden auf TEST abgebildet")
	void of_nichtStable_test() {
		assertEquals(OAuthEnvironment.TEST, OAuthEnvironment.of(ServerMode.BETA));
		assertEquals(OAuthEnvironment.TEST, OAuthEnvironment.of(ServerMode.ALPHA));
		assertEquals(OAuthEnvironment.TEST, OAuthEnvironment.of(ServerMode.DEV));
	}

}
