package de.svws_nrw.controller.schule.logoverwaltung;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.mapper.schule.logoverwaltung.LogoverwaltungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.logoverwaltung.LogoverwaltungService;
import de.svws_nrw.service.schule.logoverwaltung.LogoverwaltungServiceFactory;
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
class LogoverwaltungControllerFactoryTest {

	@Mock
	private LogoverwaltungServiceFactory cut;

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<LogoverwaltungRepositoryFactory> repositoryFactoryMock;
	private MockedStatic<LogoverwaltungServiceFactory> serviceFactoryMock;
	private MockedStatic<EigeneSchuleServiceFactory> schuleServiceFactoryMock;
	private MockedStatic<EigeneSchuleRepositoryFactory> schuleRepositoryFactoryMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		repositoryFactoryMock = mockStatic(LogoverwaltungRepositoryFactory.class);
		serviceFactoryMock = mockStatic(LogoverwaltungServiceFactory.class);
		schuleServiceFactoryMock = mockStatic(EigeneSchuleServiceFactory.class);
		schuleRepositoryFactoryMock = mockStatic(EigeneSchuleRepositoryFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		repositoryFactoryMock.close();
		serviceFactoryMock.close();
		schuleServiceFactoryMock.close();
		schuleRepositoryFactoryMock.close();
	}

	@Test
	@DisplayName("withReadAccess | Erfolg")
	void withReadAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repositoryFactory = mock(LogoverwaltungRepositoryFactory.class);
		final var serviceFactory = mock(LogoverwaltungServiceFactory.class);
		final var schuleRepositoryFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(EigeneSchuleServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN))
				.thenReturn(dbConnection);
		repositoryFactoryMock.when(LogoverwaltungRepositoryFactory::getNewInstance)
				.thenReturn(repositoryFactory);
		schuleRepositoryFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance)
				.thenReturn(schuleRepositoryFactory);
		schuleServiceFactoryMock.when(() -> EigeneSchuleServiceFactory.getNewInstance(schuleRepositoryFactory))
				.thenReturn(schuleServiceFactory);
		serviceFactoryMock.when(() -> LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, LogoverwaltungMapper.INSTANCE, schuleServiceFactory))
				.thenReturn(serviceFactory);

		final var factory = LogoverwaltungControllerFactory.withReadAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(LogoverwaltungControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN), times(1));
		repositoryFactoryMock.verify(LogoverwaltungRepositoryFactory::getNewInstance, times(1));
		serviceFactoryMock.verify(() -> LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, LogoverwaltungMapper.INSTANCE, schuleServiceFactory),
				times(1));
	}

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repositoryFactory = mock(LogoverwaltungRepositoryFactory.class);
		final var serviceFactory = mock(LogoverwaltungServiceFactory.class);
		final var schuleRepositoryFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(EigeneSchuleServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN))
				.thenReturn(dbConnection);
		repositoryFactoryMock.when(LogoverwaltungRepositoryFactory::getNewInstance)
				.thenReturn(repositoryFactory);
		schuleRepositoryFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance)
				.thenReturn(schuleRepositoryFactory);
		schuleServiceFactoryMock.when(() -> EigeneSchuleServiceFactory.getNewInstance(schuleRepositoryFactory))
				.thenReturn(schuleServiceFactory);
		serviceFactoryMock.when(() -> LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, LogoverwaltungMapper.INSTANCE, schuleServiceFactory))
				.thenReturn(serviceFactory);

		final var factory = LogoverwaltungControllerFactory.withWriteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(LogoverwaltungControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN), times(1));
		repositoryFactoryMock.verify(LogoverwaltungRepositoryFactory::getNewInstance, times(1));
		serviceFactoryMock.verify(() -> LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, LogoverwaltungMapper.INSTANCE, schuleServiceFactory),
				times(1));
	}

	@Test
	@DisplayName("withDeleteAccess | Erfolg")
	void withDeleteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repositoryFactory = mock(LogoverwaltungRepositoryFactory.class);
		final var serviceFactory = mock(LogoverwaltungServiceFactory.class);
		final var schuleRepositoryFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(EigeneSchuleServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN))
				.thenReturn(dbConnection);
		repositoryFactoryMock.when(LogoverwaltungRepositoryFactory::getNewInstance)
				.thenReturn(repositoryFactory);
		schuleRepositoryFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance)
				.thenReturn(schuleRepositoryFactory);
		schuleServiceFactoryMock.when(() -> EigeneSchuleServiceFactory.getNewInstance(schuleRepositoryFactory))
				.thenReturn(schuleServiceFactory);
		serviceFactoryMock.when(() -> LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, LogoverwaltungMapper.INSTANCE, schuleServiceFactory))
				.thenReturn(serviceFactory);

		final var factory = LogoverwaltungControllerFactory.withDeleteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(LogoverwaltungControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN), times(1));
		repositoryFactoryMock.verify(LogoverwaltungRepositoryFactory::getNewInstance, times(1));
		serviceFactoryMock.verify(() -> LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, LogoverwaltungMapper.INSTANCE, schuleServiceFactory),
				times(1));
	}

	@Test
	@DisplayName("getController | gibt einen LogoverwaltungController zurück")
	void getController() {
		final var repositoryFactory = mock(LogoverwaltungRepositoryFactory.class);
		final var serviceFactory = mock(LogoverwaltungServiceFactory.class);
		final var schuleRepositoryFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(EigeneSchuleServiceFactory.class);
		final var service = mock(LogoverwaltungService.class);

		repositoryFactoryMock.when(LogoverwaltungRepositoryFactory::getNewInstance)
				.thenReturn(repositoryFactory);
		schuleRepositoryFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance)
				.thenReturn(schuleRepositoryFactory);
		schuleServiceFactoryMock.when(() -> EigeneSchuleServiceFactory.getNewInstance(schuleRepositoryFactory))
				.thenReturn(schuleServiceFactory);
		serviceFactoryMock.when(() -> LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, LogoverwaltungMapper.INSTANCE, schuleServiceFactory))
				.thenReturn(serviceFactory);
		when(serviceFactory.getService()).thenReturn(service);

		final var controller = LogoverwaltungControllerFactory
				.withReadAccess(request)
				.getController();

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(LogoverwaltungController.class);
		verify(serviceFactory, times(1)).getService();
	}

}
