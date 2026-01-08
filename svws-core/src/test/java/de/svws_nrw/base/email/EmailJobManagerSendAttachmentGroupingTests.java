package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailJobManagerSendAttachmentGroupingTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("Attachments werden nach Limit in Gruppen versendet (force=false)")
	void testAttachmentGrouping() {
		// Maximale Anhangsgröße für den Test setzen: 1 000 Byte und force=false.
		context.withMaxAttachmentSize(1000).withForceMaxAttachmentSize(false);

		final EmailJob job = new EmailJob("from@example.org").withSubject("S").withBody("B");
		final EmailJobRecipient r = new EmailJobRecipient("to_a@example.org");
		// Größen: 700, 400, 300 >>> erwartete Gruppen: [700,300] und [400]
		r.attachments.add(new EmailJobAttachment("a.pdf", new byte[700], "application/pdf"));
		r.attachments.add(new EmailJobAttachment("b.pdf", new byte[400], "application/pdf"));
		r.attachments.add(new EmailJobAttachment("c.pdf", new byte[300], "application/pdf"));
		job.addRecipient(r);

		final long id = manager.enqueue(job);
		awaitJobStatus(id, EmailJobStatus.COMPLETED_SUCCESSFULLY);

		// Es sollten 2 E-Mails gesendet worden sein
		final List<MailSmtpSessionMockHelper.SentMail> sentMails = smtpMockHelper.getSent();
		assertSmtpSentMailCount(2);
		assertEquals("to_a@example.org", sentMails.get(0).to());
		assertEquals(2, sentMails.get(0).attachments().size());
		assertEquals(1, sentMails.get(1).attachments().size());
	}

	@Test
	@DisplayName("Zu große Attachments werden verworfen, wenn force=true")
	void testAttachmentGroupingOversizeAttachmentWithForce() {
		// Maximale Anhangsgröße für den Test setzen: 1 000 Byte und force=true.
		context.withMaxAttachmentSize(1000).withForceMaxAttachmentSize(true);

		final EmailJob job = new EmailJob("from@example.org");
		final EmailJobRecipient r = new EmailJobRecipient("to_a@example.org");
		r.attachments.add(new EmailJobAttachment("a.pdf", new byte[200], "application/pdf"));
		r.attachments.add(new EmailJobAttachment("big.pdf", new byte[1500], "application/pdf"));
		r.attachments.add(new EmailJobAttachment("c.pdf", new byte[300], "application/pdf"));
		job.addRecipient(r);

		// Wenn ein Anhang nicht versendet wird, wird er übersprungen und es ergibt den Status COMPLETED_WITH_ERRORS.
		final long id = manager.enqueue(job);
		awaitJobStatus(id, EmailJobStatus.COMPLETED_WITH_ERRORS);

		// Wenn ein Anhang nicht versendet wird, müssen aber die übrigen Anhänge versendet werden.
		final int totalAttachments = smtpMockHelper.getSent().stream().mapToInt(sm -> sm.attachments().size()).sum();
		assertEquals(2, totalAttachments, "Nur die beiden kleinen Anhänge a und c sollten gesendet werden.");

		// Wenn ein Anhang nicht versendet wird, muss dieses Überspringen geloggt werden.
		assertJobLogSkippedContains(id, "überschreitet", "maximale");
	}
}
