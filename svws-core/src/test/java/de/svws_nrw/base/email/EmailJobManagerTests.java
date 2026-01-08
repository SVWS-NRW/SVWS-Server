package de.svws_nrw.base.email;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailJobManagerTests {

	private MailSmtpSession smtp;
	private EmailJobManagerContext context;
	private EmailJobManager manager;

	@BeforeEach
	void setup() {
		smtp = mock(MailSmtpSession.class);
		// Um den Test nicht unnötig lang laufen zu lassen, wird die Haltezeit auf 30 ms für abgeschlossene Jobs gesetzt.
		context = new EmailJobManagerContext("schemaForUser7", 7L, smtp).withTimeToKeepCompletedJobs(30);
		manager = EmailJobManagerFactory.getInstance().getManager(context);
	}

	@AfterEach
	void clearManagers() {
		EmailJobManagerFactory.getInstance().freeAllManager();
	}

	@Test
	@DisplayName("getContext liefert den bei Erstellung gesetzten Kontext")
	void testGetContext() {
		assertSame(context, manager.getContext());
	}

	@Test
	@DisplayName("getJob liefert null für unbekannte ID")
	void testGetUnknownJob() {
		assertNull(manager.getJob(999999L));
	}

	@Test
	@DisplayName("Abgeschlossene Jobs werden nach Haltezeit entfernt")
	void testCompletedJobRemoval() {
		final EmailJob job = new EmailJob("from@example.org");
		job.addRecipient(new EmailJobRecipient("to_a@example.org"));
		final long id = manager.enqueue(job);
		assertTrue(id > 0);

		// Warten bis Job in einem Endzustand ist (COMPLETED/CANCELED/FAILED)
		await().atMost(Duration.ofMillis(3000))
				.untilAsserted(() -> {
					final EmailJob current = manager.getJob(id);

					// Ein Job, der null ist, wurde bereits verarbeitet und entfernt, er ist alson in einem Endzustand.
					final boolean hasFinished = ((current == null) || hasFinished(current.getStatus()));

					assertThat(hasFinished)
							.as("Failed: Job %d sollte einen Endzustand erreichen oder entfernt worden sein.", id)
							.isTrue();
				});

		// Nach der Haltezeit sollte der Job entfernt sein (Haltezeit = 30 ms)
		await().atMost(Duration.ofMillis(3000))
				.untilAsserted(() -> assertThat(manager.getJob(id))
						.as("Failed: Job %d sollte binnen 3000 ms entfernt worden sein.", id)
						.isNull()
				);
	}

	private boolean hasFinished(final EmailJobStatus status) {
		return switch (status) {
			case COMPLETED_SUCCESSFULLY, COMPLETED_WITH_ERRORS, CANCELED, FAILED -> true;
			default -> false;
		};
	}

	@Test
	@DisplayName("shutdown testen. Sollte keinen Fehler auslösen.")
	void testShutdown() {
		// Prüfe, ob ein Shutdown ohne Fehler erfolgt.
		assertDoesNotThrow(() -> manager.shutdown());
		// Prüfe, ob ein weiterer Shutdown ohne Fehler erfolgt.
		assertDoesNotThrow(() -> manager.shutdown());
	}
}
