package de.svws_nrw.service.statistik;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;

/**
 * Testklasse für die {@link StatistikServiceFactory} zur Prüfung der Instanziierung der Services
 */
@ExtendWith(MockitoExtension.class)
class StatistikServiceFactoryTest {


	@Mock
	private BenutzerRepositoryFactory repoBenutzerFactory;

	@Mock
	private KatalogRepositoryFactory repoKatalogeFactory;

	@Mock
	private KlassenRepositoryFactory repoKlassenFactory;

	@Mock
	private KurseRepositoryFactory repoKurseFactory;

	@Mock
	private LehrerRepositoryFactory repoLehrerFactory;

	@Mock
	private SchuelerRepositoryFactory repoSchuelerFactory;

	@Mock
	private EigeneSchuleRepositoryFactory repoSchuleFactory;

	@Mock
	private LehrerServiceFactory serviceFactoryLehrer;

	@Mock
	private EigeneSchuleServiceFactory serviceFactorySchule;

	@InjectMocks
	private StatistikServiceFactory factory;

	@Test
	@DisplayName("Test: getSchuleStatistikService nutzt das Repository für Schuldaten und den Service für die Schuljahresabschnitte")
	void testGetSchuleStatistikService() {
		final var service = factory.getSchuleStatistikService();
		assertNotNull(service);
		verify(repoSchuleFactory).getSchuleRepository();
		verify(serviceFactorySchule).getSchuljahresabschnittService();
	}

	@Test
	@DisplayName("Test: getLehrerStatistikService nutzt das Repository für Schuldaten, für Lehrerdaten und Lehrerabschnittsdaten und die Services für die Lehrämter und die Anrechnungsstunden")
	void testGetLehrerStatistikService() {
		final var service = factory.getLehrerStatistikService();
		assertNotNull(service);
		verify(repoSchuleFactory, atLeastOnce()).getSchuleRepository();
		verify(repoLehrerFactory).getLehrerRepository();
		verify(repoLehrerFactory).getLehrerPersonalabschnittsdatenRepository();
		verify(serviceFactoryLehrer).getLehrerLehramtService();
		verify(serviceFactoryLehrer).getLehrerPersonalabschnittsdatenAnrechnungsstundenService();
	}

	@Test
	@DisplayName("Test: getSchuelerStatistikService nutzt das Repository für Schüler, Schülerlernabschnitte, Abiturdaten und deren Fächer sowie das allgemeine Fächer-Repository")
	void testGetSchuelerStatistikService() {
		final var service = factory.getSchuelerStatistikService();
		assertNotNull(service);
		verify(repoSchuelerFactory).getSchuelerRepository();
		verify(repoSchuelerFactory).getSchuelerLernabschnittRepository();
		verify(repoSchuelerFactory).getSchuelerAbiturRepository();
		verify(repoSchuelerFactory).getSchuelerAbiturFachRepository();
		verify(repoKatalogeFactory).getFachRepository();
	}

	@Test
	@DisplayName("Test: getKlassenStatistikService nutzt das Repository für Schuldaten, Klassen, Klassenleitungen und SchülerLernabschnitte")
	void testGetKlassenStatistikService() {
		final var service = factory.getKlassenStatistikService();
		assertNotNull(service);
		verify(repoSchuleFactory, atLeastOnce()).getSchuleRepository();
		verify(repoKlassenFactory).getKlassenRepository();
		verify(repoKlassenFactory).getKlassenleitungenRepository();
		verify(repoSchuelerFactory, atLeastOnce()).getSchuelerLernabschnittRepository();
	}

	@Test
	@DisplayName("Test: getReligionStatistikService nutzt das Repository zu Religionen")
	void testGetReligionStatistikService() {
		assertNotNull(factory.getReligionStatistikService());
		verify(repoKatalogeFactory).getReligionRepository();
	}

	@Test
	@DisplayName("Test: getOrteStatistikService nutzt das Repository zu Orten")
	void testGetOrteStatistikService() {
		assertNotNull(factory.getOrteStatistikService());
		verify(repoKatalogeFactory).getOrtRepository();
	}

	@Test
	@DisplayName("Test: getJahrgaengeStatistikService nutzt das Repository zu Jahrgängen")
	void testGetJahrgaengeStatistikService() {
		assertNotNull(factory.getJahrgaengeStatistikService());
		verify(repoKatalogeFactory).getJahrgangRepository();
	}

	@Test
	@DisplayName("Test: getFoerderschwerpunkteStatistikService nutzt das Repository zu Förderschwerpunkten")
	void testGetFoerderschwerpunkteStatistikService() {
		assertNotNull(factory.getFoerderschwerpunkteStatistikService());
		verify(repoKatalogeFactory).getFoerderschwerpunktRepository();
	}

	@Test
	@DisplayName("Test: getStatistikService aggregiert alle Unter-Statistik-Services")
	void testGetStatistikService() {
		final var service = factory.getStatistikService();
		assertNotNull(service);

		// Prüft, ob für den Statistik-Service auf die benötigten Factories zugegriffen wird
		verify(repoSchuleFactory, atLeastOnce()).getSchuleRepository();
		verify(repoLehrerFactory, atLeastOnce()).getLehrerRepository();
		verify(repoSchuelerFactory, atLeastOnce()).getSchuelerRepository();
		verify(repoKatalogeFactory, atLeastOnce()).getReligionRepository();
	}

}
