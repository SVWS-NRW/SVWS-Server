package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAKategorie}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAKategorie")
class KAOAKategorieTest {

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
	 * Führt einfache Tests zu dem Core-Type KAOAKategorie aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am KAOAKategorie.")
	void testKAOAKategorie() {
		assertEquals(9, KAOAKategorie.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAKategorie aus.
	 */
	@Test
	@DisplayName("Prüfe ob jahrgaenge richtig ist")
	void testKAOAKategorieJahrgaenge() {
		assertEquals("JAHRGANG_08", KAOAKategorie.data().getHistorieByWert(KAOAKategorie.SBO_2).getFirst().jahrgaenge.getFirst());
	}


}
