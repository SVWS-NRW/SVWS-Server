package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Religion}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Religion")
class ReligionTest {

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
	 * Führt einfache Tests zu dem Core-Type Religion aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Religionen.")
	void testReligion() {
		assertEquals(11, Religion.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type Religion aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testReligionName() {
		assertEquals("evangelisch", Religion.data().getHistorieByWert(Religion.ER).getFirst().text);
	}

}
