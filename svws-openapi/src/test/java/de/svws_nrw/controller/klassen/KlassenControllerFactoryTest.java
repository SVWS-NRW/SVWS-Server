package de.svws_nrw.controller.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.withSettings;

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
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.DbConnectionProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;

/**
 * Test für die Klasse {@link KlassenControllerFactory}
 */
@ExtendWith(MockitoExtension.class)
class KlassenControllerFactoryTest {

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<DbConnectionProvider> dbConnectionProviderMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		dbConnectionProviderMock = mockStatic(DbConnectionProvider.class, withSettings().lenient());
		final DBEntityManager conn = mock(DBEntityManager.class);
		dbConnectionProviderMock.when(DbConnectionProvider::getConnection).thenReturn(conn);
	}

	@AfterEach
	void tearDown() {
		dbConnectionProviderMock.close();
		dbBenutzerUtilsMock.close();
	}

	@Test
	@DisplayName("withReadAccess | Factory wird erstellt und DBBenutzerUtils mit UNTERRICHTSVERTEILUNG_ANSEHEN aufgerufen")
	void withReadAccess() {
		final var factory = KlassenControllerFactory.withReadAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN)), times(1));
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("withNoAccess | Factory wird erstellt und DBBenutzerUtils mit KEINE aufgerufen")
	void withNoAccess() {
		final var factory = KlassenControllerFactory.withNoAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), any(ServerMode.class), eq(BenutzerKompetenz.KEINE)), times(1));
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("withReadAccess | Fehlende Berechtigung: Die Exception von DBBenutzerUtils wird weitergegeben")
	void withReadAccess_forbidden() {
		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(any(HttpServletRequest.class), any(ServerMode.class), any(BenutzerKompetenz.class)))
				.thenThrow(new ApiOperationException(Response.Status.FORBIDDEN, "Keine Berechtigung."));

		assertThatThrownBy(() -> KlassenControllerFactory.withReadAccess(request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.FORBIDDEN));
	}

	@Test
	@DisplayName("getKlassenController | Gibt einen Controller zurück")
	void getKlassenController() {
		final var factory = KlassenControllerFactory.withReadAccess(request);

		assertThat(factory.getKlassenController()).isNotNull().isInstanceOf(KlassenControllerImpl.class);
	}

}
