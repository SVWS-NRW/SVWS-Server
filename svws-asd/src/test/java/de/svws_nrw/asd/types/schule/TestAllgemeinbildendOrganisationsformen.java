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
class TestAllgemeinbildendOrganisationsformen {

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	/**
	 * Test des CoreTypes AllgemeinbildendOrganisationsformen
	 *
	 * CoreType: AllgemeinbildendOrganisationsformen
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 5
	 */
	@Test
	@DisplayName("Teste CoreType AllgemeinbildendOrganisationsformen: Anzahl der vorhandenen Werte.")
    void testAllgemeinbildendOrganisationsformen() {
    	assertEquals(5, AllgemeinbildendOrganisationsformen.data().getWerte().size());
    }


	/**
	 * Test des CoreTypes AllgemeinbildendOrganisationsformen
	 *
	 * CoreType: AllgemeinbildendOrganisationsformen
	 * Testfall: Prüft ob die Organisationsform GANZTAG_OFFEN in 2023 für die Grundschule und nicht für das Gymnasium gilt
	 * Ergebnis: Erwartete Werte - ja, nein
	 */
	@Test
	@DisplayName("Teste CoreType AllgemeinbildendOrganisationsformen: GanztagOffen in 2023 bei Grundschule aber nicht Gymnasium.")
    void testSchulformenAnAllgemeinbildendOrganisationsformen_ExistiertBeiGUndNichtGY() {
    	assertTrue(AllgemeinbildendOrganisationsformen.GANZTAG_OFFEN.hatSchulform(2023, Schulform.G));
    	assertFalse(AllgemeinbildendOrganisationsformen.GANZTAG_OFFEN.hatSchulform(2023, Schulform.GY));
    }

}
