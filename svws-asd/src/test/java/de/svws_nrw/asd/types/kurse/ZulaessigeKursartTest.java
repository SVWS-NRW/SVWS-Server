package de.svws_nrw.asd.types.kurse;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link ZulaessigeKursart}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type ZulaessigeKursart")
class ZulaessigeKursartTest {

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
	 * Führt einfache Tests zu dem Core-Type ZulaessigeKursart aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am ZulaessigeKursart.")
	void testZulaessigeKursart() {
		assertEquals(74, ZulaessigeKursart.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type ZulaessigeKursart aus.
	 */
	@Test
	@DisplayName("Prüfe ob text richtig ist")
	void testZulaessigeKursarttext() {
		assertEquals("3. Abiturfach", ZulaessigeKursart.data().getHistorieByWert(ZulaessigeKursart.AB3).getFirst().text);
	}


}
