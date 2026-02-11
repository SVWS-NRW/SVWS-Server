
package de.svws_nrw.base.email;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Mockito-basiertes Test-Double für {@link MailSmtpSession}, das keine echten E-Mails versendet, sondern die Aufrufe protokolliert und optional eine
 * künstliche Verzögerung einfügt.
 */
class MailSmtpSessionMockHelper {

	record SentMail(String from, String to, String subject, String text, List<EmailJobAttachment> attachments) { }

	private final MailSmtpSession mock;
	private final List<SentMail> sent = new ArrayList<>();
	private volatile long delayMs = 0;
	private volatile Exception exceptionToThrow = null;

	MailSmtpSessionMockHelper() {
		this.mock = mock(MailSmtpSession.class);
		setupMockBehavior();
	}

	private void setupMockBehavior() {
		try {
			// sendTextMessage
			doAnswer(invocation -> {
				throwIfConfigured();
				applyDelay();
				addMail(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3),
						List.of());
				return null;
			}).when(mock).sendTextMessage(anyString(), anyString(), anyString(), anyString());

			// sendTextMessageWithAttachment
			doAnswer(invocation -> {
				throwIfConfigured();
				applyDelay();
				addMail(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3),
						List.of(invocation.getArgument(4)));
				return null;
			}).when(mock).sendTextMessageWithAttachment(anyString(), anyString(), anyString(), anyString(), any(EmailJobAttachment.class));

			// sendTextMessageWithAttachments
			doAnswer(invocation -> {
				throwIfConfigured();
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

	private void applyDelay() {
		if (delayMs > 0) {
			await().pollDelay(Duration.ofMillis(delayMs)).atMost(Duration.ofMillis(delayMs + 1)).untilAsserted(() -> { });
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
}
