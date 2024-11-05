package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Uebergangsempfehlung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Uebergangsempfehlung")
class TestUebergangsempfehlung {

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
	 * Test des CoreTypes Uebergangsempfehlung
	 *
	 * CoreType: Uebergangsempfehlung
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 6
	 */
	@Test
	@DisplayName("Teste CoreType Uebergangsempfehlung: Anzahl der vorhandenen Werte.")
	void testUebergangsempfehlung_AnzahlEintraege() {
		assertEquals(6, Uebergangsempfehlung.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Uebergangsempfehlung
	 *
	 * CoreType: Uebergangsempfehlung
	 * Testfall: Prüft den Text beim ersten Wert von GYMNASIUM
	 * Ergebnis: Erwarteter Wert - Gymnasium
	 */
	@Test
	@DisplayName("Teste CoreType Uebergangsempfehlung: Korrekter Text beim Wert GYMNASIUM.")
	void testUebergangsempfehlung_TextBeiGymnasium() {
		assertEquals("Gymnasium", Uebergangsempfehlung.data().getHistorieByWert(Uebergangsempfehlung.GYMNASIUM).getFirst().text);
	}

}
