package de.svws_nrw.controller.schule.schuljahresabschnitt;

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
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Tests für die {@link SchuljahresabschnittControllerFactory}.
 */
@ExtendWith(MockitoExtension.class)
class SchuljahresabschnittControllerFactoryTest {

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
	@DisplayName("withWriteAccess | fordert die Kompetenz ADMIN an")
	void withWriteAccess_success() {
		final var dbConnection = mock(DBEntityManager.class);
		final var schuleRepositoryFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(EigeneSchuleServiceFactory.class);
		dbBenutzerUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.ADMIN)).thenReturn(dbConnection);
		schuleRepositoryFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance).thenReturn(schuleRepositoryFactory);
		schuleServiceFactoryMock.when(() -> EigeneSchuleServiceFactory.getNewInstance(schuleRepositoryFactory)).thenReturn(schuleServiceFactory);

		final var factory = SchuljahresabschnittControllerFactory.withWriteAccess(request);

		assertThat(factory).isNotNull();
		dbBenutzerUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.ADMIN), times(1));
		schuleServiceFactoryMock.verify(() -> EigeneSchuleServiceFactory.getNewInstance(schuleRepositoryFactory), times(1));
	}

	@Test
	@DisplayName("getController | gibt einen SchuljahresabschnittController zurück")
	void getController() {
		final var schuleRepositoryFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var schuleServiceFactory = mock(EigeneSchuleServiceFactory.class);
		final var service = mock(SchuljahresabschnittService.class);
		schuleRepositoryFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance).thenReturn(schuleRepositoryFactory);
		schuleServiceFactoryMock.when(() -> EigeneSchuleServiceFactory.getNewInstance(schuleRepositoryFactory)).thenReturn(schuleServiceFactory);
		when(schuleServiceFactory.getSchuljahresabschnittService()).thenReturn(service);

		final var controller = SchuljahresabschnittControllerFactory.withWriteAccess(request).getController();

		assertThat(controller).isNotNull().isInstanceOf(SchuljahresabschnittControllerImpl.class);
		verify(schuleServiceFactory, times(1)).getSchuljahresabschnittService();
	}

}
