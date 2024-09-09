package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAZusatzmerkmal}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAZusatzmerkmal")
class KAOAZusatzmerkmalTest {

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
	 * Führt einfache Tests zu dem Core-Type KAOAZusatzmerkmal aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am KAOAZusatzmerkmal.")
	void testKAOAZusatzmerkmal() {
		assertEquals(117, KAOAZusatzmerkmal.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAZusatzmerkmal aus.
	 */
	@Test
	@DisplayName("Prüfe ob merkmal richtig ist")
	void testKAOAZusatzmerkmalMerkmal() {
		assertEquals("SBO_2_2", KAOAZusatzmerkmal.data().getHistorieByWert(KAOAZusatzmerkmal.SBO_2_2_2).getFirst().merkmal);
	}


	/**
	 * Führt einfache Tests zu dem Core-Type KAOAZusatzmerkmal aus.
	 */
	@Test
	@DisplayName("Prüfe ob optionsart richtig ist")
	void testKAOAZusatzmerkmalOptionsart() {
		assertEquals("FREITEXT", KAOAZusatzmerkmal.data().getHistorieByWert(KAOAZusatzmerkmal.SBO_2_2_2).getFirst().optionsart);
	}


}
