package de.svws_nrw.controller.oauth;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.Benutzer;
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
class OAuthCredentialControllerFactoryTest {

	@Test
	@DisplayName("getNewInstance | Controller wird erstellt und DBBenutzerUtils mit Kompetenz aufgerufen")
	void getNewInstance() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> connectionProviderMock = mockStatic(DbConnectionProvider.class)) {
			final DBEntityManager conn = mock(DBEntityManager.class);
			connectionProviderMock.when(DbConnectionProvider::getConnection).thenReturn(conn);
			final Benutzer benutzer = mock(Benutzer.class);
			dbUtilsMock.when(() -> DBBenutzerUtils.getSVWSUser(eq(request), eq(ServerMode.DEV), eq(BenutzerKompetenz.KEINE))).thenReturn(benutzer);

			final var factory = OAuthCredentialControllerFactory.getNewInstance(request);

			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), eq(ServerMode.DEV), any(BenutzerKompetenz[].class)), times(1));
			assertNotNull(factory);

			final var controller = factory.getCredentialController();
			assertNotNull(controller);
		}
	}
}
