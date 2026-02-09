package de.svws_nrw.data.statistik;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.statistik.SchuleStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.data.TransactionSupport;

/**
 * Testet den Statistik-Service
 */
@ExtendWith(MockitoExtension.class)
class StatistikServiceTest {

	@Mock
	private SchuleStatistikService serviceSchule;

	@Mock
	private LehrerStatistikService serviceLehrer;

	@Mock
	private KlassenStatistikService serviceKlassen;

	@Mock
	private SchuelerStatistikService serviceSchueler;

	@Mock
	private JahrgaengeStatistikService serviceJahrgaenge;

	@Mock
	private OrteStatistikService serviceOrte;

	@Mock
	private FoerderschwerpunkteStatistikService serviceFoerderschwerpunkt;

	@Mock
	private ReligionStatistikService serviceReligion;

	@InjectMocks
	private StatistikService statistikService;

	private MockedStatic<TransactionSupport> transactionSupportMock;


	@BeforeEach
	void setup() {
		// Erstelle einen Mock für die transactional-Methode - Führt die Supplier-Aufrufe einfach ohne Transaktions-Klammer aus.
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any())).thenAnswer(invocation -> {
			final java.util.function.Supplier<?> supplier = invocation.getArgument(0);
			return supplier.get();
		});
	}

	@AfterEach
	void tearDown() {
		// Den statischen Mock nach jedem Test schließen
		transactionSupportMock.close();
	}

	@Test
	@DisplayName("Test: Prüfe, ob die Methode getGesamtStatistik alle Daten aus den Services in das StatistikGesamt-Objekt aggregiert.")
	void testGetGesamtStatistik() {
		// Vorbereiten der Testdaten
		final var testSchuldaten = new SchuleStatistikGesamt();
		testSchuldaten.schulNr = 123456;
		testSchuldaten.bezeichnung1 = "Eine Test-Schule";

		when(serviceSchule.get()).thenReturn(testSchuldaten);

		// TODO Das Ergebnis sollte später auch inhaltlich geprüft werden und nicht nur über leere Listen...
		when(serviceLehrer.getList()).thenReturn(new ArrayList<>());
		when(serviceKlassen.getList()).thenReturn(new ArrayList<>());
		when(serviceSchueler.getList()).thenReturn(new ArrayList<>());
		when(serviceJahrgaenge.getList()).thenReturn(new ArrayList<>());
		when(serviceOrte.getList()).thenReturn(new ArrayList<>());
		when(serviceFoerderschwerpunkt.getList()).thenReturn(new ArrayList<>());
		when(serviceReligion.getList()).thenReturn(new ArrayList<>());

		final StatistikGesamt result = statistikService.get();
		assertNotNull(result, "Das Ergebnis-Objekt darf nicht null sein.");
		assertEquals(testSchuldaten, result.schule, "Die Schuldaten wurden nicht korrekt gemappt.");

		// Verifizieren, dass die Transaktionsklammer genutzt wurde
		transactionSupportMock.verify(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()), times(1));
	}

}
