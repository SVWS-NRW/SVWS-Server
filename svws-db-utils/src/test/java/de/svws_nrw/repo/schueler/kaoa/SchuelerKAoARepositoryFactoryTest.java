package de.svws_nrw.repo.schueler.kaoa;

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
 * Tests für die Bereitstellung des {@link SchuelerKAoARepository} in der {@link SchuelerRepositoryFactory}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerKAoARepositoryFactoryTest {

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
	@DisplayName("getSchuelerKAoARepository | Gibt SchuelerKAoARepositoryImpl zurück und cached die Instanz")
	void getSchuelerKAoARepository_returnsCachedRepository() {
		final var factory = SchuelerRepositoryFactory.getNewInstance();

		final var repository = factory.getSchuelerKAoARepository();

		assertThat(repository)
				.isInstanceOf(SchuelerKAoARepositoryImpl.class)
				.isSameAs(factory.getSchuelerKAoARepository());
	}

}
