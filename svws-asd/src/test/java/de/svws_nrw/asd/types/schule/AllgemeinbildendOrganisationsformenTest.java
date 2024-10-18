package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link AllgemeinbildendOrganisationsformen}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type AllgemeinbildendOrganisationsformen")
class AllgemeinbildendOrganisationsformenTest {

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
	 * Führt einfache Tests zu dem Core-Type AllgemeinbildendOrganisationsformen aus.
	 */
    @Test
    @DisplayName("Prüfe die Anzahl der Organisationsformen an allgemeinbildenden Schulen.")
    void testAllgemeinbildendOrganisationsformen() {
    	assertEquals(5, AllgemeinbildendOrganisationsformen.data().getWerte().size());
   }


	/**
	 * Führt einfache Tests zu dem Core-Type AllgemeinbildendOrganisationsformen aus.
	 */
    @Test
    @DisplayName("Prüfe exemplarisch die zulässigen Schulformen bei den Organisationsformen an allgemeinbildenden Schulen.")
    void testSchulformenAnAllgemeinbildendOrganisationsformen() {
    	assertTrue(AllgemeinbildendOrganisationsformen.GANZTAG_OFFEN.hatSchulform(2023, Schulform.G));
    	assertFalse(AllgemeinbildendOrganisationsformen.GANZTAG_OFFEN.hatSchulform(2023, Schulform.GY));
   }


}
