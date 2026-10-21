package de.svws_nrw.service.schueler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schueler.lernplattform.SchuelerLernplattformRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformCredentialRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformRepository;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;

/**
 * Tests für die {@link SchuelerServiceFactory}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerServiceFactoryTest {

	@Mock
	private BenutzerRepositoryFactory benutzerRepositoryFactory;

	@Mock
	private SchuelerRepositoryFactory schuelerRepositoryFactory;

	@Mock
	private KatalogRepositoryFactory katalogRepositoryFactory;

	@Mock
	private KatalogServiceFactory katalogServiceFactory;

	@Mock
	private EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;

	@Mock
	private EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	private SchuelerServiceFactory factory;

	@BeforeEach
	void setUp() {
		factory = SchuelerServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepositoryFactory, katalogRepositoryFactory,
				katalogServiceFactory, eigeneSchuleRepositoryFactory, eigeneSchuleServiceFactory);
	}

	@Test
	@DisplayName("getNewInstance | Erstellt eine Factory")
	void getNewInstance_success() {
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("getSchuelerLernplattformService | Erstellt den Service und ruft alle benötigten Repositories ab")
	void getSchuelerLernplattformService_success() {
		final var schuelerLernplattformRepository = mock(SchuelerLernplattformRepository.class);
		final var schuelerRepository = mock(SchuelerRepository.class);
		final var lernplattformRepository = mock(LernplattformRepository.class);
		final var credentialRepository = mock(LernplattformCredentialRepository.class);
		when(schuelerRepositoryFactory.getSchuelerLernplattformRepository()).thenReturn(schuelerLernplattformRepository);
		when(schuelerRepositoryFactory.getSchuelerRepository()).thenReturn(schuelerRepository);
		when(katalogRepositoryFactory.getLernplattformRepository()).thenReturn(lernplattformRepository);
		when(katalogRepositoryFactory.getLernplattformCredentialRepository()).thenReturn(credentialRepository);

		final var service = factory.getSchuelerLernplattformService();

		assertThat(service).isNotNull();
		verify(schuelerRepositoryFactory).getSchuelerLernplattformRepository();
		verify(schuelerRepositoryFactory).getSchuelerRepository();
		verify(katalogRepositoryFactory).getLernplattformRepository();
		verify(katalogRepositoryFactory).getLernplattformCredentialRepository();
	}

}
