package de.svws_nrw.asd.types.jahrgang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link PrimarstufeSchuleingangsphaseBesuchsjahre}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type PrimarstufeSchuleingangsphaseBesuchsjahre")
class TestPrimarstufeSchuleingangsphaseBesuchsjahre {

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
	 * Test des CoreTypes PrimarstufeSchuleingangsphaseBesuchsjahre
	 *
	 * CoreType: PrimarstufeSchuleingangsphaseBesuchsjahre
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 3
	 */
	@Test
	@DisplayName("Teste CoreType PrimarstufeSchuleingangsphaseBesuchsjahre: Anzahl der vorhandenen Werte.")
	void testPrimarstufeSchuleingangsphaseBesuchsjahre_AnzahlEintraege() {
		assertEquals(3, PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWerte().size());
	}

}
