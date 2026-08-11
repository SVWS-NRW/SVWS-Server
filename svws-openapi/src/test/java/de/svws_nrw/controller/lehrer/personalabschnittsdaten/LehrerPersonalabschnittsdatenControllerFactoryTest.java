package de.svws_nrw.controller.lehrer.personalabschnittsdaten;

import de.svws_nrw.controller.lehrer.LehrerPersonalabschnittsdatenController;
import de.svws_nrw.controller.lehrer.LehrerPersonalabschnittsdatenControllerFactory;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.lehrer.LehrerPersonalabschnittsdatenMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.schulen.SchulenRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionServiceFactory;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenService;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
class LehrerPersonalabschnittsdatenControllerFactoryTest {

	@Mock
	private LehrerPersonalabschnittsdatenServiceFactory serviceFactory;

	@Mock
	private HttpServletRequest request;

	@InjectMocks
	private LehrerPersonalabschnittsdatenControllerFactory controllerFactory;

	private MockedStatic<LehrerRepositoryFactory> lehrerRepoFactoryMock;
	private MockedStatic<SchulenRepositoryFactory> schulenRepoFactoryMock;
	private MockedStatic<SchuleRepositoryFactory> schuleRepoFactoryMock;
	private MockedStatic<LehrerServiceFactory> lehrerServiceFactoryMock;
	private MockedStatic<LehrerFunktionServiceFactory> lehrerFunktionServiceFactoryMock;
	private MockedStatic<LehrerPersonalabschnittsdatenServiceFactory> serviceFactoryStaticMock;
	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;

	@BeforeEach
	void setUp() {
		lehrerRepoFactoryMock = mockStatic(LehrerRepositoryFactory.class);
		schulenRepoFactoryMock = mockStatic(SchulenRepositoryFactory.class);
		schuleRepoFactoryMock = mockStatic(SchuleRepositoryFactory.class);
		lehrerServiceFactoryMock = mockStatic(LehrerServiceFactory.class);
		lehrerFunktionServiceFactoryMock = mockStatic(LehrerFunktionServiceFactory.class);
		serviceFactoryStaticMock = mockStatic(LehrerPersonalabschnittsdatenServiceFactory.class);
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
	}

	@AfterEach
	void tearDown() {
		lehrerRepoFactoryMock.close();
		schulenRepoFactoryMock.close();
		schuleRepoFactoryMock.close();
		lehrerServiceFactoryMock.close();
		lehrerFunktionServiceFactoryMock.close();
		serviceFactoryStaticMock.close();
		dbBenutzerUtilsMock.close();
	}

	// -------------------------------------------------------------------------
	// withWriteAccess
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var lehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var schulenRepoFactory = mock(SchulenRepositoryFactory.class);
		final var schuleRepoFactory = mock(SchuleRepositoryFactory.class);
		final var lehrerServiceFactory = mock(LehrerServiceFactory.class);
		final var lehrerFunktionServiceFactory = mock(LehrerFunktionServiceFactory.class);
		final var mockedServiceFactory = mock(LehrerPersonalabschnittsdatenServiceFactory.class);

		lehrerRepoFactoryMock.when(LehrerRepositoryFactory::getNewInstance).thenReturn(lehrerRepoFactory);
		schulenRepoFactoryMock.when(SchulenRepositoryFactory::getNewInstance).thenReturn(schulenRepoFactory);
		schuleRepoFactoryMock.when(SchuleRepositoryFactory::getNewInstance).thenReturn(schuleRepoFactory);
		lehrerServiceFactoryMock.when(LehrerServiceFactory::getNewInstance).thenReturn(lehrerServiceFactory);
		lehrerFunktionServiceFactoryMock.when(LehrerFunktionServiceFactory::getNewInstance).thenReturn(lehrerFunktionServiceFactory);
		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(
						request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN))
				.thenAnswer(invocation -> null);
		serviceFactoryStaticMock.when(() -> LehrerPersonalabschnittsdatenServiceFactory.getNewInstance(
						lehrerRepoFactory,
						schulenRepoFactory,
						schuleRepoFactory,
						lehrerServiceFactory,
						lehrerFunktionServiceFactory,
						LehrerPersonalabschnittsdatenMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);

		final var factory = LehrerPersonalabschnittsdatenControllerFactory.withWriteAccess(request);

		assertThat(factory).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenControllerFactory.class);
		lehrerRepoFactoryMock.verify(LehrerRepositoryFactory::getNewInstance, times(1));
		schulenRepoFactoryMock.verify(SchulenRepositoryFactory::getNewInstance, times(1));
		schuleRepoFactoryMock.verify(SchuleRepositoryFactory::getNewInstance, times(1));
		lehrerServiceFactoryMock.verify(LehrerServiceFactory::getNewInstance, times(1));
		lehrerFunktionServiceFactoryMock.verify(LehrerFunktionServiceFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> LehrerPersonalabschnittsdatenServiceFactory.getNewInstance(
				lehrerRepoFactory,
				schulenRepoFactory,
				schuleRepoFactory,
				lehrerServiceFactory,
				lehrerFunktionServiceFactory,
				LehrerPersonalabschnittsdatenMapper.INSTANCE), times(1));
	}

	// -------------------------------------------------------------------------
	// withReadAccess
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("withReadAccess | Erfolg")
	void withReadAccess_success() {
		final var lehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var schulenRepoFactory = mock(SchulenRepositoryFactory.class);
		final var schuleRepoFactory = mock(SchuleRepositoryFactory.class);
		final var lehrerServiceFactory = mock(LehrerServiceFactory.class);
		final var lehrerFunktionServiceFactory = mock(LehrerFunktionServiceFactory.class);
		final var mockedServiceFactory = mock(LehrerPersonalabschnittsdatenServiceFactory.class);

		lehrerRepoFactoryMock.when(LehrerRepositoryFactory::getNewInstance).thenReturn(lehrerRepoFactory);
		schulenRepoFactoryMock.when(SchulenRepositoryFactory::getNewInstance).thenReturn(schulenRepoFactory);
		schuleRepoFactoryMock.when(SchuleRepositoryFactory::getNewInstance).thenReturn(schuleRepoFactory);
		lehrerServiceFactoryMock.when(LehrerServiceFactory::getNewInstance).thenReturn(lehrerServiceFactory);
		lehrerFunktionServiceFactoryMock.when(LehrerFunktionServiceFactory::getNewInstance).thenReturn(lehrerFunktionServiceFactory);
		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(
						request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN))
				.thenAnswer(invocation -> null);
		serviceFactoryStaticMock.when(() -> LehrerPersonalabschnittsdatenServiceFactory.getNewInstance(
						lehrerRepoFactory,
						schulenRepoFactory,
						schuleRepoFactory,
						lehrerServiceFactory,
						lehrerFunktionServiceFactory,
						LehrerPersonalabschnittsdatenMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);

		final var factory = LehrerPersonalabschnittsdatenControllerFactory.withReadAccess(request);

		assertThat(factory).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenControllerFactory.class);
		lehrerRepoFactoryMock.verify(LehrerRepositoryFactory::getNewInstance, times(1));
		schulenRepoFactoryMock.verify(SchulenRepositoryFactory::getNewInstance, times(1));
		schuleRepoFactoryMock.verify(SchuleRepositoryFactory::getNewInstance, times(1));
		lehrerServiceFactoryMock.verify(LehrerServiceFactory::getNewInstance, times(1));
		lehrerFunktionServiceFactoryMock.verify(LehrerFunktionServiceFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> LehrerPersonalabschnittsdatenServiceFactory.getNewInstance(
				lehrerRepoFactory,
				schulenRepoFactory,
				schuleRepoFactory,
				lehrerServiceFactory,
				lehrerFunktionServiceFactory,
				LehrerPersonalabschnittsdatenMapper.INSTANCE), times(1));
	}

	// -------------------------------------------------------------------------
	// getController
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getController | Erfolg")
	void getController_success() {
		final var lehrerPersonalabschnittsdatenService = mock(LehrerPersonalabschnittsdatenService.class);
		when(serviceFactory.getLehrerPersonalabschnittsdatenService()).thenReturn(lehrerPersonalabschnittsdatenService);

		final var controller = controllerFactory.getController();

		assertThat(controller).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenController.class);
		verify(serviceFactory, times(1)).getLehrerPersonalabschnittsdatenService();
	}

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		assertThat(controllerFactory).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenControllerFactory.class);
	}

}
