package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.mapper.schueler.schulbesuch.BisherigeSchuleMapper;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.schueler.schulbesuch.BisherigeSchuleRepository;
import de.svws_nrw.repo.schueler.schulbesuch.BisherigeSchuleRepositoryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BisherigeSchuleServiceFactoryTest {

	@Mock
	private BisherigeSchuleRepositoryFactory repoFactory;

	@Mock
	private BisherigeSchuleMapper mapper;

	private MockedStatic<DbConnectionProvider> dbConnectionProviderMock;

	@BeforeEach
	void setUp() {
		dbConnectionProviderMock = mockStatic(DbConnectionProvider.class);
	}

	@AfterEach
	void tearDown() {
		dbConnectionProviderMock.close();
	}

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		final var factory = BisherigeSchuleServiceFactory.getNewInstance(repoFactory, mapper);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(BisherigeSchuleServiceFactory.class);
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var factory = new BisherigeSchuleServiceFactory(repoFactory, mapper);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(BisherigeSchuleServiceFactory.class);
	}

	@Test
	@DisplayName("getBisherigeSchulenService | Erfolg")
	void getBisherigeSchulenService_success() {
		final var conn = mock(de.svws_nrw.db.DBEntityManager.class);
		final var repository = mock(BisherigeSchuleRepository.class);
		final var factory = BisherigeSchuleServiceFactory.getNewInstance(repoFactory, mapper);

		dbConnectionProviderMock.when(DbConnectionProvider::getConnection).thenReturn(conn);
		when(repoFactory.getBisherigeSchulenRepository()).thenReturn(repository);

		try (MockedConstruction<DataSchulen> ignored = mockConstruction(DataSchulen.class);
				MockedConstruction<DataKatalogEntlassgruende> ignored1 = mockConstruction(DataKatalogEntlassgruende.class)) {

			final var service = factory.getBisherigeSchulenService();

			assertThat(service)
					.isNotNull()
					.isInstanceOf(BisherigeSchuleService.class);

			verify(repoFactory, times(1)).getBisherigeSchulenRepository();
			dbConnectionProviderMock.verify(DbConnectionProvider::getConnection, times(2));
		}
	}

	@Test
	@DisplayName("getBisherigeSchulenService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getBisherigeSchulenService_multipleCallsCreateNewInstances() {
		final var conn = mock(de.svws_nrw.db.DBEntityManager.class);
		final var repository = mock(BisherigeSchuleRepository.class);
		final var factory = BisherigeSchuleServiceFactory.getNewInstance(repoFactory, mapper);

		dbConnectionProviderMock.when(DbConnectionProvider::getConnection).thenReturn(conn);
		when(repoFactory.getBisherigeSchulenRepository()).thenReturn(repository);

		try (MockedConstruction<DataSchulen> ignored = mockConstruction(DataSchulen.class);
				MockedConstruction<DataKatalogEntlassgruende> ignored1 = mockConstruction(DataKatalogEntlassgruende.class)) {

			final var service1 = factory.getBisherigeSchulenService();
			final var service2 = factory.getBisherigeSchulenService();

			assertThat(service1)
					.isNotNull()
					.isNotSameAs(service2);

			assertThat(service2).isNotNull();

			verify(repoFactory, times(2)).getBisherigeSchulenRepository();
			dbConnectionProviderMock.verify(DbConnectionProvider::getConnection, times(4));
		}
	}
}
