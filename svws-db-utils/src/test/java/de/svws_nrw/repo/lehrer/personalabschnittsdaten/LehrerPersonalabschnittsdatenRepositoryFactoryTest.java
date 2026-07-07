package de.svws_nrw.repo.lehrer.personalabschnittsdaten;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

class LehrerPersonalabschnittsdatenRepositoryFactoryTest {


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
		final var factory = LehrerPersonalabschnittsdatenRepositoryFactory.getNewInstance();

		assertThat(factory).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenRepositoryFactory.class);
	}

	@Test
	@DisplayName("getNewInstance | Erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		final var factory1 = LehrerPersonalabschnittsdatenRepositoryFactory.getNewInstance();
		final var factory2 = LehrerPersonalabschnittsdatenRepositoryFactory.getNewInstance();

		assertThat(factory1).isNotSameAs(factory2);
	}

	@Test
	@DisplayName("getRepo | Erfolg")
	void getRepo_success() {
		final var factory = LehrerPersonalabschnittsdatenRepositoryFactory.getNewInstance();

		final var repo = factory.getRepo();

		assertThat(repo).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenRepository.class);
	}

	@Test
	@DisplayName("getRepo | Gibt gecachte Instanz zurück")
	void getRepo_cachesInstance() {
		final var factory = LehrerPersonalabschnittsdatenRepositoryFactory.getNewInstance();

		final var repo1 = factory.getRepo();
		final var repo2 = factory.getRepo();

		assertThat(repo1).isSameAs(repo2);
	}

	@Test
	@DisplayName("getRepo | Verschiedene Factories erstellen verschiedene Repositories")
	void getRepo_differentFactoriesCreateDifferentRepositories() {
		final var factory1 = LehrerPersonalabschnittsdatenRepositoryFactory.getNewInstance();
		final var factory2 = LehrerPersonalabschnittsdatenRepositoryFactory.getNewInstance();

		final var repo1 = factory1.getRepo();
		final var repo2 = factory2.getRepo();

		assertThat(repo1).isNotSameAs(repo2);
	}
}
