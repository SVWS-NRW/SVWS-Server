package de.svws_nrw.base.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailJobContextTests {

	private MailSmtpSession smtp;
	private EmailJobContext emailJobContext;

	@BeforeEach
	void setup() {
		smtp = mock(MailSmtpSession.class);
		emailJobContext = new EmailJobContext(smtp);
	}

	@Test
	@DisplayName("Context-Constructor testen - Initialisierung testen")
	void testConstructorAndGetters() {
		// Über Konstruktor gesetzte Werte prüfen
		assertSame(smtp, emailJobContext.getSmtpSession());

		// Mit Initialisierung einhergehende Standardwerte prüfen
		assertEquals(20, emailJobContext.getMaxEmailsPerMinute());
		assertEquals(60000L, emailJobContext.getTimeToKeepCompletedJobs());
		assertEquals(0L, emailJobContext.getMaxAttachmentSize());
		assertFalse(emailJobContext.isForceMaxAttachmentSize());
	}

	@Test
	@DisplayName("Context-Constructor mit unzulässigem Wert initialisieren")
	void testConstructorWithNullValue() {
		assertThrows(IllegalArgumentException.class, () -> new EmailJobContext(null));
	}

	@Test
	@DisplayName("Context-Builder setzen die Werte und diese werden richtig ausgelesen")
	void testBuilderAndGetters() {
		// Neue Werte setzen und Gleichheit bei Rückgabe prüfen
		final EmailJobContext self1 = emailJobContext.withMaxEmailsPerMinute(10);
		assertSame(emailJobContext, self1);
		final EmailJobContext self2 = emailJobContext.withTimeToKeepCompletedJobs(5000L);
		assertSame(emailJobContext, self2);
		final EmailJobContext self3 = emailJobContext.withMaxAttachmentSize(2048L);
		assertSame(emailJobContext, self3);
		final EmailJobContext self4 = emailJobContext.withForceMaxAttachmentSize(true);
		assertSame(emailJobContext, self4);

		// Neue Werte prüfen
		assertEquals(10, emailJobContext.getMaxEmailsPerMinute());
		assertEquals(5000L, emailJobContext.getTimeToKeepCompletedJobs());
		assertEquals(2048L, emailJobContext.getMaxAttachmentSize());
		assertTrue(emailJobContext.isForceMaxAttachmentSize());
	}

	@Test
	@DisplayName("RateLimitTimeframe mit Wert unter 2000ms ändert Minimum nicht")
	void testRateLimitTimeframeMinimum() {
		// Wert bei Initialisierung ist 60000ms. Setzen unter 2000ms sollte Wert nicht ändern.
		emailJobContext.withRateLimitTimeframeMs(500L);
		assertEquals(60000L, emailJobContext.getRateLimitTimeframeMs(), "Werte unter 2000ms sollten Wert nicht ändern");
		// Wert über 2000ms muss gesetzt werden.
		emailJobContext.withRateLimitTimeframeMs(5000L);
		assertEquals(5000L, emailJobContext.getRateLimitTimeframeMs());
	}

	@Test
	@DisplayName("MaxEmailsPerMinute-Methode muss beim Setzen es negativen Wert den zuvor gesetzt Wert zurückgegeben.")
	void testPositiveTimeToKeepCompletedJobs() {
		// Wert bei Initialisierung ist 60000
		assertEquals(60000, emailJobContext.getTimeToKeepCompletedJobs());
		// Negativer Wert darf 20 nicht ändern.
		emailJobContext.withTimeToKeepCompletedJobs(-10000);
		assertEquals(60000, emailJobContext.getTimeToKeepCompletedJobs());
		// Positiver Wert muss angenommen werden.
		emailJobContext.withTimeToKeepCompletedJobs(30000);
		assertEquals(30000, emailJobContext.getTimeToKeepCompletedJobs());
		// 0 darf den vorherigen Wert nicht ändern.
		emailJobContext.withTimeToKeepCompletedJobs(0);
		assertEquals(30000, emailJobContext.getTimeToKeepCompletedJobs());
	}

	@Test
	@DisplayName("MaxEmailsPerMinute-Methode muss beim Setzen es negativen Wert den zuvor gesetzt Wert zurückgegeben.")
	void testPositiveMaxEmailsPerMinute() {
		// Wert bei Initialisierung ist 20
		assertEquals(20, emailJobContext.getMaxEmailsPerMinute());
		// Negativer Wert darf 20 nicht ändern.
		emailJobContext.withMaxEmailsPerMinute(-10);
		assertEquals(20, emailJobContext.getMaxEmailsPerMinute());
		// Positiver Wert muss angenommen werden.
		emailJobContext.withMaxEmailsPerMinute(30);
		assertEquals(30, emailJobContext.getMaxEmailsPerMinute());
		// 0 darf den vorherigen Wert nicht ändern.
		emailJobContext.withMaxEmailsPerMinute(0);
		assertEquals(30, emailJobContext.getMaxEmailsPerMinute());
		// 1 muss als kleinster Wert angenommen werden.
		emailJobContext.withMaxEmailsPerMinute(1);
		assertEquals(1, emailJobContext.getMaxEmailsPerMinute());
	}

	@Test
	@DisplayName("MaxAttachmentSize-Methode muss beim Setzen es negativen Wert den zuvor gesetzt Wert zurückgegeben.")
	void testNonNegativeMaxAttachmentSize() {
		// Wert bei Initialisierung ist 0
		assertEquals(0L, emailJobContext.getMaxAttachmentSize());
		// Negativer Wert darf 0 nicht ändern.
		emailJobContext.withMaxAttachmentSize(-1024L);
		assertEquals(0L, emailJobContext.getMaxAttachmentSize());
		// Positiver Wert muss angenommen werden.
		emailJobContext.withMaxAttachmentSize(2048L);
		assertEquals(2048L, emailJobContext.getMaxAttachmentSize());
		// Negativer Wert darf den vorherigen Wert nicht ändern.
		emailJobContext.withMaxAttachmentSize(-1024L);
		assertEquals(2048L, emailJobContext.getMaxAttachmentSize());
		// Das Setzen von 0 muss 0 ergeben.
		emailJobContext.withMaxAttachmentSize(0L);
		assertEquals(0L, emailJobContext.getMaxAttachmentSize());
	}

}
