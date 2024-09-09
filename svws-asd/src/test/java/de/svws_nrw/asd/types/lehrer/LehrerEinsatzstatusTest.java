package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerEinsatzstatus}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerEinsatzstatus")
class LehrerEinsatzstatusTest {

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	// TODO Schreiben von Tests

	/**
	 * Führt einfache Tests zu dem Core-Type LehrerEinsatzstatus aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am LehrerEinsatzstatus.")
	void testLehrerEinsatzstatus() {
		assertEquals(3, LehrerEinsatzstatus.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type LehrerEinsatzstatus aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Text enthalten ist")
	void testLehrerEinsatzstatusText() {
		assertEquals("Stammschule, ganz oder teilweise auch an anderen Schulen tätig", LehrerEinsatzstatus.data().getHistorieByWert(LehrerEinsatzstatus.A).getFirst().text);
	}


}
