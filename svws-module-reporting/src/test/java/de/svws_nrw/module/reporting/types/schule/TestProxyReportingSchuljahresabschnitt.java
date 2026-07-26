package de.svws_nrw.module.reporting.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;

/**
 * Tests für die Unterscheidung zwischen einem Schuljahresabschnitt aus der Datenbank und einem virtuellen
 * Schuljahresabschnitt sowie für dessen Einschränkungen.
 *
 * <p>Alle Tests konstruieren die Proxy-Objekte bewusst mit einem {@code null}-ReportingContext. Ein virtueller
 * Abschnitt darf für Klassen und Kurse gar nicht erst auf die Datenbank zugreifen; würden die Guards in
 * {@link ProxyReportingSchuljahresabschnitt#klassen()} und {@link ProxyReportingSchuljahresabschnitt#kurse()}
 * entfernt, liefen die Tests in eine NullPointerException statt still ein falsches Ergebnis zu liefern. Der
 * {@code null}-Context ist hier also kein Behelf, sondern die eigentliche Zusicherung.</p>
 */
class TestProxyReportingSchuljahresabschnitt {

	/** Die Pseudo-ID, die das Repository für einen virtuellen Abschnitt im Schuljahr 2029, Abschnitt 1 vergibt. */
	private static final long ID_VIRTUELL_2029_1 = -20291L;

	/**
	 * Erstellt einen Proxy für einen Schuljahresabschnitt, wie er aus der Datenbank gelesen wird.
	 *
	 * @param id        Die Datenbank-ID des Abschnitts.
	 * @param schuljahr Das Schuljahr des Abschnitts.
	 * @param abschnitt Die Nummer des Abschnitts im Schuljahr.
	 *
	 * @return Der Proxy zum Abschnitt aus der Datenbank.
	 */
	private static ProxyReportingSchuljahresabschnitt ausDatenbank(final long id, final int schuljahr, final int abschnitt) {
		final Schuljahresabschnitt daten = new Schuljahresabschnitt();
		daten.id = id;
		daten.schuljahr = schuljahr;
		daten.abschnitt = abschnitt;
		return new ProxyReportingSchuljahresabschnitt(null, daten);
	}

	@Test
	void testAbschnittAusDatenbankIstNichtVirtuell() {
		assertFalse(ausDatenbank(1L, 2018, 2).istVirtuell());
	}

	@Test
	void testAbschnittMitIdNullIstNichtVirtuell() {
		// Datenbank-IDs beginnen bei 0; die Abgrenzung erfolgt allein über das negative Vorzeichen.
		assertFalse(ausDatenbank(0L, 2018, 2).istVirtuell());
	}

	@Test
	void testVirtuellerAbschnittIstVirtuell() {
		final ProxyReportingSchuljahresabschnitt abschnitt = new ProxyReportingSchuljahresabschnitt(null, ID_VIRTUELL_2029_1, 2029, 1);
		assertTrue(abschnitt.istVirtuell());
		assertEquals(ID_VIRTUELL_2029_1, abschnitt.id());
	}

	@Test
	void testVirtuellerAbschnittLiefertSchuljahrUndAbschnitt() {
		final ProxyReportingSchuljahresabschnitt abschnitt = new ProxyReportingSchuljahresabschnitt(null, ID_VIRTUELL_2029_1, 2029, 1);
		assertEquals(2029, abschnitt.schuljahr());
		assertEquals(1, abschnitt.abschnitt());
		assertEquals("2029/30.1", abschnitt.textSchuljahresabschnittKurz());
	}

	@Test
	void testVirtuellerAbschnittLiefertKeineKlassenOhneDatenbankzugriff() {
		final ProxyReportingSchuljahresabschnitt abschnitt = new ProxyReportingSchuljahresabschnitt(null, ID_VIRTUELL_2029_1, 2029, 1);
		assertTrue(abschnitt.klassen().isEmpty());
		assertNull(abschnitt.klasse(4711L));
	}

	@Test
	void testVirtuellerAbschnittLiefertKeineKurseOhneDatenbankzugriff() {
		final ProxyReportingSchuljahresabschnitt abschnitt = new ProxyReportingSchuljahresabschnitt(null, ID_VIRTUELL_2029_1, 2029, 1);
		assertTrue(abschnitt.kurse().isEmpty());
		assertNull(abschnitt.kurs(4711L));
	}

	@Test
	void testVirtuellerAbschnittHatKeineNachbarabschnitte() {
		// Ein virtueller Abschnitt reiht sich nicht in die Abschnittskette der Schule ein. Da weder eine ID des
		// vorherigen noch des folgenden Abschnitts gesetzt ist, wird für das Lazy-Loading kein Repository benötigt.
		final ProxyReportingSchuljahresabschnitt abschnitt = new ProxyReportingSchuljahresabschnitt(null, ID_VIRTUELL_2029_1, 2029, 1);
		assertNull(abschnitt.idVorherigerAbschnitt());
		assertNull(abschnitt.idFolgenderAbschnitt());
		assertNull(abschnitt.vorherigerAbschnitt());
		assertNull(abschnitt.folgenderAbschnitt());
	}
}
