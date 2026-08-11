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
class SchuelerMerkmalRepositoryFactoryTest {

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
		assertThat(SchuelerRepositoryFactory.getNewInstance())
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
	@DisplayName("getSchuelerMerkmaleRepository | Gibt SchuelerMerkmaleRepositoryFactory zurück")
	void getSchuelerMerkmaleRepository_returnsSchuelerMerkmaleRepository() {
		final var factory = SchuelerRepositoryFactory.getNewInstance();

		final var repository = factory.getSchuelerMerkmaleRepository();

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalRepository.class)
				.isInstanceOf(SchuelerMerkmalRepositoryImpl.class);
	}

	@Test
	@DisplayName("getSchuelerMerkmaleRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getSchuelerMerkmaleRepository_cachesInstance() {
		final var factory = SchuelerRepositoryFactory.getNewInstance();

		final var repository1 = factory.getSchuelerMerkmaleRepository();
		final var repository2 = factory.getSchuelerMerkmaleRepository();

		assertThat(repository1).isSameAs(repository2);
	}

	@Test
	@DisplayName("getSchuelerMerkmaleRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getSchuelerMerkmaleRepository_differentFactoriesCreateDifferentRepositories() {
		final var factory1 = SchuelerRepositoryFactory.getNewInstance();
		final var factory2 = SchuelerRepositoryFactory.getNewInstance();

		final var repository1 = factory1.getSchuelerMerkmaleRepository();
		final var repository2 = factory2.getSchuelerMerkmaleRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}
}
