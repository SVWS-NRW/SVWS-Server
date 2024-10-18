package de.svws_nrw.asd.types.fach;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Fachgruppe}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Fach")
class FachTest {

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
	 * Führt einfache Tests zu dem Core-Type Fachgruppe aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Fächer.")
	void testFach() {
		assertEquals(292, Fach.data().getWerte().size());
	}

}
