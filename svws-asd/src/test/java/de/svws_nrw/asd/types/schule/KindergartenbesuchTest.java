package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Kindergartenbesuch}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Kindergartenbesuch")
class KindergartenbesuchTest {

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
	 * Führt einfache Tests zu dem Core-Type BerufskollegAnlage aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am Berufskolleg.")
	void testKindergartenbesuch() {
		assertEquals(5, Kindergartenbesuch.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type BerufskollegAnlage aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testReligionName() {
		assertEquals("kein Kindergarten", Kindergartenbesuch.data().getHistorieByWert(Kindergartenbesuch.KEINER).getFirst().text);
	}

}
