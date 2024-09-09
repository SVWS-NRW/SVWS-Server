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
class PrimarstufeSchuleingangsphaseBesuchsjahreTest {

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
	 * Führt einfache Tests zu dem Core-Type PrimarstufeSchuleingangsphaseBesuchsjahre aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Besuchsjahre.")
	void testPrimarstufeSchuleingangsphaseBesuchsjahre() {
		assertEquals(3, PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWerte().size());
	}

}
