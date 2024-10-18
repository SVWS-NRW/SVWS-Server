package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Schulgliederung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Schulgliederung")
class SchulgliederungTest {

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
	 * Führt einfache Tests zu dem Core-Type Schulform aus.
	 */
    @Test
    @DisplayName("Prüfe die Anzahl der Schulgliederungen.")
    void testSchulform() {
    	assertEquals(89, Schulgliederung.data().getWerte().size());
    	assertEquals(63, Schulgliederung.data().getWerteBySchuljahr(2023).size());
   }

}
