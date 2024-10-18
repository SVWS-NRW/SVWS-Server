package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAMerkmal}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAMerkmal")
class KAOAMerkmalTest {

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
	 * Führt einfache Tests zu dem Core-Type KAOAMerkmal aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am KAOAMerkmal.")
	void testKAOAMerkmal() {
		assertEquals(35, KAOAMerkmal.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAMerkmal aus.
	 */
	@Test
	@DisplayName("Prüfe ob kategorie richtig ist")
	void testKAOAMerkmalKategorie() {
		assertEquals("SBO_2", KAOAMerkmal.data().getHistorieByWert(KAOAMerkmal.SBO_2_1).getFirst().kategorie);
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAMerkmal aus.
	 */
	@Test
	@DisplayName("Prüfe ob optionsart richtig ist")
	void testKAOAMerkmalOptionsart() {
		assertEquals("KEINE", KAOAMerkmal.data().getHistorieByWert(KAOAMerkmal.SBO_2_1).getFirst().optionsart);
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAMerkmal aus.
	 */
	@Test
	@DisplayName("Prüfe ob bkAnlagen richtig ist")
	void testKAOAMerkmalbkAnlagen() {
		assertEquals("A12", KAOAMerkmal.data().getHistorieByWert(KAOAMerkmal.SBO_2_1).getFirst().bkAnlagen.getFirst());
	}


}
