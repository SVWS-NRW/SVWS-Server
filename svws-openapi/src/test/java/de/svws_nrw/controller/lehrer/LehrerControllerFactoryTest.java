package de.svws_nrw.controller.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.controller.lehrer.anrechnung.LehrerAnrechnungsstundenController;
import de.svws_nrw.controller.lehrer.fachrichtung.LehrerFachrichtungController;
import de.svws_nrw.controller.lehrer.funktion.LehrerFunktionController;
import de.svws_nrw.controller.lehrer.mehrleistung.LehrerMehrleistungController;
import de.svws_nrw.controller.lehrer.minderleistung.LehrerMinderleistungController;
import de.svws_nrw.controller.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenController;
import de.svws_nrw.controller.lehrer.unterrichtsfach.LehrerUnterrichtsfachController;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
class LehrerControllerFactoryTest {

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

	@BeforeAll
	static void setupAll() {
		ASDCoreTypeUtils.initAll();
	}

	// -------------------------------------------------------------------------
	// Factory-Methoden: ServerMode + BenutzerKompetenz
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("withReadAccess | DBBenutzerUtils wird mit STABLE und LEHRER_PERSONALDATEN_ANSEHEN aufgerufen")
	void withReadAccess() {
		final var factory = LehrerControllerFactory.withReadAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withWriteAccess | DBBenutzerUtils wird mit STABLE und LEHRER_PERSONALDATEN_AENDERN aufgerufen")
	void withWriteAccess() {
		final var factory = LehrerControllerFactory.withWriteAccess(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN)), times(1));
		assertNotNull(factory);
	}

	// -------------------------------------------------------------------------
	// Controller-Getter
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getLehrerUnterrichtsfachController | gibt einen Controller zurück")
	void getLehrerUnterrichtsfachController() {
		final LehrerUnterrichtsfachController controller = LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerUnterrichtsfachController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerAnrechnungsstundenController | gibt einen Controller zurück")
	void getLehrerAnrechnungsstundenController() {
		final LehrerAnrechnungsstundenController controller = LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerAnrechnungsstundenController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerMehrleistungController | gibt einen Controller zurück")
	void getLehrerMehrleistungController() {
		final LehrerMehrleistungController controller = LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerMehrleistungController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerMinderleistungController | gibt einen Controller zurück")
	void getLehrerMinderleistungController() {
		final LehrerMinderleistungController controller = LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerMinderleistungController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerPersonalabschnittsdatenController | gibt einen Controller zurück")
	void getLehrerPersonalabschnittsdatenController() {
		final LehrerPersonalabschnittsdatenController controller = LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerPersonalabschnittsdatenController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerFunktionController | gibt einen Controller zurück")
	void getLehrerFunktionController() {
		final LehrerFunktionController controller = LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerFunktionController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerFachrichtungController | gibt einen Controller zurück")
	void getLehrerFachrictungController() {
		final LehrerFachrichtungController controller = LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerFachrichtungController();

		assertNotNull(controller);
	}

}
