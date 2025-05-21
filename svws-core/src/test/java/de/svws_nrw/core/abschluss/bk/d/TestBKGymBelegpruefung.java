package de.svws_nrw.core.abschluss.bk.d;

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

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;

/**
 * Diese Klasse enthält die Testroutinen für den Belegprüfungsalgorithmus
 * für das Abitur in dem beruflichen Gymnasium.
 */
@DisplayName("Teste den Belegprüfungsalgorithmus für das Abitur in dem beruflichen Gymnasium")
class TestBKGymBelegpruefung {

	/** Eine Map mit den Fächern der Jahrgänge des beruflichen Gymnasiums aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, List<BKGymFach>> testJahrgaengeFaecher = new HashMap<>();

	/** Eine Map mit den Abiturdaten von Schülern des beruflichen Gymnasiums aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, BKGymAbiturdaten>> testAbiturdaten = new HashMap<>();

	/** Eine Map mit den Belegprüfungsergebnissen von Schülern des beruflichen Gymnasiums aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, BKGymBelegpruefungErgebnis>> testBelegpruefungsergebnisse = new HashMap<>();


	/**
	 * Initialisiert den Test und lädt dafür die Jahrgänge und die Abiturdaten aus den
	 * zugehörigen JSON-Dateien mit den Testfällen.
	 *
	 * @throws IOException bei einem Fehler beim Laden der JSON-Resourcen
	 */
	@BeforeAll
	static void setup() throws IOException {
		ASDCoreTypeUtils.initAll();
		System.out.println("- Lade die Fächer der Jahrgänge aus den JSON-Resourcen...");
		final Map<String, BKGymFach[]> tempTestJahrgaengeFaecher =
				ResourceUtils.json2Classes("de.svws_nrw.core.abschluss.bk.d", "Jahrgang_", "_Faecher", BKGymFach[].class);
		assert (tempTestJahrgaengeFaecher != null) && (tempTestJahrgaengeFaecher.size() != 0) : "Fehler beim Laden der Gost-Fächer der Testjahrgänge!";
		for (final Map.Entry<String, BKGymFach[]> entry : tempTestJahrgaengeFaecher.entrySet())
			testJahrgaengeFaecher.put(entry.getKey(), Arrays.asList(entry.getValue()));
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Abiturdaten aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, BKGymAbiturdaten> tempTestAbiturdaten =
				ResourceUtils.json2Classes("de.svws_nrw.core.abschluss.bk.d", "Jahrgang_", "_Abiturdaten", BKGymAbiturdaten.class);
		assert (tempTestAbiturdaten != null) && (tempTestAbiturdaten.size() != 0) : "Fehler beim Laden der Abiturdaten!";
		for (final Map.Entry<String, BKGymAbiturdaten> entry : tempTestAbiturdaten.entrySet()) {
			final String[] ids = entry.getKey().split("_");
			if (ids.length != 2)
				fail("Fehler beim Laden der Abiturdaten - ungültiger Dateiname: Jahrgang_" + entry.getKey() + "_Abiturdaten");
			HashMap<String, BKGymAbiturdaten> mapSchuelerJahrgang = testAbiturdaten.get(ids[0]);
			if (mapSchuelerJahrgang == null) {
				mapSchuelerJahrgang = new HashMap<>();
				testAbiturdaten.put(ids[0], mapSchuelerJahrgang);
			}
			mapSchuelerJahrgang.put(ids[1], entry.getValue());
		}
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Belegprüfungsergebnisse aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, BKGymBelegpruefungErgebnis> tempTestBelegpruefungsergebnisse =
				ResourceUtils.json2Classes("de.svws_nrw.core.abschluss.bk.d", "Jahrgang_", "_Belegpruefungsergebnis", BKGymBelegpruefungErgebnis.class);
		assert (tempTestBelegpruefungsergebnisse != null) && (tempTestBelegpruefungsergebnisse.size() != 0)
				: "Fehler beim Laden der Belegprüfungsergebnisse!";
		for (final Map.Entry<String, BKGymBelegpruefungErgebnis> entry : tempTestBelegpruefungsergebnisse.entrySet()) {
			final String[] ids = entry.getKey().split("_");
			if (ids.length != 2)
				fail("Fehler beim Laden der Belegprüfungsergebnisse - ungültiger Dateiname: Jahrgang_" + entry.getKey() + "_Belegpruefungsergebnis");
			HashMap<String, BKGymBelegpruefungErgebnis> mapSchuelerJahrgang = testBelegpruefungsergebnisse.get(ids[0]);
			if (mapSchuelerJahrgang == null) {
				mapSchuelerJahrgang = new HashMap<>();
				testBelegpruefungsergebnisse.put(ids[0], mapSchuelerJahrgang);
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
				// Lese BKGymFaecher
				final List<BKGymFach> bkGymFaecher = testJahrgaengeFaecher.get(jahrgang);
				assert bkGymFaecher != null : "Fehler bei den Testfällen: Für den Abiturjahrgang '" + jahrgang + "' der Test-Abiturdaten '" + schueler_id
						+ "' wurden keine Test-Fächerdaten des beruflichen Gymnasiums gefunden.";
				// Lese Belegprüfungsergebnis
				final var testJahrgangBelegpruefungsergebnisse = testBelegpruefungsergebnisse.get(jahrgang);
				assert testJahrgangBelegpruefungsergebnisse != null : "Es konnte kein Jahrgang " + jahrgang
						+ " mit Belegprüfungsergebnissen als json-Dateien für den Vergleich bei den Abiturdaten " + schueler_id + " gefunden werden.";
				final var vergleichErgebnis = testJahrgangBelegpruefungsergebnisse.get(schueler_id);
				assert vergleichErgebnis != null : "Es konnte kein Gesamt-Belegprüfungsergebnis für die Abiturdaten " + schueler_id + " (Jahrgang "
						+ jahrgang + ") als json-Datei für den Vergleich gefunden werden.";
				// Füge Test für die Belegprüfung hinzu
				tests.add(DynamicTest.dynamicTest(
						"Testjahrgang " + jahrgang + " - Abiturdaten " + schueler_id + " - Belegprüfung",
						() -> {
							System.out.println();
							System.out.println("- Test: Belegprüfung die Abiturdaten " + schueler_id + " des Testjahrgangs " + jahrgang + ":");
							final BKGymFaecherManager faecherManager = new BKGymFaecherManager(abidaten.schuljahrAbitur, bkGymFaecher);
							final Schulgliederung sgl = Schulgliederung.data().getWertByID(abidaten.idSchulgliederung);
							final BKGymAbiturdatenManager manager =
									new BKGymAbiturdatenManager(abidaten, sgl, abidaten.fachklassenschluessel, faecherManager, GostHalbjahr.Q22);
							final BKGymBelegpruefungErgebnis ergebnis = manager.getBelegpruefungErgebnis();
							final List<String> log = ergebnis.log;
							if (log != null) {
								System.out.println("  Log:");
							}
							final List<BKGymBelegpruefungErgebnisFehler> fehler = ergebnis.fehlercodes;
							if (fehler.size() > 0) {
								System.out.println("  Fehlercode:");
								for (final BKGymBelegpruefungErgebnisFehler fehlercode : fehler) {
									System.out.println("    " + fehlercode.art + " - " + fehlercode.code + ": " + fehlercode.beschreibung);
								}
							}

							// Prüfe den Erfolg der Belegprüfung
							assertEquals(vergleichErgebnis.erfolgreich, ergebnis.erfolgreich, ergebnis.erfolgreich
									? "Fehler: Belegprüfung war erfolgreich, obwohl der Testfall vorgibt, dass sie fehlschlagen muss!"
									: "Fehler: Belegprüfung war nicht erfolgreich, obwohl der Testfall vorgibt, dass sie erfolgreich sein muss!");

							// Prüfe, ob alle Fehlercodes des Testfalls gefunden wurden und ob zuviele Fehlercodes im Vergleich zum Testfall gefunden wurden.
							final List<String> testfallFehler =
									vergleichErgebnis.fehlercodes.stream().map(error -> error.code).collect(Collectors.toList());
							final List<String> ergebnisFehler = ergebnis.fehlercodes.stream().map(error -> error.code).collect(Collectors.toList());
							final String zuwenig = testfallFehler.stream().filter(error -> !ergebnisFehler.contains(error)).collect(Collectors.joining(", "));
							final String zuviele = ergebnisFehler.stream().filter(error -> !testfallFehler.contains(error)).collect(Collectors.joining(", "));
							if ((!"".equals(zuwenig)) || (!"".equals(zuviele)))
								fail("Fehler: Die Fehlercodes der Belegprüfung stimmen nicht mit dem Testfall überein: " + System.lineSeparator()
										+ "  - zuviel gefundene Fehler: " + (("".equals(zuviele)) ? "---" : zuviele) + System.lineSeparator()
										+ "  - zu wenig gefunden Fehler:" + (("".equals(zuwenig)) ? "---" : zuwenig));
							System.out.println("  Test erfolgreich beendet.");
						}));
			});
		});
		return tests.stream();
	}

}
