package de.svws_nrw.core.abschluss.bk.d;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVariante;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusErgebnis;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturdaten;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymAbiturUtils;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse enthält die Testroutinen für den Markierungsalgorithmus
 * für das Abitur im Beruflichen Gymnasium.
 */
@DisplayName("Teste den Markierungsalgorithmus für das Abitur im Beruflichen Gymnasium")
class TestBKGymMarkierungsalgorithmus {

	/** Eine Map mit den Fächern der Jahrgänge des beruflichen Gymnasiums aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, List<BKGymFach>> testJahrgaengeFaecher = new HashMap<>();

	/** Eine Map mit den Abiturdaten von Schülern des beruflichen Gymnasiums aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, BKGymAbiturdaten>> testAbiturdaten = new HashMap<>();

	/** Eine Map mit den Markierungsergebnissen von Schülern des beruflichen Gymnasiums aus den zugehörigen JSON-Dateien mit den Testfällen */
	static HashMap<String, HashMap<String, BKGymAbiturMarkierungsalgorithmusErgebnis>> testErgebnisseMarkierungsalgorihmus = new HashMap<>();

	/** Der Pfad zum Resource-Verzeichnis mit den Testdaten */
	static String pfadTestdaten = "";

	/**
	 * Initialisiert den Test und lädt dafür die Jahrgänge und die Aiturdaten aus den
	 * zugehörigen JSON-Dateien mit den Testfällen.
	 *
	 * @throws IOException bei einem Fehler beim Laden der JSON-Resourcen
	 */
	@BeforeAll
	static void setup() throws IOException {
		Path projectRoot;
		try {
			projectRoot = Paths.get("").toAbsolutePath();
		} catch (final Exception e) {
			throw new IOException("Fehler beim Ermitteln des Projektpfades!", e);
		}
		pfadTestdaten = projectRoot.resolve("src/test/resources/de/svws_nrw/core/abschluss/bk/d/").toString();

		ASDCoreTypeUtils.initAll();
		System.out.println("- Lade die Fächer der Jahrgänge aus den JSON-Resourcen...");
		final Map<String, BKGymFach[]> tempTestJahrgaengeFaecher =
				ResourceUtils.json2Classes("de.svws_nrw.core.abschluss.bk.d", "Jahrgang_", "_Faecher", BKGymFach[].class);
		assert (tempTestJahrgaengeFaecher != null) && (tempTestJahrgaengeFaecher.size() != 0) : "Fehler beim Laden der Gost-Fächer der Testjahrgänge!";
		for (final Map.Entry<String, BKGymFach[]> entry : tempTestJahrgaengeFaecher.entrySet()) {
			testJahrgaengeFaecher.put(entry.getKey(), Arrays.asList(entry.getValue()));
		}
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Abiturdaten aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, BKGymAbiturdaten> tempTestAbiturdaten =
				ResourceUtils.json2Classes("de.svws_nrw.core.abschluss.bk.d", "Jahrgang_", "_Abiturdaten", BKGymAbiturdaten.class);
		assert (tempTestAbiturdaten != null) && (tempTestAbiturdaten.size() != 0) : "Fehler beim Laden der Abiturdaten!";
		for (final Map.Entry<String, BKGymAbiturdaten> entry : tempTestAbiturdaten.entrySet()) {
			final String[] ids = entry.getKey().split("_");
			if (ids.length != 2) {
				fail("Fehler beim Laden der Abiturdaten - ungültiger Dateiname: Jahrgang_" + entry.getKey() + "_Abiturdaten");
			}
			HashMap<String, BKGymAbiturdaten> mapSchuelerJahrgang = testAbiturdaten.get(ids[0]);
			if (mapSchuelerJahrgang == null) {
				mapSchuelerJahrgang = new HashMap<>();
				testAbiturdaten.put(ids[0], mapSchuelerJahrgang);
			}
			mapSchuelerJahrgang.put(ids[1], entry.getValue());
		}
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Ergebnisse des Markierungsalgorithmus aus den JSON-Resourcen und ordne sie den Jahrgängen zu...");
		final Map<String, BKGymAbiturMarkierungsalgorithmusErgebnis> tempTestErgebnisseMarkierungsalgorithmus =
				ResourceUtils.json2Classes("de.svws_nrw.core.abschluss.bk.d", "Jahrgang_", "_Markierungsalgorithmus",
						BKGymAbiturMarkierungsalgorithmusErgebnis.class);
		if ((tempTestErgebnisseMarkierungsalgorithmus == null) || (tempTestErgebnisseMarkierungsalgorithmus.size() == 0)) {
			return;
		}
		assert (tempTestErgebnisseMarkierungsalgorithmus != null) && (tempTestErgebnisseMarkierungsalgorithmus.size() != 0)
				: "Fehler beim Laden der Ergebnisse für den Abitur-Markierungsalgorithmus!";
		for (final Map.Entry<String, BKGymAbiturMarkierungsalgorithmusErgebnis> entry : tempTestErgebnisseMarkierungsalgorithmus.entrySet()) {
			final String[] ids = entry.getKey().split("_");
			if (ids.length != 2) {
				fail("Fehler beim Laden der Ergebnisse des Abitur-Markierungsalgorithmus - ungültiger Dateiname: Jahrgang_" + entry.getKey()
						+ "_Markierungsalgorihmus");
			}
			HashMap<String, BKGymAbiturMarkierungsalgorithmusErgebnis> mapSchuelerJahrgang = testErgebnisseMarkierungsalgorihmus.get(ids[0]);
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
	@DisplayName("Teste Markierungsalgorithmus im Beruflichen Gymnasium...")
	Stream<DynamicTest> testMarkierungsalgorithmus() {
		final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		final DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
		pp.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
		pp.indentObjectsWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

		final ArrayList<DynamicTest> tests = new ArrayList<>();
		testAbiturdaten.forEach((jahrgang, mapSchuelerJahrgang) -> {
			mapSchuelerJahrgang.forEach((idSchueler, abidaten) -> {
				// Lese BKGymFaecher
				final List<BKGymFach> bkGymFaecher = testJahrgaengeFaecher.get(jahrgang);
				assert bkGymFaecher != null : "Fehler bei den Testfällen: Für den Abiturjahrgang '" + jahrgang + "' der Test-Abiturdaten '" + idSchueler
						+ "' wurden keine Test-Fächerdaten des beruflichen Gymnasiums gefunden.";
				// Lese Ergebnis des Abitur-Markierungsergebnis ein, sofern eines vorhanden ist.
				final var testJahrgangErgebnisseMarkierungsalgorithmus = testErgebnisseMarkierungsalgorihmus.get(jahrgang);
				final var vergleichErgebnisMarkierungsalgorithmus = testJahrgangErgebnisseMarkierungsalgorithmus == null ? null : testJahrgangErgebnisseMarkierungsalgorithmus.get(idSchueler);
				// Füge Test für den Abitur-Markierungsalgorithmus hinzu
				tests.add(DynamicTest.dynamicTest(
						"Testjahrgang " + jahrgang + " - Abiturdaten " + idSchueler + " - Markierungsalgorithmus",
						() -> {
							System.out.println();
							System.out.println("- Test: Markierungsalgorithmus für die Abiturdaten " + idSchueler + " des Testjahrgangs " + jahrgang + ":");
							final BKGymFaecherManager faecherManager = new BKGymFaecherManager(abidaten.schuljahrAbitur, bkGymFaecher);
							final Schulgliederung sgl = Schulgliederung.data().getWertByID(abidaten.idSchulgliederung);
							final BKGymAbiturdatenManager manager =
									new BKGymAbiturdatenManager(abidaten, sgl, abidaten.fachklassenschluessel, faecherManager, GostHalbjahr.Q22);
							final BKGymAbiturMarkierungsalgorithmusErgebnis ergebnis = manager.getErgebnisMarkierungsalgorithmus();
							if (!ergebnis.log.isEmpty()) {
								System.out.println("  Log:");
								for (final String text : ergebnis.log) {
									System.out.println("	" + text);
								}
							}

							if (vergleichErgebnisMarkierungsalgorithmus == null) {
								//erzeuge JSON mit Markierungsergebnis
								mapper.writer(pp).writeValue(new File(pfadTestdaten + "/Jahrgang_" + jahrgang + "_" + idSchueler + "_Markierungsalgorithmus.json"), ergebnis);
								System.out.println("Neuer Testfall " + jahrgang + "_" + idSchueler + ": Das Ergebnis des Markierungsalgorithmus wurde erstmalig erzeugt. Bitte prüfen und ggfs. korrigieren");
								fail("Neuer Testfall: Das Ergebnis des Markierungsalgorithmus wurde erstmalig erzeugt. Bitte prüfen und ggfs. korrigieren: " + System.lineSeparator());
							} else {
								// Prüfe den Erfolg der Markierung
								assertEquals(vergleichErgebnisMarkierungsalgorithmus.erfolgreich, ergebnis.erfolgreich, ergebnis.erfolgreich
										? "Fehler: Der Markierungsalgorithmus war erfolgreich, obwohl der Testfall vorgibt, dass sie fehlschlagen muss!"
										: "Fehler: Der Markierungsalgorithmus war nicht erfolgreich, obwohl der Testfall vorgibt, dass sie erfolgreich sein muss!");
								// Prüfe, ob die Einbringung der Facharbeit gleich ist
								assertEquals(vergleichErgebnisMarkierungsalgorithmus.facharbeitEinbringen, ergebnis.facharbeitEinbringen, ergebnis.facharbeitEinbringen
										? "Fehler: Die Facharbeit wurde eingebracht, obwohl der Testfall vorgibt, dass sie nicht eingebracht werden soll!"
										: "Fehler: Die Facharbeit wurde nicht eingebracht, obwohl der Testfall vorgibt, dass sie eingebracht werden soll!");
								// Prüfe, ob sich die dokumentierten Markierungen des Testfalls von den gefundenen unterscheiden.
								final String vergleichsergebnis = vergleicheMarkierungsergebnisse(new ArrayList<>(vergleichErgebnisMarkierungsalgorithmus.markierungen),
										new ArrayList<>(ergebnis.markierungen), faecherManager);
								if (!vergleichsergebnis.isEmpty()) {
									fail("Fehler: Die Markierung des Markierungsalgorithmus stimmen nicht mit dem Testfall überein: " + System.lineSeparator() + vergleichsergebnis);
								}
								// prüfe, ob die Logeinträge identisch sind
								final String vergleichLog = vergleicheLogs(vergleichErgebnisMarkierungsalgorithmus.log, ergebnis.log);
								if (!vergleichLog.isEmpty()) {
									fail("Fehler: Die Log-Einträge des Markierungsalgorithmus stimmen nicht mit dem Testfall überein: " + System.lineSeparator() + vergleichLog);
								}
							}
							System.out.println("  Test erfolgreich beendet.");
						}));
				});
		});
		return tests.stream();
	}


	/**
	 * Ermittelt die Unterschiede der Markierungen des alten dokumentierten Testlaufs mit dem aktuellen Testlaufs.
	 * Die Unterschiede werden als String zurückgegeben.
	 *
	 * @param alt              die Markierungen der Referenz
	 * @param neu              die Markierungen des aktuellen Testlaufs
	 * @param faecherManager   der Fächermanager um die Fachkürzel zu ermitteln
	 *
	 * @return der String mit den Unterschieden oder ein leerer String , wenn es keine Unterschiede gibt.
	 */
	private static String vergleicheMarkierungsergebnisse(final List<BKGymAbiturMarkierungsalgorithmusMarkierung> alt,
			final List<BKGymAbiturMarkierungsalgorithmusMarkierung> neu, final BKGymFaecherManager faecherManager) {
		BKGymAbiturMarkierungsVariante.sortMarkierungsliste(alt);
		BKGymAbiturMarkierungsVariante.sortMarkierungsliste(neu);

		final List<String> neuhinzu = new ArrayList<>();
		final List<String> fehlend = new ArrayList<>();
		int i = 0;
		int j = 0;

		while ((i < alt.size()) && (j < neu.size())) {
			final var a = alt.get(i);
			final var n = neu.get(j);

			final int cmp = BKGymAbiturUtils.comparatorMarkierung.compare(a, n);

			if (cmp == 0) {
				// gleich → weiter
				i++;
				j++;
			} else if (cmp < 0) {
				// alt-Eintrag existiert nicht mehr -> fehlend
				fehlend.add(markierungAlsString(a, faecherManager));
				i++;
			} else {
				// neu-Eintrag ist hinzugekommen -> neuhinzu
				neuhinzu.add(markierungAlsString(a, faecherManager));
				j++;
			}
		}

		// restliche alt → removed
		while (i < alt.size()) {
			fehlend.add(markierungAlsString(alt.get(i++), faecherManager));
		}

		// restliche neu → added
		while (j < neu.size()) {
			neuhinzu.add(markierungAlsString(neu.get(j++), faecherManager));
		}

		if (!fehlend.isEmpty() || !neuhinzu.isEmpty()) {
			return "Fehlende Einträge: " + fehlend + System.lineSeparator() + "Hinzugekommene Markierungen: " + neuhinzu;
		}

		return "";
	}


	/**
	 * Ermittelt ggfs den ersten Unterschied der Logeinträge und liefert beide zurück.
	 *
	 * @param alt   die Logeinträge der Referenz
	 * @param neu   die Logeinträge des aktuellen Testlaufs
	 *
	 * @return der String mit den Unterschieden oder ein leerer String , wenn es keine Unterschiede gibt.
	 */
	private static String vergleicheLogs(final @NotNull List<String> alt, final @NotNull List<String> neu) {
		int i = 0;
		int j = 0;

		while ((i < alt.size()) && (j < neu.size())) {
			final var a = alt.get(i);
			final var n = neu.get(j);
			if (a.equals(n)) {
				// gleich → weiter
				i++;
				j++;
			} else {
				// Unterschied gefunden
				return "Unterschiedliche Logeinträge: " + System.lineSeparator() + "Alt: " + a + System.lineSeparator() + "Neu: " + n;
			}
		}
		if (i < alt.size()) {
			// Alt hat zusätzliche Einträge
			return "Unterschiedliche Logeinträge: " + System.lineSeparator() + "Alt: " + alt.get(i) + System.lineSeparator() + "Neu: <keine weiteren Einträge>";
		}
		if (j < neu.size()) {
			// Neu hat zusätzliche Einträge
			return "Unterschiedliche Logeinträge: " + System.lineSeparator() + "Alt: <keine weiteren Einträge>" + System.lineSeparator() + "Neu: " + neu.get(j);
		}
		return "";
	}


	/**
	 * liefert die Markierung als String
	 *
	 * @param markierung   die Markierung, für die der String erzeugt wird
	 * @param faecherManager   der Fächermanager um die Fachkürzel zu ermitteln
	 *
	 * @return der String für die Markierung
	 */
	private static String markierungAlsString(final BKGymAbiturMarkierungsalgorithmusMarkierung markierung, final BKGymFaecherManager faecherManager) {
		final BKGymFach fbFach = faecherManager.get(markierung.fachID);
		if ((fbFach == null) || (fbFach.kuerzelAnzeige == null)) {
			return "";
		}

		return "Fach=" + fbFach.kuerzelAnzeige + ", Halbjahr=" + GostHalbjahr.kuerzelFromIDOrException(markierung.halbjahrID) + ", Punkte="
		+ (markierung.punkte == null ? -1 : markierung.punkte);
	}

}
