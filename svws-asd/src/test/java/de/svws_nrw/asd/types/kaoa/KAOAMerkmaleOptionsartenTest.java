package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAMerkmaleOptionsarten}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAMerkmaleOptionsarten")
class KAOAMerkmaleOptionsartenTest {

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
	 * Führt einfache Tests zu dem Core-Type KAOAMerkmaleOptionsarten aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am KAOAMerkmaleOptionsarten.")
	void testKAOAMerkmaleOptionsarten() {
		assertEquals(1, KAOAMerkmaleOptionsarten.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAMerkmaleOptionsarten aus.
	 */
	@Test
	@DisplayName("Prüfe ob text richtig ist")
	void testKAOAMerkmaleOptionsartenText() {
		assertEquals("keine", KAOAMerkmaleOptionsarten.data().getHistorieByWert(KAOAMerkmaleOptionsarten.KEINE).getFirst().text);
	}


}
