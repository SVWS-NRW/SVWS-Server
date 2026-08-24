package de.svws_nrw.controller.schule.katalog;

import de.svws_nrw.controller.schule.katalog.fachklasse.FachklasseController;
import de.svws_nrw.controller.schule.katalog.merkmal.MerkmalController;
import de.svws_nrw.controller.schule.katalog.ort.OrtController;
import de.svws_nrw.controller.schule.katalog.ortsteil.OrtsteilController;
import de.svws_nrw.controller.schule.katalog.religion.ReligionController;
import de.svws_nrw.controller.schule.katalog.teilleistungsart.TeilleistungsartController;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KatalogControllerFactoryTest {

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

	// -------------------------------------------------------------------------
	// Factory-Methoden: ServerMode + BenutzerKompetenz
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("withReadAccess | DBBenutzerUtils wird mit STABLE und KEINE aufgerufen")
	void withReadAccessDevStable() {
		final var factory = KatalogControllerFactory.withReadAccessStable(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.KEINE)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withWriteAccess | DBBenutzerUtils wird mit STABLE und KATALOG_EINTRAEGE_AENDERN aufgerufen")
	void withWriteAccessDevStable() {
		final var factory = KatalogControllerFactory.withWriteAccessStable(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withDeleteAccess | DBBenutzerUtils wird mit STABLE und KATALOG_EINTRAEGE_LOESCHEN aufgerufen")
	void withDeleteAccessDevStable() {
		final var factory = KatalogControllerFactory.withDeleteAccessStable(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withReadAccessDev | DBBenutzerUtils wird mit DEV und KEINE aufgerufen")
	void withReadAccessDevStableDev() {
		final var factory = KatalogControllerFactory.withReadAccessDev(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.DEV), eq(BenutzerKompetenz.KEINE)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withWriteAccessDev | DBBenutzerUtils wird mit DEV und KATALOG_EINTRAEGE_AENDERN aufgerufen")
	void withWriteAccessDevStableDev() {
		final var factory = KatalogControllerFactory.withWriteAccessDev(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.DEV), eq(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withDeleteAccessDev | DBBenutzerUtils wird mit DEV und KATALOG_EINTRAEGE_LOESCHEN aufgerufen")
	void withDeleteAccessDevStableDev() {
		final var factory = KatalogControllerFactory.withDeleteAccessDev(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.DEV), eq(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)), times(1));
		assertNotNull(factory);
	}

	// -------------------------------------------------------------------------
	// Controller-Getter
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getFachklasseController | gibt einen Controller zurück")
	void getFachklasseController() {
		final FachklasseController controller = KatalogControllerFactory
				.withReadAccessDev(request)
				.getFachklasseController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getMerkmalController | gibt einen Controller zurück")
	void getMerkmalController() {
		final MerkmalController controller = KatalogControllerFactory
				.withReadAccessStable(request)
				.getMerkmalController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getTeilLeistungsartController | gibt einen Controller zurück")
	void getTeilLeistungsartController() {
		final TeilleistungsartController controller = KatalogControllerFactory
				.withReadAccessStable(request)
				.getTeilLeistungsartController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getOrtController | gibt einen Controller zurück")
	void getOrtController() {
		final OrtController controller = KatalogControllerFactory
				.withReadAccessStable(request)
				.getOrtController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getOrtsteilController | gibt einen Controller zurück")
	void getOrtsteilController() {
		final OrtsteilController controller = KatalogControllerFactory
				.withReadAccessStable(request)
				.getOrtsteilController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getReligionController | gibt einen Controller zurück")
	void getReligionController() {
		final ReligionController controller = KatalogControllerFactory
				.withReadAccessStable(request)
				.getReligionController();

		assertNotNull(controller);
	}

}
