package de.svws_nrw.controller.schule.merkmale;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.mapper.schule.merkmale.MerkmalMapper;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.merkmale.MerkmalService;
import de.svws_nrw.service.schule.merkmale.MerkmalServiceFactory;
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
class MerkmalControllerFactoryTest {

	@Mock
	private MerkmalServiceFactory merkmalServiceFactory;

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<KatalogRepositoryFactory> katalogRepositoryFactoryMockedStatic;
	private MockedStatic<MerkmalServiceFactory> merkmalServiceFactoryStaticMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		katalogRepositoryFactoryMockedStatic = mockStatic(KatalogRepositoryFactory.class);
		merkmalServiceFactoryStaticMock = mockStatic(MerkmalServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		katalogRepositoryFactoryMockedStatic.close();
		merkmalServiceFactoryStaticMock.close();
	}

	@Test
	@DisplayName("withReadAccess | Erfolg")
	void withReadAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var katalogRepositoryFactory = mock(KatalogRepositoryFactory.class);
		final var serviceFactory = mock(MerkmalServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE))
				.thenReturn(dbConnection);
		katalogRepositoryFactoryMockedStatic.when(KatalogRepositoryFactory::getNewInstance)
				.thenReturn(katalogRepositoryFactory);
		merkmalServiceFactoryStaticMock.when(() -> MerkmalServiceFactory.getNewInstance(katalogRepositoryFactory, MerkmalMapper.INSTANCE))
				.thenReturn(serviceFactory);

		final var factory = MerkmalControllerFactory.withReadAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(MerkmalControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE), times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		merkmalServiceFactoryStaticMock.verify(() -> MerkmalServiceFactory.getNewInstance(katalogRepositoryFactory, MerkmalMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repositoryFactory = mock(KatalogRepositoryFactory.class);
		final var serviceFactory = mock(MerkmalServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN))
				.thenReturn(dbConnection);
		katalogRepositoryFactoryMockedStatic.when(KatalogRepositoryFactory::getNewInstance)
				.thenReturn(repositoryFactory);
		merkmalServiceFactoryStaticMock.when(() -> MerkmalServiceFactory.getNewInstance(repositoryFactory, MerkmalMapper.INSTANCE))
				.thenReturn(serviceFactory);

		final var factory = MerkmalControllerFactory.withWriteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(MerkmalControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN), times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		merkmalServiceFactoryStaticMock.verify(() -> MerkmalServiceFactory.getNewInstance(repositoryFactory, MerkmalMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("withDeleteAccess | Erfolg")
	void withDeleteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repositoryFactory = mock(KatalogRepositoryFactory.class);
		final var serviceFactory = mock(MerkmalServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN))
				.thenReturn(dbConnection);
		katalogRepositoryFactoryMockedStatic.when(KatalogRepositoryFactory::getNewInstance)
				.thenReturn(repositoryFactory);
		merkmalServiceFactoryStaticMock.when(() -> MerkmalServiceFactory.getNewInstance(repositoryFactory, MerkmalMapper.INSTANCE))
				.thenReturn(serviceFactory);

		final var factory = MerkmalControllerFactory.withDeleteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(MerkmalControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN), times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		merkmalServiceFactoryStaticMock.verify(() -> MerkmalServiceFactory.getNewInstance(repositoryFactory, MerkmalMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("getMerkmalController | Erfolg")
	void getMerkmalController_success() {
		final var merkmalService = mock(MerkmalService.class);
		final var factory = new MerkmalControllerFactory(merkmalServiceFactory);

		when(merkmalServiceFactory.getMerkmalService()).thenReturn(merkmalService);

		final var controller = factory.getMerkmalController();

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(MerkmalController.class);

		verify(merkmalServiceFactory, times(1)).getMerkmalService();
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var factory = new MerkmalControllerFactory(merkmalServiceFactory);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(MerkmalControllerFactory.class);
	}
}
