package de.svws_nrw.repo.schule.schulleitung;

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
class SchulleitungRepositoryFactoryTest {

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
		assertThat(SchulleitungRepositoryFactory.getNewInstance())
				.isNotNull()
				.isInstanceOf(SchulleitungRepositoryFactory.class);
	}

	@Test
	@DisplayName("getNewInstance | Jeder Aufruf erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		final var factory1 = SchulleitungRepositoryFactory.getNewInstance();
		final var factory2 = SchulleitungRepositoryFactory.getNewInstance();

		assertThat(factory1).isNotSameAs(factory2);
	}

	@Test
	@DisplayName("getSchulleitungRepository | Gibt SchulleitungRepository zurück")
	void getSchulleitungRepository_returnsSchulleitungRepository() {
		final var factory = SchulleitungRepositoryFactory.getNewInstance();

		final var repository = factory.getSchulleitungRepository();

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(SchulleitungRepository.class)
				.isInstanceOf(SchulleitungRepositoryImpl.class);
	}

	@Test
	@DisplayName("getSchulleitungRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getSchulleitungRepository_cachesInstance() {
		final var factory = SchulleitungRepositoryFactory.getNewInstance();

		final var repository1 = factory.getSchulleitungRepository();
		final var repository2 = factory.getSchulleitungRepository();

		assertThat(repository1).isSameAs(repository2);
	}

	@Test
	@DisplayName("getSchulleitungRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getSchulleitungRepository_differentFactoriesCreateDifferentRepositories() {
		final var factory1 = SchulleitungRepositoryFactory.getNewInstance();
		final var factory2 = SchulleitungRepositoryFactory.getNewInstance();

		final var repository1 = factory1.getSchulleitungRepository();
		final var repository2 = factory2.getSchulleitungRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}
}
