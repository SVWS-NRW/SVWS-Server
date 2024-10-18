package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Uebergangsempfehlung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Uebergangsempfehlung")
class UebergangsempfehlungTest {

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
	 * Führt einfache Tests zu dem Core-Type Uebergangsempfehlung aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am Uebergangsempfehlung.")
	void testUebergangsempfehlung() {
		assertEquals(6, Uebergangsempfehlung.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type Uebergangsempfehlung aus.
	 */
	@Test
	@DisplayName("Prüfe ob zusatzmerkmal richtig ist")
	void testUebergangsempfehlungZusatzmerkmal() {
		assertEquals("Gymnasium", Uebergangsempfehlung.data().getHistorieByWert(Uebergangsempfehlung.GYMNASIUM).getFirst().text);
	}


}
