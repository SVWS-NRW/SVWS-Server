package de.svws_nrw.core.types.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

/**
 * Tests für {@link OAuthServiceDomain}.
 */
class OAuthServiceDomainTest {

	@Test
	@DisplayName("getDbValue | liefert die ausgeschriebenen DB-Werte")
	void dbValues() {
		assertEquals("WENOM", OAuthServiceDomain.WENOM.getDbValue());
		assertEquals("SCHULBEWERBUNG", OAuthServiceDomain.SCHULBEWERBUNG.getDbValue());
		assertEquals("IT_NRW", OAuthServiceDomain.IT_NRW.getDbValue());
	}

	@Test
	@DisplayName("getByDbValue | Hin- und Rückabbildung für alle Werte")
	void roundTrip() {
		for (final OAuthServiceDomain domain : OAuthServiceDomain.values()) {
			assertEquals(domain, OAuthServiceDomain.getByDbValue(domain.getDbValue()));
		}
	}

	@Test
	@DisplayName("getByDbValue | unbekannter Wert wirft eine DeveloperNotificationException")
	void unknownValue() {
		assertThrows(DeveloperNotificationException.class, () -> OAuthServiceDomain.getByDbValue("UNBEKANNT"));
		assertThrows(DeveloperNotificationException.class, () -> OAuthServiceDomain.getByDbValue(""));
	}
}
