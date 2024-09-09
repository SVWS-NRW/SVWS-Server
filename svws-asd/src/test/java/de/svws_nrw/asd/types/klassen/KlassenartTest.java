package de.svws_nrw.asd.types.klassen;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Klassenart}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Klassenart")
class KlassenartTest {

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
	 * Führt einfache Tests zu dem Core-Type Klassenart aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am Klassenart.")
	void testKlassenart() {
		assertEquals(9, Klassenart.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type Klassenart aus.
	 */
	@Test
	@DisplayName("Prüfe ob text richtig ist")
	void testKlassenartText() {
		assertEquals("Klasse 10 Typ A (Hauptschule)", Klassenart.data().getHistorieByWert(Klassenart.HA_1A).getFirst().text);
	}


}
