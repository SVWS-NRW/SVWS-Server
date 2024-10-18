package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link BerufskollegBildungsgangTyp}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type BerufskollegBildungsgangTyp")
class BerufskollegBildungsgangTypTest {

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
	 * Führt einfache Tests zu dem Core-Type BerufskollegBildungsgangTyp aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Bildungsgangtypen am Berufskolleg.")
	void testBerufskollegBildungsgangTyp() {
		assertEquals(5, BerufskollegBildungsgangTyp.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type WeiterbildungskollegBildungsgangTyp aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testBerufskollegBildungsgangTypText() {
		assertEquals("Berufsschule", BerufskollegBildungsgangTyp.data().getHistorieByWert(BerufskollegBildungsgangTyp.BERUFSSCHULE).getFirst().text);
	}

}
