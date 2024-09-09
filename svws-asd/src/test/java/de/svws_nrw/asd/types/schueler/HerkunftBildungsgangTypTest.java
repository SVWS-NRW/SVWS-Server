package de.svws_nrw.asd.types.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link HerkunftBildungsgangTyp}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type HerkunftBildungsgangTyp")
class HerkunftBildungsgangTypTest {

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
	 * Führt einfache Tests zu dem Core-Type HerkunftBildungsgangTyp aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der HerkunftBildungsgangTypen.")
	void testHerkunftBildungsgangTyp() {
		assertEquals(8, HerkunftBildungsgangTyp.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type HerkunftBildungsgangTyp aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testHerkunftBildungsgangTypName() {
		assertEquals("KOLLEG", HerkunftBildungsgangTyp.data().getHistorieByWert(HerkunftBildungsgangTyp.KL).getFirst().text);
	}

}
