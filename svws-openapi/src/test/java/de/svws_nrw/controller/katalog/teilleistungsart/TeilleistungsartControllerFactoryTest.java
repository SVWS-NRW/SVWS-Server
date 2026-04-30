package de.svws_nrw.controller.katalog.teilleistungsart;

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
class TeilleistungsartControllerFactoryTest {

	@Test
	@DisplayName("withDeleteAccess | success")
	void testWithDeleteAccess() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			final BenutzerKompetenz kompetenz = BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN;
			final var factory = TeilleistungsartControllerFactory.withDeleteAccess(request);

			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class), eq(kompetenz)), times(1));
			assertNotNull(factory);

			final var controller = factory.getTeilLeistungsartenController();
			assertNotNull(controller);

		}
	}

	@Test
	@DisplayName("withReadAccess | success")
	void testWithReadAccess() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			final BenutzerKompetenz kompetenz = BenutzerKompetenz.KEINE;
			final var factory = TeilleistungsartControllerFactory.withReadAccess(request);

			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class), eq(kompetenz)), times(1));
			assertNotNull(factory);

			final var controller = factory.getTeilLeistungsartenController();
			assertNotNull(controller);

		}
	}

	@Test
	@DisplayName("withWriteAccess | success")
	void testWithWriteAccess() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			final BenutzerKompetenz kompetenz = BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN;
			final var factory = TeilleistungsartControllerFactory.withWriteAccess(request);

			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class), eq(kompetenz)), times(1));
			assertNotNull(factory);

			final var controller = factory.getTeilLeistungsartenController();
			assertNotNull(controller);

		}
	}

}
