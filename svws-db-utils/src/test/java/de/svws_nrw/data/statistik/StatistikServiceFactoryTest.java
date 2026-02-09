package de.svws_nrw.data.statistik;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.data.lehrer.LehrerServiceFactory;
import de.svws_nrw.data.schule.SchuleServiceFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;

/**
 * Testklasse für die {@link StatistikServiceFactory} zur Prüfung der Instanziierung der Services
 */
@ExtendWith(MockitoExtension.class)
class StatistikServiceFactoryTest {

	@Mock
	private KatalogeRepositoryFactory repoKatalogeFactory;

	@Mock
	private KlassenRepositoryFactory repoKlassenFactory;

	@Mock
	private LehrerRepositoryFactory repoLehrerFactory;

	@Mock
	private SchuelerRepositoryFactory repoSchuelerFactory;

	@Mock
	private SchuleRepositoryFactory repoSchuleFactory;

	@Mock
	private LehrerServiceFactory serviceFactoryLehrer;

	@Mock
	private SchuleServiceFactory serviceFactorySchule;

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
		verify(repoLehrerFactory).getLehrerAbschnittsdatenRepository();
		verify(serviceFactoryLehrer).getLehrerLehramtService();
		verify(serviceFactoryLehrer).getLehrerPersonalabschnittsdatenAnrechnungsstundenService();
	}

	@Test
	@DisplayName("Test: getSchuelerStatistikService nutzt das Repository für Schuldaten, Schüler, Schülerlernabschnitte, Abiturdaten und deren Fächer sowie das allgemeine Fächer-Repository")
	void testGetSchuelerStatistikService() {
		final var service = factory.getSchuelerStatistikService();
		assertNotNull(service);
		verify(repoSchuleFactory, atLeastOnce()).getSchuleRepository();
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
		verify(repoKatalogeFactory).getOrteRepository();
	}

	@Test
	@DisplayName("Test: getJahrgaengeStatistikService nutzt das Repository zu Jahrgängen")
	void testGetJahrgaengeStatistikService() {
		assertNotNull(factory.getJahrgaengeStatistikService());
		verify(repoKatalogeFactory).getJahrgaengeRepository();
	}

	@Test
	@DisplayName("Test: getFoerderschwerpunkteStatistikService nutzt das Repository zu Förderschwerpunkten")
	void testGetFoerderschwerpunkteStatistikService() {
		assertNotNull(factory.getFoerderschwerpunkteStatistikService());
		verify(repoKatalogeFactory).getFoerderschwerpunkteRepository();
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
