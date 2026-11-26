package de.svws_nrw.base.email;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Prüft, wie der Manager beendete Jobs aufbewahrt. Ein beendeter Job gibt seine Anhänge sofort frei, Status und Log bleiben bis zum Ende der
 * Haltezeit abrufbar.
 */
class EmailJobManagerSendCleanupTests extends AbstractEmailJobManagerSendTestBase {

	/**
	 * Erstellt einen Job, bei dem jeder Empfänger einen Anhang erhält.
	 *
	 * @param recipients   die Adressen der Empfänger
	 *
	 * @return der Job
	 */
	private EmailJob createJobWithAttachments(final String... recipients) {
		final EmailJob job = createSimpleJob(recipients);
		for (final EmailJobRecipient recipient : job.getRecipients()) {
			recipient.attachments.add(new EmailJobAttachment("a.pdf", new byte[100], "application/pdf"));
		}
		return job;
	}

	@Test
	@DisplayName("Ein abgeschlossener Job gibt seine Anhänge frei und bleibt abrufbar")
	void testAnhaengeNachAbschluss() {
		final EmailJob job = createJobWithAttachments("to_a@example.org");
		final long id = manager.enqueue(job);
		awaitJobStatus(id, EmailJobStatus.COMPLETED_SUCCESSFULLY);

		assertThat(smtpMockHelper.getSent().get(0).attachments()).hasSize(1);
		assertThat(job.getRecipients().get(0).attachments).isEmpty();
		assertThat(manager.getJob(id)).isSameAs(job);
	}

	@Test
	@DisplayName("Ein vor dem Start abgebrochener Job gibt seine Anhänge sofort frei")
	void testAnhaengeNachAbbruchVorStart() throws InterruptedException {
		smtpMockHelper.blockSend();
		final long idLaufend = manager.enqueue(createSimpleJob("to_a@example.org"));
		assertThat(smtpMockHelper.awaitSendStarted(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS)).isTrue();

		final EmailJob job = createJobWithAttachments("to_b@example.org");
		final long id = manager.enqueue(job);
		assertThat(manager.cancelJob(id)).isTrue();
		assertThat(manager.getJob(id).getStatus()).isEqualTo(EmailJobStatus.CANCELED);
		assertThat(job.getRecipients().get(0).attachments).isEmpty();

		smtpMockHelper.releaseSend();
		awaitJobStatus(idLaufend, EmailJobStatus.COMPLETED_SUCCESSFULLY);
	}

	@Test
	@DisplayName("Ein während des Versands abgebrochener Job behält seine Anhänge, bis der Versand endet")
	void testAnhaengeNachAbbruchWaehrendVersand() throws InterruptedException {
		smtpMockHelper.blockSend();
		final EmailJob job = createJobWithAttachments("to_a@example.org", "to_b@example.org");
		final long id = manager.enqueue(job);
		assertThat(smtpMockHelper.awaitSendStarted(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS)).isTrue();

		assertThat(manager.cancelJob(id)).isTrue();
		assertThat(manager.getJob(id).getStatus()).isEqualTo(EmailJobStatus.SENDING);
		assertThat(job.getRecipients().get(0).attachments).hasSize(1);

		smtpMockHelper.releaseSend();
		awaitJobStatus(id, EmailJobStatus.CANCELED);
		assertThat(smtpMockHelper.getSent().get(0).attachments()).hasSize(1);
		assertThat(job.getRecipients()).allSatisfy(recipient -> assertThat(recipient.attachments).isEmpty());
	}

	@Test
	@DisplayName("Ein beendeter Job wird erst nach seiner Haltezeit entfernt")
	void testEntfernenNachHaltezeit() {
		final EmailJobContext kurzeHaltezeit = new EmailJobContext(smtpMockHelper.getMock()).withTimeToKeepCompletedJobs(50);
		final long idLang = manager.enqueue(createSimpleJob("to_a@example.org"));
		final long idKurz = manager.enqueue(createSimpleJob(kurzeHaltezeit, "to_b@example.org"));

		awaitJobStatus(idLang, EmailJobStatus.COMPLETED_SUCCESSFULLY);
		awaitJobRemoval(idKurz);
		assertThat(manager.getJob(idLang)).isNotNull();
	}

}
