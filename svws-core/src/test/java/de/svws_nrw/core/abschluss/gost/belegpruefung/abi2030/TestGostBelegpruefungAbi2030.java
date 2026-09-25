package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.base.compression.GZip;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnisFehler;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2;
import de.svws_nrw.core.utils.gost.GostLaufbahnplanungDataHandler;

/**
 * Diese Klasse enthält die Testroutinen für den Belegprüfungsalgorithmus
 * für das Abitur in der gymnasialen Oberstufe.
 */
@DisplayName("Teste den Belegprüfungsalgorithmus für das Abitur in der gymnasialen Oberstufe")
class TestGostBelegpruefungAbi2030 {

	/** Der Object-Mapper für das Laden der Testfälle */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** Das Pattern für den Namensteil eines Testfalles */
	private static Pattern pattern = Pattern.compile("(\\d+_Jg_[a-zA-Z0-9]+_\\d+)");

	/** Eine Map mit den Testfällen der Laufbahnplanung */
	static Map<String, GostLaufbahnplanungDataHandler> testLaufbahnen = new HashMap<>();

	/** Eine Map mit den EF1-Abitur-Belegprüfungsergebnissen von Schülern der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static Map<String, GostBelegpruefungErgebnis> testBelegpruefungsergebnisseEF1 = new HashMap<>();

	/** Eine Map mit den Gesamt-Abitur-Belegprüfungsergebnissen von Schülern der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static Map<String, GostBelegpruefungErgebnis> testBelegpruefungsergebnisseGesamt = new HashMap<>();



	static GostLaufbahnplanungExportV2 decodeLpFile(final byte[] encoded) throws CompressionException {
		try {
			final byte[] daten = GZip.decode(encoded);
			return mapper.readValue(daten, GostLaufbahnplanungExportV2.class);
		} catch (IOException | CompressionException e) {
			if (e instanceof final CompressionException ce) {
				throw ce;
			}
			throw new CompressionException("Fehler beim Deserialisieren der JSON-Daten.", e);
		}
	}


	static Map<String, GostLaufbahnplanungDataHandler> ladeTestfaelleLaufbahnplanung() throws IOException {
		final Map<String, GostLaufbahnplanungDataHandler> classes = new TreeMap<>();
		final List<Path> paths = ResourceUtils.getFilesInPackage(TestGostBelegpruefungAbi2030.class.getPackageName(), ".lp");
		for (final Path filePath : paths) {
			final String filename = filePath.getFileName().toString();
			try {
				if (filename.toLowerCase().startsWith("Testschule_".toLowerCase()) && filename.toLowerCase().endsWith((".lp").toLowerCase())) {
					final Matcher matcher = pattern.matcher(filename);
					if (!matcher.find()) {
						throw new IOException("Die Benennung des Testfalles entspricht nicht dem geforderten Schema!");
					}
					final String name = matcher.group(1);
					final byte[] encoded = Files.readAllBytes(filePath);
					final GostLaufbahnplanungExportV2 decoded = decodeLpFile(encoded);
					classes.put(name, GostLaufbahnplanungDataHandler.importV2(decoded));
				}
			} catch (final IOException | CompressionException e) {
				throw new IOException("Fehler beim Lesen aus der Datei " + filename + "!", e);
			}
		}
		return classes;
	}


	/**
	 * Initialisiert den Test und lädt dafür die Jahrgänge und die Aiturdaten aus den
	 * zugehörigen JSON-Dateien mit den Testfällen.
	 *
	 * @throws IOException bei einem Fehler beim Laden der JSON-Resourcen
	 */
	@BeforeAll
	static void setup() throws IOException {
		ASDCoreTypeUtils.initAll();
		System.out.println("- Lade die Laufbahnplanungsdaten aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		testLaufbahnen = ladeTestfaelleLaufbahnplanung();
		assert (testLaufbahnen != null) && (testLaufbahnen.size() != 0) : "Fehler beim Laden der Laufbahnplanungsdateien!";
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Gesamt-Belegprüfungsergebnisse aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, GostBelegpruefungErgebnis> tempTestBelegpruefungsergebnisseGesamt =
				ResourceUtils.json2Classes(TestGostBelegpruefungAbi2030.class.getPackageName(), "Testschule_", "_Belegpruefungsergebnis_Gesamt",
						GostBelegpruefungErgebnis.class);
		assert (tempTestBelegpruefungsergebnisseGesamt != null) && (tempTestBelegpruefungsergebnisseGesamt.size() != 0)
				: "Fehler beim Laden der Gesamt-Belegprüfungsergebnisse!";
		for (final Map.Entry<String, GostBelegpruefungErgebnis> entry : tempTestBelegpruefungsergebnisseGesamt.entrySet()) {
			final Matcher matcher = pattern.matcher(entry.getKey());
			if (!matcher.find()) {
				fail("Fehler beim Laden der Gesamt-Belegprüfungsergebnisse - ungültiger Dateiname: Jahrgang_" + entry.getKey()
						+ "_Belegpruefungsergebnis_Gesamt");
			}
			final String name = matcher.group(1);
			testBelegpruefungsergebnisseGesamt.put(name, entry.getValue());
		}
		System.out.println("  FERTIG!");

		System.out.println("- Lade die EF1-Belegprüfungsergebnisse aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, GostBelegpruefungErgebnis> tempTestBelegpruefungsergebnisseEF1 =
				ResourceUtils.json2Classes(TestGostBelegpruefungAbi2030.class.getPackageName(), "Testschule_", "_Belegpruefungsergebnis_EF1",
						GostBelegpruefungErgebnis.class);
		assert (tempTestBelegpruefungsergebnisseEF1 != null) && (tempTestBelegpruefungsergebnisseEF1.size() != 0)
				: "Fehler beim Laden der EF1-Belegprüfungsergebnisse!";
		for (final Map.Entry<String, GostBelegpruefungErgebnis> entry : tempTestBelegpruefungsergebnisseEF1.entrySet()) {
			final Matcher matcher = pattern.matcher(entry.getKey());
			if (!matcher.find()) {
				fail("Fehler beim Laden der EF1-Belegprüfungsergebnisse - ungültiger Dateiname: Jahrgang_" + entry.getKey() + "_Belegpruefungsergebnis_EF1");
			}
			final String name = matcher.group(1);
			testBelegpruefungsergebnisseEF1.put(name, entry.getValue());
		}
		System.out.println("  FERTIG!");
	}


	@Test
	@Disabled("Aktiviere diesen Test, um fehlende Ergebnisse zu Testfällen automatisch zu erzeugen. Im Allgemeinen bliebt dieser Test deaktiviert.")
	void createNonExistingResults() {
		System.out.println("- Erzeuge ggf. fehlende Ergebniss der Belegprüfung der Laufbahnplanung...");
		final String path = "src/test/resources/" + TestGostBelegpruefungAbi2030.class.getPackageName().replace(".", "/") + "/";
		System.out.println(path);
		testLaufbahnen.forEach((name, lpDaten) -> {
			final boolean hatEF1 = (testBelegpruefungsergebnisseEF1.get(name) != null);
			if (!hatEF1) {
				final AbiturdatenManager manager = new AbiturdatenManager(lpDaten.getAbiturdaten(), lpDaten.getGostJahrgangsdaten(),
						lpDaten.getFaecherManager(), GostBelegpruefungsArt.EF1);
				final GostBelegpruefungErgebnis ergebnis = manager.getBelegpruefungErgebnis();
				final String filename = path + "Testschule_" + name + "_Belegpruefungsergebnis_EF1.json";
				assertDoesNotThrow(() ->  {
					mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), ergebnis);
					testBelegpruefungsergebnisseEF1.put(name, ergebnis);
					System.out.println("  - Schreiben des Testfalles " + name + " für die EF1-Prüfung erfolgreich");
				}, "Fehler beim Schreiben des Testfallergenisses " + name);
			}
			final boolean hatGesamt = (testBelegpruefungsergebnisseGesamt.get(name) != null);
			if (!hatGesamt) {
				final AbiturdatenManager manager = new AbiturdatenManager(lpDaten.getAbiturdaten(), lpDaten.getGostJahrgangsdaten(),
						lpDaten.getFaecherManager(), GostBelegpruefungsArt.GESAMT);
				final GostBelegpruefungErgebnis ergebnis = manager.getBelegpruefungErgebnis();
				final String filename = path + "Testschule_" + name + "_Belegpruefungsergebnis_Gesamt.json";
				testBelegpruefungsergebnisseGesamt.put(name, ergebnis);
				assertDoesNotThrow(() ->  {
					mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), ergebnis);
					System.out.println("  - Schreiben des Testfalles " + name + " für die Gesamt-Prüfung erfolgreich");
				}, "Fehler beim Schreiben des Testfallergenisses " + name);
			}
		});
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
		testLaufbahnen.forEach((name, lpDaten) -> {
			// Lese EF1-Belegprüefungsergebnis
			final var vergleichErgebnisEF1 = testBelegpruefungsergebnisseEF1.get(name);
			assert vergleichErgebnisEF1 != null
					: "Es konnte kein EF1-Belegprüfungsergebnis für den Testfall " + name + " für den Vergleich gefunden werden.";
			// Lese Gesamt-Belegprüefungsergebnis
			final var vergleichErgebnisGesamt = testBelegpruefungsergebnisseGesamt.get(name);
			assert vergleichErgebnisGesamt != null
					: "Es konnte kein Gesamt-Belegprüfungsergebnis für den Testfall " + name + " für den Vergleich gefunden werden.";
			// Füge Test für die EF1-Belegprüfung hinzu
			tests.add(DynamicTest.dynamicTest(
					"Testfall " + name + " - Belegprüfung EF1",
					() -> {
						System.out.println();
						System.out.println("- Test: EF1-Belegprüfung, Testfall " + name + ":");
						final AbiturdatenManager manager = new AbiturdatenManager(lpDaten.getAbiturdaten(), lpDaten.getGostJahrgangsdaten(),
								lpDaten.getFaecherManager(), GostBelegpruefungsArt.EF1);
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
						final List<String> testfallFehler = vergleichErgebnisEF1.fehlercodes.stream().map(error -> error.code).toList();
						final List<String> ergebnisFehler = ergebnis.fehlercodes.stream().map(error -> error.code).toList();
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
					"Testfall " + name + " - Belegprüfung Gesamt",
					() -> {
						System.out.println();
						System.out.println("- Test: Gesamt-Belegprüfung, Testfall " + name + ":");
						final AbiturdatenManager manager = new AbiturdatenManager(lpDaten.getAbiturdaten(), lpDaten.getGostJahrgangsdaten(),
								lpDaten.getFaecherManager(), GostBelegpruefungsArt.GESAMT);
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
						final List<String> testfallFehler =
								vergleichErgebnisGesamt.fehlercodes.stream().map(error -> error.code).toList();
						final List<String> ergebnisFehler = ergebnis.fehlercodes.stream().map(error -> error.code).toList();
						final String zuwenig = testfallFehler.stream().filter(error -> !ergebnisFehler.contains(error)).collect(Collectors.joining(", "));
						final String zuviele = ergebnisFehler.stream().filter(error -> !testfallFehler.contains(error)).collect(Collectors.joining(", "));
						if ((!"".equals(zuwenig)) || (!"".equals(zuviele))) {
							fail("Fehler: Die Fehlercodes der Gesamt-Belegprüfung stimmen nicht mit dem Testfall überein: " + System.lineSeparator()
									+ "  - zuviel gefundene Fehler: " + (("".equals(zuviele)) ? "---" : zuviele) + System.lineSeparator()
									+ "  - zu wenig gefunden Fehler:" + (("".equals(zuwenig)) ? "---" : zuwenig));
						}
						System.out.println("  Test erfolgreich beendet.");
					}));
		});
		return tests.stream();
	}

}
