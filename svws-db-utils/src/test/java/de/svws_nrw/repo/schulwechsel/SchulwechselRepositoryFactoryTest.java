package de.svws_nrw.repo.schulwechsel;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.schulwechsel.abgang.SchulwechselAbgangRepository;
import de.svws_nrw.repo.schulwechsel.abgang.SchulwechselAbgangRepositoryImpl;
import de.svws_nrw.repo.schulwechsel.dokument.SchulwechselDokumentRepository;
import de.svws_nrw.repo.schulwechsel.dokument.SchulwechselDokumentRepositoryImpl;
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
class SchulwechselRepositoryFactoryTest {

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
		assertThat(SchulwechselRepositoryFactory.getNewInstance())
				.isNotNull()
				.isInstanceOf(SchulwechselRepositoryFactory.class);
	}

	@Test
	@DisplayName("getNewInstance | Jeder Aufruf erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		assertThat(SchulwechselRepositoryFactory.getNewInstance())
				.isNotSameAs(SchulwechselRepositoryFactory.getNewInstance());
	}

	@Test
	@DisplayName("getSchulwechselAbgangRepository | Erfolg")
	void getSchulwechselAbgangRepository_success() {
		assertThat(SchulwechselRepositoryFactory.getNewInstance().getSchulwechselAbgangRepository())
				.isNotNull()
				.isInstanceOf(SchulwechselAbgangRepository.class)
				.isInstanceOf(SchulwechselAbgangRepositoryImpl.class);
	}

	@Test
	@DisplayName("getSchulwechselAbgangRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getSchulwechselAbgangRepository_cachesInstance() {
		final var factory = SchulwechselRepositoryFactory.getNewInstance();

		assertThat(factory.getSchulwechselAbgangRepository())
				.isSameAs(factory.getSchulwechselAbgangRepository());
	}

	@Test
	@DisplayName("getSchulwechselAbgangRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getSchulwechselAbgangRepository_differentFactoriesCreateDifferentRepositories() {
		assertThat(SchulwechselRepositoryFactory.getNewInstance().getSchulwechselAbgangRepository())
				.isNotSameAs(SchulwechselRepositoryFactory.getNewInstance().getSchulwechselAbgangRepository());
	}

	@Test
	@DisplayName("getSchulwechselDokumentRepository | Erfolg")
	void getSchulwechselDokumentRepository_success() {
		assertThat(SchulwechselRepositoryFactory.getNewInstance().getSchulwechselDokumentRepository())
				.isNotNull()
				.isInstanceOf(SchulwechselDokumentRepository.class)
				.isInstanceOf(SchulwechselDokumentRepositoryImpl.class);
	}

	@Test
	@DisplayName("getSchulwechselDokumentRepository | Mehrfache Aufrufe geben gleiche Instanz zurück")
	void getSchulwechselDokumentRepository_cachesInstance() {
		final var factory = SchulwechselRepositoryFactory.getNewInstance();

		assertThat(factory.getSchulwechselDokumentRepository())
				.isSameAs(factory.getSchulwechselDokumentRepository());
	}

	@Test
	@DisplayName("getSchulwechselDokumentRepository | Verschiedene Factories erstellen verschiedene Repositories")
	void getSchulwechselDokumentRepository_differentFactoriesCreateDifferentRepositories() {
		assertThat(SchulwechselRepositoryFactory.getNewInstance().getSchulwechselDokumentRepository())
				.isNotSameAs(SchulwechselRepositoryFactory.getNewInstance().getSchulwechselDokumentRepository());
	}
}
