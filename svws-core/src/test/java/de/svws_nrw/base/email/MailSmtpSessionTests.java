package de.svws_nrw.base.email;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailSmtpSessionTests {

    @Test
    @DisplayName("Session-Erstellung mit gültiger Konfiguration")
    void testSessionCreationWithValidConfig() {
        final MailSmtpSessionConfig config = new MailSmtpSessionConfig("localhost", "user", "pass");
        assertDoesNotThrow(() -> new MailSmtpSession(config));
    }

    @Test
    @DisplayName("Eine gültige Adresse mit Umlaut-Domain geht mit der Domain in ASCII-Form und unverändertem lokalem Teil an SMTP")
    void testUmlautDomainNurDomainInAscii() throws Exception {
        final String absender = "lehrer@müller.de";
        final String empfaenger = "max@müller.de";
        assertTrue(EmailUtils.isValidEmail(absender));
        assertTrue(EmailUtils.isValidEmail(empfaenger));
        try (SmtpTestServer server = new SmtpTestServer(SmtpTestServer.Verhalten.NORMAL)) {
            final MailSmtpSessionConfig config = new MailSmtpSessionConfig("127.0.0.1", "user", "pass");
            config.setPort(server.getPort());
            config.setTimeout(5000);
            new MailSmtpSession(config).sendTextMessage(absender, empfaenger, "Betreff", "Text");
            final var befehle = server.getBefehle().stream().map(b -> b.toUpperCase(Locale.ROOT)).toList();
            assertTrue(befehle.contains("MAIL FROM:<LEHRER@XN--MLLER-KVA.DE>"), befehle.toString());
            assertTrue(befehle.contains("RCPT TO:<MAX@XN--MLLER-KVA.DE>"), befehle.toString());
        }
    }

}
