package de.svws_nrw.module.reporting.diagnose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Prüft den öffentlichen Hinweisvertrag: die Zählweise, die Projektion der internen Befunde auf den kleinen Kategorienkatalog und die Zusagen, die der Header
 * einem Verbraucher macht.
 * <p>Die Sollwerte sind <b>als Literale</b> hinterlegt und nicht aus dem Produktivcode abgeleitet. Der Headerwert ist ein Vertrag: Würde der Test ihn aus
 * denselben Konstanten bilden, die er prüfen soll, schriebe er jede Änderung des Vertrags stillschweigend mit.</p>
 */
class TestReportingHinweisSerializer {

	/** Der erwartete Aufbau des Headerwerts: nur Kleinbuchstaben, Zahlen und die zulässige Boolean-Schreibweise nach RFC 9651. */
	private static final Pattern ZULAESSIGER_WERT = Pattern.compile("v=\\d+, gesamt=\\d+, leer=\\?[01](, [a-z][a-z0-9_-]*=\\d+)*");


	/**
	 * Erzeugt ein Ausgabeproblem mit den übergebenen Bestandteilen.
	 *
	 * @param ursache    Die Ursache des Problems.
	 * @param auswirkung Die Auswirkung des Problems.
	 * @param id         Die ID des betroffenen Datensatzes.
	 *
	 * @return Das Ausgabeproblem.
	 */
	private static ReportingProblem problem(final ReportingProblemursache ursache, final ReportingProblemauswirkung auswirkung, final long id) {
		return new ReportingProblem(ursache, auswirkung, ReportingProblemSchluessel.fuer(ReportingSchueler.class, id));
	}

	/**
	 * Gibt ein Ausgabeproblem zurück, das in der Kategorie der fehlenden Datensätze erscheint.
	 *
	 * @param id Die ID des betroffenen Datensatzes.
	 *
	 * @return Das Ausgabeproblem.
	 */
	private static ReportingProblem ausgelassenerDatensatz(final long id) {
		return problem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, id);
	}


	@Test
	void testOhneBefundMeldetDerHeaderDenStabilenKern() {
		assertEquals("v=0, gesamt=0, leer=?0", ReportingHinweisSerializer.headerwert(List.of(), false));
	}

	@Test
	void testEineBewusstLeereAusgabeMeldetDenLeerstandOhneBefund() {
		// Der reine Filter-Leerfall: Es fehlt nichts, es wurde nur alles ausgeschlossen. Eine Warnbehauptung wäre hier falsch.
		assertEquals("v=0, gesamt=0, leer=?1", ReportingHinweisSerializer.headerwert(List.of(), true));
	}

	@Test
	void testAusgelasseneDatensaetzeErscheinenAlsFehlendeDatensaetze() {
		final List<ReportingProblem> probleme = List.of(
				problem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, 1L),
				problem(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, 2L));

		// Beide Ursachen fallen in dieselbe Kategorie: Für den Empfänger fehlt der Datensatz, unabhängig davon, warum.
		assertEquals("v=0, gesamt=2, leer=?0, datensaetze=2", ReportingHinweisSerializer.headerwert(probleme, false));
	}

	@Test
	void testFehlendeTeildatenErscheinenAlsFehlendeAngaben() {
		final List<ReportingProblem> probleme = List.of(
				problem(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.TEILDATEN_FEHLEN, 1L),
				problem(ReportingProblemursache.OPTIONALER_WERT_FEHLT, ReportingProblemauswirkung.TEILDATEN_FEHLEN, 2L));

		assertEquals("v=0, gesamt=2, leer=?0, angaben=2", ReportingHinweisSerializer.headerwert(probleme, false));
	}

	@Test
	void testEinNichtDarstellbarerWertBildetEineEigeneKategorie() {
		// Die Daten liegen vor, allein ihre Ausgabe misslang - bei einer Bescheinigung heißt das, dass sie ihren Prüfcode nicht trägt.
		final List<ReportingProblem> probleme =
				List.of(problem(ReportingProblemursache.NICHT_DARSTELLBAR, ReportingProblemauswirkung.TEILDATEN_FEHLEN, 1L));

		assertEquals("v=0, gesamt=1, leer=?0, darstellung=1", ReportingHinweisSerializer.headerwert(probleme, false));
	}

	@Test
	void testDieKategorienErscheinenInDerReihenfolgeDesKatalogs() {
		// Feste Reihenfolge, damit derselbe Sachverhalt denselben Headerwert ergibt - sonst wären Vertragstests von der Eingabereihenfolge abhängig.
		final List<ReportingProblem> probleme = List.of(
				problem(ReportingProblemursache.NICHT_DARSTELLBAR, ReportingProblemauswirkung.TEILDATEN_FEHLEN, 1L),
				problem(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.TEILDATEN_FEHLEN, 2L),
				problem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, 3L));

		assertEquals("v=0, gesamt=3, leer=?0, datensaetze=1, angaben=1, darstellung=1",
				ReportingHinweisSerializer.headerwert(probleme, false));
	}

	@Test
	void testDieSummeDerKategorienErgibtDieGesamtzahl() {
		// Die Kategorien zerlegen dieselbe Menge. Ohne diese Zusage könnte ein Empfänger die Zahlen nicht zusammenzählen.
		final List<ReportingProblem> probleme = new ArrayList<>();
		for (long id = 1; id <= 4; id++) {
			probleme.add(problem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, id));
			probleme.add(problem(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.TEILDATEN_FEHLEN, id));
			probleme.add(problem(ReportingProblemursache.NICHT_DARSTELLBAR, ReportingProblemauswirkung.TEILDATEN_FEHLEN, id));
		}

		final int summe = ReportingHinweisSerializer.anzahlenJeKategorie(probleme).values().stream().mapToInt(Integer::intValue).sum();

		assertEquals(probleme.size(), summe);
		assertEquals("v=0, gesamt=12, leer=?0, datensaetze=4, angaben=4, darstellung=4", ReportingHinweisSerializer.headerwert(probleme, false));
	}

	@Test
	void testJedeKombinationAusUrsacheUndAuswirkungErreichtDieVorgeseheneKategorie() {
		// Die Sollwerte stehen als Literale: Eine Prüfung auf "nicht null" wäre wertlos, weil die Projektion konstruktionsbedingt immer eine Kategorie
		// liefert. Geprüft wird deshalb, welche - eine versehentlich geänderte Zuordnung ändert den Vertrag mit dem Client.
		final Map<String, ReportingHinweisKategorie> soll = new TreeMap<>();
		soll.put("NICHT_VORHANDEN/DATENSATZ_AUSGELASSEN", ReportingHinweisKategorie.DATENSAETZE_FEHLEN);
		soll.put("NICHT_VORHANDEN/TEILDATEN_FEHLEN", ReportingHinweisKategorie.ANGABEN_FEHLEN);
		soll.put("DATENSATZBEZOGENER_LADEFEHLER/DATENSATZ_AUSGELASSEN", ReportingHinweisKategorie.DATENSAETZE_FEHLEN);
		soll.put("DATENSATZBEZOGENER_LADEFEHLER/TEILDATEN_FEHLEN", ReportingHinweisKategorie.ANGABEN_FEHLEN);
		soll.put("NICHT_DARSTELLBAR/DATENSATZ_AUSGELASSEN", ReportingHinweisKategorie.WERT_NICHT_DARSTELLBAR);
		soll.put("NICHT_DARSTELLBAR/TEILDATEN_FEHLEN", ReportingHinweisKategorie.WERT_NICHT_DARSTELLBAR);
		soll.put("OPTIONALER_WERT_FEHLT/DATENSATZ_AUSGELASSEN", ReportingHinweisKategorie.DATENSAETZE_FEHLEN);
		soll.put("OPTIONALER_WERT_FEHLT/TEILDATEN_FEHLEN", ReportingHinweisKategorie.ANGABEN_FEHLEN);

		final Map<String, ReportingHinweisKategorie> ist = new TreeMap<>();
		for (final ReportingProblemursache ursache : ReportingProblemursache.values()) {
			// Eine abbrechende Ursache ist kein hingenommenes Problem und erreicht den Hinweisvertrag nie.
			if (ursache.istAbbruch()) {
				continue;
			}
			for (final ReportingProblemauswirkung auswirkung : ReportingProblemauswirkung.values()) {
				ist.put(ursache.name() + "/" + auswirkung.name(), ReportingHinweisKategorie.fuer(problem(ursache, auswirkung, 1L)));
			}
		}

		// Die Sollwerte decken zugleich alle Kombinationen ab: Ein neuer Enum-Wert lässt diesen Vergleich auffallen, während der Übersetzer die Zuordnung im
		// vollständigen switch der Projektion erzwingt.
		assertEquals(soll, ist);
	}

	@Test
	void testEinFehlenderDiagnosebestandWirdAbgelehnt() {
		// null als "keine Probleme" zu lesen, hieße einen fehlenden Bestand wie eine geprüfte problemfreie Ausgabe zu behandeln. Der Header bescheinigte damit
		// eine Vollständigkeit, die niemand festgestellt hat - genau das soll der Vertrag ausschließen. Der problemfreie Fall ist die leere Sammlung.
		assertThrows(NullPointerException.class, () -> ReportingHinweisSerializer.headerwert(null, false));
		assertThrows(NullPointerException.class, () -> ReportingHinweisSerializer.anzahlenJeKategorie(null));
	}

	@Test
	void testDerHeaderwertTraegtWederIdsNochFreitexte() {
		// Der Header begleitet ein Dokument, das weitergegeben und gespeichert wird. Personenbezogene Angaben haben darin nichts zu suchen.
		final List<ReportingProblem> probleme =
				List.of(problem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, 4711L));

		final String wert = ReportingHinweisSerializer.headerwert(probleme, true);

		assertTrue(ZULAESSIGER_WERT.matcher(wert).matches(), "Der Headerwert entspricht nicht dem zugesagten Aufbau: " + wert);
		assertTrue(!wert.contains("4711"), "Die ID des betroffenen Datensatzes darf den Header nicht erreichen: " + wert);
		assertTrue(!wert.contains("Schueler"), "Die Objektart darf den Header nicht erreichen: " + wert);
	}

	// ##### Hinweisdatei der ZIP-Ausgabe #####

	@Test
	void testOhneBefundUndOhneLeerstandEntstehtKeineHinweisdatei() {
		// Eine Beilage ohne Inhalt stellte jede vollständige Ausgabe unter einen Vorbehalt.
		assertFalse(ReportingHinweisSerializer.brauchtHinweisdatei(List.of(), false));
	}

	@Test
	void testEinBefundOderEinLeerstandVerlangtEineHinweisdatei() {
		assertTrue(ReportingHinweisSerializer.brauchtHinweisdatei(List.of(), true), "Ein Archiv ohne PDF-Datei muss erklärt werden.");
		assertTrue(ReportingHinweisSerializer.brauchtHinweisdatei(List.of(ausgelassenerDatensatz(1L)), false));
	}

	@Test
	void testDerReineFilterLeerfallBleibtOhneFehlerbehauptung() {
		// Wer alles ausfiltert und deshalb nichts erhält, hat nichts falsch gemacht. Eine Warnung wäre dort eine Unterstellung.
		final String text = ReportingHinweisSerializer.hinweisdatei(List.of(), true);

		assertTrue(text.contains("blieb nach der Auswahl kein Datensatz übrig"), text);
		assertTrue(text.contains("keine PDF-Datei erzeugt"), text);
		assertFalse(text.contains("unvollständig"), "Der reine Filter-Leerfall ist keine unvollständige Ausgabe: " + text);
		assertFalse(text.contains("Hinweise insgesamt"), "Ohne Befund gibt es keine Anzahl zu nennen: " + text);
	}

	@Test
	void testEineUnvollstaendigeAusgabeNenntAnzahlUndKategorien() {
		final List<ReportingProblem> probleme = List.of(
				ausgelassenerDatensatz(1L),
				ausgelassenerDatensatz(2L),
				new ReportingProblem(ReportingProblemursache.NICHT_DARSTELLBAR, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
						ReportingProblemSchluessel.fuer(ReportingSchueler.class, 3L)));

		final String text = ReportingHinweisSerializer.hinweisdatei(probleme, false);

		assertTrue(text.contains("Die Ausgabe wurde erstellt, ist aber unvollständig."), text);
		assertTrue(text.contains("Hinweise insgesamt: 3"), text);
		assertTrue(text.contains(ReportingHinweisKategorie.DATENSAETZE_FEHLEN.bezeichnung() + ": 2"), text);
		assertTrue(text.contains(ReportingHinweisKategorie.WERT_NICHT_DARSTELLBAR.bezeichnung() + ": 1"), text);
		assertFalse(text.contains(ReportingHinweisKategorie.ANGABEN_FEHLEN.bezeichnung()), "Eine Kategorie ohne Befund gehört nicht in den Text: " + text);
	}

	@Test
	void testEinLeerstandMitBefundenNenntBeides() {
		final String text = ReportingHinweisSerializer.hinweisdatei(List.of(ausgelassenerDatensatz(1L)), true);

		assertTrue(text.contains("keine PDF-Datei erzeugt"), text);
		assertTrue(text.contains("Hinweise insgesamt: 1"), text);
	}

	@Test
	void testDieHinweisdateiTraegtWederIdsNochNamenNochFehlermeldungen() {
		// Ein ZIP-Archiv wird weitergegeben, gespeichert und geöffnet, ohne dass jemand über den Inhalt seiner Beilagen nachdenkt.
		final String text = ReportingHinweisSerializer.hinweisdatei(List.of(ausgelassenerDatensatz(4711L)), true);

		assertFalse(text.contains("4711"), "Die ID des betroffenen Datensatzes darf die Hinweisdatei nicht erreichen: " + text);
		assertFalse(text.contains("Schueler"), "Die Objektart darf die Hinweisdatei nicht erreichen: " + text);
		assertFalse(text.contains("NICHT_VORHANDEN"), "Interne Ursachen sind kein Anzeigetext: " + text);
	}

	@Test
	void testDieAnzahlWirdNichtAlsMengeFehlenderDateienAusgegeben() {
		// gesamt ist eine Diagnosegröße. Oberflächentexte dürfen daraus keine Zahl fehlender Datensätze oder Dateien machen.
		final String text = ReportingHinweisSerializer.hinweisdatei(List.of(ausgelassenerDatensatz(1L)), false);

		assertTrue(text.contains("nicht die Anzahl fehlender Datensätze oder Dateien"), text);
	}

}
