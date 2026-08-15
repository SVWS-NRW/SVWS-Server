package de.svws_nrw.module.reporting.diagnose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Prüft die Invarianten des {@link ReportingLadezustand}: Ein geladener Wert ist niemals {@code null}, eine leere Collection ist ein geladener Wert, und ein
 * Fehlschlag trägt eine Ursache samt auslösendem Fehler.
 * <p>Die Invarianten sind die Grundlage dafür, dass {@code null} nicht länger allein bezeichnet, warum ein Wert fehlt. Fielen sie, ließe sich am Zugriff weder
 * der HTTP-Status noch die Meldung an den Anwender bestimmen.</p>
 */
class TestReportingLadezustand {

	@Test
	void testEinGeladenerWertLiefertSichSelbst() {
		final String wert = "Ein Wert";

		final ReportingLadezustand<String> zustand = ReportingLadezustand.geladen(wert);

		assertTrue(zustand.istGeladen());
		assertSame(wert, zustand.wert());
		assertNull(zustand.ursache(), "Ein geladener Wert hat keine Ursache, die sein Fehlen erklärt.");
		assertNull(zustand.fehler());
	}

	@Test
	void testEineLeereListeIstEinGeladenerWert() {
		// Ein eigener Zustand für "leer" würde die Doppeldeutigkeit von null nur an anderer Stelle wieder einführen.
		final ReportingLadezustand<List<String>> zustand = ReportingLadezustand.geladen(List.of());

		assertTrue(zustand.istGeladen());
		assertTrue(zustand.wert().isEmpty());
	}

	@Test
	void testEinGeladenerWertDarfNichtFehlen() {
		assertThrows(IllegalArgumentException.class, () -> ReportingLadezustand.geladen(null),
				"Ohne Wert ist es einer der anderen Zustände; welcher, weiß allein die ladende Stelle.");
	}

	@Test
	void testEinNichtVorhandenerDatensatzTraegtSeineUrsache() {
		final ReportingLadezustand<String> zustand = ReportingLadezustand.nichtVorhanden();

		assertFalse(zustand.istGeladen());
		assertNull(zustand.wert());
		assertEquals(ReportingProblemursache.NICHT_VORHANDEN, zustand.ursache());
	}

	@Test
	void testEinFehlschlagBehaeltDenAusloesendenFehler() {
		// Ohne den Fehler bliebe beim Zugriff nur bekannt, dass das Laden scheiterte - nicht, woran.
		final IllegalStateException fehler = new IllegalStateException("Die Verbindung zur Datenbank wurde unterbrochen.");

		final ReportingLadezustand<String> zustand = ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.INFRASTRUKTURSTOERUNG, fehler);

		assertFalse(zustand.istGeladen());
		assertEquals(ReportingProblemursache.INFRASTRUKTURSTOERUNG, zustand.ursache());
		assertSame(fehler, zustand.fehler());
	}

	@Test
	void testEinFehlschlagOhneExceptionIstZulaessig() {
		final ReportingLadezustand<String> zustand = ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, null);

		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, zustand.ursache());
		assertNull(zustand.fehler());
	}

	@Test
	void testEinFehlschlagBrauchtEineLadefehlerUrsache() {
		// Ein nicht vorhandener oder nicht darstellbarer Wert ist kein gescheiterter Ladevorgang; sonst wäre die Ursache als Kriterium für Abbruch oder
		// hingenommenes Ausgabeproblem wertlos.
		assertThrows(IllegalArgumentException.class, () -> ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.NICHT_VORHANDEN, null));
		assertThrows(IllegalArgumentException.class, () -> ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.NICHT_DARSTELLBAR, null));
		assertThrows(NullPointerException.class, () -> ReportingLadezustand.fehlgeschlagen(null, null));
	}

	@Test
	void testDieUebernommeneUrsacheBehaeltUrsacheUndFehlerUeberDenTypwechsel() {
		// Ein Datensatz entfällt aus der Auswahl, weil seine Stammdaten fehlen: Die Auswahl führt Zustände über einem anderen Werttyp, die Ursache stammt vom
		// geprüften Wert. Ginge sie beim Wechsel verloren, würde aus einem gescheiterten Zugriff eine fachlich fehlende Akte.
		final IllegalStateException fehler = new IllegalStateException("Die Verbindung zur Datenbank wurde unterbrochen.");
		final ReportingLadezustand<String> quelle = ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, fehler);

		final ReportingLadezustand<Integer> ziel = ReportingLadezustand.uebernimmUrsache(quelle);

		assertFalse(ziel.istGeladen());
		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ziel.ursache());
		assertSame(fehler, ziel.fehler());
	}

	@Test
	void testDieUebernommeneUrsacheEinesNichtVorhandenenBleibtNichtVorhanden() {
		final ReportingLadezustand<String> quelle = ReportingLadezustand.nichtVorhanden();

		final ReportingLadezustand<Integer> ziel = ReportingLadezustand.uebernimmUrsache(quelle);

		assertEquals(ReportingProblemursache.NICHT_VORHANDEN, ziel.ursache());
		assertNull(ziel.fehler());
	}

	@Test
	void testEinGeladenerZustandLiefertKeineUrsacheZumUebernehmen() {
		final ReportingLadezustand<String> geladen = ReportingLadezustand.geladen("Ein Wert");

		assertThrows(IllegalArgumentException.class, () -> ReportingLadezustand.uebernimmUrsache(geladen),
				"Ein geladener Zustand erklärt kein Fehlen; ihn zu übernehmen wäre ein Programmierfehler der Auswahl.");
		assertThrows(NullPointerException.class, () -> ReportingLadezustand.uebernimmUrsache(null));
	}

}
