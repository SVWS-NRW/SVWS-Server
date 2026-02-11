package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class EmailJobManagerSendLoadAndErrorTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("Job mit sehr vielen Empfängern (z.B. 150) funktioniert")
	void testJobWithManyRecipients() {
		// Setze das Rate-Limit auf 200, damit der Versand ohne dessen Einfluss erfolgt.
		context.withMaxEmailsPerMinute(200);

		// Erzeuge 150 Absender mit Anhang
		final EmailJob job = new EmailJob("from@example.org").withSubject("S").withBody("B");
		for (int i = 0; i < 150; i++) {
			final EmailJobRecipient r = new EmailJobRecipient("to" + i + "@example.org");
			r.attachments.add(new EmailJobAttachment("a" + i + ".pdf", new byte[64000], "application/pdf"));
			job.addRecipient(r);
		}

		final long id = manager.enqueue(job);
		awaitJobStatus(id, EmailJobStatus.COMPLETED_SUCCESSFULLY, 5000);
		assertJobSentMailCount(id, 150);
	}

	@Test
	@DisplayName("SMTP-Fehler markiert Job als FAILED")
	void testSmtpErrorHandling() {
		// Erstelle einen Mock, der eine Exception wirft
		final MailSmtpSessionMockHelper errorMock = new MailSmtpSessionMockHelper();
		errorMock.throwOnSend(new jakarta.mail.MessagingException("SMTP connection failed"));

		// Erstelle einen neuen Manager mit dem fehlerhaften Mock
		// WICHTIG: Erhöhe die Zeit zum Halten abgeschlossener Jobs über dem Timeout
		final EmailJobManagerContext errorContext = new EmailJobManagerContext("schemaError", 99L, errorMock.getMock()).withTimeToKeepCompletedJobs(5000);
		final EmailJobManager errorManager = EmailJobManagerFactory.getInstance().getManager(errorContext);

		try {
			final EmailJob job = createSimpleJob("to_a@example.org");
			final long id = errorManager.enqueue(job);

			awaitJobStatusForManager(errorManager, id, EmailJobStatus.FAILED, 2000);

			// Prüfe, ob keine Mail versendet wurde und die Exception-Nachricht im Log erscheint.
			assertJobSentMailCount(errorManager, id, 0);
			assertJobLogErrorContains(errorManager, id, "SMTP connection failed");
		} finally {
			EmailJobManagerFactory.getInstance().freeManager("schemaError", 99L);
		}
	}
}
