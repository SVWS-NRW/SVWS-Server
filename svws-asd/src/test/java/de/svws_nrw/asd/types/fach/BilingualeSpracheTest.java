package de.svws_nrw.asd.types.fach;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link BilingualeSprache}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type BilingualeSprache")
class BilingualeSpracheTest {

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
	 * Führt einfache Tests zu dem Core-Type BilingualeSprache aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am BilingualeSprache.")
	void testBilingualeSprache() {
		assertEquals(7, BilingualeSprache.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type BilingualeSprache aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Text enthalten ist")
	void testBilingualeSpracheText() {
		assertEquals(2022, BilingualeSprache.data().getHistorieByWert(BilingualeSprache.ENGLISCH).getFirst().gueltigBis);
	}


}
