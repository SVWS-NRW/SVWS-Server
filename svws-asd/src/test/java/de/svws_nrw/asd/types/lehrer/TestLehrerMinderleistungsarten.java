package de.svws_nrw.asd.types.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type LehrerMinderleistungsarten.
 */
@DisplayName("Teste den Core-Type LehrerMinderleistungsarten")
class TestLehrerMinderleistungsarten {

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
	 * Test des CoreTypes LehrerMinderleistungsarten
	 *
	 * CoreType: LehrerMinderleistungsarten
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 18
	 */
	@Test
	@DisplayName("Teste CoreType LehrerMinderleistungsarten: Anzahl der vorhandenen Werte.")
	void testLehrerMinderleistungsarten_AnzahlEintraege() {
		assertEquals(18, LehrerMinderleistungsarten.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerMinderleistungsarten
	 *
	 * CoreType: LehrerMinderleistungsarten
	 * Testfall: Prüft zwei Kürzel und ob alle Texte stimmen
	 * Ergebnis: Erwartete Werte -
	 */
	@Test
	@DisplayName("Teste CoreType LehrerMinderleistungsarten: Anzahl der vorhandenen Werte.")
	void testLehrerMinderleistungsarten() {
		assertEquals("200", LehrerMinderleistungsarten.data().getWertByKuerzel("200").daten(2024).kuerzel);
		assertEquals("210", LehrerMinderleistungsarten.data().getWertByKuerzel("210").daten(2024).kuerzel);

		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("200").daten(2024).text, ("Pflichtstundenermäßigung aus Altersgründen"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("210").daten(2024).text,
				("Pflichtstundenermäßigung wegen Schwerbehinderung (Regelanrechnung)"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("220").daten(2024).text,
				("Pflichtstundenermäßigung wegen Schwerbehinderung (Erhöhung auf Antrag)"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("230").daten(2024).text, ("Beurlaubung, Rückkehr im Laufe des Schuljahres"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("240").daten(2024).text, ("Langfristige Erkrankung"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("250").daten(2024).text, ("Abwesend wegen Beschäftigungsverbot gem. § 3 MuSchG"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("255").daten(2024).text, ("Abwesend wegen Teilbeschäftigungsverbot gem. § 3 MuSchG"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("260").daten(2024).text, ("Wiedereingliederungsmaßnahme"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("270").daten(2024).text,
				("Rückgabe vorgeleisteter Stunden wegen Nichtinanspruchnahme von Altersteilzeit"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("275").daten(2024).text, ("Rückgabe der Vorgriffsstunden"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("280").daten(2024).text, ("Seiteneinsteigerentlastung"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("290").daten(2024).text,
				("Ermäßigungs-/Freistellungsphase \"Teilzeitbeschäftigung im Blockmodell\" (§ 65 LBG) (vormals Sabbatjahr)"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("300").daten(2024).text, ("Sonstige Ermäßigungen aus besonderen persönlichen Gründen"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("350").daten(2024).text,
				("Abrundung der Pflichtstundenzahl wegen Aufrundung im vorhergehenden Schuljahr"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("360").daten(2024).text,
				("Unterschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("365").daten(2022).text, ("Unterschreitung der Pflichtstundenzahl wegen COVID-19"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("370").daten(2024).text,
				("Unterschreitung der Pflichtstundenzahl wegen Pflichstunden-Bandbreite"));
		assertEquals(LehrerMinderleistungsarten.data().getWertByKuerzel("380").daten(2024).text,
				("Fortbildung: Nachträglicher Erwerb des sonderpädagogischen Lehramtes"));
	}

}
