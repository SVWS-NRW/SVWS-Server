package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.mapper.schueler.schulbesuch.BisherigeSchuleMapper;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.service.schueler.schulbesuch.BisherigeSchuleService;
import de.svws_nrw.service.schueler.schulbesuch.BisherigeSchuleServiceFactory;
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
class BisherigeSchuleControllerFactoryTest {

	@Mock
	private BisherigeSchuleServiceFactory serviceFactory;

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<SchuelerRepositoryFactory> repositoryFactoryMock;
	private MockedStatic<BisherigeSchuleServiceFactory> serviceFactoryStaticMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		repositoryFactoryMock = mockStatic(SchuelerRepositoryFactory.class);
		serviceFactoryStaticMock = mockStatic(BisherigeSchuleServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		repositoryFactoryMock.close();
		serviceFactoryStaticMock.close();
	}

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repoFactory = mock(SchuelerRepositoryFactory.class);
		final var mockedServiceFactory = mock(BisherigeSchuleServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN))
				.thenReturn(dbConnection);
		repositoryFactoryMock.when(SchuelerRepositoryFactory::getNewInstance)
				.thenReturn(repoFactory);
		serviceFactoryStaticMock.when(() -> BisherigeSchuleServiceFactory.getNewInstance(repoFactory, BisherigeSchuleMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);

		final var factory = BisherigeSchuleControllerFactory.withWriteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(BisherigeSchuleControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN), times(1));
		repositoryFactoryMock.verify(SchuelerRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> BisherigeSchuleServiceFactory.getNewInstance(repoFactory, BisherigeSchuleMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("withDeleteAccess | Erfolg")
	void withDeleteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var repoFactory = mock(SchuelerRepositoryFactory.class);
		final var mockedServiceFactory = mock(BisherigeSchuleServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN))
				.thenReturn(dbConnection);
		repositoryFactoryMock.when(SchuelerRepositoryFactory::getNewInstance)
				.thenReturn(repoFactory);
		serviceFactoryStaticMock.when(() -> BisherigeSchuleServiceFactory.getNewInstance(repoFactory, BisherigeSchuleMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);

		final var factory = BisherigeSchuleControllerFactory.withDeleteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(BisherigeSchuleControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN), times(1));
		repositoryFactoryMock.verify(SchuelerRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> BisherigeSchuleServiceFactory.getNewInstance(repoFactory, BisherigeSchuleMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("getBisherigeSchulenController | Erfolg")
	void getBisherigeSchuleController_success() {
		final var bisherigeSchulenService = mock(BisherigeSchuleService.class);
		final var factory = new BisherigeSchuleControllerFactory(serviceFactory);

		when(serviceFactory.getBisherigeSchuleService()).thenReturn(bisherigeSchulenService);

		final var controller = factory.getBisherigeSchuleController();

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(BisherigeSchuleController.class);

		verify(serviceFactory, times(1)).getBisherigeSchuleService();
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var factory = new BisherigeSchuleControllerFactory(serviceFactory);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(BisherigeSchuleControllerFactory.class);
	}
}
