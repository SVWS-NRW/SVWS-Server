package de.svws_nrw.controller.wiedervorlage;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class WiedervorlageControllerFactoryTest {

	@Test
	@DisplayName("withReadAccess | Controller wird erstellt und DBBenutzerUtils mit KEINE aufgerufen")
	void withReadAccess() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			final var factory = WiedervorlageControllerFactory.withReadAccess(request);

			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class), eq(BenutzerKompetenz.KEINE)), times(1));
			assertNotNull(factory);

			final var controller = factory.getWiedervorlageController();
			assertNotNull(controller);
		}
	}

	@Test
	@DisplayName("withWriteAccess | Controller wird erstellt und DBBenutzerUtils mit ADMIN aufgerufen")
	void withWriteAccess() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			final var factory = WiedervorlageControllerFactory.withWriteAccess(request);

			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class), eq(BenutzerKompetenz.KEINE)), times(1));
			assertNotNull(factory);

			final var controller = factory.getWiedervorlageController();
			assertNotNull(controller);
		}
	}

	@Test
	@DisplayName("withDeleteAccess | Controller wird erstellt und DBBenutzerUtils mit ADMIN aufgerufen")
	void withDeleteAccess() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			final var factory = WiedervorlageControllerFactory.withDeleteAccess(request);

			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class), eq(BenutzerKompetenz.KEINE)), times(1));
			assertNotNull(factory);

			final var controller = factory.getWiedervorlageController();
			assertNotNull(controller);
		}
	}
}
