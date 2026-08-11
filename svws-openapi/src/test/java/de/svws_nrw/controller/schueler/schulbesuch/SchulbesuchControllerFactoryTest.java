package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.mapper.schueler.schulbesuch.BisherigeSchuleMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMapper;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.schulbesuch.BisherigeSchuleServiceFactory;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalServiceFactory;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SchulbesuchControllerFactoryTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;

	@Mock
	private MockedStatic<SchuelerRepositoryFactory> schuelerRepositoryFactoryMock;

	@Mock
	private MockedStatic<KatalogRepositoryFactory> katalogRepositoryFactoryMockedStatic;

	@Mock
	private MockedStatic<SchuelerMerkmalServiceFactory> schuelerMerkmalServiceFactoryMock;

	@Mock
	private MockedStatic<BisherigeSchuleServiceFactory> bisherigeSchuleServiceFactoryMock;

	@Mock
	private MockedStatic<SchulbesuchServiceFactory> schulbesuchServiceFactoryMock;

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		schuelerRepositoryFactoryMock.close();
		katalogRepositoryFactoryMockedStatic.close();
		schuelerMerkmalServiceFactoryMock.close();
		bisherigeSchuleServiceFactoryMock.close();
		schulbesuchServiceFactoryMock.close();
	}

	private void setupFactoryChain(final BenutzerKompetenz kompetenz) {
		final var dbConnection = mock(DBEntityManager.class);
		final var schuelerRepoFactory = mock(SchuelerRepositoryFactory.class);
		final var katalogRepositoryFactory = mock(KatalogRepositoryFactory.class);
		final var schuelerMerkmalServiceFactory = mock(SchuelerMerkmalServiceFactory.class);
		final var bisherigeSchuleServiceFactory = mock(BisherigeSchuleServiceFactory.class);
		final var mockedServiceFactory = mock(SchulbesuchServiceFactory.class);

		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, kompetenz))
				.thenReturn(dbConnection);
		schuelerRepositoryFactoryMock.when(SchuelerRepositoryFactory::getNewInstance)
				.thenReturn(schuelerRepoFactory);
		katalogRepositoryFactoryMockedStatic.when(KatalogRepositoryFactory::getNewInstance)
				.thenReturn(katalogRepositoryFactory);
		schuelerMerkmalServiceFactoryMock.when(() -> SchuelerMerkmalServiceFactory.getNewInstance(
						schuelerRepoFactory, katalogRepositoryFactory, SchuelerMerkmalMapper.INSTANCE))
				.thenReturn(schuelerMerkmalServiceFactory);
		bisherigeSchuleServiceFactoryMock.when(() -> BisherigeSchuleServiceFactory.getNewInstance(
						schuelerRepoFactory, BisherigeSchuleMapper.INSTANCE))
				.thenReturn(bisherigeSchuleServiceFactory);
		schulbesuchServiceFactoryMock.when(() -> SchulbesuchServiceFactory.getNewInstance(
						schuelerRepoFactory, schuelerMerkmalServiceFactory, bisherigeSchuleServiceFactory, SchulbesuchMapper.INSTANCE))
				.thenReturn(mockedServiceFactory);
	}

	// -------------------------------------------------------------------------
	// withReadAccess
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("withReadAccess | Erfolg")
	void withReadAccess_success() {
		setupFactoryChain(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);

		final var factory = SchulbesuchControllerFactory.withReadAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulbesuchControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN), times(1));
	}

	// -------------------------------------------------------------------------
	// withWriteAccess
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("withWriteAccess | Erfolg")
	void withWriteAccess_success() {
		setupFactoryChain(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);

		final var factory = SchulbesuchControllerFactory.withWriteAccess(request);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulbesuchControllerFactory.class);

		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN), times(1));
	}

}
