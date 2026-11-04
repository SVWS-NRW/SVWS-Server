package de.svws_nrw.service.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.SchuljahresabschnittService;

/**
 * Test für die Klasse {@link KlassenServiceFactory}
 */
@ExtendWith(MockitoExtension.class)
class KlassenServiceFactoryTest {

	@Mock
	private KlassenRepositoryFactory klassenRepositoryFactory;

	@Mock
	private SchuelerRepositoryFactory schuelerRepositoryFactory;

	@Mock
	private EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	@Mock
	private KlassenRepository klassenRepository;

	@Mock
	private KlassenleitungenRepository klassenleitungenRepository;

	@Mock
	private SchuelerLernabschnittRepository lernabschnittRepository;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	private KlassenServiceFactory factory;

	@BeforeEach
	void setUp() {
		factory = KlassenServiceFactory.getNewInstance(klassenRepositoryFactory, schuelerRepositoryFactory, eigeneSchuleServiceFactory);
	}

	private void stubAbhaengigkeiten() {
		when(klassenRepositoryFactory.getKlassenRepository()).thenReturn(klassenRepository);
		when(klassenRepositoryFactory.getKlassenleitungenRepository()).thenReturn(klassenleitungenRepository);
		when(schuelerRepositoryFactory.getSchuelerLernabschnittRepository()).thenReturn(lernabschnittRepository);
		when(schuelerRepositoryFactory.getSchuelerRepository()).thenReturn(schuelerRepository);
		when(eigeneSchuleServiceFactory.getSchuleService()).thenReturn(eigeneSchuleService);
		when(eigeneSchuleServiceFactory.getSchuljahresabschnittService()).thenReturn(schuljahresabschnittService);
	}

	@Test
	@DisplayName("getNewInstance mit expliziten Abhängigkeiten liefert eine Factory")
	void getNewInstance() {
		assertThat(factory).isNotNull();
	}

	@Test
	@DisplayName("getKlassenService erstellt einen Service mit den Repositories und Services der Factories")
	void getKlassenService() {
		stubAbhaengigkeiten();

		assertThat(factory.getKlassenService()).isNotNull().isInstanceOf(KlassenService.class);

		verify(klassenRepositoryFactory, times(1)).getKlassenRepository();
		verify(klassenRepositoryFactory, times(1)).getKlassenleitungenRepository();
		verify(schuelerRepositoryFactory, times(1)).getSchuelerLernabschnittRepository();
		verify(schuelerRepositoryFactory, times(1)).getSchuelerRepository();
		verify(eigeneSchuleServiceFactory, times(1)).getSchuleService();
		verify(eigeneSchuleServiceFactory, times(1)).getSchuljahresabschnittService();
	}

	@Test
	@DisplayName("getKlassenService erstellt bei mehrfachen Aufrufen jeweils eine neue Instanz")
	void getKlassenServiceNeueInstanzen() {
		stubAbhaengigkeiten();

		final KlassenService service1 = factory.getKlassenService();
		final KlassenService service2 = factory.getKlassenService();

		assertThat(service1).isNotSameAs(service2);
		verify(klassenRepositoryFactory, times(2)).getKlassenRepository();
	}

}
