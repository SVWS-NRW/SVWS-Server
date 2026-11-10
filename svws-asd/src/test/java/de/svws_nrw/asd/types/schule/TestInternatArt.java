package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link InternatArt}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type InternatArt")
class TestInternatArt {

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
	 * Test des CoreTypes InternatArt
	 *
	 * CoreType: InternatArt
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 4
	 */
	@Test
	@DisplayName("Teste CoreType InternatArt: Anzahl der vorhandenen Werte.")
	void testInternatArt_AnzahlEintraege() {
		assertEquals(4, InternatArt.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes InternatArt
	 *
	 * CoreType: InternatArt
	 * Testfall: Prüft, ob zu jedem Wert des Enums ein Eintrag mit Historie in den Daten vorhanden ist
	 * Ergebnis: Jeder Wert besitzt genau einen Historieneintrag
	 */
	@Test
	@DisplayName("Teste CoreType InternatArt: Zu jedem Wert existiert ein Historieneintrag.")
	void testInternatArt_HistorieZuJedemWert() {
		for (final InternatArt wert : InternatArt.values()) {
			assertEquals(1, InternatArt.data().getHistorieByWert(wert).size());
		}
	}

	/**
	 * Test des CoreTypes InternatArt
	 *
	 * CoreType: InternatArt
	 * Testfall: Prüft den Text beim Wert KEIN_INTERNAT
	 * Ergebnis: Erwarteter Wert - "kein Internat"
	 */
	@Test
	@DisplayName("Teste CoreType InternatArt: Korrekter Text beim Wert KEIN_INTERNAT.")
	void testInternatArt_TextBeiKEIN_INTERNAT() {
		assertEquals("kein Internat", InternatArt.data().getHistorieByWert(InternatArt.KEIN_INTERNAT).getFirst().text);
	}

	/**
	 * Test des CoreTypes InternatArt
	 *
	 * CoreType: InternatArt
	 * Testfall: Prüft den Zugriff über die ID und den Text bei INTERNAT_NEUTRAL
	 * Ergebnis: ID 4 liefert den Eintrag "Internat für Jungen und Mädchen"
	 */
	@Test
	@DisplayName("Teste CoreType InternatArt: Zugriff über die ID beim Wert INTERNAT_NEUTRAL.")
	void testInternatArt_IDBeiINTERNAT_NEUTRAL() {
		final var eintrag = InternatArt.data().getEintragByID(4L);
		assertNotNull(eintrag);
		assertEquals("Internat für Jungen und Mädchen", eintrag.text);
	}

}
