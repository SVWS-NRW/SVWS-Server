package de.svws_nrw.service.schule.logoverwaltung;

import de.svws_nrw.mapper.schule.logoverwaltung.LogoverwaltungMapper;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepository;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
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
class LogoverwaltungServiceFactoryTest {

	@Mock
	private LogoverwaltungRepositoryFactory repositoryFactory;

	@Mock
	private LogoverwaltungMapper mapper;

	@Mock
	private EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		final var factory = LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, mapper, eigeneSchuleServiceFactory);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(LogoverwaltungServiceFactory.class);
	}

	@Test
	@DisplayName("getMerkmalService | Erfolg")
	void getService_success() {
		final var repository = mock(LogoverwaltungRepository.class);
		final var factory = LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, mapper, eigeneSchuleServiceFactory);

		when(repositoryFactory.getRepository()).thenReturn(repository);

		final var service = factory.getService();

		assertThat(service)
				.isNotNull()
				.isInstanceOf(LogoverwaltungService.class);

		verify(repositoryFactory, times(1)).getRepository();
	}

	@Test
	@DisplayName("getMerkmalService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getService_multipleCallsCreateNewInstances() {
		final var repository = mock(LogoverwaltungRepository.class);
		final var factory = LogoverwaltungServiceFactory.getNewInstance(repositoryFactory, mapper, eigeneSchuleServiceFactory);

		when(repositoryFactory.getRepository()).thenReturn(repository);

		final var service1 = factory.getService();
		final var service2 = factory.getService();

		assertThat(service1)
				.isNotNull()
				.isNotSameAs(service2);

		assertThat(service2)
				.isNotNull();

		verify(repositoryFactory, times(2)).getRepository();
	}
}
