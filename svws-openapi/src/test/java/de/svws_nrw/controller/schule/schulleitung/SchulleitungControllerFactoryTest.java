package de.svws_nrw.controller.schule.schulleitung;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.schulleitung.SchulleitungService;
import de.svws_nrw.service.schule.schulleitung.SchulleitungServiceFactory;
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
class SchulleitungControllerFactoryTest {

	@Mock
	private SchulleitungServiceFactory serviceFactory;

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<EigeneSchuleRepositoryFactory> schuleRepoFactoryMock;
	private MockedStatic<LehrerRepositoryFactory> lehreRepoFactoryMock;
	private MockedStatic<SchulleitungServiceFactory> serviceFactoryStaticMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		schuleRepoFactoryMock = mockStatic(EigeneSchuleRepositoryFactory.class);
		lehreRepoFactoryMock = mockStatic(LehrerRepositoryFactory.class);
		serviceFactoryStaticMock = mockStatic(SchulleitungServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		schuleRepoFactoryMock.close();
		lehreRepoFactoryMock.close();
		serviceFactoryStaticMock.close();
	}

	private void mockInfrastruktur(final EigeneSchuleRepositoryFactory schuleRepoFactory,
			final LehrerRepositoryFactory lehrerRepoFactory,
			final SchulleitungServiceFactory mockedServiceFactory) {
		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_AENDERN))
				.thenReturn(mock(DBEntityManager.class));
		schuleRepoFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance)
				.thenReturn(schuleRepoFactory);
		lehreRepoFactoryMock.when(LehrerRepositoryFactory::getNewInstance)
				.thenReturn(lehrerRepoFactory);
		serviceFactoryStaticMock.when(() -> SchulleitungServiceFactory.getNewInstance(schuleRepoFactory, lehrerRepoFactory, SchulleitungMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);
	}

	@Test
	@DisplayName("withReadAccess | Erfolg")
	void withReadAccess_success() {
		final var schuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var lehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var mockedServiceFactory = mock(SchulleitungServiceFactory.class);

		mockInfrastruktur(schuleRepoFactory, lehrerRepoFactory, mockedServiceFactory);

		final var factory = SchulleitungControllerFactory.withReadAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulleitungControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_ANSEHEN), times(1));
		schuleRepoFactoryMock.verify(EigeneSchuleRepositoryFactory::getNewInstance, times(1));
		lehreRepoFactoryMock.verify(LehrerRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> SchulleitungServiceFactory.getNewInstance(schuleRepoFactory, lehrerRepoFactory, SchulleitungMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var schuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var lehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var mockedServiceFactory = mock(SchulleitungServiceFactory.class);

		mockInfrastruktur(schuleRepoFactory, lehrerRepoFactory, mockedServiceFactory);

		final var factory = SchulleitungControllerFactory.withWriteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulleitungControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_AENDERN), times(1));
		schuleRepoFactoryMock.verify(EigeneSchuleRepositoryFactory::getNewInstance, times(1));
		lehreRepoFactoryMock.verify(LehrerRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> SchulleitungServiceFactory.getNewInstance(schuleRepoFactory, lehrerRepoFactory, SchulleitungMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("withDeleteAccess | Erfolg")
	void withDeleteAccess_success() {
		final var schuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var lehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var mockedServiceFactory = mock(SchulleitungServiceFactory.class);

		mockInfrastruktur(schuleRepoFactory, lehrerRepoFactory, mockedServiceFactory);

		final var factory = SchulleitungControllerFactory.withDeleteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulleitungControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_LOESCHEN), times(1));
		schuleRepoFactoryMock.verify(EigeneSchuleRepositoryFactory::getNewInstance, times(1));
		lehreRepoFactoryMock.verify(LehrerRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> SchulleitungServiceFactory.getNewInstance(schuleRepoFactory, lehrerRepoFactory, SchulleitungMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("getSchulleitungController | Erfolg")
	void getSchulleitungController_success() {
		final var schulleitungService = mock(SchulleitungService.class);
		final var factory = new SchulleitungControllerFactory(serviceFactory);

		when(serviceFactory.getSchulleitungService()).thenReturn(schulleitungService);

		final var controller = factory.getSchulleitungController();

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(SchulleitungController.class);

		verify(serviceFactory, times(1)).getSchulleitungService();
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var factory = new SchulleitungControllerFactory(serviceFactory);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulleitungControllerFactory.class);
	}
}
