package de.svws_nrw.data.email;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.svws_nrw.base.email.EmailJob;
import de.svws_nrw.base.email.EmailJobContext;
import de.svws_nrw.base.email.EmailJobManagerFactory;
import de.svws_nrw.base.email.EmailJobRecipient;
import de.svws_nrw.base.email.EmailJobStatus;
import de.svws_nrw.base.email.MailSmtpSession;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Prüft den Abbruch eines E-Mail-Jobs: Er findet nur Jobs des angemeldeten Benutzers und meldet, ob der Job noch lief.
 */
@DisplayName("Diese Testklasse testet den Abbruch in der Klasse DataEmailJobs")
@ExtendWith(MockitoExtension.class)
class DataEmailJobsTest {

	private static final String SCHEMA = "DataEmailJobsTest";

	@Mock
	private DBEntityManager conn;

	@Mock
	private Benutzer user;

	@InjectMocks
	private DataEmailJobs data;

	@AfterEach
	void tearDown() {
		EmailJobManagerFactory.getInstance().freeManager(SCHEMA, 1L);
	}

	@Test
	@DisplayName("cancelEmailJob: ohne Job-Manager des Benutzers antwortet der Abbruch mit NOT_FOUND")
	void cancelEmailJobOhneManager() {
		when(conn.getDBSchema()).thenReturn(SCHEMA);
		when(conn.getUser()).thenReturn(user);
		when(user.getId()).thenReturn(1L);
		final Response response = data.cancelEmailJob(4711);
		assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
		assertThat(((SimpleOperationResponse) response.getEntity()).success).isFalse();
	}

	@Test
	@DisplayName("cancelEmailJob: ein laufender Job endet nach der aktuellen E-Mail, ein erneuter Abbruch meldet ihn als bereits beendet")
	void cancelEmailJobLaufenderJob() throws Exception {
		when(conn.getDBSchema()).thenReturn(SCHEMA);
		when(conn.getUser()).thenReturn(user);
		when(user.getId()).thenReturn(1L);
		final CountDownLatch gestartet = new CountDownLatch(1);
		final CountDownLatch weiter = new CountDownLatch(1);
		final MailSmtpSession session = mock(MailSmtpSession.class);
		doAnswer(invocation -> {
			gestartet.countDown();
			weiter.await(5, TimeUnit.SECONDS);
			return null;
		}).when(session).sendTextMessageWithAttachments(any(), any(), any(), any(), any());
		final EmailJob job = new EmailJob("absender@schule.de", new EmailJobContext(session)).withSubject("Betreff").withBody("Text")
				.addRecipient(new EmailJobRecipient("a@schule.de")).addRecipient(new EmailJobRecipient("b@schule.de"));
		final long idJob = EmailJobManagerFactory.getInstance().getManager(SCHEMA, 1L).enqueue(job);
		assertThat(gestartet.await(5, TimeUnit.SECONDS)).isTrue();

		final Response laufend = data.cancelEmailJob(idJob);
		weiter.countDown();
		assertThat(laufend.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(((SimpleOperationResponse) laufend.getEntity()).log).containsExactly("Abbruch wurde veranlasst.");
		final long ende = System.currentTimeMillis() + 5000;
		while ((job.getStatus() != EmailJobStatus.CANCELED) && (System.currentTimeMillis() < ende)) {
			Thread.sleep(20);
		}
		assertThat(job.getStatus()).isEqualTo(EmailJobStatus.CANCELED);
		assertThat(job.getEmailsSent()).isEqualTo(1);

		final Response beendet = data.cancelEmailJob(idJob);
		assertThat(beendet.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(((SimpleOperationResponse) beendet.getEntity()).log).containsExactly("Job war bereits beendet (Status = CANCELED).");
	}

	@Test
	@DisplayName("cancelEmailJob: ein wartender Job endet sofort ohne Versand und wird als veranlasster Abbruch gemeldet")
	void cancelEmailJobWartenderJob() throws Exception {
		when(conn.getDBSchema()).thenReturn(SCHEMA);
		when(conn.getUser()).thenReturn(user);
		when(user.getId()).thenReturn(1L);
		final CountDownLatch gestartet = new CountDownLatch(1);
		final CountDownLatch weiter = new CountDownLatch(1);
		final MailSmtpSession session = mock(MailSmtpSession.class);
		doAnswer(invocation -> {
			gestartet.countDown();
			weiter.await(5, TimeUnit.SECONDS);
			return null;
		}).when(session).sendTextMessageWithAttachments(any(), any(), any(), any(), any());
		// Der Manager arbeitet seine Jobs nacheinander ab. Solange der erste Job versendet, wartet der zweite.
		final EmailJob laufend = new EmailJob("absender@schule.de", new EmailJobContext(session)).withSubject("Betreff").withBody("Text")
				.addRecipient(new EmailJobRecipient("a@schule.de"));
		final EmailJob wartend = new EmailJob("absender@schule.de", new EmailJobContext(session)).withSubject("Betreff").withBody("Text")
				.addRecipient(new EmailJobRecipient("b@schule.de"));
		EmailJobManagerFactory.getInstance().getManager(SCHEMA, 1L).enqueue(laufend);
		final long idWartend = EmailJobManagerFactory.getInstance().getManager(SCHEMA, 1L).enqueue(wartend);
		assertThat(gestartet.await(5, TimeUnit.SECONDS)).isTrue();
		assertThat(wartend.getStatus()).isEqualTo(EmailJobStatus.QUEUED);

		final Response response = data.cancelEmailJob(idWartend);
		weiter.countDown();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(((SimpleOperationResponse) response.getEntity()).log).containsExactly("Abbruch wurde veranlasst.");
		assertThat(wartend.getStatus()).isEqualTo(EmailJobStatus.CANCELED);
		assertThat(wartend.getEmailsSent()).isZero();
	}

	@Test
	@DisplayName("cancelEmailJob: eine unbekannte Job-ID beim vorhandenen Job-Manager antwortet mit NOT_FOUND")
	void cancelEmailJobUnbekannteId() {
		when(conn.getDBSchema()).thenReturn(SCHEMA);
		when(conn.getUser()).thenReturn(user);
		when(user.getId()).thenReturn(1L);
		EmailJobManagerFactory.getInstance().getManager(SCHEMA, 1L);
		final Response response = data.cancelEmailJob(4711);
		assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
		assertThat(((SimpleOperationResponse) response.getEntity()).log)
				.anyMatch(s -> s.contains("Es konnte kein Email-Job mit der Job-ID 4711 beim aktuellen Benutzer gefunden werden."));
	}

}
