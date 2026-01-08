package de.svws_nrw.base.email;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gemeinsame Basis-Klasse für die Versandtests des E-Mail-Job-Managers.
 * Enthält Setup/Teardown und Hilfsmethoden zum Warten auf Zustände sowie
 * zum Erstellen einfacher Test-Jobs.
 */
abstract class AbstractEmailJobManagerSendTestBase {

	/** Zeit, die standardgemäß gewartet wird, bis eine Operation abgeschlossen ist. */
	protected static final long DEFAULT_TIMEOUT_MS = 2000;
	/** Das Zeitintervall, nach dem eine neue Anfrage gestartet werden soll. */
	protected static final long POLL_INTERVAL_MS = 10;
	/** Helferklasse, um ein Mock für die SMTP Verbindung zu erzeugen. */
	protected MailSmtpSessionMockHelper smtpMockHelper;
	/** Der Kontext, der für den Manager verwendet werden soll.  */
	protected EmailJobManagerContext context;
	/** Der E-Mail-Job-Manager für die Tests. */
	protected EmailJobManager manager;

	@BeforeEach
	void setupBase() {
		smtpMockHelper = new MailSmtpSessionMockHelper();
		context = new EmailJobManagerContext("schemaX", 42L, smtpMockHelper.getMock());
		manager = EmailJobManagerFactory.getInstance().getManager(context);
	}

	@AfterEach
	void clearManagersBase() {
		EmailJobManagerFactory.getInstance().freeAllManager();
		smtpMockHelper = null;
		context = null;
		manager = null;
	}

	/**
	 * Wartet auf einen bestimmten Status eines Jobs unter Verwendung des Standard-Timeouts.
	 *
	 * @param jobId          die ID des zu prüfenden Jobs
	 * @param expectedStatus der erwartete Status
	 */
	protected void awaitJobStatus(final long jobId, final EmailJobStatus expectedStatus) {
		awaitJobStatus(jobId, expectedStatus, DEFAULT_TIMEOUT_MS);
	}

	/**
	 * Wartet auf einen bestimmten Status eines Jobs innerhalb eines angegebenen Timeouts. Der E-Mail-Job-Manager kann dabei übergeben werden.
	 *
	 * @param jobId          die ID des zu prüfenden Jobs
	 * @param expectedStatus der erwartete Status
	 * @param timeoutMs      das Timeout in Millisekunden
	 */
	protected void awaitJobStatus(final long jobId, final EmailJobStatus expectedStatus, final long timeoutMs) {
		await().atMost(Duration.ofMillis(timeoutMs))
				.untilAsserted(() -> assertJobHasStatus(jobId, expectedStatus));
	}

	/**
	 * Wartet auf einen bestimmten Status bei einem spezifischen Manager innerhalb eines Timeouts.
	 *
	 * @param mgr            der zu verwendende {@link EmailJobManager}
	 * @param jobId          die ID des zu prüfenden Jobs
	 * @param expectedStatus der erwartete Status
	 * @param timeoutMs      das Timeout in Millisekunden
	 */
	protected void awaitJobStatusForManager(final EmailJobManager mgr, final long jobId, final EmailJobStatus expectedStatus, final long timeoutMs) {
		await().atMost(Duration.ofMillis(timeoutMs))
				.untilAsserted(() -> assertJobHasStatus(mgr, jobId, expectedStatus));
	}

	/**
	 * Wartet darauf, dass ein Job aus dem Manager mit Standard-Timeout entfernt wurde.
	 *
	 * @param jobId die ID des Jobs, der entfernt werden soll
	 */
	protected void awaitJobRemoval(final long jobId) {
		awaitJobRemoval(jobId, DEFAULT_TIMEOUT_MS);
	}

	/**
	 * Wartet darauf, dass ein Job aus dem Manager entfernt wurde, innerhalb eines Timeouts.
	 *
	 * @param jobId     die ID des Jobs, der entfernt werden soll
	 * @param timeoutMs das Timeout in Millisekunden
	 */
	protected void awaitJobRemoval(final long jobId, final long timeoutMs) {
		await().atMost(Duration.ofMillis(timeoutMs))
				.untilAsserted(() -> assertThat(manager.getJob(jobId))
						.as("Failed: Job %d sollte binnen %d ms entfernt worden sein.", jobId, timeoutMs)
						.isNull());
	}

	/**
	 * Wartet darauf, dass der SMTP-Mock eine exakte Anzahl an E-Mails empfangen hat.
	 *
	 * @param exactCount die erwartete exakte Anzahl
	 * @param timeoutMs  das Timeout in Millisekunden
	 */
	protected void awaitSmtpSentMailCount(final int exactCount, final long timeoutMs) {
		await().atMost(Duration.ofMillis(timeoutMs))
				.untilAsserted(() -> assertSmtpSentMailCount(exactCount));
	}

	/**
	 * Wartet darauf, dass der SMTP-Mock maximal eine bestimmte Anzahl an E-Mails empfangen hat.
	 *
	 * @param maxCount  die maximal erlaubte Anzahl
	 * @param timeoutMs das Timeout in Millisekunden
	 */
	protected void awaitSmtpSentMailMaxCount(final int maxCount, final long timeoutMs) {
		await().atMost(Duration.ofMillis(timeoutMs))
				.untilAsserted(() -> assertSmtpSentMailMaxCount(maxCount));
	}

	/**
	 * Wartet darauf, dass der SMTP-Mock mindestens eine bestimmte Anzahl an E-Mails empfangen hat.
	 *
	 * @param minCount  die erwartete Mindestanzahl
	 * @param timeoutMs das Timeout in Millisekunden
	 */
	protected void awaitSmtpSentMailMinCount(final int minCount, final long timeoutMs) {
		await().atMost(Duration.ofMillis(timeoutMs))
				.untilAsserted(() -> assertSmtpSentMailMinCount(minCount));
	}

	/**
	 * Prüft, ob die Anzahl der tatsächlich über SMTP versendeten E-Mails dem erwarteten Wert entspricht.
	 *
	 * @param expectedCount die erwartete Anzahl
	 */
	protected void assertSmtpSentMailCount(final int expectedCount) {
		assertThat(smtpMockHelper.getSent())
				.as("Failed: Anzahl der tatsächlich versendeten E-Mails ergab nicht %d.", expectedCount)
				.hasSize(expectedCount);
	}

	/**
	 * Prüft, ob die Anzahl der über SMTP versendeten E-Mails einen Maximalwert nicht überschreitet.
	 *
	 * @param expectedMaxCount die maximal erwartete Anzahl
	 */
	protected void assertSmtpSentMailMaxCount(final int expectedMaxCount) {
		assertThat(smtpMockHelper.getSent())
				.as("Failed: Erwartete max. %d versendete E-Mails. Tatsächliche Anzahl: %d", expectedMaxCount, smtpMockHelper.getSent().size())
				.hasSizeLessThanOrEqualTo(expectedMaxCount);
	}

	/**
	 * Prüft, ob die Anzahl der über SMTP versendeten E-Mails einen Minimalwert erreicht.
	 *
	 * @param expectedMinCount die minimal erwartete Anzahl
	 */
	protected void assertSmtpSentMailMinCount(final int expectedMinCount) {
		assertThat(smtpMockHelper.getSent())
				.as("Failed: Erwartete mind. %d versendete E-Mails. Tatsächliche Anzahl: %d", expectedMinCount, smtpMockHelper.getSent().size())
				.hasSizeGreaterThanOrEqualTo(expectedMinCount);
	}

	/**
	 * Prüft, ob ein Job intern die erwartete Anzahl an erfolgreich versendeten E-Mails registriert hat.
	 *
	 * @param jobId         die ID des Jobs
	 * @param expectedCount die erwartete Anzahl
	 */
	protected void assertJobSentMailCount(final long jobId, final int expectedCount) {
		assertJobSentMailCount(this.manager, jobId, expectedCount);
	}

	/**
	 * Prüft bei einem spezifischen Manager, ob ein Job intern die erwartete Anzahl an erfolgreich versendeten E-Mails registriert hat.
	 *
	 * @param mgr           der zu prüfende Manager
	 * @param jobId         die ID des Jobs
	 * @param expectedCount die erwartete Anzahl
	 */
	protected void assertJobSentMailCount(final EmailJobManager mgr, final long jobId, final int expectedCount) {
		final EmailJob job = mgr.getJob(jobId);
		assertThat(job).isNotNull();
		assertThat(job.getEmailsSent())
				.as("Failed: Anzahl der als 'gesendet' markierten E-Mails im Job %d ergab nicht %d", jobId, expectedCount)
				.isEqualTo(expectedCount);
	}

	/**
	 * Prüft konsistent sowohl den internen Zähler des Jobs als auch die tatsächlichen SMTP-Versendungen.
	 *
	 * @param jobId         die ID des Jobs
	 * @param expectedCount die erwartete Anzahl
	 */
	protected void assertSentMailsCount(final long jobId, final int expectedCount) {
		assertSentMailsCount(this.manager, jobId, expectedCount);
	}

	/**
	 * Prüft bei einem spezifischen Manager konsistent sowohl den internen Zähler des Jobs als auch die tatsächlichen SMTP-Versendungen.
	 *
	 * @param mgr           der zu prüfende Manager
	 * @param jobId         die ID des Jobs
	 * @param expectedCount die erwartete Anzahl
	 */
	protected void assertSentMailsCount(final EmailJobManager mgr, final long jobId, final int expectedCount) {
		assertJobSentMailCount(mgr, jobId, expectedCount);
		assertSmtpSentMailCount(expectedCount);
	}

	/**
	 * Prüft, ob das Protokoll für übersprungene Empfänger (Skip-Log) eines Jobs leer ist.
	 *
	 * @param jobId die ID des Jobs
	 */
	protected void assertJobLogSkippedEmpty(final long jobId) {
		final EmailJob job = manager.getJob(jobId);
		assertThat(job).isNotNull();
		assertThat(job.logSkipped).as("Skip-Log von Job %d sollte leer sein", jobId).isEmpty();
	}

	/**
	 * Prüft, ob das Protokoll für übersprungene Empfänger mindestens einen der erwarteten Texte enthält.
	 *
	 * @param jobId            die ID des Jobs
	 * @param expectedSnippets die Texte, nach denen gesucht werden soll
	 */
	protected void assertJobLogSkippedContains(final long jobId, final String... expectedSnippets) {
		assertJobLogSkippedContains(this.manager, jobId, expectedSnippets);
	}

	/**
	 * Prüft bei einem spezifischen Manager, ob das Skip-Log eines Jobs bestimmte Texte enthält.
	 *
	 * @param mgr              der zu prüfende Manager
	 * @param jobId            die ID des Jobs
	 * @param expectedSnippets die Texte, nach denen gesucht werden soll
	 */
	protected void assertJobLogSkippedContains(final EmailJobManager mgr, final long jobId, final String... expectedSnippets) {
		final EmailJob job = mgr.getJob(jobId);
		assertThat(job).isNotNull();

		final List<String> allLogs = new ArrayList<>(job.logSkipped);
		final boolean found = Arrays.stream(expectedSnippets).anyMatch(snippet -> allLogs.stream().anyMatch(log -> log.contains(snippet)));

		assertThat(found)
				.as("Failed: Job-Skipped-Log sollte mindestens einen dieser Begriffe enthalten: %s", Arrays.toString(expectedSnippets))
				.isTrue();
	}

	/**
	 * Prüft, ob das Fehler-Log eines Jobs leer ist.
	 *
	 * @param jobId die ID des Jobs
	 */
	protected void assertJobLogErrorsIsEmpty(final long jobId) {
		final EmailJob job = manager.getJob(jobId);
		assertThat(job).isNotNull();
		assertThat(job.logError).as("Error-Log von Job %d sollte leer sein", jobId).isEmpty();
	}

	/**
	 * Prüft, ob das Fehler-Log eines Jobs mindestens einen der erwarteten Texte enthält.
	 *
	 * @param jobId            die ID des Jobs
	 * @param expectedSnippets die Texte, nach denen gesucht werden soll
	 */
	protected void assertJobLogErrorContains(final long jobId, final String... expectedSnippets) {
		assertJobLogErrorContains(this.manager, jobId, expectedSnippets);
	}

	/**
	 * Prüft bei einem spezifischen Manager, ob das Fehler-Log eines Jobs bestimmte Texte enthält.
	 *
	 * @param mgr              der zu prüfende Manager
	 * @param jobId            die ID des Jobs
	 * @param expectedSnippets die Texte, nach denen gesucht werden soll
	 */
	protected void assertJobLogErrorContains(final EmailJobManager mgr, final long jobId, final String... expectedSnippets) {
		final EmailJob job = mgr.getJob(jobId);
		assertThat(job).isNotNull();

		final List<String> allLogs = new ArrayList<>(job.logError);
		final boolean found = Arrays.stream(expectedSnippets).anyMatch(snippet -> allLogs.stream().anyMatch(log -> log.contains(snippet)));

		assertThat(found)
				.as("Failed: Job-Error-Log sollte mindestens einen dieser Begriffe enthalten: %s", Arrays.toString(expectedSnippets))
				.isTrue();
	}

	/**
	 * Prüft, ob ein Job den erwarteten Status besitzt.
	 *
	 * @param jobId          die ID des Jobs
	 * @param expectedStatus der erwartete Status
	 */
	private void assertJobHasStatus(final long jobId, final EmailJobStatus expectedStatus) {
		final EmailJob job = manager.getJob(jobId);
		assertThat(job)
				.as("Failed: Job %d sollte existieren. Erwarteter Status: %s", jobId, expectedStatus)
				.isNotNull();

		assertThat(job.getStatus())
				.as("Failed: Job %d (separater Manager) sollte Status %s erreichen. Aktueller Zustand: %s", jobId, expectedStatus, job.getStatus())
				.isEqualTo(expectedStatus);
	}

	/**
	 * Prüft bei einem spezifischen Manager, ob ein Job den erwarteten Status besitzt.
	 *
	 * @param mgr            der zu prüfende Manager
	 * @param jobId          die ID des Jobs
	 * @param expectedStatus der erwartete Status
	 */
	private void assertJobHasStatus(final EmailJobManager mgr, final long jobId, final EmailJobStatus expectedStatus) {
		final EmailJob job = mgr.getJob(jobId);
		assertThat(job)
				.as("Failed: Job %d (separater Manager) sollte existieren.", jobId)
				.isNotNull();

		assertThat(job.getStatus())
				.as("Failed: Job %d (separater Manager) sollte Status %s erreichen. Aktueller Zustand: %s", jobId, expectedStatus, job.getStatus())
				.isEqualTo(expectedStatus);
	}

	/**
	 * Hilfsmethode zum Erstellen eines einfachen {@link EmailJob}s für Testzwecke.
	 *
	 * @param recipients die E-Mail-Adressen der Empfänger
	 *
	 * @return das erzeugte Job-Objekt
	 */
	protected EmailJob createSimpleJob(final String... recipients) {
		final EmailJob job = new EmailJob("from@example.org").withSubject("Test").withBody("Body");
		for (final String recipient : recipients) {
			job.addRecipient(new EmailJobRecipient(recipient));
		}
		return job;
	}
}
