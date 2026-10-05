package de.svws_nrw.controller.schulwechsel;

import de.svws_nrw.controller.schulwechsel.abgang.SchulwechselAbgangControllerImpl;
import de.svws_nrw.controller.schulwechsel.dokument.SchulwechselDokumentControllerImpl;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.service.schulwechsel.SchulwechselServiceFactory;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangService;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchulwechselControllerFactoryTest {

	@Mock
	private SchulwechselServiceFactory serviceFactory;

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<SchulwechselServiceFactory> serviceFactoryStaticMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		serviceFactoryStaticMock = mockStatic(SchulwechselServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		serviceFactoryStaticMock.close();
	}

	@Test
	@DisplayName("withReadAccess | Erfolg")
	void withReadAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var mockedServiceFactory = mock(SchulwechselServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV,
				BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE)).thenReturn(dbConnection);
		serviceFactoryStaticMock.when(SchulwechselServiceFactory::getNewInstance)
				.thenReturn(mockedServiceFactory);

		final var factory = SchulwechselControllerFactory.withReadAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulwechselControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV,
				BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE), times(1));
		serviceFactoryStaticMock.verify(SchulwechselServiceFactory::getNewInstance, times(1));
	}

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var mockedServiceFactory = mock(SchulwechselServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV,
				BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE)).thenReturn(dbConnection);
		serviceFactoryStaticMock.when(SchulwechselServiceFactory::getNewInstance)
				.thenReturn(mockedServiceFactory);

		final var factory = SchulwechselControllerFactory.withWriteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulwechselControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV,
				BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE), times(1));
		serviceFactoryStaticMock.verify(SchulwechselServiceFactory::getNewInstance, times(1));
	}

	@Test
	@DisplayName("withDeleteAccess | Erfolg")
	void withDeleteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var mockedServiceFactory = mock(SchulwechselServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV,
				BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE)).thenReturn(dbConnection);
		serviceFactoryStaticMock.when(SchulwechselServiceFactory::getNewInstance)
				.thenReturn(mockedServiceFactory);

		final var factory = SchulwechselControllerFactory.withDeleteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulwechselControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV,
				BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE), times(1));
		serviceFactoryStaticMock.verify(SchulwechselServiceFactory::getNewInstance, times(1));
	}

	@Test
	@DisplayName("getSchulwechselAbgangController | Erfolg")
	void getSchulwechselAbgangController_success() {
		final var service = mock(SchulwechselAbgangService.class);
		final var factory = new SchulwechselControllerFactory(serviceFactory);

		when(serviceFactory.getSchulwechselAbgangService()).thenReturn(service);

		final var controller = factory.getSchulwechselAbgangController();

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(SchulwechselAbgangControllerImpl.class);

		verify(serviceFactory, times(1)).getSchulwechselAbgangService();
	}

	@Test
	@DisplayName("getSchulwechselDokumentController | Erfolg")
	void getSchulwechselDokumentController_success() {
		final var service = mock(SchulwechselDokumentService.class);
		final var factory = new SchulwechselControllerFactory(serviceFactory);

		when(serviceFactory.getSchulwechselDokumentService()).thenReturn(service);

		final var controller = factory.getSchulwechselDokumentController();

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(SchulwechselDokumentControllerImpl.class);

		verify(serviceFactory, times(1)).getSchulwechselDokumentService();
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var factory = new SchulwechselControllerFactory(serviceFactory);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulwechselControllerFactory.class);
	}
}
