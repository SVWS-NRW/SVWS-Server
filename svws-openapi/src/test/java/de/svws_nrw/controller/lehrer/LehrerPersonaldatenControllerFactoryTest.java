package de.svws_nrw.controller.lehrer;

import de.svws_nrw.asd.utils.CoreTypeDataManager;
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
class LehrerPersonaldatenControllerFactoryTest {

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
	@DisplayName("withReadAccess | Factory wird erstellt und DBBenutzerUtils mit LEHRER_PERSONALDATEN_ANSEHEN aufgerufen")
	void withReadAccess() {
		final var factory = LehrerPersonaldatenControllerFactory.withReadAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withWriteAccess | Factory wird erstellt und DBBenutzerUtils mit LEHRER_PERSONALDATEN_AENDERN aufgerufen")
	void withWriteAccess() {
		final var factory = LehrerPersonaldatenControllerFactory.withWriteAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withReadAccess | getLehrerUnterrichtsfachController gibt einen Controller zurück")
	void getLehrerUnterrichtsfachController() {
		final var factory = LehrerPersonaldatenControllerFactory.withReadAccess(request);
		assertNotNull(factory);

		final var controller = factory.getLehrerUnterrichtsfachController();
		assertNotNull(controller);
	}

	@Test
	@DisplayName("withReadAccess | getLehrerAnrechnungsstundenController gibt einen Controller zurück")
	void getLehrerAnrechnungsstundenController() {
		final var factory = LehrerPersonaldatenControllerFactory.withReadAccess(request);
		assertNotNull(factory);

		final var controller = factory.getLehrerAnrechnungsstundenController();
		assertNotNull(controller);
	}

	@Test
	@DisplayName("withReadAccess | getLehrerMehrleistungController gibt einen Controller zurück")
	void getLehrerMehrleistungController() {
		final var factory = LehrerPersonaldatenControllerFactory.withReadAccess(request);
		assertNotNull(factory);

		final var controller = factory.getLehrerMehrleistungController();
		assertNotNull(controller);
	}

	@Test
	@DisplayName("withReadAccess | getLehrerMinderleistungController gibt einen Controller zurück")
	void getLehrerMinderleistungController() {
		try (MockedStatic<CoreTypeDataManager> ignored = mockStatic(CoreTypeDataManager.class)) {
			final var factory = LehrerPersonaldatenControllerFactory.withReadAccess(request);
			assertNotNull(factory);

			final var controller = factory.getLehrerMinderleistungController();
			assertNotNull(controller);
		}
	}
}
