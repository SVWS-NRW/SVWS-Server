package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MailSmtpSessionTests {

    @Test
    @DisplayName("Session-Erstellung mit gültiger Konfiguration")
    void testSessionCreationWithValidConfig() {
        final MailSmtpSessionConfig config = new MailSmtpSessionConfig("localhost", "user", "pass");
        assertDoesNotThrow(() -> new MailSmtpSession(config));
    }

}
