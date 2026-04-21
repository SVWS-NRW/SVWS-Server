package de.svws_nrw.repo.schule.merkmale;

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
class MerkmalRepositoryFactoryTest {

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
		final var factory = MerkmalRepositoryFactory.getNewInstance();

		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("getNewInstance | Gibt MerkmalRepositoryFactory zurück")
	void getNewInstance_returnsMerkmalRepositoryFactory() {
		final var factory = MerkmalRepositoryFactory.getNewInstance();

		assertThat(factory).isInstanceOf(MerkmalRepositoryFactory.class);
	}

	@Test
	@DisplayName("getNewInstance | Jeder Aufruf erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		final var factory1 = MerkmalRepositoryFactory.getNewInstance();
		final var factory2 = MerkmalRepositoryFactory.getNewInstance();

		assertThat(factory1).isNotSameAs(factory2);
	}

	@Test
	@DisplayName("getMerkmalRepository | Erfolg")
	void getMerkmalRepository_success() {
		final var factory = MerkmalRepositoryFactory.getNewInstance();

		final var repository = factory.getMerkmalRepository();

		assertThat(repository).isNotNull();
	}

	@Test
	@DisplayName("getMerkmalRepository | Gibt MerkmalRepository zurück")
	void getMerkmalRepository_returnsMerkmalRepository() {
		final var factory = MerkmalRepositoryFactory.getNewInstance();

		final var repository = factory.getMerkmalRepository();

		assertThat(repository).isInstanceOf(MerkmalRepository.class);
	}

	@Test
	@DisplayName("getMerkmalRepository | Gibt MerkmalRepositoryImpl zurück")
	void getMerkmalRepository_returnsMerkmalRepositoryImpl() {
		final var factory = MerkmalRepositoryFactory.getNewInstance();

		final var repository = factory.getMerkmalRepository();

		assertThat(repository).isInstanceOf(MerkmalRepositoryImpl.class);
	}

	@Test
	@DisplayName("getMerkmalRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getMerkmalRepository_cachesInstance() {
		final var factory = MerkmalRepositoryFactory.getNewInstance();

		final var repository1 = factory.getMerkmalRepository();
		final var repository2 = factory.getMerkmalRepository();

		assertThat(repository1).isSameAs(repository2);
	}

	@Test
	@DisplayName("getMerkmalRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getMerkmalRepository_differentFactoriesCreateDifferentRepositories() {
		final var factory1 = MerkmalRepositoryFactory.getNewInstance();
		final var factory2 = MerkmalRepositoryFactory.getNewInstance();

		final var repository1 = factory1.getMerkmalRepository();
		final var repository2 = factory2.getMerkmalRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}
}
