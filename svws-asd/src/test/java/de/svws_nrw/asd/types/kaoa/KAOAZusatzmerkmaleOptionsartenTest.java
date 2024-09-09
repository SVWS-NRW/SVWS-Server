package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAZusatzmerkmaleOptionsarten}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAZusatzmerkmaleOptionsarten")
class KAOAZusatzmerkmaleOptionsartenTest {

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
	 * Führt einfache Tests zu dem Core-Type KAOAZusatzmerkmaleOptionsarten aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am KAOAZusatzmerkmaleOptionsarten.")
	void testKAOAZusatzmerkmaleOptionsarten() {
		assertEquals(6, KAOAZusatzmerkmaleOptionsarten.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAZusatzmerkmaleOptionsarten aus.
	 */
	@Test
	@DisplayName("Prüfe ob kuerzel richtig ist")
	void testKAOAZusatzmerkmaleOptionsartenKuerzel() {
		assertEquals("Anschlussoption", KAOAZusatzmerkmaleOptionsarten.data().getHistorieByWert(KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION).getFirst().kuerzel);
	}


}
