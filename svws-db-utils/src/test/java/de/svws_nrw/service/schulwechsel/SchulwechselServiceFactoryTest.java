package de.svws_nrw.service.schulwechsel;

import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schulwechsel.SchulwechselRepositoryFactory;
import de.svws_nrw.repo.schulwechsel.abgang.SchulwechselAbgangRepository;
import de.svws_nrw.repo.schulwechsel.dokument.SchulwechselDokumentRepository;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangService;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchulwechselServiceFactoryTest {

	@Mock
	private SchulwechselRepositoryFactory repoFactory;

	@Mock
	private SchuelerRepositoryFactory schuelerRepoFactory;

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		assertThat(SchulwechselServiceFactory.getNewInstance(repoFactory, schuelerRepoFactory))
				.isNotNull()
				.isInstanceOf(SchulwechselServiceFactory.class);
	}

	@Test
	@DisplayName("getSchulwechselAbgangService | Erfolg")
	void getSchulwechselAbgangService_success() {
		final var repository = mock(SchulwechselAbgangRepository.class);
		final var factory = SchulwechselServiceFactory.getNewInstance(repoFactory, schuelerRepoFactory);

		when(repoFactory.getSchulwechselAbgangRepository()).thenReturn(repository);

		assertThat(factory.getSchulwechselAbgangService())
				.isNotNull()
				.isInstanceOf(SchulwechselAbgangService.class);

		verify(repoFactory, times(1)).getSchulwechselAbgangRepository();
	}

	@Test
	@DisplayName("getSchulwechselAbgangService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getSchulwechselAbgangService_multipleCallsCreateNewInstances() {
		final var repository = mock(SchulwechselAbgangRepository.class);
		final var factory = SchulwechselServiceFactory.getNewInstance(repoFactory, schuelerRepoFactory);

		when(repoFactory.getSchulwechselAbgangRepository()).thenReturn(repository);

		final var service1 = factory.getSchulwechselAbgangService();
		final var service2 = factory.getSchulwechselAbgangService();

		assertThat(service1).isNotNull().isNotSameAs(service2);
		assertThat(service2).isNotNull();

		verify(repoFactory, times(2)).getSchulwechselAbgangRepository();
	}

	@Test
	@DisplayName("getSchulwechselDokumentService | Erfolg")
	void getSchulwechselDokumentService_success() {
		final var repository = mock(SchulwechselDokumentRepository.class);
		final var factory = SchulwechselServiceFactory.getNewInstance(repoFactory, schuelerRepoFactory);

		when(repoFactory.getSchulwechselDokumentRepository()).thenReturn(repository);

		assertThat(factory.getSchulwechselDokumentService())
				.isNotNull()
				.isInstanceOf(SchulwechselDokumentService.class);

		verify(repoFactory, times(1)).getSchulwechselDokumentRepository();
	}

	@Test
	@DisplayName("getSchulwechselDokumentService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getSchulwechselDokumentService_multipleCallsCreateNewInstances() {
		final var repository = mock(SchulwechselDokumentRepository.class);
		final var factory = SchulwechselServiceFactory.getNewInstance(repoFactory, schuelerRepoFactory);

		when(repoFactory.getSchulwechselDokumentRepository()).thenReturn(repository);

		final var service1 = factory.getSchulwechselDokumentService();
		final var service2 = factory.getSchulwechselDokumentService();

		assertThat(service1).isNotNull().isNotSameAs(service2);
		assertThat(service2).isNotNull();

		verify(repoFactory, times(2)).getSchulwechselDokumentRepository();
	}
}
