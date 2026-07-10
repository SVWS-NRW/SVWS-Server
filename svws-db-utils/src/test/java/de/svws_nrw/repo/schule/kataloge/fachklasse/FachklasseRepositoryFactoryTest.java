package de.svws_nrw.repo.schule.kataloge.fachklasse;

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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class FachklasseRepositoryFactoryTest {

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
	void getNewInstance() {
		assertThat(FachklasseRepositoryFactory.getNewInstance())
				.isNotNull()
				.isInstanceOf(FachklasseRepositoryFactory.class);
	}

	@Test
	@DisplayName("getRepository")
	void getRepository() {
		final var factory = FachklasseRepositoryFactory.getNewInstance();

		assertThat(factory.getRepository())
				.isNotNull()
				.isInstanceOf(FachklasseRepository.class);
	}

}
