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

class EmailJobManagerContextTests {

	private MailSmtpSession smtp;
	private EmailJobManagerContext emailJobManagerContext;

	@BeforeEach
	void setup() {
		smtp = mock(MailSmtpSession.class);
		emailJobManagerContext = new EmailJobManagerContext("SCHEMA_A", 5L, smtp);
	}

	@Test
	@DisplayName("Context-Constructor testen - Initialisierung testen")
	void testConstructorAndGetters() {
		// Über Konstruktor gesetzte Werte prüfen
		assertEquals("SCHEMA_A", emailJobManagerContext.getDBSchema());
		assertEquals(5L, emailJobManagerContext.getUserId());
		assertSame(smtp, emailJobManagerContext.getSmtpSession());

		// Mit Initialisierung einhergehende Standardwerte prüfen
		assertEquals(20, emailJobManagerContext.getMaxEmailsPerMinute());
		assertEquals(60000L, emailJobManagerContext.getTimeToKeepCompletedJobs());
		assertEquals(0L, emailJobManagerContext.getMaxAttachmentSize());
		assertFalse(emailJobManagerContext.isForceMaxAttachmentSize());
	}

	@Test
	@DisplayName("Context-Constructor mit unzulässigen Werten initialisieren")
	void testConstructorWithNullEmptyValues() {
		assertThrows(IllegalArgumentException.class, () -> new EmailJobManagerContext("schema", 1, null));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobManagerContext("", 1, smtp));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobManagerContext("   ", 1, smtp));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobManagerContext("\t\n ", 1, smtp));
	}

	@Test
	@DisplayName("Context-Builder setzen die Werte und diese werden richtig ausgelesen")
	void testBuilderAndGetters() {
		// Neue Werte setzen und Gleichheit bei Rückgabe prüfen
		final EmailJobManagerContext self1 = emailJobManagerContext.withMaxEmailsPerMinute(10);
		assertSame(emailJobManagerContext, self1);
		final EmailJobManagerContext self2 = emailJobManagerContext.withTimeToKeepCompletedJobs(5000L);
		assertSame(emailJobManagerContext, self2);
		final EmailJobManagerContext self3 = emailJobManagerContext.withMaxAttachmentSize(2048L);
		assertSame(emailJobManagerContext, self3);
		final EmailJobManagerContext self4 = emailJobManagerContext.withForceMaxAttachmentSize(true);
		assertSame(emailJobManagerContext, self4);

		// Neue Werte prüfen
		assertEquals(10, emailJobManagerContext.getMaxEmailsPerMinute());
		assertEquals(5000L, emailJobManagerContext.getTimeToKeepCompletedJobs());
		assertEquals(2048L, emailJobManagerContext.getMaxAttachmentSize());
		assertTrue(emailJobManagerContext.isForceMaxAttachmentSize());
	}

	@Test
	@DisplayName("RateLimitTimeframe mit Wert unter 2000ms ändert Minimum nicht")
	void testRateLimitTimeframeMinimum() {
		// Wert bei Initialisierung ist 60000ms. Setzen unter 2000ms sollte Wert nicht ändern.
		emailJobManagerContext.withRateLimitTimeframeMs(500L);
		assertEquals(60000L, emailJobManagerContext.getRateLimitTimeframeMs(), "Werte unter 2000ms sollten Wert nicht ändern");
		// Wert über 2000ms muss gesetzt werden.
		emailJobManagerContext.withRateLimitTimeframeMs(5000L);
		assertEquals(5000L, emailJobManagerContext.getRateLimitTimeframeMs());
	}

	@Test
	@DisplayName("MaxEmailsPerMinute-Methode muss beim Setzen es negativen Wert den zuvor gesetzt Wert zurückgegeben.")
	void testPositiveTimeToKeepCompletedJobs() {
		// Wert bei Initialisierung ist 60000
		assertEquals(60000, emailJobManagerContext.getTimeToKeepCompletedJobs());
		// Negativer Wert darf 20 nicht ändern.
		emailJobManagerContext.withTimeToKeepCompletedJobs(-10000);
		assertEquals(60000, emailJobManagerContext.getTimeToKeepCompletedJobs());
		// Positiver Wert muss angenommen werden.
		emailJobManagerContext.withTimeToKeepCompletedJobs(30000);
		assertEquals(30000, emailJobManagerContext.getTimeToKeepCompletedJobs());
		// 0 darf den vorherigen Wert nicht ändern.
		emailJobManagerContext.withTimeToKeepCompletedJobs(0);
		assertEquals(30000, emailJobManagerContext.getTimeToKeepCompletedJobs());
	}

	@Test
	@DisplayName("MaxEmailsPerMinute-Methode muss beim Setzen es negativen Wert den zuvor gesetzt Wert zurückgegeben.")
	void testPositiveMaxEmailsPerMinute() {
		// Wert bei Initialisierung ist 20
		assertEquals(20, emailJobManagerContext.getMaxEmailsPerMinute());
		// Negativer Wert darf 20 nicht ändern.
		emailJobManagerContext.withMaxEmailsPerMinute(-10);
		assertEquals(20, emailJobManagerContext.getMaxEmailsPerMinute());
		// Positiver Wert muss angenommen werden.
		emailJobManagerContext.withMaxEmailsPerMinute(30);
		assertEquals(30, emailJobManagerContext.getMaxEmailsPerMinute());
		// 0 darf den vorherigen Wert nicht ändern.
		emailJobManagerContext.withMaxEmailsPerMinute(0);
		assertEquals(30, emailJobManagerContext.getMaxEmailsPerMinute());
		// 1 muss als kleinster Wert angenommen werden.
		emailJobManagerContext.withMaxEmailsPerMinute(1);
		assertEquals(1, emailJobManagerContext.getMaxEmailsPerMinute());
	}

	@Test
	@DisplayName("MaxAttachmentSize-Methode muss beim Setzen es negativen Wert den zuvor gesetzt Wert zurückgegeben.")
	void testNonNegativeMaxAttachmentSize() {
		// Wert bei Initialisierung ist 0
		assertEquals(0L, emailJobManagerContext.getMaxAttachmentSize());
		// Negativer Wert darf 0 nicht ändern.
		emailJobManagerContext.withMaxAttachmentSize(-1024L);
		assertEquals(0L, emailJobManagerContext.getMaxAttachmentSize());
		// Positiver Wert muss angenommen werden.
		emailJobManagerContext.withMaxAttachmentSize(2048L);
		assertEquals(2048L, emailJobManagerContext.getMaxAttachmentSize());
		// Negativer Wert darf den vorherigen Wert nicht ändern.
		emailJobManagerContext.withMaxAttachmentSize(-1024L);
		assertEquals(2048L, emailJobManagerContext.getMaxAttachmentSize());
		// Das Setzen von 0 muss 0 ergeben.
		emailJobManagerContext.withMaxAttachmentSize(0L);
		assertEquals(0L, emailJobManagerContext.getMaxAttachmentSize());
	}

}
