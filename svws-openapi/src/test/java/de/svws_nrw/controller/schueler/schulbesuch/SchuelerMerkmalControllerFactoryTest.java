package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerMerkmaleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalService;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalServiceFactory;
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
class SchuelerMerkmalControllerFactoryTest {

	@Mock
	private SchuelerMerkmalServiceFactory serviceFactory;

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<SchuelerMerkmaleRepositoryFactory> repoFactoryStaticMock;
	private MockedStatic<KatalogRepositoryFactory> katalogRepositoryFactoryMockedStatic;
	private MockedStatic<SchuelerMerkmalServiceFactory> serviceFactoryStaticMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		repoFactoryStaticMock = mockStatic(SchuelerMerkmaleRepositoryFactory.class);
		katalogRepositoryFactoryMockedStatic = mockStatic(KatalogRepositoryFactory.class);
		serviceFactoryStaticMock = mockStatic(SchuelerMerkmalServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		repoFactoryStaticMock.close();
		katalogRepositoryFactoryMockedStatic.close();
		serviceFactoryStaticMock.close();
	}

	private void mockInfrastruktur(final SchuelerMerkmaleRepositoryFactory repoFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final SchuelerMerkmalServiceFactory mockedServiceFactory) {
		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN))
				.thenReturn(mock(DBEntityManager.class));
		repoFactoryStaticMock.when(SchuelerMerkmaleRepositoryFactory::getNewInstance)
				.thenReturn(repoFactory);
		katalogRepositoryFactoryMockedStatic.when(KatalogRepositoryFactory::getNewInstance)
				.thenReturn(katalogRepositoryFactory);
		serviceFactoryStaticMock.when(() -> SchuelerMerkmalServiceFactory.getNewInstance(repoFactory, katalogRepositoryFactory, SchuelerMerkmalMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);
	}

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		final var repoFactory = mock(SchuelerMerkmaleRepositoryFactory.class);
		final var katalogRepositoryFactory = mock(KatalogRepositoryFactory.class);
		final var mockedServiceFactory = mock(SchuelerMerkmalServiceFactory.class);

		mockInfrastruktur(repoFactory, katalogRepositoryFactory, mockedServiceFactory);

		final var factory = SchuelerMerkmalControllerFactory.withWriteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN), times(1));
		repoFactoryStaticMock.verify(SchuelerMerkmaleRepositoryFactory::getNewInstance, times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> SchuelerMerkmalServiceFactory.getNewInstance(repoFactory, katalogRepositoryFactory, SchuelerMerkmalMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("withDeleteAccess | Erfolg")
	void withDeleteAccess_success() {
		final var repoFactory = mock(SchuelerMerkmaleRepositoryFactory.class);
		final var katalogRepositoryFactory = mock(KatalogRepositoryFactory.class);
		final var mockedServiceFactory = mock(SchuelerMerkmalServiceFactory.class);

		mockInfrastruktur(repoFactory, katalogRepositoryFactory, mockedServiceFactory);

		final var factory = SchuelerMerkmalControllerFactory.withDeleteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN), times(1));
		repoFactoryStaticMock.verify(SchuelerMerkmaleRepositoryFactory::getNewInstance, times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		serviceFactoryStaticMock.verify(() -> SchuelerMerkmalServiceFactory.getNewInstance(repoFactory, katalogRepositoryFactory, SchuelerMerkmalMapper.INSTANCE), times(1));
	}

	@Test
	@DisplayName("getBisherigeSchulenController | Erfolg")
	void getBisherigeSchulenController_success() {
		final var schuelerMerkmalService = mock(SchuelerMerkmalService.class);
		final var factory = new SchuelerMerkmalControllerFactory(serviceFactory);

		when(serviceFactory.getSchuelerMerkmalService()).thenReturn(schuelerMerkmalService);

		final var controller = factory.getBisherigeSchulenController();

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalController.class);

		verify(serviceFactory, times(1)).getSchuelerMerkmalService();
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var factory = new SchuelerMerkmalControllerFactory(serviceFactory);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalControllerFactory.class);
	}
}
