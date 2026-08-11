package de.svws_nrw.controller.schule.kataloge.fachklasse;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.mapper.schule.kataloge.fachklasse.FachklasseMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;
import de.svws_nrw.service.schule.kataloge.fachklasse.FachklasseService;
import de.svws_nrw.service.schule.kataloge.fachklasse.FachklasseServiceFactory;
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
class FachklasseControllerFactoryTest {

	@Mock
	private FachklasseServiceFactory fachklasseServiceFactory;

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<KatalogRepositoryFactory> katalogRepositoryFactoryMockedStatic;
	private MockedStatic<EigeneSchuleRepositoryFactory> schuleRepositoryFactoryMock;
	private MockedStatic<SchuleServiceFactory> schuleServiceFactoryMock;
	private MockedStatic<FachklasseServiceFactory> fachklasseServiceFactoryStaticMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		katalogRepositoryFactoryMockedStatic = mockStatic(KatalogRepositoryFactory.class);
		schuleRepositoryFactoryMock = mockStatic(EigeneSchuleRepositoryFactory.class);
		schuleServiceFactoryMock = mockStatic(SchuleServiceFactory.class);
		fachklasseServiceFactoryStaticMock = mockStatic(FachklasseServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		katalogRepositoryFactoryMockedStatic.close();
		schuleRepositoryFactoryMock.close();
		schuleServiceFactoryMock.close();
		fachklasseServiceFactoryStaticMock.close();
	}

	private void setupStaticMocks(final BenutzerKompetenz kompetenz,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final EigeneSchuleRepositoryFactory schuleRepoFactory,
			final SchuleServiceFactory schuleServiceFactory,
			final FachklasseServiceFactory serviceFactory) {
		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, kompetenz))
				.thenReturn(mock(DBEntityManager.class));
		katalogRepositoryFactoryMockedStatic.when(KatalogRepositoryFactory::getNewInstance)
				.thenReturn(katalogRepositoryFactory);
		schuleRepositoryFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance)
				.thenReturn(schuleRepoFactory);
		schuleServiceFactoryMock.when(() -> SchuleServiceFactory.getNewInstance(schuleRepoFactory))
				.thenReturn(schuleServiceFactory);
		fachklasseServiceFactoryStaticMock.when(() -> FachklasseServiceFactory.getNewInstance(
				katalogRepositoryFactory,
				FachklasseMapper.INSTANCE,
				schuleServiceFactory)
		).thenReturn(serviceFactory);
	}

	@Test
	@DisplayName("withReadAccess | Erfolg")
	void withReadAccess_success() {
		final var katalogRepositoryFactory = mock(KatalogRepositoryFactory.class);
		final var schuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(SchuleServiceFactory.class);
		final var serviceFactory = mock(FachklasseServiceFactory.class);

		setupStaticMocks(BenutzerKompetenz.KEINE, katalogRepositoryFactory, schuleRepoFactory, schuleServiceFactory, serviceFactory);

		final var factory = FachklasseControllerFactory.withReadAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(FachklasseControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.KEINE), times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		schuleRepositoryFactoryMock.verify(EigeneSchuleRepositoryFactory::getNewInstance, times(1));
		schuleServiceFactoryMock.verify(() -> SchuleServiceFactory.getNewInstance(schuleRepoFactory), times(1));
		fachklasseServiceFactoryStaticMock.verify(() -> FachklasseServiceFactory.getNewInstance(
				katalogRepositoryFactory, FachklasseMapper.INSTANCE, schuleServiceFactory), times(1));
	}

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var katalogRepositoryFactory = mock(KatalogRepositoryFactory.class);
		final var schuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(SchuleServiceFactory.class);
		final var serviceFactory = mock(FachklasseServiceFactory.class);

		setupStaticMocks(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, katalogRepositoryFactory, schuleRepoFactory, schuleServiceFactory, serviceFactory);

		final var factory = FachklasseControllerFactory.withWriteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(FachklasseControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN), times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		schuleRepositoryFactoryMock.verify(EigeneSchuleRepositoryFactory::getNewInstance, times(1));
		schuleServiceFactoryMock.verify(() -> SchuleServiceFactory.getNewInstance(schuleRepoFactory), times(1));
		fachklasseServiceFactoryStaticMock.verify(() -> FachklasseServiceFactory.getNewInstance(
				katalogRepositoryFactory, FachklasseMapper.INSTANCE, schuleServiceFactory), times(1));
	}

	@Test
	@DisplayName("withDeleteAccess | Erfolg")
	void withDeleteAccess_success() {
		final var katalogRepositoryFactory = mock(KatalogRepositoryFactory.class);
		final var schuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(SchuleServiceFactory.class);
		final var serviceFactory = mock(FachklasseServiceFactory.class);

		setupStaticMocks(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN, katalogRepositoryFactory, schuleRepoFactory, schuleServiceFactory, serviceFactory);

		final var factory = FachklasseControllerFactory.withDeleteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(FachklasseControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN), times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		schuleRepositoryFactoryMock.verify(EigeneSchuleRepositoryFactory::getNewInstance, times(1));
		schuleServiceFactoryMock.verify(() -> SchuleServiceFactory.getNewInstance(schuleRepoFactory), times(1));
		fachklasseServiceFactoryStaticMock.verify(() -> FachklasseServiceFactory.getNewInstance(
				katalogRepositoryFactory, FachklasseMapper.INSTANCE, schuleServiceFactory), times(1));
	}

	@Test
	@DisplayName("getController | Erfolg")
	void getController_success() {
		final var fachklasseService = mock(FachklasseService.class);
		final var factory = new FachklasseControllerFactory(fachklasseServiceFactory);

		when(fachklasseServiceFactory.getService()).thenReturn(fachklasseService);

		final var controller = factory.getController();

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(FachklasseController.class);

		verify(fachklasseServiceFactory, times(1)).getService();
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var factory = new FachklasseControllerFactory(fachklasseServiceFactory);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(FachklasseControllerFactory.class);
	}
}
