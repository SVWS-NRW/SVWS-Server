package de.svws_nrw.service.schueler.foto;

import de.svws_nrw.mapper.schueler.foto.SchuelerFotoMapper;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schueler.foto.SchuelerFotoRepository;
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
class SchuelerFotoServiceFactoryTest {

	@Mock
	private SchuelerRepositoryFactory repoFactory;

	@Mock
	private SchuelerFotoMapper mapper;

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		final var factory = SchuelerFotoServiceFactory.getNewInstance(repoFactory, mapper);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchuelerFotoServiceFactory.class);
	}

	@Test
	@DisplayName("getSchuelerFotoService | Erfolg")
	void getSchuelerFotoService_success() {
		final var repository = mock(SchuelerFotoRepository.class);
		final var factory = SchuelerFotoServiceFactory.getNewInstance(repoFactory, mapper);

		when(repoFactory.getSchuelerFotoRepository()).thenReturn(repository);

		final var service = factory.getSchuelerFotoService();

		assertThat(service)
				.isNotNull()
				.isInstanceOf(SchuelerFotoService.class);

		verify(repoFactory, times(1)).getSchuelerFotoRepository();
	}

	@Test
	@DisplayName("getSchuelerFotoService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getSchuelerFotoService_multipleCallsCreateNewInstances() {
		final var repository = mock(SchuelerFotoRepository.class);
		final var factory = SchuelerFotoServiceFactory.getNewInstance(repoFactory, mapper);

		when(repoFactory.getSchuelerFotoRepository()).thenReturn(repository);

		final var service1 = factory.getSchuelerFotoService();
		final var service2 = factory.getSchuelerFotoService();

		assertThat(service1)
				.isNotNull()
				.isNotSameAs(service2);

		assertThat(service2)
				.isNotNull();

		verify(repoFactory, times(2)).getSchuelerFotoRepository();
	}
}
