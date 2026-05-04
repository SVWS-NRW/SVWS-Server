package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;

import de.svws_nrw.repo.schueler.schulbesuch.SchuelerMerkmalRepository;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerMerkmaleRepositoryFactory;
import de.svws_nrw.repo.schule.merkmale.MerkmalRepository;
import de.svws_nrw.repo.schule.merkmale.MerkmalRepositoryFactory;
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
class SchuelerMerkmalServiceFactoryTest {

	@Mock
	private SchuelerMerkmaleRepositoryFactory repoFactory;

	@Mock
	private MerkmalRepositoryFactory merkmalRepoFactory;

	@Mock
	private SchuelerMerkmalMapper mapper;

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		final var factory = SchuelerMerkmalServiceFactory.getNewInstance(repoFactory, merkmalRepoFactory, mapper);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalServiceFactory.class);
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var factory = new SchuelerMerkmalServiceFactory(repoFactory, merkmalRepoFactory, mapper);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalServiceFactory.class);
	}

	@Test
	@DisplayName("getSchuelerMerkmalService | Erfolg")
	void getSchuelerMerkmalService_success() {
		final var schuelerMerkmaleRepository = mock(SchuelerMerkmalRepository.class);
		final var merkmalRepository = mock(MerkmalRepository.class);
		final var factory = SchuelerMerkmalServiceFactory.getNewInstance(repoFactory, merkmalRepoFactory, mapper);

		when(repoFactory.getSchuelerMerkmaleRepository()).thenReturn(schuelerMerkmaleRepository);
		when(merkmalRepoFactory.getMerkmalRepository()).thenReturn(merkmalRepository);

		final var service = factory.getSchuelerMerkmalService();

		assertThat(service)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalService.class);

		verify(repoFactory, times(1)).getSchuelerMerkmaleRepository();
		verify(merkmalRepoFactory, times(1)).getMerkmalRepository();
	}

	@Test
	@DisplayName("getSchuelerMerkmalService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getSchuelerMerkmalService_multipleCallsCreateNewInstances() {
		final var schuelerMerkmaleRepository = mock(SchuelerMerkmalRepository.class);
		final var merkmalRepository = mock(MerkmalRepository.class);
		final var factory = SchuelerMerkmalServiceFactory.getNewInstance(repoFactory, merkmalRepoFactory, mapper);

		when(repoFactory.getSchuelerMerkmaleRepository()).thenReturn(schuelerMerkmaleRepository);
		when(merkmalRepoFactory.getMerkmalRepository()).thenReturn(merkmalRepository);

		final var service1 = factory.getSchuelerMerkmalService();
		final var service2 = factory.getSchuelerMerkmalService();

		assertThat(service1)
				.isNotNull()
				.isNotSameAs(service2);

		assertThat(service2).isNotNull();

		verify(repoFactory, times(2)).getSchuelerMerkmaleRepository();
		verify(merkmalRepoFactory, times(2)).getMerkmalRepository();
	}
}
