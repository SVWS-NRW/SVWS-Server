package de.svws_nrw.abschluesse.gost.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusErgebnis;
import de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.GostFaecherManager;

@DisplayName("Teste den Markierungsalgorithmus für das Abitur in der gymnasialen Oberstufe")
class TestGostMarkierungsalgorithmus {

	/** Eine Map mit den Jahrgängen der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, GostJahrgangsdaten> testGostJahrgaenge = new HashMap<>();

	/** Eine Map mit den Fächern der Jahrgänge der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, List<GostFach>> testGostJahrgaengeFaecher = new HashMap<>();

	/** Eine Map mit mit den nicht erlaubten und geforderten Fachkombinationen für die Jahrgänge der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, List<GostJahrgangFachkombination>> testGostJahrgaengeFachkombinationen = new HashMap<>();

	/** Eine Map mit den Abiturdaten von Schülern der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, Abiturdaten>> testAbiturdaten = new HashMap<>();

	/** Eine Map mit den Ergenissen des Abitur-Markierungsalgorithmus für die Abiturdaten von Schülern der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, GostAbiturMarkierungsalgorithmusErgebnis>> testErgebnisseMarkierungsalgorihmus = new HashMap<>();


	/**
	 * Initialisiert den Test und lädt dafür die Jahrgänge und die Aiturdaten aus den
	 * zugehörigen JSON-Dateien mit den Testfällen.
	 *
	 * @throws IOException bei einem Fehler beim Laden der JSON-Resourcen
	 */
	@BeforeAll
	static void setup() throws IOException {
		ASDCoreTypeUtils.initAll();
		System.out.println("- Lade die Gost-Jahrgänge aus den JSON-Resourcen...");
		final Map<String, GostJahrgangsdaten> tempTestGostJahrgaenge =
				ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_GostJahrgangsdaten", GostJahrgangsdaten.class);
		assert (tempTestGostJahrgaenge != null) && (tempTestGostJahrgaenge.size() != 0) : "Fehler beim Laden der Gost-Testjahrgänge!";
		for (final Map.Entry<String, GostJahrgangsdaten> entry : tempTestGostJahrgaenge.entrySet())
			testGostJahrgaenge.put(entry.getKey(), entry.getValue());
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Fächer der Gost-Jahrgänge aus den JSON-Resourcen...");
		final Map<String, GostFach[]> tempTestGostJahrgaengeFaecher =
				ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_GostFaecher", GostFach[].class);
		assert (tempTestGostJahrgaengeFaecher != null) && (tempTestGostJahrgaengeFaecher.size() != 0) : "Fehler beim Laden der Gost-Fächer der Testjahrgänge!";
		for (final Map.Entry<String, GostFach[]> entry : tempTestGostJahrgaengeFaecher.entrySet())
			testGostJahrgaengeFaecher.put(entry.getKey(), Arrays.asList(entry.getValue()));
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Informationen zu nicht erlaubten und geforderten Fachkombinationen zu den Gost-Jahrgängen aus den JSON-Resourcen...");
		final Map<String, GostJahrgangFachkombination[]> tempTestGostJahrgaengeFachkombinationen = ResourceUtils
				.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_GostJahrgangFachkombination", GostJahrgangFachkombination[].class);
		assert (tempTestGostJahrgaengeFachkombinationen != null) : "Fehler beim Laden der Gost-Fachkombinationen der Testjahrgänge!";
		for (final Map.Entry<String, GostJahrgangFachkombination[]> entry : tempTestGostJahrgaengeFachkombinationen.entrySet())
			testGostJahrgaengeFachkombinationen.put(entry.getKey(), Arrays.asList(entry.getValue()));
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Abiturdaten aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, Abiturdaten> tempTestAbiturdaten =
				ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_Abiturdaten", Abiturdaten.class);
		assert (tempTestAbiturdaten != null) && (tempTestAbiturdaten.size() != 0) : "Fehler beim Laden der Abiturdaten!";
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

		System.out.println("- Lade die Ergebnisse des Markierungsalgorithmus aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, GostAbiturMarkierungsalgorithmusErgebnis> tempTestErgebnisseMarkierungsalgorihmus =
				ResourceUtils.json2Classes("de.svws_nrw.abschluesse.gost.test", "Jahrgang_", "_Markierungsalgorithmus",
						GostAbiturMarkierungsalgorithmusErgebnis.class);
		assert (tempTestErgebnisseMarkierungsalgorihmus != null) && (tempTestErgebnisseMarkierungsalgorihmus.size() != 0)
				: "Fehler beim Laden der Ergebnisse für den Abitur-Markierungsalgorithmus!";
		for (final Map.Entry<String, GostAbiturMarkierungsalgorithmusErgebnis> entry : tempTestErgebnisseMarkierungsalgorihmus.entrySet()) {
			final String[] ids = entry.getKey().split("_");
			if (ids.length != 2)
				fail("Fehler beim Laden der Ergebnisse des Abitur-Markierungsalgorithmus - ungültiger Dateiname: Jahrgang_" + entry.getKey()
						+ "_Markierungsalgorihmus");
			HashMap<String, GostAbiturMarkierungsalgorithmusErgebnis> mapSchuelerJahrgang = testErgebnisseMarkierungsalgorihmus.get(ids[0]);
			if (mapSchuelerJahrgang == null) {
				mapSchuelerJahrgang = new HashMap<>();
				testErgebnisseMarkierungsalgorihmus.put(ids[0], mapSchuelerJahrgang);
			}
			mapSchuelerJahrgang.put(ids[1], entry.getValue());
		}
		System.out.println("  FERTIG!");
	}


	/**
	 * Führt für alle Testfälle eine Prüfung des Markierungsalgorithmus durch.
	 *
	 * @return ein Stream der Testfälle als {@link DynamicTest}-Objekte
	 */
	@TestFactory
	@DisplayName("Teste Markierungsalgorithmus...")
	Stream<DynamicTest> testBelegpruefung() {
		final ArrayList<DynamicTest> tests = new ArrayList<>();
		testAbiturdaten.forEach((jahrgang, mapSchuelerJahrgang) -> {
			mapSchuelerJahrgang.forEach((schueler_id, abidaten) -> {
				// Lese GostJahrgangsdaten
				final GostJahrgangsdaten gostJahrgangsdaten = testGostJahrgaenge.get(jahrgang);
				assert gostJahrgangsdaten != null : "Fehler bei den Testfällen: Für den Abiturjahrgang '" + jahrgang + "' der Test-Abiturdaten '" + schueler_id
						+ "' wurden keine Jahrgangsdaten der gymnasialen Oberstufe gefunden.";
				// Lese GostFaecher
				final List<GostFach> gostFaecher = testGostJahrgaengeFaecher.get(jahrgang);
				assert gostFaecher != null : "Fehler bei den Testfällen: Für den Abiturjahrgang '" + jahrgang + "' der Test-Abiturdaten '" + schueler_id
						+ "' wurden keine Test-Fächerdaten der gymnasialen Oberstufe gefunden.";
				final List<GostJahrgangFachkombination> gostFachkombinationen = testGostJahrgaengeFachkombinationen.get(jahrgang);
				assert gostFachkombinationen != null : "Fehler bei den Testfällen: Für den Abiturjahrgang '" + jahrgang + "' der Test-Abiturdaten '"
						+ schueler_id + "' wurden keine Test-Fachkombinationen der gymnasialen Oberstufe gefunden.";
				// Lese Ergebnis des Abitur-Markierungsergebnis ein, sofern eines vorhanden ist.
				final var testJahrgangErgebnisseMarkierungsalgorihmus = testErgebnisseMarkierungsalgorihmus.get(jahrgang);
				if (testJahrgangErgebnisseMarkierungsalgorihmus == null)
					return;
				final var vergleichErgebnisMarkierungsalgorihmus = testJahrgangErgebnisseMarkierungsalgorihmus.get(schueler_id);
				if (vergleichErgebnisMarkierungsalgorihmus == null)
					return;
				// Füge Test für den Abitur-Markierungsalgorithmus hinzu
				tests.add(DynamicTest.dynamicTest(
						"Testjahrgang " + jahrgang + " - Abiturdaten " + schueler_id + " - Markierungsalgorithmus",
						() -> {
							System.out.println();
							System.out.println("- Test: Markierungsalgorithmus für die Abiturdaten " + schueler_id + " des Testjahrgangs " + jahrgang + ":");
							final GostFaecherManager faecherManager =
									new GostFaecherManager(gostJahrgangsdaten.abiturjahr - 1, gostFaecher, gostFachkombinationen);
							final AbiturdatenManager manager =
									new AbiturdatenManager(ServerMode.DEV, abidaten, gostJahrgangsdaten, faecherManager, GostBelegpruefungsArt.GESAMT);
							final GostAbiturMarkierungsalgorithmusErgebnis ergebnis = manager.getErgebnisMarkierungsalgorithmus();
							if (!ergebnis.log.isEmpty()) {
								System.out.println("  Log:");
								for (final String text : ergebnis.log)
									System.out.println("    " + text);
							}

							// Prüfe den Erfolg der Belegprüfung
							assertEquals(vergleichErgebnisMarkierungsalgorihmus.erfolgreich, ergebnis.erfolgreich, ergebnis.erfolgreich
									? "Fehler: Der Markierungsalgorithmus war erfolgreich, obwohl der Testfall vorgibt, dass sie fehlschlagen muss!"
									: "Fehler: Der Markierungsalgorithmus war nicht erfolgreich, obwohl der Testfall vorgibt, dass sie erfolgreich sein muss!");

							// Prüfe, ob alle Fehlercodes des Testfalls gefunden wurden und ob zuviele Fehlercodes im Vergleich zum Testfall gefunden wurden.
							final HashMap3D<Long, Integer, Boolean, GostAbiturMarkierungsalgorithmusMarkierung> testfallMarkierungen = new HashMap3D<>();
							for (final GostAbiturMarkierungsalgorithmusMarkierung markierung : vergleichErgebnisMarkierungsalgorihmus.markierungen)
								testfallMarkierungen.put(markierung.idFach, markierung.idHalbjahr, markierung.markiert, markierung);
							final HashMap3D<Long, Integer, Boolean, GostAbiturMarkierungsalgorithmusMarkierung> ergebnisMarkierungen = new HashMap3D<>();
							for (final GostAbiturMarkierungsalgorithmusMarkierung markierung : ergebnis.markierungen)
								ergebnisMarkierungen.put(markierung.idFach, markierung.idHalbjahr, markierung.markiert, markierung);
							// Vergleiche das Ergebnis mit dem erwarteten Ergebnis...
							final List<String> zuwenig = new ArrayList<>();
							for (final GostAbiturMarkierungsalgorithmusMarkierung markierung : vergleichErgebnisMarkierungsalgorihmus.markierungen) {
								if (!ergebnisMarkierungen.contains(markierung.idFach, markierung.idHalbjahr, markierung.markiert)) {
									final GostFach fach = manager.faecher().get(markierung.idFach);
									assert fach != null : "Fehler bei der Bestimmung des Faches mit der ID " + markierung.idFach;
									final GostHalbjahr halbjahr = GostHalbjahr.fromID(markierung.idHalbjahr);
									assert (halbjahr != null) && (halbjahr != GostHalbjahr.EF1) && (halbjahr != GostHalbjahr.EF2)
											: "Die ID des Halbjahres " + markierung.idHalbjahr + " ist ungültig oder nicht aus der Qualifikationsphase.";
									if (markierung.markiert)
										zuwenig.add("(%s-%s)".formatted(fach.kuerzelAnzeige, halbjahr.kuerzel));
									if ((!markierung.markiert) && (!ergebnisMarkierungen.contains(markierung.idFach, markierung.idHalbjahr, !markierung.markiert)))
										zuwenig.add("(%s-%s,nicht belegt)".formatted(fach.kuerzelAnzeige, halbjahr.kuerzel));
								}
							}
							final List<String> zuviele = new ArrayList<>();
							for (final GostAbiturMarkierungsalgorithmusMarkierung markierung : ergebnis.markierungen) {
								if (!testfallMarkierungen.contains(markierung.idFach, markierung.idHalbjahr, markierung.markiert)) {
									final GostFach fach = manager.faecher().get(markierung.idFach);
									assert fach != null : "Fehler bei der Bestimmung des Faches mit der ID " + markierung.idFach;
									final GostHalbjahr halbjahr = GostHalbjahr.fromID(markierung.idHalbjahr);
									assert (halbjahr != null) && (halbjahr != GostHalbjahr.EF1) && (halbjahr != GostHalbjahr.EF2)
											: "Die ID des Halbjahres " + markierung.idHalbjahr + " ist ungültig oder nicht aus der Qualifikationsphase.";
									if (markierung.markiert)
										zuviele.add("(%s-%s)".formatted(fach.kuerzelAnzeige, halbjahr.kuerzel));
									if ((!markierung.markiert) && (!testfallMarkierungen.contains(markierung.idFach, markierung.idHalbjahr, !markierung.markiert)))
										zuviele.add("(%s-%s,nicht beleggt)".formatted(fach.kuerzelAnzeige, halbjahr.kuerzel));
								}
							}

							if ((!zuwenig.isEmpty()) || (!zuviele.isEmpty())) {
								final String error = "Fehler: Die Markierungen stimmen nicht mit dem Testfall überein: " + System.lineSeparator()
										+ "  - zuviel gefundene Markierungen: "
										+ ((zuviele.isEmpty()) ? "---" : zuviele.stream().collect(Collectors.joining(", "))) + System.lineSeparator()
										+ "  - zu wenig gefunden Markierungen:"
										+ ((zuwenig.isEmpty()) ? "---" : zuwenig.stream().collect(Collectors.joining(", ")));
								System.out.println("  " + error);
								fail(error);
							}
							System.out.println("  Test erfolgreich beendet.");
						}));
			});
		});
		return tests.stream();
	}

}
