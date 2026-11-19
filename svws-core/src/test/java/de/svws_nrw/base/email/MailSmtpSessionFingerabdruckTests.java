package de.svws_nrw.base.email;

import java.security.KeyStore;
import java.time.Duration;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prüft, dass ein hinterlegter Fingerabdruck genau ein Zertifikat freigibt. Das passende Zertifikat wird angenommen, auch wenn es
 * selbst signiert ist und einen anderen Namen trägt. Jedes andere Zertifikat wird abgelehnt, auch ein sonst vertrauenswürdiges.
 */
class MailSmtpSessionFingerabdruckTests {

	private static final Duration TESTDAUER_MAX = Duration.ofSeconds(20);

	private static MailSmtpSession erzeugeSession(final SmtpTestServer server, final boolean tls, final String fingerabdruck,
			final KeyStore truststore) {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
		config.setPort(server.getPort());
		config.setStartTLS(!tls);
		config.setTLS(tls);
		config.setTimeout(5000);
		config.setZertifikatFingerabdruck(fingerabdruck);
		return new MailSmtpSession(config, truststore);
	}

	private static void sendeErfolgreich(final MailSmtpSession session, final SmtpTestServer server) {
		assertTimeoutPreemptively(TESTDAUER_MAX,
				() -> assertDoesNotThrow(() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
		assertTrue(server.getBefehle().contains("DATA"), "Der Testserver hat keine Nachricht erhalten.");
	}

	private static void sendeAbgelehnt(final MailSmtpSession session, final SmtpTestServer server) {
		final MessagingException e = assertTimeoutPreemptively(TESTDAUER_MAX,
				() -> assertThrows(MessagingException.class,
						() -> session.sendTextMessage("absender@example.com", "empfaenger@example.com", "Betreff", "Text")));
		assertEquals(MailSmtpSession.MELDUNG_FINGERABDRUCK, e.getMessage());
		assertFalse(server.getBefehle().stream().anyMatch(b -> b.toUpperCase(Locale.ROOT).startsWith("AUTH")),
				"Die Zugangsdaten gingen an einen Mailserver mit fremdem Zertifikat.");
	}

	@Test
	@DisplayName("STARTTLS: Ein selbst signiertes Zertifikat mit passendem Fingerabdruck wird trotz anderem Namen angenommen")
	void testStarttlsPassenderFingerabdruck() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat("mail.schule.intern");
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, zertifikat.getServerKontext())) {
			sendeErfolgreich(erzeugeSession(server, false, zertifikat.getFingerabdruck(), null), server);
		}
	}

	@Test
	@DisplayName("TLS: Ein selbst signiertes Zertifikat mit passendem Fingerabdruck wird trotz anderem Namen angenommen")
	void testTlsPassenderFingerabdruck() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat("mail.schule.intern");
		try (SmtpTestServer server = SmtpTestServer.mitTls(zertifikat.getServerKontext())) {
			sendeErfolgreich(erzeugeSession(server, true, zertifikat.getFingerabdruck(), null), server);
		}
	}

	@Test
	@DisplayName("STARTTLS: Ein Zertifikat mit anderem Fingerabdruck wird abgelehnt")
	void testStarttlsFremderFingerabdruck() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, zertifikat.getServerKontext())) {
			sendeAbgelehnt(erzeugeSession(server, false, new TestZertifikat().getFingerabdruck(), null), server);
		}
	}

	@Test
	@DisplayName("TLS: Ein Zertifikat mit anderem Fingerabdruck wird abgelehnt")
	void testTlsFremderFingerabdruck() throws Exception {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = SmtpTestServer.mitTls(zertifikat.getServerKontext())) {
			sendeAbgelehnt(erzeugeSession(server, true, new TestZertifikat().getFingerabdruck(), null), server);
		}
	}

	/**
	 * Lässt Java dem Testzertifikat vertrauen, solange die Aktion läuft. Nur so zeigt sich, ob der Fingerabdruck auch dann gilt,
	 * wenn die Standardprüfung von Java das Zertifikat annehmen würde.
	 *
	 * @param zertifikat   das Zertifikat, dem Java vertrauen soll
	 * @param aktion       die Aktion, während der Java dem Zertifikat vertraut
	 *
	 * @throws Throwable   falls die Aktion scheitert
	 */
	private static void mitJavaVertrauen(final TestZertifikat zertifikat, final Executable aktion) throws Throwable {
		final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(zertifikat.getTruststore());
		final SSLContext vertraut = SSLContext.getInstance("TLS");
		vertraut.init(null, tmf.getTrustManagers(), null);
		final SSLContext standard = SSLContext.getDefault();
		SSLContext.setDefault(vertraut);
		try {
			aktion.execute();
		} finally {
			SSLContext.setDefault(standard);
		}
	}

	@Test
	@DisplayName("STARTTLS: Mit Fingerabdruck wird auch ein Zertifikat abgelehnt, dem Java vertraut")
	void testStarttlsFingerabdruckHatVorrangVorJava() throws Throwable {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL, zertifikat.getServerKontext())) {
			mitJavaVertrauen(zertifikat, () -> sendeAbgelehnt(erzeugeSession(server, false, new TestZertifikat().getFingerabdruck(), null), server));
		}
	}

	@Test
	@DisplayName("TLS: Mit Fingerabdruck wird auch ein Zertifikat abgelehnt, dem Java vertraut")
	void testTlsFingerabdruckHatVorrangVorJava() throws Throwable {
		final TestZertifikat zertifikat = new TestZertifikat();
		try (SmtpTestServer server = SmtpTestServer.mitTls(zertifikat.getServerKontext())) {
			mitJavaVertrauen(zertifikat, () -> sendeAbgelehnt(erzeugeSession(server, true, new TestZertifikat().getFingerabdruck(), null), server));
		}
	}

}
