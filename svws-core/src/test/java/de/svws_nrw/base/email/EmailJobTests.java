package de.svws_nrw.base.email;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

class EmailJobTests {

	@Test
	@DisplayName("Job-Constructor mit unzulässigen Werten initialisieren")
	void testConstructorWithNullEmptyValues() {
		assertThrows(IllegalArgumentException.class, () -> new EmailJob(""));
		assertThrows(IllegalArgumentException.class, () -> new EmailJob("   "));
		assertThrows(IllegalArgumentException.class, () -> new EmailJob("\t\n "));
	}

	@Test
	@DisplayName("Job-Id auslesen und setzen testen: getId/setId")
	void testJobId() {
		final EmailJob job = new EmailJob("from@example.org");
		// Die JobId wird vom EmailJobManager gesetzt und nicht bei der Erzeugung. Folglich erzeugt getId einen Fehler, wenn keine ID gesetzt worden ist.
		assertThrows(DeveloperNotificationException.class, job::getId);

		// Setze eine Id, die übernommen werden muss.
		assertTrue(job.setId(123L));
		assertEquals(123L, job.getId());

		// Eine einmal gesetzte ID kann nicht geändert werden.
		assertFalse(job.setId(124L), "Zweites Setzen einer ID muss false liefern");
		assertEquals(123L, job.getId(), "ID muss nach erneutem Setzen unverändert bleiben");
	}

	@Test
	@DisplayName("Job-Builder Getter und Setter testen: withSubject/withBody/addRecipient/addRecipients/getFrom/getSubject/getBody")
	void testJobBuilderGettersAndSetters() {
		final EmailJob job = new EmailJob("from@example.org");
		final EmailJobRecipient r1 = new EmailJobRecipient("to_a@example.org");
		final EmailJobRecipient r2 = new EmailJobRecipient("to_b@example.org");
		final EmailJobRecipient r3 = new EmailJobRecipient("to_c@example.org");

		// Gleichheit bei Rückgabe prüfen
		final EmailJob self1 = job.withSubject("S");
		assertSame(job, self1);
		final EmailJob self2 = job.withBody("B");
		assertSame(job, self2);
		final EmailJob self3 = job.addRecipient(r1);
		assertSame(job, self3);
		final EmailJob self4 = job.addRecipients(List.of(r2, r3));
		assertSame(job, self4);
		final EmailJob self5 = job.addRecipient(null);
		assertSame(job, self5);
		final EmailJob self6 = job.addRecipients(null);
		assertSame(job, self6);
		final EmailJob self7 = job.addRecipients(new ArrayList<>());
		assertSame(job, self7);

		// Gesetzte Werte prüfen
		assertEquals("from@example.org", job.getFrom());
		assertEquals("S", job.getSubject());
		assertEquals("B", job.getBody());
		assertEquals(List.of(r1, r2, r3), job.getRecipients());
	}

	@Test
	@DisplayName("Job-Status setzen und dadurch geänderten Zeitstempel auslesen (setStatus/timeLastChanged)")
	void testSetStatusUpdatesTime() {
		final EmailJob job = new EmailJob("from@example.org");
		// Bei der Initialisierung des Jobs werden der Status und der Zeitstempel schon gesetzt. Speichere diesen zwischen.
		final long t1 = job.getTimeLastChanged();
		// Warte kurz, bevor Änderungen vorgenommen werden.
		await().pollDelay(Duration.ofMillis(100)).atMost(Duration.ofMillis(200)).untilAsserted(() -> { });
		// Ändere den Status, dabei muss auch der Zeitstempel neu gesetzt werden.
		job.setStatus(EmailJobStatus.SENDING);
		final long t2 = job.getTimeLastChanged();
		// Prüfe geänderten Status und Zeitstempel
		assertEquals(EmailJobStatus.SENDING, job.getStatus());
		assertTrue(t2 >= (t1 + 5), "timeLastChanged sollte nach setStatus aktualisiert werden");
	}

	@Test
	@DisplayName("Benachrichtigung über E-Mail-Versand und damit verbundene Zähleränderung testen (notifyEmailSent/getEmailsSent)")
	void testNotifyEmailSent() {
		final EmailJob job = new EmailJob("from@example.org");
		assertEquals(0, job.getEmailsSent());
		// Jedes notify erhöht den Zähler um 1. Wird normalerweise vom JobManager aufgerufen.
		job.notifyEmailSent();
		job.notifyEmailSent();
		assertEquals(2, job.getEmailsSent());
	}

	@Test
	@DisplayName("CancellationRequest testen: requestCancellation/hasCancellationRequest")
	void testCancellationFlag() {
		final EmailJob job = new EmailJob("from@example.org");
		assertFalse(job.hasCancellationRequest());
		job.requestCancellation();
		assertTrue(job.hasCancellationRequest());
	}
}
