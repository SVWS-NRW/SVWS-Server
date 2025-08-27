package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.validate.DateManager;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Termin}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Termin")
public class TestTermin {

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Test des CoreTypes Termin
	 *
	 * CoreType: Termin
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 4
	 */
	@Test
	@DisplayName("Teste CoreType Termin: Anzahl der vorhandenen Werte.")
    void testTermin_AnzahlEintraege() {
		assertEquals(4, Termin.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Termin
	 *
	 * CoreType: Termin
	 * Testfall: Prüft das Datum des letzten Unterrichtstages des ersten Halbjahres in den Schuljahren 2021, 2022, 2023
	 * Ergebnis: Erwartete Werte -
	 */
	@Test
	@DisplayName("Teste CoreType Termin: Prüft das Datum des letzten Unterrichtstages des ersten Halbjahres in den Schuljahren 2021, 2022, 2023.")
    void testTermin_DatumLetzterUnterrichtstag_2021_2022_2023() {
		final DateManager date2021 = Termin.getLetzterUnterrichtstagImErstenHalbjahr(2021); // "2022-01-28"
    	assertEquals(2022, date2021.getJahr());
    	assertEquals(1, date2021.getMonat());
    	assertEquals(28, date2021.getTag());

    	final DateManager date2022 = Termin.getLetzterUnterrichtstagImErstenHalbjahr(2022); // "2023-01-20"
    	assertEquals(2023, date2022.getJahr());
    	assertEquals(1, date2022.getMonat());
    	assertEquals(20, date2022.getTag());

    	final DateManager date2023 = Termin.getLetzterUnterrichtstagImErstenHalbjahr(2023); // "2024-01-26"
    	assertEquals(1, date2023.getMonat());
    	assertEquals(26, date2023.getTag());
    	assertEquals(2024, date2023.getJahr());

		final DateManager date2024 = Termin.getLetzterUnterrichtstagImErstenHalbjahr(2024); // "2025-02-07"
    	assertEquals(2, date2024.getMonat());
    	assertEquals(7, date2024.getTag());
    	assertEquals(2025, date2024.getJahr());
    }

}
