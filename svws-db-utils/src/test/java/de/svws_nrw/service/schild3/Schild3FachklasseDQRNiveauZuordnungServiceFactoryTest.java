package de.svws_nrw.service.schild3;

import de.svws_nrw.mapper.Schild3FachklasseDQRNiveauZuordnungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Schild3FachklasseDQRNiveauZuordnungServiceFactoryTest {

	@InjectMocks
	private Schild3FachklasseDQRNiveauZuordnungServiceFactory cut;

	@Mock
	private Schild3FachklasseDQRNiveauZuordnungMapper mapperMock;

	@Mock
	private EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactoryMock;

	@Mock
	private EigeneSchuleServiceFactory eigeneSchuleServiceFactoryMock;

	@Test
	@DisplayName("Test: Prüfe, ob die Factory korrekt initialisiert wird")
	void getNewInstance() {
		final var factory = Schild3FachklasseDQRNiveauZuordnungServiceFactory.getNewInstance(mapperMock, eigeneSchuleRepositoryFactoryMock,
				eigeneSchuleServiceFactoryMock);
		assertNotNull(factory);
	}

	@Test
	@DisplayName("Test: Prüfe, ob Service korrekt initialisiert wird")
	void getSchild3FachklasseDQRNiveauZuordnungService() {
		final var service = cut.getSchild3FachklasseDQRNiveauZuordnungService();
		assertNotNull(service);

		verify(eigeneSchuleRepositoryFactoryMock, times(1)).getSchuleRepository();
		verify(eigeneSchuleServiceFactoryMock, times(1)).getSchuljahresabschnittService();
	}

}
