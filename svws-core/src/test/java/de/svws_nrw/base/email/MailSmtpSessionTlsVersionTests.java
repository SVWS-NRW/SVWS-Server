package de.svws_nrw.base.email;

import java.time.Duration;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * Prüft, dass die Session TLS 1.3 und TLS 1.2 anbietet, bei TLS und bei STARTTLS. Ältere Versionen bietet sie nicht an.
 */
class MailSmtpSessionTlsVersionTests {

	private static final Duration TESTDAUER_MAX = Duration.ofSeconds(20);

	private static void sende(final SmtpTestServer server, final boolean tls, final TestZertifikat zertifikat) throws Exception {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
		config.setPort(server.getPort());
		config.setStartTLS(!tls);
		config.setTLS(tls);
		config.setTimeout(5000);
		final MailSmtpSession session = new MailSmtpSession(config, zertifikat.getTruststore());
		assertTimeoutPreemptively(TESTDAUER_MAX,
				() -> assertDoesNotThrow(() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
	}

	@Test
	@DisplayName("TLS: Die Verbindung nutzt TLS 1.3, wenn der Mailserver es anbietet")
	void testTlsNutztTls13() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = SmtpTestServer.mitTls(zertifikat.getServerKontext())) {
			sende(server, true, zertifikat);
			assertEquals("TLSv1.3", server.getProtokoll());
		}
	}

	@Test
	@DisplayName("STARTTLS: Die Verbindung nutzt TLS 1.3, wenn der Mailserver es anbietet")
	void testStarttlsNutztTls13() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, zertifikat.getServerKontext())) {
			sende(server, false, zertifikat);
			assertEquals("TLSv1.3", server.getProtokoll());
		}
	}

	@Test
	@DisplayName("TLS: Ein Mailserver, der nur TLS 1.3 kennt, wird erreicht")
	void testTlsNurTls13() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = SmtpTestServer.mitTls(zertifikat.getServerKontext())) {
			server.setProtokolle("TLSv1.3");
			sende(server, true, zertifikat);
			assertEquals("TLSv1.3", server.getProtokoll());
		}
	}

	@Test
	@DisplayName("STARTTLS: Ein Mailserver, der nur TLS 1.2 kennt, wird weiter erreicht")
	void testStarttlsNurTls12() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, zertifikat.getServerKontext())) {
			server.setProtokolle("TLSv1.2");
			sende(server, false, zertifikat);
			assertEquals("TLSv1.2", server.getProtokoll());
		}
	}

	@Test
	@DisplayName("Scheitert der Handshake an der TLS-Version, nennt die Meldung kein falsches Zertifikat")
	void testHandshakeOhneGemeinsameVersion() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = SmtpTestServer.mitTls(zertifikat.getServerKontext())) {
			server.setProtokolle("TLSv1.3");
			final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
			config.setPort(server.getPort());
			config.setTLS(true);
			config.setTimeout(5000);
			final MailSmtpSession session = new MailSmtpSession(config, zertifikat.getTruststore());
			// Die Session bietet beide Versionen an. Nur so lässt sich ein Handshake ohne gemeinsame Version erzwingen.
			session.getProperties().setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
			final MessagingException e = assertTimeoutPreemptively(TESTDAUER_MAX,
					() -> assertThrows(MessagingException.class,
							() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
			assertNotEquals(MailSmtpSession.MELDUNG_ZERTIFIKAT, e.getMessage());
		}
	}

	@Test
	@DisplayName("Die Session bietet nur TLS 1.3 und TLS 1.2 an")
	void testNurAktuelleVersionen() {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("localhost", "user", "pass");
		config.setStartTLS(true);
		assertEquals("TLSv1.3 TLSv1.2", new MailSmtpSession(config).getProperties().getProperty("mail.smtp.ssl.protocols"));
	}

}
