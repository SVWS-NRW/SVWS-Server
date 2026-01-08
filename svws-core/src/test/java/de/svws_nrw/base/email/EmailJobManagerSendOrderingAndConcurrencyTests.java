package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailJobManagerSendOrderingAndConcurrencyTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("Jobs werden in FIFO-Reihenfolge verarbeitet")
	void testJobProcessingOrder() {
		// Verzögerung hinzufügen, um sicherzustellen, dass Jobs nacheinander verarbeitet werden
		smtpMockHelper.setDelayMs(100);

		final EmailJob job1 = createSimpleJob("to_a@example.org");
		final EmailJob job2 = createSimpleJob("to_b@example.org");
		final EmailJob job3 = createSimpleJob("to_c@example.org");

		final long id1 = manager.enqueue(job1);
		final long id2 = manager.enqueue(job2);
		final long id3 = manager.enqueue(job3);

		// Warte auf Abschluss aller Jobs
		awaitJobStatus(id1, EmailJobStatus.COMPLETED_SUCCESSFULLY, 3000);
		awaitJobStatus(id2, EmailJobStatus.COMPLETED_SUCCESSFULLY, 3000);
		awaitJobStatus(id3, EmailJobStatus.COMPLETED_SUCCESSFULLY, 3000);

		// Prüfe die Reihenfolge der versendeten E-Mails
		final List<MailSmtpSessionMockHelper.SentMail> sentMails = smtpMockHelper.getSent();
		assertEquals(3, sentMails.size());
		assertEquals("to_a@example.org", sentMails.get(0).to());
		assertEquals("to_b@example.org", sentMails.get(1).to());
		assertEquals("to_c@example.org", sentMails.get(2).to());
	}

	@Test
	@DisplayName("Gleichzeitiges Enqueue während des Versands funktioniert korrekt")
	void testConcurrentEnqueue() {
		// Verzögerung hinzufügen, um Überschneidungen zu erzwingen
		smtpMockHelper.setDelayMs(200);

		final EmailJob job1 = createSimpleJob("to_a@example.org");
		final long id1 = manager.enqueue(job1);

		// Warte kurz, damit job1 zu versenden beginnt
		await().pollDelay(Duration.ofMillis(50)).atMost(Duration.ofMillis(51)).untilAsserted(() -> { });

		// Füge einen zweiten Job hinzu, während der erste noch verarbeitet wird
		final EmailJob job2 = createSimpleJob("to_b@example.org");
		final long id2 = manager.enqueue(job2);

		// Beide sollten erfolgreich abgeschlossen werden
		awaitJobStatus(id1, EmailJobStatus.COMPLETED_SUCCESSFULLY, 3000);
		awaitJobStatus(id2, EmailJobStatus.COMPLETED_SUCCESSFULLY, 3000);

		assertSmtpSentMailCount(2);
	}
}
