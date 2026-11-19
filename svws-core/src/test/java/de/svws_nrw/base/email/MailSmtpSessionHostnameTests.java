package de.svws_nrw.base.email;

import java.net.InetAddress;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prüft, dass weder die Begrüßung beim Mailserver noch die Message-ID den internen Hostnamen des SVWS-Servers nennt.
 * Beide nennen die Domain des Absenders.
 */
class MailSmtpSessionHostnameTests {

	private static final Duration TESTDAUER_MAX = Duration.ofSeconds(20);

	private static SmtpTestServer sende(final String absender) throws Exception {
		final SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL);
		final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
		config.setPort(server.getPort());
		config.setTimeout(5000);
		final MailSmtpSession session = new MailSmtpSession(config);
		assertTimeoutPreemptively(TESTDAUER_MAX,
				() -> assertDoesNotThrow(() -> session.sendTextMessage(absender, "empfaenger@example.org", "Betreff", "Text")));
		return server;
	}

	private static String ehlo(final SmtpTestServer server) {
		return server.getBefehle().stream().filter(b -> b.toUpperCase(Locale.ROOT).startsWith("EHLO ")).findFirst().orElseThrow();
	}

	private static String messageId(final SmtpTestServer server) {
		final List<String> nachrichten = server.getNachrichten();
		assertEquals(1, nachrichten.size());
		return nachrichten.get(0).lines().filter(z -> z.toLowerCase(Locale.ROOT).startsWith("message-id:")).findFirst().orElseThrow();
	}

	@Test
	@DisplayName("EHLO nennt die Domain des Absenders")
	void testEhloNenntAbsenderdomain() throws Exception {
		try (SmtpTestServer server = sende("absender@schule.example.com")) {
			assertEquals("EHLO schule.example.com", ehlo(server));
		}
	}

	@Test
	@DisplayName("Die Message-ID nennt die Domain des Absenders")
	void testMessageIdNenntAbsenderdomain() throws Exception {
		try (SmtpTestServer server = sende("Schule <absender@schule.example.com>")) {
			final String messageId = messageId(server);
			assertTrue(messageId.matches("(?i)message-id: <[0-9a-f-]{36}@schule\\.example\\.com>"), messageId);
		}
	}

	@Test
	@DisplayName("Der interne Hostname steht weder in EHLO noch in der Message-ID")
	void testKeinInternerHostname() throws Exception {
		final String hostname = InetAddress.getLocalHost().getHostName().toLowerCase(Locale.ROOT);
		final String kanonisch = InetAddress.getLocalHost().getCanonicalHostName().toLowerCase(Locale.ROOT);
		try (SmtpTestServer server = sende("absender@schule.example.com")) {
			for (final String text : List.of(ehlo(server), messageId(server))) {
				assertFalse(text.toLowerCase(Locale.ROOT).contains(hostname), text);
				assertFalse(text.toLowerCase(Locale.ROOT).contains(kanonisch), text);
			}
		}
	}

	@Test
	@DisplayName("Ohne Domain im Absender nennen EHLO und Message-ID ein Adressliteral")
	void testAbsenderOhneDomain() throws Exception {
		try (SmtpTestServer server = sende("absender")) {
			assertEquals("EHLO [127.0.0.1]", ehlo(server));
			assertTrue(messageId(server).endsWith("@[127.0.0.1]>"), messageId(server));
		}
	}

}
