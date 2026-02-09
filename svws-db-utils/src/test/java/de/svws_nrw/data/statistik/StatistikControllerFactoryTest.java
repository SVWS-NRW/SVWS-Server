package de.svws_nrw.data.statistik;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.DbConnectionProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;

/**
 * Testklasse für die Default-Methode zu Prüfung der Benutzerauthentifizierung in dem Interface {@link StatistikControllerFactory}.
 */
class StatistikControllerFactoryTest {

	@Test
	@DisplayName("Test: Die Methode getAdmin baut die Datenbank-Verbindung mit der Admin-Kompetenz im Stable-Mode auf und gibt die Factory zurück")
	void testGetAdminSuccess() throws ApiOperationException {
		final HttpServletRequest request = mock(HttpServletRequest.class);

		// Mocking der Klasse DBBenutzerUtils, um den Aufruf von DBBenutzerUtils.getDBConnection abzufangen
		// und der RepositorySupport-Klasse
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			// Simuliere eine erfolgreiche Verbindung (Rückgabe eines Mocks für die Verbindung)
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			// Führe den Test aus ...
			final StatistikControllerFactory result = StatistikControllerFactory.getAdmin(request);

			// ... und prüfe, ob die Factory erfolgreich erstellt wurde
			assertNotNull(result, "Die erzeugte Factory darf nicht null sein.");
			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN), times(1));
		}
	}

	@Test
	@DisplayName("Test: getAdmin reicht Exception bei fehlender Berechtigung weiter")
	void testGetAdminUnauthorized() {
		final HttpServletRequest request = mock(HttpServletRequest.class);

		// Mocking der Klasse DBBenutzerUtils, um den Aufruf von DBBenutzerUtils.getDBConnection abzufangen
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class)) {
			// Wenn DBBenutzerUtils.getDBConnection aufgerufen wird, dann wird eine Exception (403 Forbidden) geworfen
			dbUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(any(), any(), any())).thenThrow(new ApiOperationException(Response.Status.FORBIDDEN));

			// Die Factory muss die ApiOperationException weitergeben
			assertThrows(ApiOperationException.class, () -> StatistikControllerFactory.getAdmin(request),
					"Es wird eine ApiOperationException aufgrund fehlender Rechte erwartet.");
		}
	}

}
