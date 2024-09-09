package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Schulform}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Schulform")
class SchulformTest {

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
    @DisplayName("Prüfe die Anzahl der Schulformen.")
    void testSchulform() {
    	assertEquals(19, Schulform.data().getWerte().size());
    	assertEquals(19, Schulform.data().getWerteBySchuljahr(2021).size());
    	assertEquals(19, Schulform.data().getWerteBySchuljahr(2022).size());
    	assertEquals(18, Schulform.data().getWerteBySchuljahr(2023).size());   // Schulform GM ausgelaufen
    	assertEquals(5, Schulform.getListMitGymOb(2021).size());
    	assertEquals(5, Schulform.getListMitGymOb(2022).size());
    	assertEquals(5, Schulform.getListMitGymOb(2023).size());
   }


	/**
	 * Führt einfache Tests zu dem Core-Type Schulform aus.
	 */
    @Test
    @DisplayName("Prüfe exemplarisch Abfragen auf die Schulformenen.")
    void testSchulformgetter() {
    	assertEquals(Schulform.GE, Schulform.data().getWertBySchluessel("15"));
    	assertEquals(Schulform.GE, Schulform.data().getWertByID(4000));
    	assertEquals(Schulform.GE, Schulform.data().getWertByKuerzel("GE"));
    	assertEquals("Gesamtschule", Schulform.GE.daten(2000).text);
    	assertNotNull(Schulform.GM.daten(2022));
    	assertNull(Schulform.GM.daten(2023));
   }

}
