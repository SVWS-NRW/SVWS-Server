package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailSmtpSessionConfigTests {

	@Test
	@DisplayName("Session-Erstellung mit dem Host null muss Fehler werfen")
	void testSessionCreationWithHostNull() {
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig(null, "user", "pass"));
	}

	@Test
	@DisplayName("Session-Erstellung mit dem User null muss Fehler werfen")
	void testSessionCreationWithUserNull() {
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig("localhost", null, "pass"));
	}

	@Test
	@DisplayName("Session-Erstellung mit dem Passwort null muss Fehler werfen")
	void testSessionCreationWithPasswordNull() {
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig("localhost", "user", null));
	}

	@Test
	@DisplayName("Session-Erstellung mit dem leerem Host muss Fehler werfen")
	void testSessionCreationWithHostBlank() {
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig("", "user", "pass"));
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig("   ", "user", "pass"));
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig("\t\n ", "user", "pass"));
	}

	@Test
	@DisplayName("Session-Erstellung mit dem leerem User muss Fehler werfen")
	void testSessionCreationWithUserBlank() {
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig("localhost", "", "pass"));
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig("localhost", "   ", "pass"));
		assertThrows(IllegalArgumentException.class, () ->  new MailSmtpSessionConfig("localhost", "\t\n ", "pass"));
	}

	@Test
	@DisplayName("Getter-Methoden geben die richtigen Werte zurück")
	void testGetters() {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("smtp.example.com", "testuser", "testpass");

		assertEquals("smtp.example.com", config.getHost());
		assertEquals("testuser", config.getUsername());
		assertEquals("testpass", config.getPassword());
		assertEquals(25, config.getPort(), "Standardport sollte 25 sein");
		assertFalse(config.isTLS(), "TLS sollte standardmäßig deaktiviert sein");
		assertFalse(config.isStartTLS(), "StartTLS sollte standardmäßig deaktiviert sein");
	}

	@Test
	@DisplayName("setPort und getPort funktionieren korrekt")
	void testSetAndGetPort() {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("localhost", "user", "pass");

		// Standardwert prüfen
		assertEquals(25, config.getPort());

		// Neuen Port setzen
		config.setPort(587);
		assertEquals(587, config.getPort());

		// Weiteren Port setzen
		config.setPort(465);
		assertEquals(465, config.getPort());
	}

	@Test
	@DisplayName("setTLS und isTLS funktionieren korrekt")
	void testSetAndGetTLS() {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("localhost", "user", "pass");

		// Standardwert prüfen
		assertFalse(config.isTLS());

		// TLS aktivieren
		config.setTLS(true);
		assertTrue(config.isTLS());

		// TLS deaktivieren
		config.setTLS(false);
		assertFalse(config.isTLS());
	}

	@Test
	@DisplayName("setStartTLS und isStartTLS funktionieren korrekt")
	void testSetAndGetStartTLS() {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("localhost", "user", "pass");

		// Standardwert prüfen
		assertFalse(config.isStartTLS());

		// StartTLS aktivieren
		config.setStartTLS(true);
		assertTrue(config.isStartTLS());

		// StartTLS deaktivieren
		config.setStartTLS(false);
		assertFalse(config.isStartTLS());
	}

	@Test
	@DisplayName("Kombination von TLS- und Port-Einstellungen")
	void testCombinedSettings() {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("smtp.gmail.com", "user@example.com", "secret");

		// Konfiguration für TLS auf Port 465
		config.setTLS(true);
		config.setPort(465);

		assertTrue(config.isTLS());
		assertFalse(config.isStartTLS());
		assertEquals(465, config.getPort());
		assertEquals("smtp.gmail.com", config.getHost());
		assertEquals("user@example.com", config.getUsername());

		// Konfiguration für StartTLS auf Port 587
		config.setTLS(false);
		config.setStartTLS(true);
		config.setPort(587);

		assertFalse(config.isTLS());
		assertTrue(config.isStartTLS());
		assertEquals(587, config.getPort());
	}

}
