package de.svws_nrw.controller.schule.teilstandort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.teilstandort.TeilstandortService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Tests für die {@link TeilstandortControllerFactory}.
 */
@ExtendWith(MockitoExtension.class)
class TeilstandortControllerFactoryTest {

	@Mock
	private HttpServletRequest request;

	private MockedStatic<DBBenutzerUtils> dbBenutzerUtilsMock;
	private MockedStatic<EigeneSchuleRepositoryFactory> schuleRepositoryFactoryMock;
	private MockedStatic<EigeneSchuleServiceFactory> schuleServiceFactoryMock;

	@BeforeEach
	void setUp() {
		dbBenutzerUtilsMock = mockStatic(DBBenutzerUtils.class);
		schuleRepositoryFactoryMock = mockStatic(EigeneSchuleRepositoryFactory.class);
		schuleServiceFactoryMock = mockStatic(EigeneSchuleServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		dbBenutzerUtilsMock.close();
		schuleRepositoryFactoryMock.close();
		schuleServiceFactoryMock.close();
	}

	@Test
	@DisplayName("withReadAccess | fordert keine Kompetenz an")
	void withReadAccess() {
		final var factory = TeilstandortControllerFactory.withReadAccess(request);

		assertThat(factory).isNotNull();
		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE), times(1));
	}

	@Test
	@DisplayName("withWriteAccess | fordert KATALOG_EINTRAEGE_AENDERN an")
	void withWriteAccess() {
		final var factory = TeilstandortControllerFactory.withWriteAccess(request);

		assertThat(factory).isNotNull();
		dbBenutzerUtilsMock.verify(
				() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN), times(1));
	}

	@Test
	@DisplayName("withDeleteAccess | fordert KATALOG_EINTRAEGE_LOESCHEN an")
	void withDeleteAccess() {
		final var factory = TeilstandortControllerFactory.withDeleteAccess(request);

		assertThat(factory).isNotNull();
		dbBenutzerUtilsMock.verify(
				() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN), times(1));
	}

	@Test
	@DisplayName("getController | gibt einen TeilstandortController zurück")
	void getController() {
		final var dbConnection = mock(DBEntityManager.class);
		final var schuleRepositoryFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(EigeneSchuleServiceFactory.class);
		final var service = mock(TeilstandortService.class);
		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)).thenReturn(dbConnection);
		schuleRepositoryFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance).thenReturn(schuleRepositoryFactory);
		schuleServiceFactoryMock.when(() -> EigeneSchuleServiceFactory.getNewInstance(schuleRepositoryFactory)).thenReturn(schuleServiceFactory);
		when(schuleServiceFactory.getTeilstandortService()).thenReturn(service);

		final var controller = TeilstandortControllerFactory.withReadAccess(request).getController();

		assertThat(controller).isNotNull().isInstanceOf(TeilstandortControllerImpl.class);
		verify(schuleServiceFactory, times(1)).getTeilstandortService();
	}

}
