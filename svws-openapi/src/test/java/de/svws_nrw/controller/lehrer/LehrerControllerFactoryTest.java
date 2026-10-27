package de.svws_nrw.controller.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.controller.lehrer.anrechnung.LehrerAnrechnungsstundenController;
import de.svws_nrw.controller.lehrer.einwilligung.LehrerEinwilligungController;
import de.svws_nrw.controller.lehrer.fachrichtung.LehrerFachrichtungController;
import de.svws_nrw.controller.lehrer.funktion.LehrerFunktionController;
import de.svws_nrw.controller.lehrer.lehramt.LehrerLehramtController;
import de.svws_nrw.controller.lehrer.lehrbefaehigung.LehrerLehrbefaehigungController;
import de.svws_nrw.controller.lehrer.lernplattform.LehrerLernplattformController;
import de.svws_nrw.controller.lehrer.mehrleistung.LehrerMehrleistungController;
import de.svws_nrw.controller.lehrer.minderleistung.LehrerMinderleistungController;
import de.svws_nrw.controller.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenController;
import de.svws_nrw.controller.lehrer.personaldaten.LehrerPersonaldatenController;
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
		final var factory = LehrerControllerFactory.withReadAccessStable(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withWriteAccess | DBBenutzerUtils wird mit STABLE und LEHRER_PERSONALDATEN_AENDERN aufgerufen")
	void withWriteAccess() {
		final var factory = LehrerControllerFactory.withWriteAccessStable(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.STABLE), eq(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withReadAccessDev | DBBenutzerUtils wird mit DEV und LEHRERDATEN_ANSEHEN aufgerufen")
	void withReadAccessDev() {
		final var factory = LehrerControllerFactory.withReadAccessDev(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.DEV), eq(BenutzerKompetenz.LEHRERDATEN_ANSEHEN)), times(1));
		assertNotNull(factory);
	}

	@Test
	@DisplayName("withWriteAccessDev | DBBenutzerUtils wird mit DEV und LEHRERDATEN_AENDERN aufgerufen")
	void withWriteAccessDev() {
		final var factory = LehrerControllerFactory.withWriteAccessDev(request);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				eq(request), eq(ServerMode.DEV), eq(BenutzerKompetenz.LEHRERDATEN_AENDERN)), times(1));
		assertNotNull(factory);
	}

	// -------------------------------------------------------------------------
	// Controller-Getter
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getLehrerUnterrichtsfachController | gibt einen Controller zurück")
	void getLehrerUnterrichtsfachController() {
		final LehrerUnterrichtsfachController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerUnterrichtsfachController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerAnrechnungsstundenController | gibt einen Controller zurück")
	void getLehrerAnrechnungsstundenController() {
		final LehrerAnrechnungsstundenController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerAnrechnungsstundenController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerMehrleistungController | gibt einen Controller zurück")
	void getLehrerMehrleistungController() {
		final LehrerMehrleistungController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerMehrleistungController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerMinderleistungController | gibt einen Controller zurück")
	void getLehrerMinderleistungController() {
		final LehrerMinderleistungController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerMinderleistungController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerPersonalabschnittsdatenController | gibt einen Controller zurück")
	void getLehrerPersonalabschnittsdatenController() {
		final LehrerPersonalabschnittsdatenController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerPersonalabschnittsdatenController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerFunktionController | gibt einen Controller zurück")
	void getLehrerFunktionController() {
		final LehrerFunktionController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerFunktionController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerFachrichtungController | gibt einen Controller zurück")
	void getLehrerFachrichtungController() {
		final LehrerFachrichtungController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerFachrichtungController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerLehrbefaehigungController | gibt einen Controller zurück")
	void getLehrerLehrbefaehigungController() {
		final LehrerLehrbefaehigungController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerLehrbefaehigungController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerLehramtController | gibt einen Controller zurück")
	void getLehrerLehramtController() {
		final LehrerLehramtController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerLehramtController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerPersonaldatenController | gibt einen Controller zurück")
	void getLehrerPersonaldatenController() {
		final LehrerPersonaldatenController controller = LehrerControllerFactory
				.withReadAccessStable(request)
				.getLehrerPersonaldatenController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerLernplattformController | gibt einen Controller zurück")
	void getLehrerLernplattformController() {
		final LehrerLernplattformController controller = LehrerControllerFactory
				.withReadAccessDev(request)
				.getLehrerLernplattformController();

		assertNotNull(controller);
	}

	@Test
	@DisplayName("getLehrerEinwilligungController | gibt einen Controller zurück")
	void getLehrerEinwilligungController() {
		final LehrerEinwilligungController controller = LehrerControllerFactory
				.withReadAccessDev(request)
				.getLehrerEinwilligungController();

		assertNotNull(controller);
	}

}
