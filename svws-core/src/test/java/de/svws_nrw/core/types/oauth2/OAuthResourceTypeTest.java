package de.svws_nrw.core.types.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

/**
 * Tests für {@link OAuthResourceType}.
 */
class OAuthResourceTypeTest {

	@Test
	@DisplayName("getDbValue | liefert den ausgeschriebenen DB-Wert")
	void dbValue() {
		assertEquals("SIGNIERDIENST", OAuthResourceType.IT_NRW_SIGNIERDIENST.getDbValue());
		assertEquals("ERFOLGREICHE_BEWERBUNGEN", OAuthResourceType.SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN.getDbValue());
	}

	@Test
	@DisplayName("getByDbValue | Hin- und Rückabbildung für alle Werte")
	void roundTrip() {
		for (final OAuthResourceType type : OAuthResourceType.values()) {
			assertEquals(type, OAuthResourceType.getByDbValue(type.getDbValue()));
		}
	}

	@Test
	@DisplayName("getByDbValue | unbekannter Wert wirft eine DeveloperNotificationException")
	void unknownValue() {
		assertThrows(DeveloperNotificationException.class, () -> OAuthResourceType.getByDbValue("UNBEKANNT"));
		assertThrows(DeveloperNotificationException.class, () -> OAuthResourceType.getByDbValue(""));
	}
}
