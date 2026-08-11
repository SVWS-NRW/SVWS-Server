package de.svws_nrw.service.lehrer;

import de.svws_nrw.asd.utils.CoreTypeDataManager;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;


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
	@DisplayName("Test: Prüfe, ob getNewInstance die Factory korrekt initialisiert")
	void testGetNewInstance() {
	    // Da der Konstruktor private ist, testen wir über die statische Methode
	    final var factory = LehrerServiceFactory.getNewInstance(repoLehrerFactory, repoSchuleFactory);

	    assertNotNull(factory);

	    // Um zu prüfen, ob die Repositories intern korrekt gesetzt wurden,
	    // rufen wir eine Methode auf, die diese nutzt.
	    factory.getLehrerLehrbefaehigungService();

	    verify(repoLehrerFactory).getLehrerPersonaldatenLehramtLehrbefaehigungenRepository();
	}

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


	@Test
	@DisplayName("Test: Prüfe, ob getLehrerAnrechnungsstundenService den Service mit dem Kontext korrekt erstellt")
	void testGetLehrerAnrechnungsstundenService() {
	    final var service = serviceFactory.getLehrerAnrechnungsstundenService();
	    assertNotNull(service);

	    verify(repoSchuleFactory).getSchuljahresabschnitteRepository();
	    verify(repoLehrerFactory).getLehrerPersonalabschnittsdatenRepository();
	    verify(repoLehrerFactory).getLehrerAnrechnungRepository();
	}

	@Test
	@DisplayName("Test: Prüfe, ob getLehrerMinderleistungService den Service mit dem Kontext korrekt erstellt")
	void testGetLehrerMinderleistungService() {
		try (MockedStatic<CoreTypeDataManager> ignored = Mockito.mockStatic(CoreTypeDataManager.class)) {
			final var service = serviceFactory.getLehrerMinderleistungService();
			assertNotNull(service);

			verify(repoSchuleFactory).getSchuljahresabschnitteRepository();
			verify(repoLehrerFactory).getLehrerPersonalabschnittsdatenRepository();
			verify(repoLehrerFactory).getLehrerMinderleistungRepository();
		}
	}

}
