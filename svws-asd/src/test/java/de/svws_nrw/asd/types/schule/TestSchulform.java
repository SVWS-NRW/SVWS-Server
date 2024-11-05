package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Schulform}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Schulform")
class TestSchulform {

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
	 * Test des CoreTypes Schulform
	 *
	 * CoreType: Schulform
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 19
	 */
	@Test
	@DisplayName("Teste CoreType Schulform: Anzahl der vorhandenen Werte.")
    void testSchulform_AnzahlEintraege() {
		assertEquals(19, Schulform.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Schulform
	 *
	 * CoreType: Schulform
	 * Testfall: Prüft die Anzahl der Werte in den Schuljahren 21, 22, 23
	 * Ergebnis: Erwartete Werte - 19, 19, 19, 18, 5, 5, 5
	 */
	@Test
	@DisplayName("Teste CoreType Schulform: Anzahl der Einträge für die Schuljahre 21, 22, 23.")
    void testSchulform_AnzahlenBei21_22_23() {
    	assertEquals(19, Schulform.data().getWerte().size());
    	assertEquals(19, Schulform.data().getWerteBySchuljahr(2021).size());
    	assertEquals(19, Schulform.data().getWerteBySchuljahr(2022).size());
    	assertEquals(18, Schulform.data().getWerteBySchuljahr(2023).size());   // Schulform GM ausgelaufen
    	assertEquals(5, Schulform.getListMitGymOb(2021).size());
    	assertEquals(5, Schulform.getListMitGymOb(2022).size());
    	assertEquals(5, Schulform.getListMitGymOb(2023).size());
    }

	/**
	 * Test des CoreTypes Schulform
	 *
	 * CoreType: Schulform
	 * Testfall: Prüft die Anzahl der Werte in den Schuljahren 21, 22, 23
	 * Ergebnis: Erwartete Werte - GE, GE, GE, Gesamtschule, GM exists, GM exists not
	 */
	@Test
	@DisplayName("Teste CoreType Schulform: Korrekte Schulform, Text und Existenz von GM in verschiedenen Jahren.")
    void testSchulform_GetterAndExistensGM() {
    	assertEquals(Schulform.GE, Schulform.data().getWertBySchluessel("15"));
    	assertEquals(Schulform.GE, Schulform.data().getWertByID(4000));
    	assertEquals(Schulform.GE, Schulform.data().getWertByKuerzel("GE"));
    	assertEquals("Gesamtschule", Schulform.GE.daten(2000).text);
    	assertNotNull(Schulform.GM.daten(2022));
    	assertNull(Schulform.GM.daten(2023));
    }

}
