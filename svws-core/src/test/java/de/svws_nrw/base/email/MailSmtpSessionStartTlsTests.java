package de.svws_nrw.base.email;

import java.security.KeyStore;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prüft, dass bei gewähltem STARTTLS das Kennwort nur verschlüsselt zum Mailserver geht. Bietet der Mailserver kein STARTTLS an,
 * etwa weil ein Angreifer das Angebot entfernt hat, bricht der Versand vor der Anmeldung ab.
 */
class MailSmtpSessionStartTlsTests {

	private static final Duration TESTDAUER_MAX = Duration.ofSeconds(20);

	private static MailSmtpSession erzeugeSession(final SmtpTestServer server, final boolean starttls) {
		return erzeugeSession(server, starttls, null);
	}

	private static MailSmtpSession erzeugeSession(final SmtpTestServer server, final boolean starttls, final KeyStore truststore) {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
		config.setPort(server.getPort());
		config.setStartTLS(starttls);
		config.setTimeout(5000);
		return new MailSmtpSession(config, truststore);
	}

	private static boolean enthaeltAnmeldung(final List<String> befehle) {
		return befehle.stream().anyMatch(b -> b.toUpperCase(Locale.ROOT).startsWith("AUTH"));
	}

	@Test
	@DisplayName("Mit STARTTLS meldet sich der Server erst nach dem Wechsel zu TLS an")
	void testAnmeldungNurVerschluesselt() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, zertifikat.getServerKontext())) {
			final MailSmtpSession session = erzeugeSession(server, true, zertifikat.getTruststore());
			assertTimeoutPreemptively(TESTDAUER_MAX,
					() -> assertDoesNotThrow(() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
			assertTrue(server.getBefehleUnverschluesselt().contains("STARTTLS"), "Der Server hat nicht zu TLS gewechselt.");
			assertTrue(enthaeltAnmeldung(server.getBefehle()), "Der Server hat sich nicht angemeldet.");
			assertFalse(enthaeltAnmeldung(server.getBefehleUnverschluesselt()), "Die Anmeldung ging unverschlüsselt zum Mailserver.");
		}
	}

	@Test
	@DisplayName("Mit STARTTLS bricht der Versand ab, wenn der Mailserver kein STARTTLS anbietet")
	void testAbbruchOhneStarttlsAngebot() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL)) {
			final MailSmtpSession session = erzeugeSession(server, true);
			assertTimeoutPreemptively(TESTDAUER_MAX,
					() -> assertThrows(MessagingException.class,
							() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
			assertFalse(enthaeltAnmeldung(server.getBefehle()), "Das Kennwort ging ohne Verschlüsselung zum Mailserver.");
			assertFalse(server.getBefehle().contains("DATA"), "Die Nachricht ging ohne Verschlüsselung zum Mailserver.");
		}
	}

	@Test
	@DisplayName("Ohne STARTTLS bleibt eine bewusst unverschlüsselte Verbindung möglich")
	void testUnverschluesseltOhneStarttls() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL)) {
			final MailSmtpSession session = erzeugeSession(server, false);
			assertTimeoutPreemptively(TESTDAUER_MAX,
					() -> assertDoesNotThrow(() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
			assertTrue(server.getBefehle().contains("DATA"), "Der Testserver hat keine Nachricht erhalten.");
		}
	}

}
