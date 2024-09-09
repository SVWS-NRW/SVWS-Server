package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerZugangsgrund}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerZugangsgrund")
class LehrerZugangsgrundTest {

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
	 * Führt einfache Tests zu dem Core-Type LehrerZugangsgrund aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am LehrerZugangsgrund.")
	void testLehrerZugangsgrund() {
		assertEquals(5, LehrerZugangsgrund.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type LehrerZugangsgrund aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Text enthalten ist")
	void testLehrerZugangsgrundText() {
		assertEquals("Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung", LehrerZugangsgrund.data().getHistorieByWert(LehrerZugangsgrund.NEU).getFirst().text);
	}


}
