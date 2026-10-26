package de.svws_nrw.repo.schueler.einwilligung;

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
 * Tests für die Bereitstellung des {@link SchuelerEinwilligungRepository} in der {@link SchuelerRepositoryFactory}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerEinwilligungRepositoryFactoryTest {

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
	@DisplayName("getSchuelerEinwilligungRepository | Gibt SchuelerEinwilligungRepositoryImpl zurück")
	void getSchuelerEinwilligungRepository_returnsRepository() {
		final var repository = SchuelerRepositoryFactory.getNewInstance().getSchuelerEinwilligungRepository();

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(SchuelerEinwilligungRepository.class)
				.isInstanceOf(SchuelerEinwilligungRepositoryImpl.class);
	}

	@Test
	@DisplayName("getSchuelerEinwilligungRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getSchuelerEinwilligungRepository_cachesInstance() {
		final var factory = SchuelerRepositoryFactory.getNewInstance();

		assertThat(factory.getSchuelerEinwilligungRepository()).isSameAs(factory.getSchuelerEinwilligungRepository());
	}

	@Test
	@DisplayName("getSchuelerEinwilligungRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getSchuelerEinwilligungRepository_differentFactoriesCreateDifferentRepositories() {
		final var repository1 = SchuelerRepositoryFactory.getNewInstance().getSchuelerEinwilligungRepository();
		final var repository2 = SchuelerRepositoryFactory.getNewInstance().getSchuelerEinwilligungRepository();

		assertThat(repository1).isNotSameAs(repository2);
	}

}
