package de.svws_nrw.abschluesse.gost.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnisFehler;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;

/**
 * Diese Klasse enthält die Testroutinen für den Belegprüfungsalgorithmus
 * für das Abitur in der gymnasialen Oberstufe.
 */
@DisplayName("Teste den Belegprüfungsalgorithmus für das Abitur in der gymnasialen Oberstufe")
class TestGostBelegpruefung {

	/** Eine Map mit den Jahrgängen der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, GostJahrgangsdaten> testGostJahrgaenge = new HashMap<>();

	/** Eine Map mit den Fächern der Jahrgänge der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, List<GostFach>> testGostJahrgaengeFaecher = new HashMap<>();

	/** Eine Map mit mit den nicht erlaubten und geforderten Fachkombinationen für die Jahrgänge der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, List<GostJahrgangFachkombination>> testGostJahrgaengeFachkombinationen = new HashMap<>();

	/** Eine Map mit den Abiturdaten von Schülern der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, Abiturdaten>> testAbiturdaten = new HashMap<>();

	/** Eine Map mit den EF1-Abitur-Belegprüfungsergebnissen von Schülern der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, GostBelegpruefungErgebnis>> testBelegpruefungsergebnisseEF1 = new HashMap<>();

	/** Eine Map mit den Gesamt-Abitur-Belegprüfungsergebnissen von Schülern der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, GostBelegpruefungErgebnis>> testBelegpruefungsergebnisseGesamt = new HashMap<>();


	/**
	 * Initialisiert den Test und lädt dafür die Jahrgänge und die Aiturdaten aus den
	 * zugehörigen JSON-Dateien mit den Testfällen.
	 *
	 * @throws IOException bei einem Fehler beim Laden der JSON-Resourcen
	 */
	@BeforeAll
	static void setup() throws IOException {
		System.out.println("- Lade die Gost-Jahrgänge aus den JSON-Resourcen...");
		final Map<String, GostJahrgangsdaten> tempTestGostJahrgaenge = ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_GostJahrgangsdaten", GostJahrgangsdaten.class);
		assert (tempTestGostJahrgaenge != null) && tempTestGostJahrgaenge.size() != 0 : "Fehler beim Laden der Gost-Testjahrgänge!";
		for (final Map.Entry<String, GostJahrgangsdaten> entry : tempTestGostJahrgaenge.entrySet())
			testGostJahrgaenge.put(entry.getKey(), entry.getValue());
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Fächer der Gost-Jahrgänge aus den JSON-Resourcen...");
		final Map<String, GostFach[]> tempTestGostJahrgaengeFaecher = ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_GostFaecher", GostFach[].class);
		assert (tempTestGostJahrgaengeFaecher != null) && tempTestGostJahrgaengeFaecher.size() != 0 : "Fehler beim Laden der Gost-Fächer der Testjahrgänge!";
		for (final Map.Entry<String, GostFach[]> entry : tempTestGostJahrgaengeFaecher.entrySet())
			testGostJahrgaengeFaecher.put(entry.getKey(), Arrays.asList(entry.getValue()));
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Informationen zu nicht erlaubten und geforderten Fachkombinationen zu den Gost-Jahrgängen aus den JSON-Resourcen...");
		final Map<String, GostJahrgangFachkombination[]> tempTestGostJahrgaengeFachkombinationen = ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_GostJahrgangFachkombination", GostJahrgangFachkombination[].class);
		assert (tempTestGostJahrgaengeFachkombinationen != null) : "Fehler beim Laden der Gost-Fachkombinationen der Testjahrgänge!";
		for (final Map.Entry<String, GostJahrgangFachkombination[]> entry : tempTestGostJahrgaengeFachkombinationen.entrySet())
			testGostJahrgaengeFachkombinationen.put(entry.getKey(), Arrays.asList(entry.getValue()));
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Abiturdaten aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, Abiturdaten> tempTestAbiturdaten = ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_Abiturdaten", Abiturdaten.class);
		assert (tempTestAbiturdaten != null) && tempTestAbiturdaten.size() != 0 : "Fehler beim Laden der Abiturdaten!";
		for (final Map.Entry<String, Abiturdaten> entry : tempTestAbiturdaten.entrySet()) {
			final String[] ids = entry.getKey().split("_");
			if (ids.length != 2)
				fail("Fehler beim Laden der Abiturdaten - ungültiger Dateiname: Jahrgang_" + entry.getKey() + "_Abiturdaten");
			HashMap<String, Abiturdaten> mapSchuelerJahrgang = testAbiturdaten.get(ids[0]);
			if (mapSchuelerJahrgang == null) {
				mapSchuelerJahrgang = new HashMap<>();
				testAbiturdaten.put(ids[0], mapSchuelerJahrgang);
			}
			mapSchuelerJahrgang.put(ids[1], entry.getValue());
		}
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Gesamt-Belegprüfungsergebnisse aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, GostBelegpruefungErgebnis> tempTestBelegpruefungsergebnisseGesamt = ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_Belegpruefungsergebnis_Gesamt", GostBelegpruefungErgebnis.class);
		assert (tempTestBelegpruefungsergebnisseGesamt != null) && tempTestBelegpruefungsergebnisseGesamt.size() != 0 : "Fehler beim Laden der Gesamt-Belegprüfungsergebnisse!";
		for (final Map.Entry<String, GostBelegpruefungErgebnis> entry : tempTestBelegpruefungsergebnisseGesamt.entrySet()) {
			final String[] ids = entry.getKey().split("_");
			if (ids.length != 2)
				fail("Fehler beim Laden der Gesamt-Belegprüfungsergebnisse - ungültiger Dateiname: Jahrgang_" + entry.getKey() + "_Belegpruefungsergebnis_Gesamt");
			HashMap<String, GostBelegpruefungErgebnis> mapSchuelerJahrgang = testBelegpruefungsergebnisseGesamt.get(ids[0]);
			if (mapSchuelerJahrgang == null) {
				mapSchuelerJahrgang = new HashMap<>();
				testBelegpruefungsergebnisseGesamt.put(ids[0], mapSchuelerJahrgang);
			}
			mapSchuelerJahrgang.put(ids[1], entry.getValue());
		}
		System.out.println("  FERTIG!");

		System.out.println("- Lade die EF1-Belegprüfungsergebnisse aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, GostBelegpruefungErgebnis> tempTestBelegpruefungsergebnisseEF1 = ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_Belegpruefungsergebnis_EF1", GostBelegpruefungErgebnis.class);
		assert (tempTestBelegpruefungsergebnisseEF1 != null) && tempTestBelegpruefungsergebnisseEF1.size() != 0 : "Fehler beim Laden der EF1-Belegprüfungsergebnisse!";
		for (final Map.Entry<String, GostBelegpruefungErgebnis> entry : tempTestBelegpruefungsergebnisseEF1.entrySet()) {
			final String[] ids = entry.getKey().split("_");
			if (ids.length != 2)
				fail("Fehler beim Laden der EF1-Belegprüfungsergebnisse - ungültiger Dateiname: Jahrgang_" + entry.getKey() + "_Belegpruefungsergebnis_EF1");
			HashMap<String, GostBelegpruefungErgebnis> mapSchuelerJahrgang = testBelegpruefungsergebnisseEF1.get(ids[0]);
			if (mapSchuelerJahrgang == null) {
				mapSchuelerJahrgang = new HashMap<>();
				testBelegpruefungsergebnisseEF1.put(ids[0], mapSchuelerJahrgang);
			}
			mapSchuelerJahrgang.put(ids[1], entry.getValue());
		}
		System.out.println("  FERTIG!");
	}



	/**
	 * Führt für alle Testfälle eine Belegpruefung durch.
	 *
	 * @return ein Stream der Testfälle als {@link DynamicTest}-Objekte
	 */
	@TestFactory
	@DisplayName("Teste Belegprüfungsalgorithmus...")
	Stream<DynamicTest> testBelegpruefung() {
		final ArrayList<DynamicTest> tests = new ArrayList<>();
		testAbiturdaten.forEach((jahrgang, mapSchuelerJahrgang) -> {
			mapSchuelerJahrgang.forEach((schueler_id, abidaten) -> {
				// Lese GostJahrgangsdaten
				final GostJahrgangsdaten gostJahrgangsdaten = testGostJahrgaenge.get(jahrgang);
				assert gostJahrgangsdaten != null : "Fehler bei den Testfällen: Für den Abiturjahrgang '" + jahrgang + "' der Test-Abiturdaten '" + schueler_id + "' wurden keine Jahrgangsdaten der gymnasialen Oberstufe gefunden.";
				// Lese GostFaecher
				final List<GostFach> gostFaecher = testGostJahrgaengeFaecher.get(jahrgang);
				assert gostFaecher != null : "Fehler bei den Testfällen: Für den Abiturjahrgang '" + jahrgang + "' der Test-Abiturdaten '" + schueler_id + "' wurden keine Test-Fächerdaten der gymnasialen Oberstufe gefunden.";
				final List<GostJahrgangFachkombination> gostFachkombinationen = testGostJahrgaengeFachkombinationen.get(jahrgang);
				assert gostFachkombinationen != null : "Fehler bei den Testfällen: Für den Abiturjahrgang '" + jahrgang + "' der Test-Abiturdaten '" + schueler_id + "' wurden keine Test-Fachkombinationen der gymnasialen Oberstufe gefunden.";
				// Lese EF1-Belegprüefungsergebnis
				final var testJahrgangBelegpruefungsergebnisseEF1 = testBelegpruefungsergebnisseEF1.get(jahrgang);
				assert testJahrgangBelegpruefungsergebnisseEF1 != null : "Es konnte kein Jahrgang " + jahrgang + " mit EF1-Belegprüfungsergebnissen als json-Dateien für den Vergleich bei den Abiturdaten " + schueler_id + " gefunden werden.";
				final var vergleichErgebnisEF1 = testJahrgangBelegpruefungsergebnisseEF1.get(schueler_id);
				assert vergleichErgebnisEF1 != null : "Es konnte kein EF1-Belegprüfungsergebnis für die Abiturdaten " + schueler_id + " (Jahrgang " + jahrgang + ") als json-Datei für den Vergleich gefunden werden.";
				// Lese Gesamt-Belegprüefungsergebnis
				final var testJahrgangBelegpruefungsergebnisseGesamt = testBelegpruefungsergebnisseGesamt.get(jahrgang);
				assert testJahrgangBelegpruefungsergebnisseGesamt != null : "Es konnte kein Jahrgang " + jahrgang + " mit Gesamt-Belegprüfungsergebnissen als json-Dateien für den Vergleich bei den Abiturdaten " + schueler_id + " gefunden werden.";
				final var vergleichErgebnisGesamt = testJahrgangBelegpruefungsergebnisseGesamt.get(schueler_id);
				assert vergleichErgebnisGesamt != null : "Es konnte kein Gesamt-Belegprüfungsergebnis für die Abiturdaten " + schueler_id + " (Jahrgang " + jahrgang + ") als json-Datei für den Vergleich gefunden werden.";
				// Füge Test für die EF1-Belegprüfung hinzu
				tests.add(DynamicTest.dynamicTest(
						"Testjahrgang " + jahrgang + " - Abiturdaten " + schueler_id + " - Belegprüfung EF1",
						() -> {
							System.out.println();
							System.out.println("- Test: EF1-Belegprüfung die Abiturdaten " + schueler_id + " des Testjahrgangs " + jahrgang + ":");
							final AbiturdatenManager manager = new AbiturdatenManager(abidaten, gostJahrgangsdaten, gostFaecher, gostFachkombinationen, GostBelegpruefungsArt.EF1);
							final GostBelegpruefungErgebnis ergebnis = manager.getBelegpruefungErgebnis();
							final List<String> log = ergebnis.log;
							if (log != null) {
								System.out.println("  Log:");
							}
							final List<GostBelegpruefungErgebnisFehler> fehler = ergebnis.fehlercodes;
							if (fehler.size() > 0) {
								System.out.println("  Fehlercode:");
								for (final GostBelegpruefungErgebnisFehler fehlercode : fehler) {
									System.out.println("    " + fehlercode.art + " - " + fehlercode.code + ": " + fehlercode.beschreibung);
								}
							}

							// Prüfe den Erfolg der Belegprüfung
							final boolean ergebnisUnterschiedlich = (ergebnis.erfolgreich != vergleichErgebnisEF1.erfolgreich);
							final String ergebnisUnterschiedlichFehler = !ergebnisUnterschiedlich ? "" : (ergebnis.erfolgreich
									? "Fehler: EF1-Belegprüfung war erfolgreich, obwohl der Testfall vorgibt, dass sie fehlschlagen muss!"
											: "Fehler: EF1-Belegprüfung war nicht erfolgreich, obwohl der Testfall vorgibt, dass sie erfolgreich sein muss!");

							// Prüfe, ob alle Fehlercodes des Testfalls gefunden wurden und ob zuviele Fehlercodes im Vergleich zum Testfall gefunden wurden.
							final List<String> testfallFehler = vergleichErgebnisEF1.fehlercodes.stream().map(error -> error.code).collect(Collectors.toList());
							final List<String> ergebnisFehler = ergebnis.fehlercodes.stream().map(error -> error.code).collect(Collectors.toList());
							final String zuwenig = testfallFehler.stream().filter(error -> !ergebnisFehler.contains(error)).collect(Collectors.joining(", "));
							final String zuviele = ergebnisFehler.stream().filter(error -> !testfallFehler.contains(error)).collect(Collectors.joining(", "));
							if ((!"".equals(zuwenig)) || (!"".equals(zuviele))) {
								fail((ergebnisUnterschiedlich ? ergebnisUnterschiedlichFehler + " " : "Fehler: ")
										+ "Die Fehlercodes der EF1-Belegprüfung stimmen nicht mit dem Testfall überein: " + System.lineSeparator()
										+ "  - zuviel gefundene Fehler: " + (("".equals(zuviele)) ? "---" : zuviele) + System.lineSeparator()
										+ "  - zu wenig gefunden Fehler:" + (("".equals(zuwenig)) ? "---" : zuwenig));
							} else if (ergebnisUnterschiedlich) {
								fail(ergebnisUnterschiedlichFehler);
							}
							System.out.println("  Test erfolgreich beendet.");
						}));
				// Füge Test für die Gesamt-Belegprüfung hinzu
				tests.add(DynamicTest.dynamicTest(
						"Testjahrgang " + jahrgang + " - Abiturdaten " + schueler_id + " - Belegprüfung Gesamt",
						() -> {
							System.out.println();
							System.out.println("- Test: Gesamt-Belegprüfung die Abiturdaten " + schueler_id + " des Testjahrgangs " + jahrgang + ":");
							final AbiturdatenManager manager = new AbiturdatenManager(abidaten, gostJahrgangsdaten, gostFaecher, gostFachkombinationen, GostBelegpruefungsArt.GESAMT);
							final GostBelegpruefungErgebnis ergebnis = manager.getBelegpruefungErgebnis();
							final List<String> log = ergebnis.log;
							if (log != null) {
								System.out.println("  Log:");
							}
							final List<GostBelegpruefungErgebnisFehler> fehler = ergebnis.fehlercodes;
							if (fehler.size() > 0) {
								System.out.println("  Fehlercode:");
								for (final GostBelegpruefungErgebnisFehler fehlercode : fehler) {
									System.out.println("    " + fehlercode.art + " - " + fehlercode.code + ": " + fehlercode.beschreibung);
								}
							}

							// Prüfe den Erfolg der Belegprüfung
							assertEquals(vergleichErgebnisGesamt.erfolgreich, ergebnis.erfolgreich, ergebnis.erfolgreich
									? "Fehler: Gesamt-Belegprüfung war erfolgreich, obwohl der Testfall vorgibt, dass sie fehlschlagen muss!"
											: "Fehler: Gesamt-Belegprüfung war nicht erfolgreich, obwohl der Testfall vorgibt, dass sie erfolgreich sein muss!");

							// Prüfe, ob alle Fehlercodes des Testfalls gefunden wurden und ob zuviele Fehlercodes im Vergleich zum Testfall gefunden wurden.
							final List<String> testfallFehler = vergleichErgebnisGesamt.fehlercodes.stream().map(error -> error.code).collect(Collectors.toList());
							final List<String> ergebnisFehler = ergebnis.fehlercodes.stream().map(error -> error.code).collect(Collectors.toList());
							final String zuwenig = testfallFehler.stream().filter(error -> !ergebnisFehler.contains(error)).collect(Collectors.joining(", "));
							final String zuviele = ergebnisFehler.stream().filter(error -> !testfallFehler.contains(error)).collect(Collectors.joining(", "));
							if ((!"".equals(zuwenig)) || (!"".equals(zuviele)))
								fail("Fehler: Die Fehlercodes der Gesamt-Belegprüfung stimmen nicht mit dem Testfall überein: " + System.lineSeparator()
								+ "  - zuviel gefundene Fehler: " + (("".equals(zuviele)) ? "---" : zuviele)  + System.lineSeparator()
								+ "  - zu wenig gefunden Fehler:" + (("".equals(zuwenig)) ? "---" : zuwenig));
							System.out.println("  Test erfolgreich beendet.");
						}));
			});
		});
		return tests.stream();
	}

}
