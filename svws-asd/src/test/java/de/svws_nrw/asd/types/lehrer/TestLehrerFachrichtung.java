package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import jakarta.validation.constraints.NotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerFachrichtung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerFachrichtung")
class TestLehrerFachrichtung {

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
	 * Test des CoreTypes LehrerFachrichtung
	 *
	 * CoreType: LehrerFachrichtung
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 111
	 */
	@Test
	@DisplayName("Teste CoreType LehrerFachrichtung: Anzahl der vorhandenen Werte.")
	void testLehrerFachrichtung_AnzahlEintraege() {
		assertEquals(111, LehrerFachrichtung.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerFachrichtung
	 *
	 * CoreType: LehrerFachrichtung
	 * Testfall: Prüft den Text beim ersten Wert von ID_04
	 * Ergebnis: Erwarteter Wert - "Metalltechnik, Maschinenbau (außer Kfz), Verfahrens-, Fertigungstechnik"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerFachrichtung: Korrekter Text bei ID_04.")
	void testLehrerFachrichtung_TextBeiID_04() {
		assertEquals("Maschinentechnik, Metalltechnik, Maschinenbau (außer Kfz), Verfahrens-, Fertigungstechnik", LehrerFachrichtung.data().getHistorieByWert(LehrerFachrichtung.ID_04).getFirst().text);
	}


	/**
	 * Test des CoreTypes LehrerFachrichtung
	 *
	 * CoreType: LehrerFachrichtung
	 * Testfall: Prüft, ob die Liste der zulässigen Lehrämter für mehrere Schuljahr erfolgreich für die Fachrichung Fertigungstechnik ermittelt werden kann.
	 * Ergebnis:
	 */
	@Test
	@DisplayName("Teste die Liste der zulässigen Lehrämter für die Fachrichtung Fertigungstechnik")
	void testLehrerFachrichtung_Lehraemter() {
		final @NotNull LehrerFachrichtung fr = LehrerFachrichtung.ID_FT;
		for (int schuljahr = 2010; schuljahr < 2040; schuljahr++) {
			final @NotNull List<LehrerLehramt> lehraemter = fr.getLehraemterBySchuljahr(schuljahr);
			assertEquals(28, lehraemter.size());
		}
	}

}
