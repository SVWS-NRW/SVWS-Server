package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link WeiterbildungskollegBildungsgangTyp}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type WeiterbildungskollegBildungsgangTyp")
class WeiterbildungskollegBildungsgangTypTest {

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
	 * Führt einfache Tests zu dem Core-Type WeiterbildungskollegBildungsgangTyp aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am Berufskolleg.")
	void testWeiterbildungskollegBildungsgangTyp() {
		assertEquals(3, WeiterbildungskollegBildungsgangTyp.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type WeiterbildungskollegBildungsgangTyp aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testWeiterbildungskollegBildungsgangTypText() {
		assertEquals("Abendgymnasium", WeiterbildungskollegBildungsgangTyp.data().getHistorieByWert(WeiterbildungskollegBildungsgangTyp.ABENDGYMNASIUM).getFirst().text);
	}

}
