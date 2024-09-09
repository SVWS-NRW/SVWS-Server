package de.svws_nrw.asd.types.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link HerkunftBildungsgang}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type HerkunftBildungsgang")
class HerkunftBildungsgangTest {

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
	 * Führt einfache Tests zu dem Core-Type HerkunftBildungsgang aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der HerkunftBildungsgänge.")
	void testHerkunftBildungsgang() {
		assertEquals(41, HerkunftBildungsgang.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type HerkunftBildungsgang aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testHerkunftBildungsgangName() {
		assertEquals("Fachschule für Sozialwesen (mit Berufspraktikum/6-jährig, Teilzeit)", HerkunftBildungsgang.data().getHistorieByWert(HerkunftBildungsgang.E07).getFirst().text);
	}

}
