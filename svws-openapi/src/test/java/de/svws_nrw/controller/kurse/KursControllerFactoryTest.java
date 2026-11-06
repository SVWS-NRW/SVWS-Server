package de.svws_nrw.controller.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Testklasse für die {@link KursControllerFactory}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für die KursControllerFactory")
class KursControllerFactoryTest {

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;

	private MockedStatic<DbConnectionProvider> dbConnectionProviderMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		dbConnectionProviderMock = mockStatic(DbConnectionProvider.class);
		final var conn = mock(DBEntityManager.class);
		dbConnectionProviderMock.when(DbConnectionProvider::getConnection).thenReturn(conn);
	}

	@AfterEach
	void tearDown() {
		dbConnectionProviderMock.close();
		dbBenutzerUtilsMock.close();
	}

	@Test
	@DisplayName("withReadAccess | Die DB-Verbindung wird mit UNTERRICHTSVERTEILUNG_ANSEHEN aufgebaut")
	void withReadAccess() {
		final var factory = KursControllerFactory.withReadAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN)), times(1));
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("withNoAccess | Die DB-Verbindung wird mit KEINE aufgebaut")
	void withNoAccess() {
		final var factory = KursControllerFactory.withNoAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.KEINE)), times(1));
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("getKursController | Es wird ein KursControllerImpl erstellt")
	void getKursController() {
		assertThat(KursControllerFactory.withReadAccess(request).getKursController()).isInstanceOf(KursControllerImpl.class);
	}

}
