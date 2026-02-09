package de.svws_nrw.data.lehrer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;


/**
 * Testklasse für die Factory von Lehrer-Services.
 */
@ExtendWith(MockitoExtension.class)
class LehrerServiceFactoryTest {

	@Mock
	private LehrerRepositoryFactory repoLehrerFactory;

	@Mock
	private SchuleRepositoryFactory repoSchuleFactory;

	@InjectMocks
	private LehrerServiceFactory serviceFactory;

	@Test
	@DisplayName("Test: Prüfe, on getLehrerLehramtService den Service korrekt erstellt")
	void testGetLehrerLehramtService() {
		final var service = serviceFactory.getLehrerLehramtService();
		assertNotNull(service);

		// Prüfe, ob die das Repository für die Lehramtsdaten genau einmal genutzt wird...
		verify(repoLehrerFactory).getLehrerPersonaldatenLehramtRepository();

		// ... und auch (indirekt) die beiden Repositories für die Fachrichtungen und die Lehrbefähigungen
		verify(repoLehrerFactory).getLehrerPersonaldatenLehramtFachrichtungRepository();
		verify(repoLehrerFactory).getLehrerPersonaldatenLehramtLehrbefaehigungenRepository();
	}

	@Test
	@DisplayName("Test: Prüfe, on getLehrerLehrbefaehigungService den Service korrekt erstellt")
	void testGetLehrerLehrbefaehigungService() {
		final var service = serviceFactory.getLehrerLehrbefaehigungService();
		assertNotNull(service);
		verify(repoLehrerFactory).getLehrerPersonaldatenLehramtLehrbefaehigungenRepository();
	}

	@Test
	@DisplayName("Test: Prüfe, on getLehrerFachrichtungService den Service korrekt erstellt")
	void testGetLehrerFachrichtungService() {
		final var service = serviceFactory.getLehrerFachrichtungService();
		assertNotNull(service);
		verify(repoLehrerFactory).getLehrerPersonaldatenLehramtFachrichtungRepository();
	}

	@Test
	@DisplayName("Test: Prüfe, on getLehrerPersonalabschnittsdatenAnrechnungsstundenService den Service korrekt erstellt")
	void testGetLehrerPersonalabschnittsdatenAnrechnungsstundenService() {
		final var service = serviceFactory.getLehrerPersonalabschnittsdatenAnrechnungsstundenService();
		assertNotNull(service);

		// Prüfe, ob auf das Schul-Repository ...
		verify(repoSchuleFactory).getSchuljahresabschnitteRepository();

		// ... und auf die drei Repositories zu Anrechnungs-, Mehr- und Minderleistungsstunden zurückgegriffen wird
		verify(repoLehrerFactory).getLehrerMehrleistungRepository();
		verify(repoLehrerFactory).getLehrerMinderleistungRepository();
		verify(repoLehrerFactory).getLehrerAnrechnungRepository();
	}

}
