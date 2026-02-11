package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class EmailJobManagerSendFilteringTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("Einzelner Empfänger ohne Anhänge wird bei filterMailsWithoutAttachments=true übersprungen")
	void testOneRecipientWithoutAttachmentsFilteredWhenEnabled() {
		// Aktiviere das Filtern von Mails ohne Anhänge
		context.withFilterMailsWithoutAttachments(true);

		final EmailJob job = new EmailJob("from@example.org").withSubject("Test").withBody("Body");
		final EmailJobRecipient recipient = new EmailJobRecipient("to_a@example.org");
		// Explizit keine Anhänge hinzufügen
		job.addRecipient(recipient);

		final long id = manager.enqueue(job);
		// Da keine einzige E-Mail versendet wird, gilt der Job als FAILED.
		awaitJobStatus(id, EmailJobStatus.FAILED, 2000);
		assertSentMailsCount(id, 0);

		// Prüfe, dass der Empfänger im Skip-Log erscheint
		assertJobLogSkippedContains(id, "to_a@example.org", "keine versendbaren Anhänge");
	}

	@Test
	@DisplayName("Zwei Empfänger, einer mit und einem zu großem Anhang. Dieser wird bei filterMailsWithoutAttachments=true übersprungen")
	void testTwoRecipientsWithOneTooBigAttachmentsFilteredWhenEnabled() {
		// Aktiviere das Filtern von Mails ohne Anhänge
		context.withFilterMailsWithoutAttachments(true).withForceMaxAttachmentSize(true).withMaxAttachmentSize(1000);

		// Empfänger mit passendem Anhang
		final EmailJob job = new EmailJob("from@example.org").withSubject("Test").withBody("Body");
		final EmailJobRecipient r1 = new EmailJobRecipient("to_a@example.org");
		r1.attachments.add(new EmailJobAttachment("a.pdf", new byte[700], "application/pdf"));
		job.addRecipient(r1);
		// Empfänger mit zu großem Anhang
		final EmailJobRecipient r2 = new EmailJobRecipient("to_b@example.org");
		r2.attachments.add(new EmailJobAttachment("b.pdf", new byte[1700], "application/pdf"));
		job.addRecipient(r2);

		final long id = manager.enqueue(job);
		// Da eine E-Mail versendet wird, gilt der Job als COMPLETED_WITH_ERRORS.
		awaitJobStatus(id, EmailJobStatus.COMPLETED_WITH_ERRORS, 200000);
		assertSentMailsCount(id, 1);

		// Prüfe, dass der Empfänger im Skip-Log erscheint
		assertJobLogSkippedContains(id, "to_b@example.org", "keine versendbaren Anhänge");
	}

	@Test
	@DisplayName("Einzelner Empfänger ohne Anhänge wird bei filterMailsWithoutAttachments=false nicht übersprungen")
	void testOneRecipientWithoutAttachmentsNotFilteredWhenDisabled() {
		// Deaktiviere das Filtern von Mails ohne Anhänge (ist Standard, wird aber explizit gesetzt)
		context.withFilterMailsWithoutAttachments(false);

		final EmailJob job = new EmailJob("from@example.org").withSubject("Test").withBody("Body");
		final EmailJobRecipient recipient = new EmailJobRecipient("to@example.org");
		// Explizit keine Anhänge hinzufügen
		job.addRecipient(recipient);

		final long id = manager.enqueue(job);
		awaitJobStatus(id, EmailJobStatus.COMPLETED_SUCCESSFULLY, 2000);
		assertSentMailsCount(id, 1);

		// Prüfe die versendete Mail
		final MailSmtpSessionMockHelper.SentMail sentMail = smtpMockHelper.getSent().getFirst();
		assertEquals("to@example.org", sentMail.to());
		assertTrue(sentMail.attachments().isEmpty(), "Die E-Mail sollte keine Anhänge haben");

		// Prüfe, dass keine Fehler oder Skips geloggt wurden
		assertJobLogSkippedEmpty(id);
		assertJobLogErrorsIsEmpty(id);
	}
}
