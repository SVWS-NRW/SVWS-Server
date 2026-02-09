package de.svws_nrw.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.jboss.resteasy.core.ResteasyContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class RepositoryFactoryTest {

	@Mock
	private DBEntityManager mockedConn;

	@Mock
	private HttpServletRequest mockedRequest;

	@BeforeEach
	void setup() {
		// RestEasy-Kontext vorbereiten, da RepositorySupport genutzt wird und dieser dafür benötigt wird
		ResteasyContext.pushContext(HttpServletRequest.class, mockedRequest);
		when(mockedRequest.getAttribute("connection")).thenReturn(mockedConn);
	}

	@AfterEach
	void tearDown() {
		ResteasyContext.clearContextData();
	}

	@Test
	@DisplayName("Test: Erstelle eine Factory und erstelle alle Repository-Instanzen einmalig.")
	void testAllRepositoryGetters() {
		// Prüfe, ob eine Factory erfolgreich erstellt werden kann
		final RepositoryFactory factory = new RepositoryFactory() {
			/* anonyme Klasse erzeugen */ };

		assertNotNull(factory);
		assertEquals(mockedConn, factory.conn);
	}

}
