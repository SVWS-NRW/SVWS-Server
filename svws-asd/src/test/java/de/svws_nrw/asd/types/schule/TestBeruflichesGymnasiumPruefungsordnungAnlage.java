package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link BeruflichesGymnasiumPruefungsordnungAnlage}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type BeruflichesGymnasiumPruefungsordnungAnlage")
class TestBeruflichesGymnasiumPruefungsordnungAnlage {

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
	 * Test des CoreTypes BeruflichesGymnasiumPruefungsordnungAnlage
	 *
	 * CoreType: BeruflichesGymnasiumPruefungsordnungAnlage
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 27
	 */
	@Test
	@DisplayName("Teste CoreType BeruflichesGymnasiumPruefungsordnungAnlage: Anzahl der vorhandenen Werte.")
	void testBeruflichesGymnasiumPruefungsordnungAnlage_AnzahlEintraege() {
		assertEquals(27, BeruflichesGymnasiumPruefungsordnungAnlage.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes BeruflichesGymnasiumPruefungsordnungAnlage
	 *
	 * CoreType: BeruflichesGymnasiumPruefungsordnungAnlage
	 * Testfall: Prüft den Text beim Ersten Eintrag von D20
	 * Ergebnis: Erwarteter Wert - "Allgemeine Hochschulreife/Maschinenbautechnik"
	 */
	@Test
	@DisplayName("Teste CoreType BerufskollegAnlage: Korrekter Text beim Wert D20.")
	void testBeruflichesGymnasiumPruefungsordnungAnlage_TextBeiD20() {
		assertEquals("Allgemeine Hochschulreife/Maschinenbautechnik", BeruflichesGymnasiumPruefungsordnungAnlage.data().getHistorieByWert(BeruflichesGymnasiumPruefungsordnungAnlage.D20).getFirst().text);
	}

}
