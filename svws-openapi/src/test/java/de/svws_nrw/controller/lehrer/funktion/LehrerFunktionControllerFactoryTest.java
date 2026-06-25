package de.svws_nrw.controller.lehrer.funktion;

import de.svws_nrw.controller.lehrer.LehrerFunktionController;
import de.svws_nrw.controller.lehrer.LehrerFunktionControllerFactory;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.mapper.lehrer.LehrerFunktionMapper;
import de.svws_nrw.repo.lehrer.LehrerAbschnittsdatenRepositoryFactory;
import de.svws_nrw.repo.lehrer.funktion.LehrerFunktionRepositoryFactory;
import de.svws_nrw.repo.schule.leitungsfunktion.LehrerLeitungsfunktionRepositoryFactory;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionServiceFactory;
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
class LehrerFunktionControllerFactoryTest {

	@Mock
	private LehrerFunktionServiceFactory serviceFactory;

	@Mock
	private HttpServletRequest request;

	@InjectMocks
	private LehrerFunktionControllerFactory controllerFactory;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<LehrerFunktionRepositoryFactory> repositoryFactoryMock;
	private MockedStatic<LehrerAbschnittsdatenRepositoryFactory> abschnittsdatenRepositoryFactoryMock;
	private MockedStatic<LehrerLeitungsfunktionRepositoryFactory> leitungsfunktionRepositoryFactoryMock;
	private MockedStatic<LehrerFunktionServiceFactory> serviceFactoryStaticMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		repositoryFactoryMock = mockStatic(LehrerFunktionRepositoryFactory.class);
		abschnittsdatenRepositoryFactoryMock = mockStatic(LehrerAbschnittsdatenRepositoryFactory.class);
		leitungsfunktionRepositoryFactoryMock = mockStatic(LehrerLeitungsfunktionRepositoryFactory.class);
		serviceFactoryStaticMock = mockStatic(LehrerFunktionServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		repositoryFactoryMock.close();
		abschnittsdatenRepositoryFactoryMock.close();
		leitungsfunktionRepositoryFactoryMock.close();
		serviceFactoryStaticMock.close();
	}

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repoFactory = mock(LehrerFunktionRepositoryFactory.class);
		final var abschnittsdatenRepoFactory = mock(LehrerAbschnittsdatenRepositoryFactory.class);
		final var leitungsfunktionRepoFactory = mock(LehrerLeitungsfunktionRepositoryFactory.class);
		final var mockedServiceFactory = mock(LehrerFunktionServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(
						request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN))
				.thenReturn(dbConnection);
		repositoryFactoryMock.when(LehrerFunktionRepositoryFactory::getNewInstance).thenReturn(repoFactory);
		abschnittsdatenRepositoryFactoryMock.when(LehrerAbschnittsdatenRepositoryFactory::getNewInstance).thenReturn(abschnittsdatenRepoFactory);
		leitungsfunktionRepositoryFactoryMock.when(LehrerLeitungsfunktionRepositoryFactory::getNewInstance).thenReturn(leitungsfunktionRepoFactory);
		serviceFactoryStaticMock.when(() -> LehrerFunktionServiceFactory.getNewInstance(
						repoFactory, abschnittsdatenRepoFactory, leitungsfunktionRepoFactory, LehrerFunktionMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);

		final var factory = LehrerFunktionControllerFactory.withWriteAccess(request);

		assertThat(factory).isNotNull().isInstanceOf(LehrerFunktionControllerFactory.class);
		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN), times(1));
		repositoryFactoryMock.verify(LehrerFunktionRepositoryFactory::getNewInstance, times(1));
		abschnittsdatenRepositoryFactoryMock.verify(LehrerAbschnittsdatenRepositoryFactory::getNewInstance, times(1));
		leitungsfunktionRepositoryFactoryMock.verify(LehrerLeitungsfunktionRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> LehrerFunktionServiceFactory.getNewInstance(
				repoFactory, abschnittsdatenRepoFactory, leitungsfunktionRepoFactory, LehrerFunktionMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("withReadAccess | Erfolg")
	void withReadAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repoFactory = mock(LehrerFunktionRepositoryFactory.class);
		final var abschnittsdatenRepoFactory = mock(LehrerAbschnittsdatenRepositoryFactory.class);
		final var leitungsfunktionRepoFactory = mock(LehrerLeitungsfunktionRepositoryFactory.class);
		final var mockedServiceFactory = mock(LehrerFunktionServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(
						request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN))
				.thenReturn(dbConnection);
		repositoryFactoryMock.when(LehrerFunktionRepositoryFactory::getNewInstance).thenReturn(repoFactory);
		abschnittsdatenRepositoryFactoryMock.when(LehrerAbschnittsdatenRepositoryFactory::getNewInstance).thenReturn(abschnittsdatenRepoFactory);
		leitungsfunktionRepositoryFactoryMock.when(LehrerLeitungsfunktionRepositoryFactory::getNewInstance).thenReturn(leitungsfunktionRepoFactory);
		serviceFactoryStaticMock.when(() -> LehrerFunktionServiceFactory.getNewInstance(
						repoFactory, abschnittsdatenRepoFactory, leitungsfunktionRepoFactory, LehrerFunktionMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);

		final var factory = LehrerFunktionControllerFactory.withReadAccess(request);

		assertThat(factory).isNotNull().isInstanceOf(LehrerFunktionControllerFactory.class);
		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN), times(1));
		repositoryFactoryMock.verify(LehrerFunktionRepositoryFactory::getNewInstance, times(1));
		abschnittsdatenRepositoryFactoryMock.verify(LehrerAbschnittsdatenRepositoryFactory::getNewInstance, times(1));
		leitungsfunktionRepositoryFactoryMock.verify(LehrerLeitungsfunktionRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> LehrerFunktionServiceFactory.getNewInstance(
				repoFactory, abschnittsdatenRepoFactory, leitungsfunktionRepoFactory, LehrerFunktionMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("getController | Erfolg")
	void getController_success() {
		final var lehrerFunktionService = mock(LehrerFunktionService.class);
		when(serviceFactory.getLehrerFunktionService()).thenReturn(lehrerFunktionService);

		final var controller = controllerFactory.getController();

		assertThat(controller).isNotNull().isInstanceOf(LehrerFunktionController.class);
		verify(serviceFactory, times(1)).getLehrerFunktionService();
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		assertThat(controllerFactory).isNotNull().isInstanceOf(LehrerFunktionControllerFactory.class);
	}
}
