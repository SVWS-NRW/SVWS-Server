package de.svws_nrw.controller.schueler;

import de.svws_nrw.controller.schueler.schulbesuch.SchuelerBisherigeSchuleController;
import de.svws_nrw.controller.schueler.schulbesuch.SchuelerMerkmalController;
import de.svws_nrw.controller.schueler.schulbesuch.SchuelerSchulbesuchController;
import de.svws_nrw.controller.schueler.stammdaten.SchuelerStammdatenController;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SchuelerControllerFactoryTest {

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<DbConnectionProvider> dbConnectionProviderMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		dbConnectionProviderMock = mockStatic(DbConnectionProvider.class);

		dbConnectionProviderMock.when(DbConnectionProvider::getConnection)
				.thenReturn(mock(DBEntityManager.class));
	}

	@AfterEach
	void tearDown() {
		dbConnectionProviderMock.close();
		dbBenutzerUtilsMock.close();
	}

	@Test
	@DisplayName("withReadAccess | Factory wird erstellt und DBBenutzerUtils mit SCHUELER_INDIVIDUALDATEN_ANSEHEN aufgerufen")
	void withReadAccess() {
		final var factory = SchuelerControllerFactory.withReadAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withWriteAccess | Factory wird erstellt und DBBenutzerUtils mit SCHUELER_INDIVIDUALDATEN_AENDERN aufgerufen")
	void withWriteAccess() {
		final var factory = SchuelerControllerFactory.withWriteAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withDeleteAccess | Factory wird erstellt und DBBenutzerUtils mit SCHUELER_INDIVIDUALDATEN_AENDERN aufgerufen")
	void withDeleteAccess() {
		final var factory = SchuelerControllerFactory.withDeleteAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.SCHUELER_LOESCHEN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withNoAccess | Factory wird erstellt und DBBenutzerUtils mit KEINE aufgerufen")
	void withNoAccess() {
		final var factory = SchuelerControllerFactory.withNoAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.KEINE)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withReadAccess | getSchuelerBisherigeSchuleController gibt einen Controller zurück")
	void getSchuelerBisherigeSchuleController() {
		final var factory = SchuelerControllerFactory.withReadAccess(request);
		assertNotNull(factory);

		final SchuelerBisherigeSchuleController controller = factory.getSchuelerBisherigeSchuleController();
		assertNotNull(controller);
	}

	@Test
	@DisplayName("withReadAccess | getSchuelerMerkmalController gibt einen Controller zurück")
	void getSchuelerMerkmalController() {
		final var factory = SchuelerControllerFactory.withReadAccess(request);
		assertNotNull(factory);

		final SchuelerMerkmalController controller = factory.getSchuelerMerkmalController();
		assertNotNull(controller);
	}

	@Test
	@DisplayName("withReadAccess | getSchuelerSchulbesuchController gibt einen Controller zurück")
	void getSchuelerSchulbesuchController() {
		final var factory = SchuelerControllerFactory.withReadAccess(request);
		assertNotNull(factory);

		final SchuelerSchulbesuchController controller = factory.getSchuelerSchulbesuchController();
		assertNotNull(controller);
	}

	@Test
	@DisplayName("withReadAccess | getSchuelerStammdatenController gibt einen Controller zurück")
	void getSchuelerStammdatenController() {
		final var factory = SchuelerControllerFactory.withReadAccess(request);
		assertNotNull(factory);

		final SchuelerStammdatenController controller = factory.getSchuelerStammdatenController();
		assertNotNull(controller);
	}
}
