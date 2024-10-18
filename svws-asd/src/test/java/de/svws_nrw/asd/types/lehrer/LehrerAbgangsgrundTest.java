package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerAbgangsgrund}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerAbgangsgrund")
class LehrerAbgangsgrundTest {

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
	 * Führt einfache Tests zu dem Core-Type LehrerAbgangsgrund aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am Berufskolleg.")
	void testLehrerAbgangsgrund() {
		assertEquals(7, LehrerAbgangsgrund.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type LehrerAbgangsgrund aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testLehrerAbgangsgrundName() {
		assertEquals("Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule", LehrerAbgangsgrund.data().getHistorieByWert(LehrerAbgangsgrund.WECHSEL).getFirst().text);
	}


}
