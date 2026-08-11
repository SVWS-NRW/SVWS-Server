package de.svws_nrw.service.schule.kataloge.fachklasse;

import de.svws_nrw.mapper.schule.kataloge.fachklasse.FachklasseMapper;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.service.schule.SchuleService;
import de.svws_nrw.service.schule.SchuleServiceFactory;
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
class FachklasseServiceFactoryTest {

	@Mock
	private KatalogRepositoryFactory repoFactory;

	@Mock
	private FachklasseMapper mapper;

	@Mock
	private SchuleServiceFactory schuleServiceFactory;

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		final var factory = FachklasseServiceFactory.getNewInstance(repoFactory, mapper, schuleServiceFactory);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(FachklasseServiceFactory.class);
	}

	@Test
	@DisplayName("getService | Erfolg")
	void getService_success() {
		final var repository = mock(FachklasseRepository.class);
		final var schuleService = mock(SchuleService.class);
		final var factory = FachklasseServiceFactory.getNewInstance(repoFactory, mapper, schuleServiceFactory);

		when(repoFactory.getFachklasseRepository()).thenReturn(repository);
		when(schuleServiceFactory.getSchuleService()).thenReturn(schuleService);

		final var service = factory.getService();

		assertThat(service)
				.isNotNull()
				.isInstanceOf(FachklasseService.class);

		verify(repoFactory, times(1)).getFachklasseRepository();
		verify(schuleServiceFactory, times(1)).getSchuleService();
	}

	@Test
	@DisplayName("getService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getService_multipleCallsCreateNewInstances() {
		final var repository = mock(FachklasseRepository.class);
		final var schuleService = mock(SchuleService.class);
		final var factory = FachklasseServiceFactory.getNewInstance(repoFactory, mapper, schuleServiceFactory);

		when(repoFactory.getFachklasseRepository()).thenReturn(repository);
		when(schuleServiceFactory.getSchuleService()).thenReturn(schuleService);

		final var service1 = factory.getService();
		final var service2 = factory.getService();

		assertThat(service1)
				.isNotNull()
				.isNotSameAs(service2);

		assertThat(service2)
				.isNotNull();

		verify(repoFactory, times(2)).getFachklasseRepository();
		verify(schuleServiceFactory, times(2)).getSchuleService();
	}

}
