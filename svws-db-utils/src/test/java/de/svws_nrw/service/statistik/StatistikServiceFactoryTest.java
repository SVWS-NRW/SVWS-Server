package de.svws_nrw.service.statistik;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;

/**
 * Testklasse für die {@link StatistikServiceFactory} zur Prüfung der Instanziierung der Services
 */
@ExtendWith(MockitoExtension.class)
class StatistikServiceFactoryTest {

	@Mock
	private BenutzerRepositoryFactory repoBenutzerFactory;

	@Mock
	private KatalogeRepositoryFactory repoKatalogeFactory;

	@Mock
	private KlassenRepositoryFactory repoKlassenFactory;

	@Mock
	private KurseRepositoryFactory repoKurseFactory;

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
		final var service = this.factory.getSchuleStatistikService();
		assertNotNull(service);
		verify(this.repoSchuleFactory).getSchuleRepository();
		verify(this.serviceFactorySchule).getSchuljahresabschnittService();
	}

	@Test
	@DisplayName("Test: getLehrerStatistikService nutzt das Repository für Schuldaten, für Lehrerdaten und Lehrerabschnittsdaten und die Services für die Lehrämter und die Anrechnungsstunden")
	void testGetLehrerStatistikService() {
		final var service = this.factory.getLehrerStatistikService();
		assertNotNull(service);
		verify(this.repoSchuleFactory, atLeastOnce()).getSchuleRepository();
		verify(this.repoLehrerFactory).getLehrerRepository();
		verify(this.repoLehrerFactory).getLehrerAbschnittsdatenRepository();
		verify(this.serviceFactoryLehrer).getLehrerLehramtService();
		verify(this.serviceFactoryLehrer).getLehrerPersonalabschnittsdatenAnrechnungsstundenService();
	}

	@Test
	@DisplayName("Test: getSchuelerStatistikService nutzt das Repository für Schuldaten, Schüler, Schülerlernabschnitte, Abiturdaten und deren Fächer sowie das allgemeine Fächer-Repository")
	void testGetSchuelerStatistikService() {
		final var service = this.factory.getSchuelerStatistikService();
		assertNotNull(service);
		verify(this.repoSchuleFactory, atLeastOnce()).getSchuleRepository();
		verify(this.repoSchuelerFactory).getSchuelerRepository();
		verify(this.repoSchuelerFactory).getSchuelerLernabschnittRepository();
		verify(this.repoSchuelerFactory).getSchuelerAbiturRepository();
		verify(this.repoSchuelerFactory).getSchuelerAbiturFachRepository();
		verify(this.repoKatalogeFactory).getFachRepository();
	}

	@Test
	@DisplayName("Test: getKlassenStatistikService nutzt das Repository für Schuldaten, Klassen, Klassenleitungen und SchülerLernabschnitte")
	void testGetKlassenStatistikService() {
		final var service = this.factory.getKlassenStatistikService();
		assertNotNull(service);
		verify(this.repoSchuleFactory, atLeastOnce()).getSchuleRepository();
		verify(this.repoKlassenFactory).getKlassenRepository();
		verify(this.repoKlassenFactory).getKlassenleitungenRepository();
		verify(this.repoSchuelerFactory, atLeastOnce()).getSchuelerLernabschnittRepository();
	}

	@Test
	@DisplayName("Test: getReligionStatistikService nutzt das Repository zu Religionen")
	void testGetReligionStatistikService() {
		assertNotNull(this.factory.getReligionStatistikService());
		verify(this.repoKatalogeFactory).getReligionRepository();
	}

	@Test
	@DisplayName("Test: getOrteStatistikService nutzt das Repository zu Orten")
	void testGetOrteStatistikService() {
		assertNotNull(this.factory.getOrteStatistikService());
		verify(this.repoKatalogeFactory).getOrteRepository();
	}

	@Test
	@DisplayName("Test: getJahrgaengeStatistikService nutzt das Repository zu Jahrgängen")
	void testGetJahrgaengeStatistikService() {
		assertNotNull(this.factory.getJahrgaengeStatistikService());
		verify(this.repoKatalogeFactory).getJahrgaengeRepository();
	}

	@Test
	@DisplayName("Test: getFoerderschwerpunkteStatistikService nutzt das Repository zu Förderschwerpunkten")
	void testGetFoerderschwerpunkteStatistikService() {
		assertNotNull(this.factory.getFoerderschwerpunkteStatistikService());
		verify(this.repoKatalogeFactory).getFoerderschwerpunkteRepository();
	}

	@Test
	@DisplayName("Test: getStatistikService aggregiert alle Unter-Statistik-Services")
	void testGetStatistikService() {
		final var service = this.factory.getStatistikService();
		assertNotNull(service);

		// Prüft, ob für den Statistik-Service auf die benötigten Factories zugegriffen wird
		verify(this.repoSchuleFactory, atLeastOnce()).getSchuleRepository();
		verify(this.repoLehrerFactory, atLeastOnce()).getLehrerRepository();
		verify(this.repoSchuelerFactory, atLeastOnce()).getSchuelerRepository();
		verify(this.repoKatalogeFactory, atLeastOnce()).getReligionRepository();
	}

}
