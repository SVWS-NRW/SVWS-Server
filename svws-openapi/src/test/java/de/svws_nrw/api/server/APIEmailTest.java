package de.svws_nrw.api.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Prüft die Rechte für die SMTP-Server-Konfiguration. Ändern darf sie nur ein Admin, weil sie bestimmt, an welchen Server
 * alle Benutzer ihre Zugangsdaten senden. Lesen darf sie, wer Schuldaten ansehen darf.
 */
class APIEmailTest {

	@Test
	@DisplayName("Das Ändern der SMTP-Server-Konfiguration verlangt das Admin-Recht")
	void testPatchVerlangtAdmin() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class)) {
			new APIEmail().patchSMTPServerKonfiguration("schema", new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)), request);
			dbUtilsMock.verify(() -> DBBenutzerUtils.runWithTransaction(any(), eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.ADMIN)));
		}
	}

	@Test
	@DisplayName("Das Lesen der SMTP-Server-Konfiguration verlangt nur das Recht, Schuldaten anzusehen")
	void testGetVerlangtSchuldatenAnsehen() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class)) {
			new APIEmail().getSMTPServerKonfiguration("schema", request);
			dbUtilsMock.verify(() -> DBBenutzerUtils.runWithTransaction(any(), eq(request), eq(ServerMode.STABLE),
					eq(BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN)));
		}
	}

}
