package de.svws_nrw.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.jboss.resteasy.core.ResteasyContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Testet den Umgang mit dem Resteasy-Context in dem DBConnectionProvider
 */
@ExtendWith(MockitoExtension.class)
class DbConnectionProviderTest {

	@Mock
	private HttpServletRequest mockedRequest;

	@Mock
	private DBEntityManager mockedConn;

	@AfterEach
	void tearDown() {
		// Sicherstellen, dass der Thread-Local Kontext für andere Tests leer ist
		ResteasyContext.clearContextData();
	}

	@Test
	@DisplayName("Die Verbindung wird Erfolgreich aus dem Request extrahiert")
	void testGetConnectionSuccess() {
		// Der aktuelle Thread muss den Request im aktuellen Context haben
		ResteasyContext.pushContext(HttpServletRequest.class, mockedRequest);
		when(mockedRequest.getAttribute("connection")).thenReturn(mockedConn);

		final DBEntityManager result = DbConnectionProvider.getConnection();

		assertNotNull(result);
		assertEquals(mockedConn, result);
	}

	@Test
	@DisplayName("Die Verbindung kann nicht aus dem vorhandenen Request extrahiert werden (Attribut 'connection' fehlt oder ist vom falschen Typ)")
	void testGetConnectionNoAttribute() {
		// Der aktuelle Thread muss den Request im aktuellen Context haben
		ResteasyContext.pushContext(HttpServletRequest.class, mockedRequest);

		// Fall 1: Das Attribut "connection" wurde nicht gesetzt
		when(mockedRequest.getAttribute("connection")).thenReturn(null);
		assertThrows(IllegalStateException.class, DbConnectionProvider::getConnection,
				"Bei einem fehlenden Attribut 'connection', sollte eine IllegalStateException geworfen werden.");

		// Fall 2: Das Attribut "connection" hat einen falschen Typ (nicht DBEntityManager)
		when(mockedRequest.getAttribute("connection")).thenReturn("Ein String ist hier ein falscher Typ");
		final IllegalStateException exception = assertThrows(IllegalStateException.class, DbConnectionProvider::getConnection,
				"Bei einem Fehlerhaften Typ für das Attribut 'connection', sollte eine IllegalStateException geworfen werden.");

		assertEquals("Es konnte keine Datenbank-Verbindung für den aktuellen Request ausgelesen werden.", exception.getMessage());
	}

	@Test
	@DisplayName("Im RESTEasy-Kontext ist kein aktueller Request hinterlegt.")
	void testGetConnectionNoRequest() {
		// Es wird kein Request im Kontext hinterlegt
		final IllegalStateException exception = assertThrows(IllegalStateException.class,
				DbConnectionProvider::getConnection);

		assertEquals("Es konnte kein aktueller Request ausgelesen werden.", exception.getMessage());
	}

}
