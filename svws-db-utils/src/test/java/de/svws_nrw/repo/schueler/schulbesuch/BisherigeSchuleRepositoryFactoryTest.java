package de.svws_nrw.repo.schueler.schulbesuch;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class BisherigeSchuleRepositoryFactoryTest {

	@Mock
	private DBEntityManager conn;

	private MockedStatic<DbConnectionProvider> dbConnectionProviderMock;

	@BeforeEach
	void setUp() {
		dbConnectionProviderMock = mockStatic(DbConnectionProvider.class);
		dbConnectionProviderMock.when(DbConnectionProvider::getConnection).thenReturn(conn);
	}

	@AfterEach
	void tearDown() {
		dbConnectionProviderMock.close();
	}

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		final var factory = BisherigeSchuleRepositoryFactory.getNewInstance();

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(BisherigeSchuleRepositoryFactory.class);
	}


	@Test
	@DisplayName("getNewInstance | Jeder Aufruf erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		final var factory1 = BisherigeSchuleRepositoryFactory.getNewInstance();
		final var factory2 = BisherigeSchuleRepositoryFactory.getNewInstance();

		assertThat(factory1).isNotSameAs(factory2);
	}

	@Test
	@DisplayName("getBisherigeSchulenRepository | Erfolg")
	void getBisherigeSchulenRepository_success() {
		final var factory = BisherigeSchuleRepositoryFactory.getNewInstance();

		final var repository = factory.getBisherigeSchulenRepository();

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(BisherigeSchuleRepository.class)
				.isInstanceOf(BisherigeSchuleRepositoryImpl.class);
	}

	@Test
	@DisplayName("getBisherigeSchulenRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getBisherigeSchulenRepository_cachesInstance() {
		final var factory = BisherigeSchuleRepositoryFactory.getNewInstance();

		final var repository1 = factory.getBisherigeSchulenRepository();
		final var repository2 = factory.getBisherigeSchulenRepository();

		assertThat(repository1).isSameAs(repository2);
	}

	@Test
	@DisplayName("getBisherigeSchulenRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getBisherigeSchulenRepository_differentFactoriesCreateDifferentRepositories() {
		final var factory1 = BisherigeSchuleRepositoryFactory.getNewInstance();
		final var factory2 = BisherigeSchuleRepositoryFactory.getNewInstance();

		final var repository1 = factory1.getBisherigeSchulenRepository();
		final var repository2 = factory2.getBisherigeSchulenRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}
}
