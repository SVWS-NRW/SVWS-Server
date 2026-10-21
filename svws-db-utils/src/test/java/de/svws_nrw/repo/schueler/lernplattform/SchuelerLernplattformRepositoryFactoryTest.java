package de.svws_nrw.repo.schueler.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;

/**
 * Tests für die Bereitstellung des {@link SchuelerLernplattformRepository} in der {@link SchuelerRepositoryFactory}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerLernplattformRepositoryFactoryTest {

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
	@DisplayName("getSchuelerLernplattformRepository | Gibt SchuelerLernplattformRepositoryImpl zurück")
	void getSchuelerLernplattformRepository_returnsRepository() {
		final var repository = SchuelerRepositoryFactory.getNewInstance().getSchuelerLernplattformRepository();

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(SchuelerLernplattformRepository.class)
				.isInstanceOf(SchuelerLernplattformRepositoryImpl.class);
	}

	@Test
	@DisplayName("getSchuelerLernplattformRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getSchuelerLernplattformRepository_cachesInstance() {
		final var factory = SchuelerRepositoryFactory.getNewInstance();

		assertThat(factory.getSchuelerLernplattformRepository()).isSameAs(factory.getSchuelerLernplattformRepository());
	}

	@Test
	@DisplayName("getSchuelerLernplattformRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getSchuelerLernplattformRepository_differentFactoriesCreateDifferentRepositories() {
		final var repository1 = SchuelerRepositoryFactory.getNewInstance().getSchuelerLernplattformRepository();
		final var repository2 = SchuelerRepositoryFactory.getNewInstance().getSchuelerLernplattformRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}

}
