package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class EmailJobManagerSendCancelAndShutdownTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("Cancel eines unbekannten Jobs gibt false zurück")
	void testCancelUnknownJob() {
		assertFalse(manager.cancelJob(999999L));
	}

	@Test
	@DisplayName("Cancel vor Start markiert Job als CANCELED")
	void testCancelBeforeStart() {
		// Verlangsame den Versand, damit ein Cancel auch vor dem Start erfolgen kann.
		smtpMockHelper.setDelayMs(500);

		final EmailJob job = createSimpleJob("to_a@example.org");
		final long id = manager.enqueue(job);

		// Breche den Job sofort ab und warte auf den entsprechenden Status.
		assertTrue(manager.cancelJob(id));
		awaitJobStatus(id, EmailJobStatus.CANCELED);

		// Prüfe, ob keine Mail versendet worden ist.
		assertSmtpSentMailCount(0);

		// Der Abbruch sollte im Log festgehalten worden sein.
		assertJobLogErrorContains(id, "ABBRUCH");
	}

	@Test
	@DisplayName("Cancel während des Versands bricht weiteren Versand ab")
	void testCancelDuringProcessing() {
		// Verlangsame den Versand, damit ein Cancel auch erfolgen kann.
		smtpMockHelper.setDelayMs(300);

		final EmailJob job = createSimpleJob("to_a@example.org", "to_b@example.org", "to_c@example.org", "to_d@example.org");
		final long id = manager.enqueue(job);

		// Warte, bis mindestens zwei Mails gesendet wurden.
		awaitSmtpSentMailMinCount(2, 3000);

		// Cancel den Job und warte auf den entsprechenden Status.
		assertTrue(manager.cancelJob(id));
		awaitJobStatus(id, EmailJobStatus.CANCELED, 3000);

		// Da während des Cancel die dritte Mail im Worker-Thread bereits in den Versand gelangt sein kann, prüfe hier, ob nicht alle 4 Mails (also max. 3) gesendet wurden.
		assertSmtpSentMailMaxCount(3);

		// Der Abbruch sollte im Log festgehalten worden sein.
		assertJobLogErrorContains(id, "ABBRUCH");
	}

	@Test
	@DisplayName("Shutdown während des Versands bricht weiteren Versand ab und löst keinen Fehler aus")
	void testShutdownDuringProcessing() {
		smtpMockHelper.setDelayMs(300);

		final EmailJob job = createSimpleJob("to_a@example.org", "to_b@example.org", "to_c@example.org", "to_d@example.org");
		manager.enqueue(job);

		// Warte, bis mindestens zwei Mails gesendet wurden.
		awaitSmtpSentMailMinCount(2, 3000);
		assertDoesNotThrow(() -> manager.shutdown());

		// Da während des Shutdown die dritte Mail im Worker-Thread bereits in den Versand gelangt sein kann, prüfe hier, ob nicht alle 4 Mails (also max. 3) gesendet wurden.
		assertSmtpSentMailMaxCount(3);
	}
}
