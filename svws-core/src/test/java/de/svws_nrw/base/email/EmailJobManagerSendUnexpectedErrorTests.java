package de.svws_nrw.base.email;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;


class EmailJobManagerSendUnexpectedErrorTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("Unerwartete Exception während des Versands markiert den Job als FAILED und protokolliert den Fehler")
	void testUnexpectedExceptionDuringSendMarksJobAsFailed() {
		// Präpariere einen Job, der beim Zugriff auf die Empfängerliste eine unerwartete Exception auslöst.
		// Diese wird erst innerhalb der Versandbehandlung von processJob() ausgelöst und dort abgefangen.
		final EmailJob job = spy(createSimpleJob("to_a@example.org"));
		doThrow(new IllegalStateException("Unerwarteter Testfehler")).when(job).getRecipients();

		final long id = manager.enqueue(job);

		// Der Job muss als FAILED enden, der Fehler muss im Job-Log stehen und es darf keine E-Mail versendet worden sein.
		awaitJobStatus(id, EmailJobStatus.FAILED);
		assertJobLogErrorContains(id, "Unerwarteter Fehler während des Versands");
		assertSmtpSentMailCount(0);
	}

	@Test
	@DisplayName("Unerwartete Exception vor dem Versand beendet den Worker-Thread nicht und protokolliert den Fehler")
	void testUnexpectedExceptionInProcessJobKeepsWorkerAlive() {
		// Präpariere einen Job, der bereits bei der Abbruch-Prüfung vor dem Versand eine unerwartete Exception
		// auslöst. Diese schlägt aus processJob() bis in die Schleife des Worker-Threads (run) durch.
		final EmailJob brokenJob = spy(createSimpleJob("to_a@example.org"));
		doThrow(new IllegalStateException("Unerwarteter Testfehler")).when(brokenJob).hasCancellationRequest();

		final long idBroken = manager.enqueue(brokenJob);

		// Der Worker-Thread muss den Fehler im Job-Log protokollieren.
		await().atMost(Duration.ofMillis(DEFAULT_TIMEOUT_MS))
				.untilAsserted(() -> assertJobLogErrorContains(idBroken, "Unerwarteter Fehler bei der Verarbeitung"));

		// Der Job konnte nie regulär abgeschlossen werden und verbleibt daher im Status QUEUED.
		assertEquals(EmailJobStatus.QUEUED, manager.getJob(idBroken).getStatus());

		// Der Worker-Thread muss die Exception überlebt haben und nachfolgende Jobs regulär verarbeiten.
		final long idNext = manager.enqueue(createSimpleJob("to_b@example.org"));
		awaitJobStatus(idNext, EmailJobStatus.COMPLETED_SUCCESSFULLY);
		assertSentMailsCount(idNext, 1);
	}
}
