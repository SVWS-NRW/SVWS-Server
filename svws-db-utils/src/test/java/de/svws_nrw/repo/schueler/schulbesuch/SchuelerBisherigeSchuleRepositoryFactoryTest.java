package de.svws_nrw.repo.schueler.schulbesuch;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
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
class SchuelerBisherigeSchuleRepositoryFactoryTest {

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
		final var factory = SchuelerRepositoryFactory.getNewInstance();

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchuelerRepositoryFactory.class);
	}


	@Test
	@DisplayName("getNewInstance | Jeder Aufruf erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		final var factory1 = SchuelerRepositoryFactory.getNewInstance();
		final var factory2 = SchuelerRepositoryFactory.getNewInstance();

		assertThat(factory1).isNotSameAs(factory2);
	}

	@Test
	@DisplayName("getBisherigeSchulenRepository | Erfolg")
	void getBisherigeSchuleRepository_success() {
		final var factory = SchuelerRepositoryFactory.getNewInstance();

		final var repository = factory.getSchuelerBisherigeSchuleRepository();

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(SchuelerBisherigeSchuleRepository.class)
				.isInstanceOf(SchuelerBisherigeSchuleRepositoryImpl.class);
	}

	@Test
	@DisplayName("getBisherigeSchulenRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getBisherigeSchuleRepository_cachesInstance() {
		final var factory = SchuelerRepositoryFactory.getNewInstance();

		final var repository1 = factory.getSchuelerBisherigeSchuleRepository();
		final var repository2 = factory.getSchuelerBisherigeSchuleRepository();

		assertThat(repository1).isSameAs(repository2);
	}

	@Test
	@DisplayName("getBisherigeSchulenRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getBisherigeSchuleRepository_differentFactoriesCreateDifferentRepositories() {
		final var factory1 = SchuelerRepositoryFactory.getNewInstance();
		final var factory2 = SchuelerRepositoryFactory.getNewInstance();

		final var repository1 = factory1.getSchuelerBisherigeSchuleRepository();
		final var repository2 = factory2.getSchuelerBisherigeSchuleRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}
}
