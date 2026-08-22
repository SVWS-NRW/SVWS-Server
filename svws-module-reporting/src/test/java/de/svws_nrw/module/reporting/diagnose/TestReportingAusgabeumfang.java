package de.svws_nrw.module.reporting.diagnose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Prüft die Bildung des Ausgabeumfangs aus einer ID-Auswahl: die Zählwerte und das Kennzeichen der zulässig leeren Ausgabe.
 */
class TestReportingAusgabeumfang {

	@Test
	void testDerUmfangEinerAuswahlZaehltAngeforderteUndAusgewaehlte() {
		final ReportingAuswahlergebnis<String> auswahl = ReportingAuswahlergebnis.aus(List.of(1L, 2L, 3L), Map.of(1L, "a", 2L, "b"),
				Map.of(3L, ReportingLadezustand.nichtVorhanden()), List.of());

		assertEquals(new ReportingAusgabeumfang(3, 2, false), ReportingAusgabeumfang.ausAuswahl(auswahl));
	}

	@Test
	void testEineAuswahlOhneVerbleibendeDatensaetzeErlaubtDieLeereAusgabe() {
		final ReportingAuswahlergebnis<String> auswahl = ReportingAuswahlergebnis.aus(List.of(1L), Map.of(),
				Map.of(1L, ReportingLadezustand.nichtVorhanden()), List.of());

		assertEquals(new ReportingAusgabeumfang(1, 0, true), ReportingAusgabeumfang.ausAuswahl(auswahl));
	}

	@Test
	void testOhneAuswahlergebnisGibtEsKeinenUmfang() {
		// null als "kein Umfang" durchzureichen, hieße die Meldestelle still zu überspringen - genau das soll die Schranke der Ausgabefactory finden.
		assertThrows(NullPointerException.class, () -> ReportingAusgabeumfang.ausAuswahl(null));
	}

}
