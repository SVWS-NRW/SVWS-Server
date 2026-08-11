package de.svws_nrw.service.schule.merkmale;

import de.svws_nrw.mapper.schule.merkmale.MerkmalMapper;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.merkmal.MerkmalRepository;
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
class MerkmalServiceFactoryTest {

	@Mock
	private KatalogRepositoryFactory repositoryFactory;

	@Mock
	private MerkmalMapper mapper;

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		final var factory = MerkmalServiceFactory.getNewInstance(repositoryFactory, mapper);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(MerkmalServiceFactory.class);
	}

	@Test
	@DisplayName("getMerkmalService | Erfolg")
	void getMerkmalService_success() {
		final var repository = mock(MerkmalRepository.class);
		final var factory = MerkmalServiceFactory.getNewInstance(repositoryFactory, mapper);

		when(repositoryFactory.getMerkmalRepository()).thenReturn(repository);

		final var service = factory.getMerkmalService();

		assertThat(service)
				.isNotNull()
				.isInstanceOf(MerkmalService.class);

		verify(repositoryFactory, times(1)).getMerkmalRepository();
	}

	@Test
	@DisplayName("getMerkmalService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getMerkmalService_multipleCallsCreateNewInstances() {
		final var repository = mock(MerkmalRepository.class);
		final var factory = MerkmalServiceFactory.getNewInstance(repositoryFactory, mapper);

		when(repositoryFactory.getMerkmalRepository()).thenReturn(repository);

		final var service1 = factory.getMerkmalService();
		final var service2 = factory.getMerkmalService();

		assertThat(service1)
				.isNotNull()
				.isNotSameAs(service2);

		assertThat(service2)
				.isNotNull();

		verify(repositoryFactory, times(2)).getMerkmalRepository();
	}
}
