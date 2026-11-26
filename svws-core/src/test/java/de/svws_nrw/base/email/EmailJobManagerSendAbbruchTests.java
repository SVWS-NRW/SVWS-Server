package de.svws_nrw.base.email;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Prüft gegen einen Test-SMTP-Server, dass ein Job nach einem Fehler für alle Empfänger endet. Lehnt der Mailserver nur einen Empfänger ab,
 * erhalten die übrigen Empfänger ihre E-Mail.
 */
class EmailJobManagerSendAbbruchTests extends AbstractEmailJobManagerSendTestBase {

	private static final long TIMEOUT_MS = 10000;

	/**
	 * Erstellt einen Job, der über eine echte SMTP-Session an den Test-Server sendet.
	 *
	 * @param server       der Test-Server
	 * @param recipients   die Adressen der Empfänger
	 *
	 * @return der Job
	 */
	private EmailJob createJob(final SmtpTestServer server, final String... recipients) {
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
		config.setPort(server.getPort());
		config.setConnectionTimeout(500);
		config.setTimeout(500);
		return createSimpleJob(new EmailJobContext(new MailSmtpSession(config)), recipients);
	}

	private long countLogErrors(final long id, final String snippet) {
		return manager.getJob(id).getLogError().stream().filter(log -> log.contains(snippet)).count();
	}

	@Test
	@DisplayName("Ein hängender Mailserver beendet den Job nach dem ersten Empfänger")
	void testHaengenderMailserver() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.STUMM)) {
			final long id = manager.enqueue(createJob(server, "a@schule.de", "b@schule.de", "c@schule.de"));
			awaitJobStatus(id, EmailJobStatus.FAILED, TIMEOUT_MS);
			assertThat(countLogErrors(id, "Fehler beim Versand an Empfänger")).isEqualTo(1);
			assertJobLogErrorContains(id, "2 weitere Empfänger erhalten keine E-Mail");
		}
	}

	@Test
	@DisplayName("Eine abgelehnte Anmeldung versucht der Job nur einmal")
	void testAbgelehnteAnmeldung() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.ANMELDUNG_ABGELEHNT)) {
			final long id = manager.enqueue(createJob(server, "a@schule.de", "b@schule.de", "c@schule.de"));
			awaitJobStatus(id, EmailJobStatus.FAILED, TIMEOUT_MS);
			assertThat(server.getBefehle().stream().filter(b -> b.toUpperCase(Locale.ROOT).startsWith("AUTH")).count()).isEqualTo(1);
			assertThat(countLogErrors(id, "Fehler beim Versand an Empfänger")).isEqualTo(1);
			assertJobLogErrorContains(id, "2 weitere Empfänger erhalten keine E-Mail");
		}
	}

	@Test
	@DisplayName("Ein abgelehnter Empfänger beendet den Job nicht, die übrigen Empfänger erhalten ihre E-Mail")
	void testAbgelehnterEmpfaenger() throws Exception {
		try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL)) {
			final long id = manager.enqueue(createJob(server, "a@schule.de", SmtpTestServer.ABGELEHNTER_EMPFAENGER + "@schule.de", "c@schule.de"));
			awaitJobStatus(id, EmailJobStatus.COMPLETED_WITH_ERRORS, TIMEOUT_MS);
			assertThat(server.getNachrichten()).hasSize(2);
			assertThat(countLogErrors(id, "Fehler beim Versand an Empfänger " + SmtpTestServer.ABGELEHNTER_EMPFAENGER + "@schule.de")).isEqualTo(1);
			assertThat(countLogErrors(id, "ABBRUCH")).isZero();
		}
	}

}
