package de.svws_nrw.repo.lehrer.funktion;

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
class LehrerFunktionRepositoryFactoryTest {

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
		final var factory = LehrerFunktionRepositoryFactory.getNewInstance();

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(LehrerFunktionRepositoryFactory.class);
	}

	@Test
	@DisplayName("getNewInstance | Jeder Aufruf erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		final var factory1 = LehrerFunktionRepositoryFactory.getNewInstance();
		final var factory2 = LehrerFunktionRepositoryFactory.getNewInstance();

		assertThat(factory1).isNotSameAs(factory2);
	}

	@Test
	@DisplayName("getRepository | Erfolg")
	void getRepository_success() {
		final var factory = LehrerFunktionRepositoryFactory.getNewInstance();

		final var repository = factory.getRepository();

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(LehrerFunktionRepository.class)
				.isInstanceOf(LehrerFunktionRepositoryImpl.class);
	}

	@Test
	@DisplayName("getRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getRepository_cachesInstance() {
		final var factory = LehrerFunktionRepositoryFactory.getNewInstance();

		final var repository1 = factory.getRepository();
		final var repository2 = factory.getRepository();

		assertThat(repository1).isSameAs(repository2);
	}

	@Test
	@DisplayName("getRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getRepository_differentFactoriesCreateDifferentRepositories() {
		final var factory1 = LehrerFunktionRepositoryFactory.getNewInstance();
		final var factory2 = LehrerFunktionRepositoryFactory.getNewInstance();

		final var repository1 = factory1.getRepository();
		final var repository2 = factory2.getRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}
}
