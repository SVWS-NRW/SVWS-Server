package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class EmailJobManagerSendBasicTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("Enqueue vergibt ID und Statusablauf bis COMPLETED bei erfolgreichem Versand")
	void testEnqueueAndComplete() {
		final EmailJob job = createSimpleJob("to_a@example.org", "to_b@texample.org");
		final long id = manager.enqueue(job);

		assertTrue(id > 0);
		awaitJobStatus(id, EmailJobStatus.COMPLETED_SUCCESSFULLY);
		assertSentMailsCount(id, 2);
		assertJobLogErrorsIsEmpty(id);
		assertJobLogSkippedEmpty(id);
	}

	@Test
	@DisplayName("Job mit mehreren Empfängern versendet an alle Empfänger")
	void testMultipleRecipients() {
		final EmailJob job = new EmailJob("from@example.org").withSubject("Test").withBody("Body");
		job.addRecipient(new EmailJobRecipient("to1@example.org"));
		job.addRecipient(new EmailJobRecipient("to2@example.org"));
		job.addRecipient(new EmailJobRecipient("to3@example.org"));

		final long id = manager.enqueue(job);
		awaitJobStatus(id, EmailJobStatus.COMPLETED_SUCCESSFULLY);
		assertSentMailsCount(id, 3);
	}

	@Test
	@DisplayName("Leere Empfängerliste führt zu sofortigem Abschluss")
	void testEmptyRecipientList() {
		final EmailJob job = new EmailJob("from@example.org").withSubject("Test").withBody("Body");
		// Keine Empfänger hinzugefügt
		final long id = manager.enqueue(job);
		awaitJobStatus(id, EmailJobStatus.COMPLETED_SUCCESSFULLY, 1000);
		assertSentMailsCount(id, 0);
	}

	@Test
	@DisplayName("Enqueue desselben Jobs zweimal gibt -1 beim zweiten Mal zurück")
	void testEnqueueSameJobTwice() {
		final EmailJob job = createSimpleJob("to@example.org");
		final long id1 = manager.enqueue(job);
		assertTrue(id1 > 0);
		final long id2 = manager.enqueue(job);
		assertEquals(-1, id2, "Zweites Enqueue desselben Jobs sollte -1 zurückgeben");
	}
}
