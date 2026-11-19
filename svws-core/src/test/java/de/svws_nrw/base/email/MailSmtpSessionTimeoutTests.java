package de.svws_nrw.base.email;

import java.time.Duration;
import java.util.Properties;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prüft, dass ein Versand bei einem hängenden Mailserver nach den eingestellten Zeitgrenzen abbricht, statt unbegrenzt zu warten.
 */
class MailSmtpSessionTimeoutTests {

	/** Die Obergrenze für einen Test. Ohne Zeitgrenzen in der Session würde der Versand nie enden. */
	private static final Duration TESTDAUER_MAX = Duration.ofSeconds(20);

	private static MailSmtpSessionConfig erzeugeConfig(final SmtpTestServer server) {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
		config.setPort(server.getPort());
		return config;
	}

	@Test
	@DisplayName("Die Zeitgrenzen der Konfiguration landen in der Mail-Session")
	void testZeitgrenzenInSession() {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("localhost", "user", "pass");
		config.setConnectionTimeout(1234);
		config.setTimeout(5678);
		final Properties props = new MailSmtpSession(config).getProperties();
		assertEquals("1234", props.getProperty("mail.smtp.connectiontimeout"));
		assertEquals("5678", props.getProperty("mail.smtp.timeout"));
		assertEquals("5678", props.getProperty("mail.smtp.writetimeout"));
	}

	@Test
	@DisplayName("Ohne Angabe gelten feste Zeitgrenzen statt unbegrenzten Wartens")
	void testStandardZeitgrenzen() {
		final Properties props = new MailSmtpSession(new MailSmtpSessionConfig("localhost", "user", "pass")).getProperties();
		assertEquals("30000", props.getProperty("mail.smtp.connectiontimeout"));
		assertEquals("60000", props.getProperty("mail.smtp.timeout"));
		assertEquals("60000", props.getProperty("mail.smtp.writetimeout"));
	}

	@Test
	@DisplayName("Ein antwortender Mailserver nimmt die E-Mail trotz Zeitgrenzen an")
	void testVersandMitZeitgrenzen() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL)) {
			final MailSmtpSessionConfig config = erzeugeConfig(server);
			config.setTimeout(5000);
			final MailSmtpSession session = new MailSmtpSession(config);
			assertTimeoutPreemptively(TESTDAUER_MAX,
					() -> assertDoesNotThrow(() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
			assertTrue(server.getBefehle().contains("DATA"), "Der Testserver hat keine Nachricht erhalten.");
		}
	}

	@Test
	@DisplayName("Ein stummer Mailserver führt nach der Lese-Zeitgrenze zum Abbruch")
	void testStummerServer() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.STUMM)) {
			final MailSmtpSessionConfig config = erzeugeConfig(server);
			config.setTimeout(500);
			final MailSmtpSession session = new MailSmtpSession(config);
			assertTimeoutPreemptively(TESTDAUER_MAX,
					() -> assertThrows(MessagingException.class,
							() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
		}
	}

	@Test
	@DisplayName("Ein Mailserver, der keine Daten mehr annimmt, führt nach der Schreib-Zeitgrenze zum Abbruch")
	void testServerNimmtKeineDatenAn() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.LIEST_NACH_DATA_NICHT)) {
			final MailSmtpSessionConfig config = erzeugeConfig(server);
			config.setTimeout(500);
			final MailSmtpSession session = new MailSmtpSession(config);
			// Der Anhang ist größer als die Socket-Puffer, damit das Schreiben tatsächlich blockiert.
			final EmailJobAttachment anhang = new EmailJobAttachment("gross.pdf", new byte[32 * 1024 * 1024], "application/pdf");
			assertTimeoutPreemptively(TESTDAUER_MAX,
					() -> assertThrows(MessagingException.class,
							() -> session.sendTextMessageWithAttachment("absender@example.com", "empfaenger@example.com", "Betreff", "Text", anhang)));
		}
	}

	@Test
	@DisplayName("Zeitgrenzen von 0 oder kleiner werden abgelehnt, weil sie unbegrenztes Warten bedeuten")
	void testUngueltigeZeitgrenzen() {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("localhost", "user", "pass");
		assertThrows(IllegalArgumentException.class, () -> config.setConnectionTimeout(0));
		assertThrows(IllegalArgumentException.class, () -> config.setConnectionTimeout(-1));
		assertThrows(IllegalArgumentException.class, () -> config.setTimeout(0));
		assertThrows(IllegalArgumentException.class, () -> config.setTimeout(-1));
	}

}
