package de.svws_nrw.asd.types.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type LehrerMehrleistungsarten.
 */
@DisplayName("Teste den Core-Type LehrerMehrleistungsarten")
class TestLehrerMehrleistungsarten {

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
	 * Test des CoreTypes LehrerMehrleistungsarten
	 *
	 * CoreType: LehrerMehrleistungsarten
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 6
	 */
	@Test
	@DisplayName("Teste CoreType LehrerMehrleistungsarten: Anzahl der vorhandenen Werte.")
	void testLehrerMehrleistungsarten_AnzahlEintraege() {
		assertEquals(6, LehrerMehrleistungsarten.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerMehrleistungsarten
	 *
	 * CoreType: LehrerMehrleistungsarten
	 * Testfall: Prüft zwei Kürzel und ob alle Texte stimmen
	 * Ergebnis: Erwartete Werte -
	 */
	@Test
	@DisplayName("Teste CoreType LehrerMehrleistungsarten: Korrekte Kürzel und Texte.")
	void testLehrerMehrleistungsarten_KuerzelUndTexte() {
		assertEquals("100", LehrerMehrleistungsarten.data().getWertByKuerzel("100").daten(2024).kuerzel);
		assertEquals("110", LehrerMehrleistungsarten.data().getWertByKuerzel("110").daten(2024).kuerzel);

		assertEquals(LehrerMehrleistungsarten.data().getWertByKuerzel("100").daten(2024).text, ("Ansparphase, Phase mit erhöhter Arbeitszeit \"Teilzeitbeschäftigung im Blockmodell\" (§ 65 LBG) (vormals Sabbatjahr)"));
		assertEquals(LehrerMehrleistungsarten.data().getWertByKuerzel("110").daten(2024).text, ("Mehrarbeit (angeordnet und regelmäßig)"));
		assertEquals(LehrerMehrleistungsarten.data().getWertByKuerzel("150").daten(2024).text, ("Aufrundung der Pflichtstundenzahl aufgrund von § 2 Abs. 1 AVO-RL"));
		assertEquals(LehrerMehrleistungsarten.data().getWertByKuerzel("160").daten(2024).text, ("Überschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)"));
		assertNull(LehrerMehrleistungsarten.data().getWertByKuerzel("165").daten(2024));
		assertEquals(LehrerMehrleistungsarten.data().getWertByKuerzel("170").daten(2024).text, ("Überschreitung der Pflichtstundenzahl wegen Pflichtstunden-Bandbreite"));
	}

}
