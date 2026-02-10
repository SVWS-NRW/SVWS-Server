package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class EmailJobManagerSendRateLimitingTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("Ein Jobs respektiert alleine das Rate-Limit")
	void testOneJobRateLimiting() {
		// Setze ein Rate-Limit: 2 E-Mails pro 6 Sekunden
		context.withRateLimitTimeframeMs(5000).withMaxEmailsPerMinute(2);

		// Erstelle drei Jobs mit je einem Empfänger (mehr als das Limit!)
		final EmailJob job1 = createSimpleJob("to_a@example.org", "to_b@example.org", "to_c@example.org");

		final long startTime = System.currentTimeMillis();

		final long id1 = manager.enqueue(job1);

		// Warte kurz und prüfe, dass maximal 2 E-Mails versendet wurden
		awaitSmtpSentMailMaxCount(2, 1001);

		// Warte auf alle drei Jobs
		awaitJobStatus(id1, EmailJobStatus.COMPLETED_SUCCESSFULLY, 7000);

		final long endTime = System.currentTimeMillis();
		final long duration = endTime - startTime;

		// Alle 3 E-Mails sollten versendet worden sein
		assertSmtpSentMailCount(3);

		// Die dritte E-Mail sollte durch das Rate-Limit verzögert worden sein, sodass mindestens 5 Sekunden vergangen sein sollten.
		assertTrue(duration >= 5000,
				"Der Versand sollte durch das Rate-Limit verzögert worden sein (mind. 5s), aber dauerte nur " + duration + "ms.");
	}

	@Test
	@DisplayName("Mehrere Jobs respektieren gemeinsam das Rate-Limit")
	void testMultipleJobsRateLimiting() {
		// Setze ein Rate-Limit: 2 E-Mails pro 6 Sekunden
		context.withRateLimitTimeframeMs(5000).withMaxEmailsPerMinute(2);

		// Erstelle drei Jobs mit je einem Empfänger (mehr als das Limit!)
		final EmailJob job1 = createSimpleJob("to_a@example.org");
		final EmailJob job2 = createSimpleJob("to_b@example.org");
		final EmailJob job3 = createSimpleJob("to_c@example.org");

		final long startTime = System.currentTimeMillis();

		final long id1 = manager.enqueue(job1);
		final long id2 = manager.enqueue(job2);
		final long id3 = manager.enqueue(job3);

		// Warte kurz und prüfe, dass maximal 2 E-Mails versendet wurden
		awaitSmtpSentMailMaxCount(2, 1001);

		// Warte auf alle drei Jobs
		awaitJobStatus(id1, EmailJobStatus.COMPLETED_SUCCESSFULLY, 7000);
		awaitJobStatus(id2, EmailJobStatus.COMPLETED_SUCCESSFULLY, 7000);
		awaitJobStatus(id3, EmailJobStatus.COMPLETED_SUCCESSFULLY, 7000);

		final long endTime = System.currentTimeMillis();
		final long duration = endTime - startTime;

		// Alle 3 E-Mails sollten versendet worden sein
		assertSmtpSentMailCount(3);

		// Die dritte E-Mail sollte durch das Rate-Limit verzögert worden sein, sodass mindestens 5 Sekunden vergangen sein sollten.
		assertTrue(duration >= 5000,
				"Der Versand sollte durch das Rate-Limit verzögert worden sein (mind. 5s), aber dauerte nur " + duration + "ms.");
	}
}
