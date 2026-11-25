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
 * Testklasse für die {@link KurslehrerControllerFactory}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für die KurslehrerControllerFactory")
class KurslehrerControllerFactoryTest {

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
	@DisplayName("withNoAccess | Die DB-Verbindung wird mit KEINE aufgebaut")
	void withNoAccess() {
		final var factory = KurslehrerControllerFactory.withNoAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.KEINE)), times(1));
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("withWriteAccessDev | Die DB-Verbindung wird im Modus DEV mit UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN aufgebaut")
	void withWriteAccessDev() {
		final var factory = KurslehrerControllerFactory.withWriteAccessDev(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.DEV), eq(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN)), times(1));
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("withDeleteAccessStable | Die DB-Verbindung wird im Modus STABLE mit KATALOG_EINTRAEGE_LOESCHEN aufgebaut")
	void withDeleteAccessStable() {
		final var factory = KurslehrerControllerFactory.withDeleteAccessStable(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)), times(1));
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("getKurslehrerController | Es wird ein KurslehrerControllerImpl erstellt")
	void getKurslehrerController() {
		assertThat(KurslehrerControllerFactory.withNoAccess(request).getKurslehrerController()).isInstanceOf(KurslehrerControllerImpl.class);
	}

}
