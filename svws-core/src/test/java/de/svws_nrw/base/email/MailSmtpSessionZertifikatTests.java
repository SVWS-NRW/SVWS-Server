package de.svws_nrw.base.email;

import java.security.KeyStore;
import java.time.Duration;
import java.util.Locale;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prüft, dass der Server nur einem Mailserver mit vertrauenswürdigem Zertifikat für den eingetragenen Host seine Zugangsdaten schickt.
 * Das gilt für STARTTLS und für TLS vom ersten Byte an.
 */
class MailSmtpSessionZertifikatTests {

	private static final Duration TESTDAUER_MAX = Duration.ofSeconds(20);

	private static MailSmtpSession erzeugeSession(final SmtpTestServer server, final boolean tls, final KeyStore truststore) {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
		config.setPort(server.getPort());
		config.setStartTLS(!tls);
		config.setTLS(tls);
		config.setTimeout(5000);
		return new MailSmtpSession(config, truststore);
	}

	private static void sendeErfolgreich(final MailSmtpSession session) {
		assertTimeoutPreemptively(TESTDAUER_MAX,
				() -> assertDoesNotThrow(() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
	}

	private static void sendeAbgelehnt(final MailSmtpSession session, final SmtpTestServer server) {
		final MessagingException e = assertTimeoutPreemptively(TESTDAUER_MAX,
				() -> assertThrows(MessagingException.class,
						() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
		assertEquals(MailSmtpSession.MELDUNG_ZERTIFIKAT, e.getMessage());
		assertFalse(server.getBefehle().stream().anyMatch(b -> b.toUpperCase(Locale.ROOT).startsWith("AUTH")),
				"Die Zugangsdaten gingen an einen Mailserver ohne vertrauenswürdiges Zertifikat.");
	}

	@Test
	@DisplayName("STARTTLS: Ein unbekanntes selbst signiertes Zertifikat wird abgelehnt")
	void testStarttlsUnbekanntesZertifikat() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, new TestZertifikat().getServerKontext())) {
			sendeAbgelehnt(erzeugeSession(server, false, null), server);
		}
	}

	@Test
	@DisplayName("TLS: Ein unbekanntes selbst signiertes Zertifikat wird abgelehnt")
	void testTlsUnbekanntesZertifikat() throws Exception {
		try (SmtpTestServer server = SmtpTestServer.mitTls(new TestZertifikat().getServerKontext())) {
			sendeAbgelehnt(erzeugeSession(server, true, null), server);
		}
	}

	@Test
	@DisplayName("STARTTLS: Ein vertrauenswürdiges Zertifikat für den Host wird angenommen")
	void testStarttlsVertrauenswuerdigesZertifikat() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, zertifikat.getServerKontext())) {
			sendeErfolgreich(erzeugeSession(server, false, zertifikat.getTruststore()));
			assertTrue(server.getBefehle().contains("DATA"), "Der Testserver hat keine Nachricht erhalten.");
		}
	}

	@Test
	@DisplayName("TLS: Ein vertrauenswürdiges Zertifikat für den Host wird angenommen")
	void testTlsVertrauenswuerdigesZertifikat() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = SmtpTestServer.mitTls(zertifikat.getServerKontext())) {
			sendeErfolgreich(erzeugeSession(server, true, zertifikat.getTruststore()));
			assertTrue(server.getBefehle().contains("DATA"), "Der Testserver hat keine Nachricht erhalten.");
		}
	}

	@Test
	@DisplayName("Ein vertrauenswürdiges Zertifikat für einen anderen Host wird abgelehnt")
	void testZertifikatFuerAnderenHost() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat("mail.example.com");
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, zertifikat.getServerKontext())) {
			sendeAbgelehnt(erzeugeSession(server, false, zertifikat.getTruststore()), server);
		}
	}

}
