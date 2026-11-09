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
import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.gost.GostSchuelerWiederholungRepository;
import de.svws_nrw.repo.kurse.KursSchuelerRepository;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.einwilligung.SchuelerEinwilligungRepository;
import de.svws_nrw.repo.schueler.lernplattform.SchuelerLernplattformRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.einwilligungsart.EinwilligungsartRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformCredentialRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformRepository;
import de.svws_nrw.service.klassen.KlassenService;
import de.svws_nrw.service.klassen.KlassenServiceFactory;
import de.svws_nrw.service.kurse.KursService;
import de.svws_nrw.service.kurse.KursServiceFactory;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangService;
import de.svws_nrw.repo.gost.GostJahrgangsdatenRepository;
import de.svws_nrw.repo.gost.GostJahrgangsinformationenRepository;

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

	@Mock
	private KurseRepositoryFactory kurseRepositoryFactory;

	@Mock
	private GostRepositoryFactory gostRepositoryFactory;

	@Mock
	private KlassenServiceFactory klassenServiceFactory;

	@Mock
	private KursServiceFactory kursServiceFactory;

	private SchuelerServiceFactory factory;

	@BeforeEach
	void setUp() {
		final var factories = new SchuelerFactories(benutzerRepositoryFactory, schuelerRepositoryFactory, katalogRepositoryFactory,
				katalogServiceFactory, eigeneSchuleRepositoryFactory, eigeneSchuleServiceFactory, kurseRepositoryFactory, gostRepositoryFactory);
		factory = SchuelerServiceFactory.getNewInstance(factories, klassenServiceFactory, kursServiceFactory);
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

	@Test
	@DisplayName("getSchuelerEinwilligungService | Erstellt den Service und ruft alle benötigten Repositories ab")
	void getSchuelerEinwilligungService_success() {
		final var schuelerEinwilligungRepository = mock(SchuelerEinwilligungRepository.class);
		final var schuelerRepository = mock(SchuelerRepository.class);
		final var einwilligungsartRepository = mock(EinwilligungsartRepository.class);
		when(schuelerRepositoryFactory.getSchuelerEinwilligungRepository()).thenReturn(schuelerEinwilligungRepository);
		when(schuelerRepositoryFactory.getSchuelerRepository()).thenReturn(schuelerRepository);
		when(katalogRepositoryFactory.getEinwilligungsartRepository()).thenReturn(einwilligungsartRepository);

		final var service = factory.getSchuelerEinwilligungService();

		assertThat(service).isNotNull();
		verify(schuelerRepositoryFactory).getSchuelerEinwilligungRepository();
		verify(schuelerRepositoryFactory).getSchuelerRepository();
		verify(katalogRepositoryFactory).getEinwilligungsartRepository();
	}

	@Test
	@DisplayName("getSchuelerListeEintragService | Erstellt den Service und ruft alle benötigten Repositories und Services ab")
	void getSchuelerListeEintragService_success() {
		final var schuelerRepository = mock(SchuelerRepository.class);
		final var lernabschnittRepository = mock(SchuelerLernabschnittRepository.class);
		final var kursSchuelerRepository = mock(KursSchuelerRepository.class);
		final var jahrgangRepository = mock(JahrgangRepository.class);
		final var wiederholungRepository = mock(GostSchuelerWiederholungRepository.class);
		final var eigeneSchuleService = mock(EigeneSchuleService.class);
		final var schuljahresabschnittService = mock(SchuljahresabschnittService.class);
		when(schuelerRepositoryFactory.getSchuelerRepository()).thenReturn(schuelerRepository);
		when(schuelerRepositoryFactory.getSchuelerLernabschnittRepository()).thenReturn(lernabschnittRepository);
		when(kurseRepositoryFactory.getKursSchuelerRepository()).thenReturn(kursSchuelerRepository);
		when(katalogRepositoryFactory.getJahrgangRepository()).thenReturn(jahrgangRepository);
		when(gostRepositoryFactory.getGostSchuelerWiederholungRepository()).thenReturn(wiederholungRepository);
		when(eigeneSchuleServiceFactory.getSchuleService()).thenReturn(eigeneSchuleService);
		when(eigeneSchuleServiceFactory.getSchuljahresabschnittService()).thenReturn(schuljahresabschnittService);

		final var service = factory.getSchuelerListeEintragService();

		assertThat(service).isNotNull();
		verify(schuelerRepositoryFactory).getSchuelerRepository();
		verify(schuelerRepositoryFactory).getSchuelerLernabschnittRepository();
		verify(kurseRepositoryFactory).getKursSchuelerRepository();
		verify(katalogRepositoryFactory).getJahrgangRepository();
		verify(gostRepositoryFactory).getGostSchuelerWiederholungRepository();
		verify(eigeneSchuleServiceFactory).getSchuleService();
		verify(eigeneSchuleServiceFactory).getSchuljahresabschnittService();
	}

	@Test
	@DisplayName("getSchuelerListeService | Erstellt den Service und ruft alle benötigten Factories ab")
	void getSchuelerListeService_success() {
		final var klassenService = mock(KlassenService.class);
		final var kursService = mock(KursService.class);
		final var jahrgangService = mock(JahrgangService.class);
		final var jahrgangRepository = mock(JahrgangRepository.class);
		final var jahrgangsdatenRepository = mock(GostJahrgangsdatenRepository.class);
		final var jahrgangsinformationenRepository = mock(GostJahrgangsinformationenRepository.class);
		final var eigeneSchuleService = mock(EigeneSchuleService.class);
		final var schuljahresabschnittService = mock(SchuljahresabschnittService.class);
		when(klassenServiceFactory.getKlassenService()).thenReturn(klassenService);
		when(kursServiceFactory.getKursService()).thenReturn(kursService);
		when(katalogServiceFactory.getJahrgangService()).thenReturn(jahrgangService);
		when(katalogRepositoryFactory.getJahrgangRepository()).thenReturn(jahrgangRepository);
		when(gostRepositoryFactory.getGostJahrgangsdatenRepository()).thenReturn(jahrgangsdatenRepository);
		when(gostRepositoryFactory.getGostJahrgangsinformationenRepository()).thenReturn(jahrgangsinformationenRepository);
		when(eigeneSchuleServiceFactory.getSchuleService()).thenReturn(eigeneSchuleService);
		when(eigeneSchuleServiceFactory.getSchuljahresabschnittService()).thenReturn(schuljahresabschnittService);

		final var service = factory.getSchuelerListeService();

		assertThat(service).isNotNull();
		verify(klassenServiceFactory).getKlassenService();
		verify(kursServiceFactory).getKursService();
		verify(katalogServiceFactory).getJahrgangService();
		verify(gostRepositoryFactory).getGostJahrgangsdatenRepository();
		verify(gostRepositoryFactory).getGostJahrgangsinformationenRepository();
	}

}
