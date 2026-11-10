package de.svws_nrw.core.utils.gost;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.base.compression.GZip;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusErgebnis;
import de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostZulassungSimulation;
import de.svws_nrw.core.data.gost.GostZulassungSimulationAbiturfach;
import de.svws_nrw.core.data.gost.GostZulassungSimulationNote;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2;
import de.svws_nrw.core.types.gost.GostHalbjahr;

/**
 * Prüft die Kombination von Abiturdaten mit den Eingaben einer Simulation und die Berechnung damit. Bei Abitur 2030 wird nur die Mechanik geprüft,
 * nicht die fachliche Korrektheit des Algorithmus.
 */
@DisplayName("Tests für die Simulation der Abiturzulassung")
class TestGostZulassungSimulationUtils {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String packageTests = "de.svws_nrw.abschluesse.gost.test";

	private static final String packageTestsAbi2030 = "de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030";

	/**
	 * Die Daten eines Testfalls
	 *
	 * @param daten            die Abiturdaten
	 * @param jahrgangsdaten   die Daten des Abiturjahrgangs
	 * @param faecher          der Manager für die Fächer
	 */
	private record Testfall(Abiturdaten daten, GostJahrgangsdaten jahrgangsdaten, GostFaecherManager faecher) {
		//
	}

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	private static Testfall ladeVor2030() throws IOException {
		final Abiturdaten daten = ResourceUtils.json2Classes(packageTests, "Jahrgang_", "_Abiturdaten", Abiturdaten.class).get("01_1001");
		final GostJahrgangsdaten jahrgangsdaten = ResourceUtils.json2Classes(packageTests, "Jahrgang_", "_GostJahrgangsdaten", GostJahrgangsdaten.class).get("01");
		final List<GostFach> faecher = Arrays.asList(ResourceUtils.json2Classes(packageTests, "Jahrgang_", "_GostFaecher", GostFach[].class).get("01"));
		final List<GostJahrgangFachkombination> kombinationen =
				Arrays.asList(ResourceUtils.json2Classes(packageTests, "Jahrgang_", "_GostJahrgangFachkombination", GostJahrgangFachkombination[].class).get("01"));
		return new Testfall(daten, jahrgangsdaten, new GostFaecherManager(jahrgangsdaten.abiturjahr - 1, faecher, kombinationen));
	}

	private static Testfall lade2030() throws IOException, CompressionException {
		final Path datei = ResourceUtils.getFilesInPackage(packageTestsAbi2030, ".lp").stream().filter(p -> p.getFileName().toString().contains("_Belegung")).findFirst().orElseThrow();
		final GostLaufbahnplanungDataHandler handler = GostLaufbahnplanungDataHandler.importV2(
				mapper.readValue(GZip.decode(Files.readAllBytes(datei)), GostLaufbahnplanungExportV2.class));
		return new Testfall(handler.getAbiturdaten(), handler.getGostJahrgangsdaten(), handler.getFaecherManager());
	}

	private static void combine(final Abiturdaten daten, final GostZulassungSimulation simulation, final boolean fixiereNoten) {
		GostZulassungSimulationUtils.combine(daten, simulation, new GostFaecherManager(daten.schuljahrAbitur), fixiereNoten);
	}

	private static GostFaecherManager faecherMitFach(final long idFach, final String kuerzel) {
		final GostFach fach = new GostFach();
		fach.id = idFach;
		fach.kuerzel = kuerzel;
		return new GostFaecherManager(2029, List.of(fach));
	}

	private static void setzeLeer(final Abiturdaten daten) {
		for (final AbiturFachbelegung fach : daten.fachbelegungen) {
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegung = fach.belegungen[halbjahr.id];
				if (belegung != null) {
					belegung.notenkuerzel = null;
				}
			}
		}
	}

	private static AbiturFachbelegung fachMitBelegung(final Abiturdaten daten, final GostHalbjahr halbjahr) {
		return daten.fachbelegungen.stream().filter(f -> f.belegungen[halbjahr.id] != null).findFirst().orElseThrow();
	}

	private static AbiturFachbelegung fachOhneBelegung(final Abiturdaten daten, final GostHalbjahr halbjahr) {
		return daten.fachbelegungen.stream()
				.filter(f -> (f.belegungen[halbjahr.id] == null) && Arrays.stream(GostHalbjahr.getQualifikationsphase()).anyMatch(h -> f.belegungen[h.id] != null))
				.findFirst().orElseThrow();
	}

	private static AbiturFachbelegung fach(final Abiturdaten daten, final long idFach) {
		return daten.fachbelegungen.stream().filter(f -> f.fachID == idFach).findFirst().orElseThrow();
	}

	private static GostZulassungSimulation simulationMitNote(final long idFach, final GostHalbjahr halbjahr, final Integer notenpunkte) {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		final GostZulassungSimulationNote note = new GostZulassungSimulationNote();
		note.idFach = idFach;
		note.halbjahr = halbjahr.id;
		note.notenpunkte = notenpunkte;
		simulation.noten.add(note);
		return simulation;
	}

	private static GostZulassungSimulation simulationAlleNoten(final Abiturdaten daten, final int notenpunkte) {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		for (final AbiturFachbelegung fach : daten.fachbelegungen) {
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				if (fach.belegungen[halbjahr.id] != null) {
					simulation.noten.addAll(simulationMitNote(fach.fachID, halbjahr, notenpunkte).noten);
				}
			}
		}
		return simulation;
	}

	@Test
	@DisplayName("combine: Alle Halbjahre der Qualifikationsphase gelten danach als bewertet")
	void testBewertet() throws Exception {
		final Abiturdaten daten = lade2030().daten();

		combine(daten, new GostZulassungSimulation(), true);

		for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			assertTrue(daten.bewertetesHalbjahr[halbjahr.id]);
		}
	}

	@ParameterizedTest
	@CsvSource({ "0,6", "15,1+", "7,3-" })
	@DisplayName("combine: Notenpunkte werden auf einer Belegung ohne Note als Notenkürzel gesetzt")
	void testNoteGesetzt(final int notenpunkte, final String kuerzel) throws Exception {
		final Abiturdaten daten = lade2030().daten();
		setzeLeer(daten);
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.Q21);

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, notenpunkte), true);

		assertEquals(kuerzel, fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@Test
	@DisplayName("combine: Eine vorhandene Note hat bei aktiver Fixierung Vorrang, sonst gilt die Eingabe")
	void testFixierung() throws Exception {
		final Abiturdaten fixiert = ladeVor2030().daten();
		final AbiturFachbelegung fachFixiert = fachMitBelegung(fixiert, GostHalbjahr.Q21);
		final String echteNote = fachFixiert.belegungen[GostHalbjahr.Q21.id].notenkuerzel;
		assertNotNull(echteNote);
		assertFalse("1+".equals(echteNote));

		combine(fixiert, simulationMitNote(fachFixiert.fachID, GostHalbjahr.Q21, 15), true);
		assertEquals(echteNote, fachFixiert.belegungen[GostHalbjahr.Q21.id].notenkuerzel);

		final Abiturdaten frei = ladeVor2030().daten();
		final AbiturFachbelegung fachFrei = fachMitBelegung(frei, GostHalbjahr.Q21);
		combine(frei, simulationMitNote(fachFrei.fachID, GostHalbjahr.Q21, 15), false);
		assertEquals("1+", fachFrei.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@Test
	@DisplayName("combine: Auch Pseudonoten sind fixiert")
	void testPseudonote() throws Exception {
		final Abiturdaten daten = ladeVor2030().daten();
		setzeLeer(daten);
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.Q21);
		fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel = "AT";

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11), true);

		assertEquals("AT", fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@ParameterizedTest
	@ValueSource(strings = { "E1", "E2", "E3", "NT", "NB", "NE", "LM", "AM" })
	@DisplayName("combine: Eine Pseudonote bleibt auch ohne Fixierung erhalten")
	void testPseudonoteOhneFixierung(final String kuerzel) throws Exception {
		final Abiturdaten daten = lade2030().daten();
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.Q21);
		fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel = kuerzel;

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11), false);
		assertEquals(kuerzel, fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, null), false);
		assertEquals(kuerzel, fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@Test
	@DisplayName("combine: Ein Attest bleibt vor Abitur 2030 auch ohne Fixierung erhalten")
	void testAttestOhneFixierung() throws Exception {
		final Abiturdaten daten = ladeVor2030().daten();
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.Q21);
		fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel = "AT";

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11), false);

		assertEquals("AT", fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@DisplayName("combine: Auf einem Vertiefungskurs können keine Notenpunkte eingegeben werden")
	void testVertiefungskurs(final boolean fixiereNoten) throws Exception {
		final Abiturdaten daten = lade2030().daten();
		setzeLeer(daten);
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.Q21);

		GostZulassungSimulationUtils.combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11), faecherMitFach(fach.fachID, "VX"), fixiereNoten);

		assertNull(fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@ParameterizedTest
	@CsvSource({ "D,,true,false", "D,,false,false", "D,'',false,false", "D,2,true,true", "D,2,false,false", "D,E1,true,true", "D,E1,false,true",
		"D,NT,false,true", "VX,,true,true", "VX,,false,true", "VX,NT,false,true" })
	@DisplayName("istEingabeGesperrt: Vertiefungskurse und Pseudonoten sind immer gesperrt, andere Noten nur bei Fixierung")
	void testIstEingabeGesperrt(final String fachKuerzel, final String kuerzel, final boolean fixiereNoten, final boolean erwartet) {
		final AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
		belegung.notenkuerzel = kuerzel;

		assertEquals(erwartet, GostZulassungSimulationUtils.istEingabeGesperrt(faecherMitFach(1, fachKuerzel), 1, belegung, 2029, fixiereNoten));
	}

	@Test
	@DisplayName("combine: Ein leeres Notenkürzel ist nicht fixiert")
	void testLeeresKuerzel() throws Exception {
		final Abiturdaten daten = ladeVor2030().daten();
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.Q21);
		fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel = "";

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11), true);

		assertEquals("2", fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@Test
	@DisplayName("combine: Ein Eintrag mit einem unbekannten Halbjahr bleibt unberücksichtigt")
	void testUnbekanntesHalbjahr() throws Exception {
		final Abiturdaten daten = lade2030().daten();
		final GostZulassungSimulation simulation = simulationMitNote(fachMitBelegung(daten, GostHalbjahr.Q21).fachID, GostHalbjahr.Q21, 11);
		simulation.noten.get(0).halbjahr = 99;

		assertDoesNotThrow(() -> combine(daten, simulation, true));
	}

	@Test
	@DisplayName("combine: Halbjahre außerhalb der Qualifikationsphase bleiben unverändert")
	void testEinfuehrungsphase() throws Exception {
		final Abiturdaten daten = ladeVor2030().daten();
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.EF1);
		final String echteNote = fach.belegungen[GostHalbjahr.EF1.id].notenkuerzel;

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.EF1, 15), false);

		assertEquals(echteNote, fach.belegungen[GostHalbjahr.EF1.id].notenkuerzel);
	}

	@ParameterizedTest
	@ValueSource(ints = { 16, -1 })
	@DisplayName("combine: Werte außerhalb des Katalogs werden nicht übernommen")
	void testAusserhalbKatalog(final int notenpunkte) throws Exception {
		final Abiturdaten daten = lade2030().daten();
		setzeLeer(daten);
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.Q21);

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, notenpunkte), true);

		assertNull(fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@Test
	@DisplayName("combine: Eine gelöschte Note leert ohne Fixierung auch eine echte Note")
	void testGeloescht() throws Exception {
		final Abiturdaten daten = ladeVor2030().daten();
		final AbiturFachbelegung fach = fachMitBelegung(daten, GostHalbjahr.Q21);

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, null), false);

		assertNull(fach.belegungen[GostHalbjahr.Q21.id].notenkuerzel);
	}

	@Test
	@DisplayName("combine: Einträge ohne passendes Fach oder ohne passende Belegung bleiben unberücksichtigt")
	void testOhneBelegung() throws Exception {
		final Abiturdaten daten = lade2030().daten();
		final AbiturFachbelegung ohne = fachOhneBelegung(daten, GostHalbjahr.Q22);

		assertDoesNotThrow(() -> combine(daten, simulationMitNote(-4711, GostHalbjahr.Q21, 11), true));
		combine(daten, simulationMitNote(ohne.fachID, GostHalbjahr.Q22, 11), true);

		assertNull(ohne.belegungen[GostHalbjahr.Q22.id]);
	}

	@Test
	@DisplayName("combine: Prüfungswerte werden nur bei Belegungen mit Abiturfach übernommen")
	void testPruefung() throws Exception {
		final Abiturdaten daten = lade2030().daten();
		final AbiturFachbelegung abiturfach = daten.fachbelegungen.stream().filter(f -> f.abiturFach != null).findFirst().orElseThrow();
		final AbiturFachbelegung anderes = daten.fachbelegungen.stream().filter(f -> f.abiturFach == null).findFirst().orElseThrow();
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		for (final AbiturFachbelegung fach : List.of(abiturfach, anderes)) {
			final GostZulassungSimulationAbiturfach eintrag = new GostZulassungSimulationAbiturfach();
			eintrag.idFach = fach.fachID;
			eintrag.pruefungNotenpunkte = 11;
			eintrag.muendlichBestehen = true;
			eintrag.muendlichFreiwillig = false;
			eintrag.muendlichReihenfolge = 2;
			eintrag.muendlichNotenpunkte = 3;
			simulation.abiturfaecher.add(eintrag);
		}

		combine(daten, simulation, true);

		assertEquals("2", abiturfach.block2NotenKuerzelPruefung);
		assertEquals(Boolean.TRUE, abiturfach.block2MuendlichePruefungBestehen);
		assertEquals(Boolean.FALSE, abiturfach.block2MuendlichePruefungFreiwillig);
		assertEquals(2, abiturfach.block2MuendlichePruefungReihenfolge);
		assertEquals("5+", abiturfach.block2MuendlichePruefungNotenKuerzel);
		assertNull(anderes.block2NotenKuerzelPruefung);
	}

	@Test
	@DisplayName("combine: Art und Note der besonderen Lernleistung werden übernommen")
	void testBesondereLernleistung() throws Exception {
		final Abiturdaten daten = lade2030().daten();
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		simulation.besondereLernleistung = "E";
		simulation.besondereLernleistungNotenpunkte = 11;

		combine(daten, simulation, true);

		assertEquals("E", daten.besondereLernleistung);
		assertEquals("2", daten.besondereLernleistungNotenKuerzel);
	}

	@Test
	@DisplayName("combine: Eine ungültige oder fehlende Art der besonderen Lernleistung gilt als keine besondere Lernleistung")
	void testBesondereLernleistungUngueltig() throws Exception {
		final Abiturdaten daten = lade2030().daten();
		final GostZulassungSimulation simulation = new GostZulassungSimulation();

		simulation.besondereLernleistung = "X";
		combine(daten, simulation, true);
		assertEquals("K", daten.besondereLernleistung);

		simulation.besondereLernleistung = null;
		combine(daten, simulation, true);
		assertEquals("K", daten.besondereLernleistung);
	}

	@Test
	@DisplayName("combine: Leere Belegungen aus dem Lesen der Fachwahlen werden entfernt und nehmen keine Note an")
	void testLeereBelegungen() throws Exception {
		final Testfall testfall = lade2030();
		final Abiturdaten daten = testfall.daten();
		final AbiturFachbelegung fach = fachOhneBelegung(daten, GostHalbjahr.Q22);
		new AbiturdatenManager(daten, testfall.jahrgangsdaten(), testfall.faecher(), GostBelegpruefungsArt.GESAMT).getSchuelerFachwahlen();
		assertEquals("", fach.belegungen[GostHalbjahr.Q22.id].kursartKuerzel);

		combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q22, 10), false);

		assertNull(fach.belegungen[GostHalbjahr.Q22.id]);
	}

	@Test
	@DisplayName("berechne: Das Lesen der Fachwahlen vorab ändert das Ergebnis nicht")
	void testFachwahlenOhneEinfluss() throws Exception {
		final Testfall ohneLesen = lade2030();
		final Testfall mitLesen = lade2030();
		new AbiturdatenManager(mitLesen.daten(), mitLesen.jahrgangsdaten(), mitLesen.faecher(), GostBelegpruefungsArt.GESAMT).getSchuelerFachwahlen();

		final String erwartet = mapper.writeValueAsString(rechne(ohneLesen));
		final String ergebnis = mapper.writeValueAsString(rechne(mitLesen));

		assertEquals(erwartet, ergebnis);
	}

	private static Abiturdaten rechne(final Testfall testfall) {
		final Abiturdaten daten = testfall.daten();
		combine(daten, simulationAlleNoten(daten, 11), false);
		return GostZulassungSimulationUtils.berechne(daten, testfall.jahrgangsdaten(), testfall.faecher(), false).daten();
	}

	@Test
	@DisplayName("berechne: Fehlen Noten, so gibt es keine Exception und keine Zulassung, Markierungen aus der Planung zählen nicht mit")
	void testFehlendeNoten() throws Exception {
		final Testfall testfall = lade2030();
		final Abiturdaten daten = testfall.daten();
		setzeLeer(daten);
		fachMitBelegung(daten, GostHalbjahr.Q21).belegungen[GostHalbjahr.Q21.id].block1gewertet = true;

		final AbiturdatenManager manager = GostZulassungSimulationUtils.berechne(daten, testfall.jahrgangsdaten(), testfall.faecher(), false);

		assertNull(manager.daten().block1Zulassung);
		assertFalse(manager.getErgebnisMarkierungsalgorithmus().erfolgreich);
		for (final AbiturFachbelegung fach : manager.daten().fachbelegungen) {
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegung = fach.belegungen[halbjahr.id];
				assertTrue((belegung == null) || !Boolean.TRUE.equals(belegung.block1gewertet));
			}
		}
	}

	@Test
	@DisplayName("berechne: Abitur 2030 läuft mit vollständigen Noten durch und bestimmt die Zulassung")
	void testBerechne2030() throws Exception {
		final Testfall testfall = lade2030();
		final Abiturdaten ergebnis = rechne(testfall);

		assertNotNull(ergebnis.block1Zulassung);
		assertNull(ergebnis.pruefungBestanden);
	}

	@Test
	@DisplayName("berechne: Die Markierungen vor Abitur 2030 entsprechen dem erwarteten Ergebnis des Testfalls")
	void testBerechneVor2030() throws Exception {
		final Testfall testfall = ladeVor2030();
		final GostAbiturMarkierungsalgorithmusErgebnis erwartet =
				ResourceUtils.json2Classes(packageTests, "Jahrgang_", "_Markierungsalgorithmus", GostAbiturMarkierungsalgorithmusErgebnis.class).get("01_1001");
		final Abiturdaten daten = testfall.daten();
		combine(daten, new GostZulassungSimulation(), true);

		final AbiturdatenManager manager = GostZulassungSimulationUtils.berechne(daten, testfall.jahrgangsdaten(), testfall.faecher(), false);

		assertTrue(erwartet.erfolgreich);
		assertTrue(manager.getErgebnisMarkierungsalgorithmus().erfolgreich);
		for (final GostAbiturMarkierungsalgorithmusMarkierung markierung : erwartet.markierungen) {
			assertEquals(markierung.markiert, fach(manager.daten(), markierung.idFach).belegungen[markierung.idHalbjahr].block1gewertet);
		}
		assertEquals(Boolean.TRUE, manager.daten().block1Zulassung);
		assertEquals((int) erwartet.markierungen.stream().filter(m -> m.markiert).count(), manager.daten().block1AnzahlKurse);
	}

}
