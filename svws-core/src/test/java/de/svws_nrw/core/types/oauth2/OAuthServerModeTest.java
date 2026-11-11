package de.svws_nrw.core.types.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.ServerMode;

/**
 * Tests für {@link OAuthServerMode}.
 */
class OAuthServerModeTest {

	@Test
	@DisplayName("getDbValue | liefert die ausgeschriebenen DB-Werte")
	void dbValues() {
		assertEquals("DEVELOPMENT", OAuthServerMode.DEV.getDbValue());
		assertEquals("PRODUCTION", OAuthServerMode.PROD.getDbValue());
	}

	@Test
	@DisplayName("getByDbValue | Hin- und Rückabbildung für alle Werte")
	void roundTrip() {
		for (final OAuthServerMode mode : OAuthServerMode.values()) {
			assertEquals(mode, OAuthServerMode.getByDbValue(mode.getDbValue()));
		}
	}

	@Test
	@DisplayName("getByDbValue | unbekannter Wert wirft eine DeveloperNotificationException")
	void unknownValue() {
		assertThrows(DeveloperNotificationException.class, () -> OAuthServerMode.getByDbValue("DEV"));
		assertThrows(DeveloperNotificationException.class, () -> OAuthServerMode.getByDbValue(""));
	}

	@Test
	@DisplayName("getByServerMode | STABLE wird auf PROD abgebildet")
	void stableIsProd() {
		assertEquals(OAuthServerMode.PROD, OAuthServerMode.getByServerMode(ServerMode.STABLE));
	}

	@Test
	@DisplayName("getByServerMode | BETA, ALPHA und DEV werden auf DEV abgebildet")
	void nonStableIsDev() {
		assertEquals(OAuthServerMode.DEV, OAuthServerMode.getByServerMode(ServerMode.BETA));
		assertEquals(OAuthServerMode.DEV, OAuthServerMode.getByServerMode(ServerMode.ALPHA));
		assertEquals(OAuthServerMode.DEV, OAuthServerMode.getByServerMode(ServerMode.DEV));
	}

	@Test
	@DisplayName("getByServerMode | null fällt auf PROD zurück, nie auf DEV")
	void nullIsProd() {
		assertEquals(OAuthServerMode.PROD, OAuthServerMode.getByServerMode(null));
	}
}
