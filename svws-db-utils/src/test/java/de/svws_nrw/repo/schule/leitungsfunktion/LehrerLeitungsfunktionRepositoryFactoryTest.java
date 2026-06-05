package de.svws_nrw.repo.schule.leitungsfunktion;

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
class LehrerLeitungsfunktionRepositoryFactoryTest {

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
		assertThat(LehrerLeitungsfunktionRepositoryFactory.getNewInstance())
				.isNotNull()
				.isInstanceOf(LehrerLeitungsfunktionRepositoryFactory.class);
	}

	@Test
	@DisplayName("getNewInstance | Jeder Aufruf erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		final var factory1 = LehrerLeitungsfunktionRepositoryFactory.getNewInstance();
		final var factory2 = LehrerLeitungsfunktionRepositoryFactory.getNewInstance();

		assertThat(factory1).isNotSameAs(factory2);
	}

	@Test
	@DisplayName("getLeitungsfunktionRepository | Gibt LeitungsfunktionRepository zurück")
	void getLeitungsfunktionRepository_returnsLeitungsfunktionRepository() {
		final var factory = LehrerLeitungsfunktionRepositoryFactory.getNewInstance();

		final var repository = factory.getLeitungsfunktionRepository();

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(LehrerLeitungsfunktionRepository.class)
				.isInstanceOf(LehrerLeitungsfunktionRepositoryImpl.class);
	}

	@Test
	@DisplayName("getLeitungsfunktionRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getLeitungsfunktionRepository_cachesInstance() {
		final var factory = LehrerLeitungsfunktionRepositoryFactory.getNewInstance();

		final var repository1 = factory.getLeitungsfunktionRepository();
		final var repository2 = factory.getLeitungsfunktionRepository();

		assertThat(repository1).isSameAs(repository2);
	}

	@Test
	@DisplayName("getLeitungsfunktionRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getLeitungsfunktionRepository_differentFactoriesCreateDifferentRepositories() {
		final var factory1 = LehrerLeitungsfunktionRepositoryFactory.getNewInstance();
		final var factory2 = LehrerLeitungsfunktionRepositoryFactory.getNewInstance();

		final var repository1 = factory1.getLeitungsfunktionRepository();
		final var repository2 = factory2.getLeitungsfunktionRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}
}
