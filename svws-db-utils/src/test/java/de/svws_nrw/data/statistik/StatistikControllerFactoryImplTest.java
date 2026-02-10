package de.svws_nrw.data.statistik;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.DbConnectionProvider;

/**
 * Testklasse für die Implementierung der StatistikControllerFactory.
 */
class StatistikControllerFactoryImplTest {

	@Test
	@DisplayName("Test: getControllerStatistikGesamt erzeugt eine korrekte Instanz von StatistikControllerImpl")
	void testGetControllerStatistikGesamt() throws ApiOperationException {
		// Wir mocken RepositorySupport statisch, um eine IllegalStateException im Konstruktor zu verhindern
		try (MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			// Simuliere die Bereitstellung einer DB-Verbindung
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			// Test für das Erzeugen der Controller-Factory
			final StatistikControllerFactoryImpl factory = new StatistikControllerFactoryImpl();

			// Erzeuge den konkreten Controller mithilfe der Factory
			final StatistikController controller = factory.getControllerStatistikGesamt();

			// Prüfe das Ergebnis
			assertNotNull(controller, "Der erzeugte Controller darf nicht null sein.");
			assertTrue(controller instanceof StatistikControllerImpl, "Der Controller muss eine Instanz von StatistikControllerImpl sein.");
		}
	}

}
