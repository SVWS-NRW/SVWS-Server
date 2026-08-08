
package de.svws_nrw.base.email;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import jakarta.mail.MessagingException;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Mockito-basiertes Test-Double für {@link MailSmtpSession}, das keine echten E-Mails versendet, sondern die Aufrufe protokolliert und optional eine
 * künstliche Verzögerung einfügt. Über {@link #blockSend()}, {@link #awaitSendStarted(long, TimeUnit)} und {@link #releaseSend()} kann ein Versand
 * zusätzlich gezielt angehalten und wieder freigegeben werden, um Nebenläufigkeit ohne zeitabhängige Annahmen zu testen.
 */
class MailSmtpSessionMockHelper {

	record SentMail(String from, String to, String subject, String text, List<EmailJobAttachment> attachments) { }

	/** Die maximale Zeit in Millisekunden, die ein blockierter Versand auf seine Freigabe wartet, damit ein fehlerhafter Test nicht dauerhaft hängt. */
	private static final long BLOCK_TIMEOUT_MS = 10000;

	private final MailSmtpSession mock;
	private final List<SentMail> sent = new ArrayList<>();
	private volatile long delayMs = 0;
	private volatile Exception exceptionToThrow = null;
	/** Signalisiert, dass ein Versand begonnen hat. Ist null, solange keine Blockade über {@link #blockSend()} aktiviert wurde. */
	private volatile CountDownLatch sendStarted = null;
	/** Blockiert den Versand, bis der Test ihn über {@link #releaseSend()} freigibt. Ist null, solange keine Blockade aktiviert wurde. */
	private volatile CountDownLatch sendRelease = null;

	MailSmtpSessionMockHelper() {
		this.mock = mock(MailSmtpSession.class);
		setupMockBehavior();
	}

	private void setupMockBehavior() {
		try {
			// sendTextMessage
			doAnswer(invocation -> {
				throwIfConfigured();
				signalStartAndAwaitRelease();
				applyDelay();
				addMail(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3),
						List.of());
				return null;
			}).when(mock).sendTextMessage(anyString(), anyString(), anyString(), anyString());

			// sendTextMessageWithAttachment
			doAnswer(invocation -> {
				throwIfConfigured();
				signalStartAndAwaitRelease();
				applyDelay();
				addMail(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3),
						List.of(invocation.getArgument(4)));
				return null;
			}).when(mock).sendTextMessageWithAttachment(anyString(), anyString(), anyString(), anyString(), any(EmailJobAttachment.class));

			// sendTextMessageWithAttachments
			doAnswer(invocation -> {
				throwIfConfigured();
				signalStartAndAwaitRelease();
				applyDelay();
				final List<EmailJobAttachment> attachments = invocation.getArgument(4);
				addMail(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3),
						(attachments != null) ? attachments : List.of());
				return null;
			}).when(mock).sendTextMessageWithAttachments(anyString(), anyString(), anyString(), anyString(), any());
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void throwIfConfigured() throws Exception {
		if (exceptionToThrow != null) {
			if (exceptionToThrow instanceof RuntimeException) {
				throw (RuntimeException) exceptionToThrow;
			}
			throw exceptionToThrow;
		}
	}

	/**
	 * Signalisiert, dass ein Versand begonnen hat, und wartet anschließend, bis der Versand über {@link #releaseSend()} freigegeben wird.
	 * Wurde keine Blockade über {@link #blockSend()} aktiviert, kehrt die Methode sofort zurück.
	 *
	 * @throws MessagingException   falls die Freigabe nicht innerhalb von {@link #BLOCK_TIMEOUT_MS} erfolgt oder der wartende Thread unterbrochen wurde
	 */
	private void signalStartAndAwaitRelease() throws MessagingException {
		final CountDownLatch release = this.sendRelease;
		if (release == null) {
			return;
		}
		final CountDownLatch started = this.sendStarted;
		if (started != null) {
			started.countDown();
		}
		try {
			if (!release.await(BLOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
				throw new MessagingException("Der blockierte Versand wurde nicht innerhalb von %d ms freigegeben.".formatted(BLOCK_TIMEOUT_MS));
			}
		} catch (final InterruptedException e) {
			// Der Worker-Thread des Managers wurde beendet (z.B. durch shutdown()). Das Interrupt-Flag wird für den Aufrufer wiederhergestellt.
			Thread.currentThread().interrupt();
			throw new MessagingException("Der blockierte Versand wurde unterbrochen.", e);
		}
	}

	private void applyDelay() {
		if (delayMs > 0) {
			// Aufruf garantiert ein Mindest-Delay. Ist das System stark ausgelastet, beträgt die Toleranz bis zu 1000ms mehr.
			await().pollDelay(Duration.ofMillis(delayMs)).atMost(Duration.ofMillis(delayMs + 1000)).until(() -> true);
		}
	}

	private void addMail(final String from, final String to, final String subject, final String text, final List<EmailJobAttachment> attachments) {
		synchronized (sent) {
			sent.add(new SentMail(from, to, subject, text, attachments));
		}
	}

	MailSmtpSession getMock() {
		return mock;
	}

	List<SentMail> getSent() {
		synchronized (sent) {
			return new ArrayList<>(sent);
		}
	}

	void setDelayMs(final long delay) {
		this.delayMs = delay;
	}

	/**
	 * Konfiguriert den Mock so, dass er immer die angegebene Exception wirft.
	 *
	 * @param exception die zu werfende Exception
	 */
	void throwOnSend(final Exception exception) {
		this.exceptionToThrow = exception;
	}

	/**
	 * Aktiviert die Blockade des Versands: Der nächste Versand signalisiert seinen Start über {@link #awaitSendStarted(long, TimeUnit)} und
	 * blockiert anschließend, bis der Test ihn über {@link #releaseSend()} freigibt. Nach der Freigabe laufen alle weiteren Versendungen
	 * ungehindert durch. Damit lässt sich ein laufender Versand deterministisch anhalten, ohne über Wartezeiten zu arbeiten.
	 */
	void blockSend() {
		this.sendStarted = new CountDownLatch(1);
		this.sendRelease = new CountDownLatch(1);
	}

	/**
	 * Wartet darauf, dass ein durch {@link #blockSend()} blockierter Versand tatsächlich begonnen hat.
	 *
	 * @param timeout   die maximale Wartezeit
	 * @param unit      die Einheit der maximalen Wartezeit
	 *
	 * @return true, wenn der Versand innerhalb der Wartezeit begonnen hat, und ansonsten false
	 *
	 * @throws InterruptedException   falls der wartende Thread unterbrochen wurde
	 */
	boolean awaitSendStarted(final long timeout, final TimeUnit unit) throws InterruptedException {
		final CountDownLatch started = this.sendStarted;
		if (started == null) {
			return false;
		}
		return started.await(timeout, unit);
	}

	/**
	 * Gibt einen durch {@link #blockSend()} blockierten Versand wieder frei. Der Aufruf ist auch dann unschädlich, wenn keine Blockade
	 * aktiviert wurde oder der Versand bereits freigegeben ist.
	 */
	void releaseSend() {
		final CountDownLatch release = this.sendRelease;
		if (release != null) {
			release.countDown();
		}
	}
}
