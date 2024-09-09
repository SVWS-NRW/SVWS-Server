package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerFachrichtungAnerkennung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerFachrichtungAnerkennung")
class LehrerFachrichtungAnerkennungTest {

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
	 * Führt einfache Tests zu dem Core-Type LehrerFachrichtungAnerkennung aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am LehrerFachrichtungAnerkennung.")
	void testLehrerFachrichtungAnerkennung() {
		assertEquals(4, LehrerFachrichtungAnerkennung.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type LehrerFachrichtungAnerkennung aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Text enthalten ist")
	void testLehrerFachrichtungAnerkennungText() {
		assertEquals("sonstige", LehrerFachrichtungAnerkennung.data().getHistorieByWert(LehrerFachrichtungAnerkennung.ID7).getFirst().text);
	}


}
