package de.svws_nrw.service.schule.schulleitung;

import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.schule.leitungsfunktion.LehrerLeitungsfunktionRepository;
import de.svws_nrw.repo.schule.leitungsfunktion.LehrerLeitungsfunktionRepositoryFactory;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepository;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepositoryFactory;
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
class SchulleitungServiceFactoryTest {

	@Mock
	private SchulleitungRepositoryFactory repoFactory;

	@Mock
	private LehrerLeitungsfunktionRepositoryFactory leitungsfunktionRepoFactory;

	@Mock
	private SchulleitungMapper mapper;

	@Test
	@DisplayName("getNewInstance | Erfolg")
	void getNewInstance_success() {
		final var factory = SchulleitungServiceFactory.getNewInstance(repoFactory, leitungsfunktionRepoFactory, mapper);

		assertThat(factory)
				.isNotNull()
				.isInstanceOf(SchulleitungServiceFactory.class);
	}

	@Test
	@DisplayName("getSchulleitungService | Erfolg")
	void getSchulleitungService_success() {
		final var schulleitungRepository = mock(SchulleitungRepository.class);
		final var leitungsfunktionRepository = mock(LehrerLeitungsfunktionRepository.class);
		final var factory = SchulleitungServiceFactory.getNewInstance(repoFactory, leitungsfunktionRepoFactory, mapper);

		when(repoFactory.getSchulleitungRepository()).thenReturn(schulleitungRepository);
		when(leitungsfunktionRepoFactory.getLeitungsfunktionRepository()).thenReturn(leitungsfunktionRepository);

		final var service = factory.getSchulleitungService();

		assertThat(service)
				.isNotNull()
				.isInstanceOf(SchulleitungService.class);

		verify(repoFactory, times(1)).getSchulleitungRepository();
		verify(leitungsfunktionRepoFactory, times(1)).getLeitungsfunktionRepository();
	}

	@Test
	@DisplayName("getSchulleitungService | Mehrfache Aufrufe erstellen neue Instanzen")
	void getSchulleitungService_multipleCallsCreateNewInstances() {
		final var schulleitungRepository = mock(SchulleitungRepository.class);
		final var leitungsfunktionRepository = mock(LehrerLeitungsfunktionRepository.class);
		final var factory = SchulleitungServiceFactory.getNewInstance(repoFactory, leitungsfunktionRepoFactory, mapper);

		when(repoFactory.getSchulleitungRepository()).thenReturn(schulleitungRepository);
		when(leitungsfunktionRepoFactory.getLeitungsfunktionRepository()).thenReturn(leitungsfunktionRepository);

		final var service1 = factory.getSchulleitungService();
		final var service2 = factory.getSchulleitungService();

		assertThat(service1)
				.isNotNull()
				.isNotSameAs(service2);

		assertThat(service2).isNotNull();

		verify(repoFactory, times(2)).getSchulleitungRepository();
		verify(leitungsfunktionRepoFactory, times(2)).getLeitungsfunktionRepository();
	}
}
