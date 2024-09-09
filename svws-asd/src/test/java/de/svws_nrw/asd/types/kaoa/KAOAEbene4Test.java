package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAEbene4}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAEbene4")
class KAOAEbene4Test {

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
	 * Führt einfache Tests zu dem Core-Type KAOAEbene4 aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am KAOAEbene4.")
	void testKAOAEbene4() {
		assertEquals(24, KAOAEbene4.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAEbene4 aus.
	 */
	@Test
	@DisplayName("Prüfe ob zusatzmerkmal richtig ist")
	void testKAOAEbene4Zusatzmerkmal() {
		assertEquals("SBO_9_2_1", KAOAEbene4.data().getHistorieByWert(KAOAEbene4.SBO_9_2_1_1).getFirst().zusatzmerkmal);
	}


}
