package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;

/**
 * Prüft die Zeile, mit der die {@link ReportingFactory} die Kenndaten einer Anfrage festhält.
 * <p>Die Zeile entsteht vor jeder inhaltlichen Prüfung und arbeitet damit auf ungeprüften Daten aus dem Request. Sie darf unter keinen Umständen selbst
 * scheitern: Ihr eigener Fehler verdeckte sonst den Fehler, den sie dokumentieren soll, und das Log verlöre zugleich alle übrigen Kenndaten.</p>
 * <p>Der zweite Gegenstand ist der Ausschluss. Im Fehlerfall geht das Log als Antwort an den Client, deshalb ist die Liste der nicht protokollierten Angaben
 * eine Zusage nach außen und keine Stilfrage.</p>
 */
class TestReportingParameterLogUtils {

	/** Ein Kennwert, der nur in einem ausgeschlossenen Feld steht und deshalb nie im Protokoll erscheinen darf. */
	private static final String KENNWERT = "GEHEIMER-KENNWERT-4711";

	/** Die Parameter einer gewöhnlichen Anfrage, die jeder Test nach Bedarf abwandelt. */
	private ReportingParameter parameter;


	@BeforeEach
	void setUp() {
		parameter = new ReportingParameter();
		parameter.reportvorlage = "Klasse-Liste-Schueler-Kontaktdaten-Erzieher";
		parameter.idSchuljahresabschnitt = 42L;
		parameter.ausgabeformat = ReportingAusgabeformat.PDF.getId();
		parameter.idHauptdatenObjekt = 17L;
		parameter.idsHauptdaten = new ArrayList<>(List.of(1001L, 1002L, 1003L));
		parameter.idsDetaildaten = new ArrayList<>();
		parameter.eMailDaten = new ReportingEMailDaten();
		parameter.eMailDaten.empfaengerTyp = 1;
	}

	/**
	 * Erzeugt die Zeile zu den Parametern des Tests mit dem PDF-Format des API-Aufrufs.
	 *
	 * @return Die Zeile mit den Kenndaten.
	 */
	private String zeile() {
		return ReportingParameterLogUtils.kenndaten(parameter, ReportingAusgabeformat.PDF);
	}


	// ##### 1. Alle vorgesehenen Kenndaten #####

	@Test
	void testDieZeileNenntAlleKenndatenDerAnfrage() {
		// Die ganze Zeile wird verglichen und nicht nur auf einzelne Vorkommen geprüft: Eine fehlende, eine doppelte oder eine vertauschte Angabe fällt
		// sonst nicht auf, und gerade die Reihenfolge macht die Zeile im Log lesbar.
		assertEquals("Anfrage (Rohwerte): Vorlage 'Klasse-Liste-Schueler-Kontaktdaten-Erzieher', Schuljahresabschnitt 42, Ausgabeformat der Anfrage %d, "
				.formatted(ReportingAusgabeformat.PDF.getId())
				+ "Ausgabeformat des API-Aufrufs PDF, Hauptdatenobjekt 17, Hauptdaten 3 IDs [1001, 1002, 1003], Detaildaten 0 IDs [], "
				+ "E-Mail-Empfängertyp 1",
				zeile());
	}

	@Test
	void testBeideAusgabeformateStehenInDerZeile() {
		// Die Prüfung des Formats meldet, das Format der Anfrage passe nicht zum aufgerufenen. Mit nur einer Seite des Vergleichs ist das nicht
		// nachvollziehbar. Das Format der Anfrage steht als rohe Zahl, denn ein unbekannter Wert soll erkennbar bleiben.
		parameter.ausgabeformat = 99;

		final String zeile = ReportingParameterLogUtils.kenndaten(parameter, ReportingAusgabeformat.HTML);

		assertTrue(zeile.contains("Ausgabeformat der Anfrage 99"), zeile);
		assertTrue(zeile.contains("Ausgabeformat des API-Aufrufs HTML"), zeile);
	}


	// ##### 2. Null-Werte beenden die Protokollierung nicht #####

	@Test
	void testEinFehlendesAusgabeformatDesApiAufrufsWirdProtokolliert() {
		// Genau dieser Fall ist ein Abbruchgrund und muss deshalb protokollierbar bleiben.
		final String zeile = ReportingParameterLogUtils.kenndaten(parameter, null);

		assertTrue(zeile.contains("Ausgabeformat des API-Aufrufs null"), zeile);
	}

	@Test
	void testNullInDenFeldernDerAnfrageBeendetDieProtokollierungNicht() {
		// Die @NotNull-Zusagen des Datenobjekts sind Bean-Validation; die Reporting-Endpunkte werten sie nicht aus. Ein Request mit ausdrücklichem null
		// setzt das Feld also wirklich auf null.
		parameter.reportvorlage = null;
		parameter.idsHauptdaten = null;
		parameter.idsDetaildaten = null;
		parameter.eMailDaten = null;

		final String zeile = zeile();

		assertTrue(zeile.contains("Vorlage 'null'"), zeile);
		assertTrue(zeile.contains("Hauptdaten null"), zeile);
		assertTrue(zeile.contains("Detaildaten null"), zeile);
		assertTrue(zeile.contains("E-Mail-Empfängertyp null"), zeile);
	}

	@Test
	void testEinNullEintragInDerIdListeBeendetDieProtokollierungNicht() {
		// Der Elementtyp der ID-Listen trägt kein @NotNull, und die Bereinigung läuft erst nach dieser Zeile.
		parameter.idsHauptdaten = new ArrayList<>(Arrays.asList(1001L, null, 1003L));

		assertTrue(zeile().contains("Hauptdaten 3 IDs [1001, null, 1003]"), zeile());
	}

	@Test
	void testOhneParameterobjektEntstehtEineVerstaendlicheZeile() {
		assertEquals("Anfrage: keine Reporting-Parameter übergeben.", ReportingParameterLogUtils.kenndaten(null, ReportingAusgabeformat.PDF));
	}


	// ##### 3. Die leere Liste bleibt von der fehlenden unterscheidbar #####

	@Test
	void testDieLeereListeIstVonDerFehlendenUnterscheidbar() {
		// Der Unterschied sagt, ob der Client nichts geschickt oder ausdrücklich nichts ausgewählt hat.
		parameter.idsHauptdaten = new ArrayList<>();
		parameter.idsDetaildaten = null;

		final String zeile = zeile();

		assertTrue(zeile.contains("Hauptdaten 0 IDs []"), zeile);
		assertTrue(zeile.contains("Detaildaten null"), zeile);
	}


	// ##### 4. Kürzung langer ID-Listen #####

	@Test
	void testZehnIdsStehenVollstaendigInDerZeile() {
		parameter.idsHauptdaten = new ArrayList<>(LongStream.rangeClosed(1L, 10L).boxed().toList());

		assertTrue(zeile().contains("Hauptdaten 10 IDs [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"), zeile());
	}

	@Test
	void testAbElfIdsErscheinenNurDieErstenZehnUndDieRestanzahl() {
		parameter.idsHauptdaten = new ArrayList<>(LongStream.rangeClosed(1L, 11L).boxed().toList());

		assertTrue(zeile().contains("Hauptdaten 11 IDs [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1 weitere]"), zeile());
	}

	@Test
	void testBeiVielenIdsStimmtDieRestanzahl() {
		parameter.idsHauptdaten = new ArrayList<>(LongStream.rangeClosed(1001L, 1500L).boxed().toList());

		final String zeile = zeile();

		assertTrue(zeile.contains("Hauptdaten 500 IDs [1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 490 weitere]"), zeile);
		assertFalse(zeile.contains("1011"), "Über die ersten zehn IDs hinaus darf keine einzelne ID im Protokoll stehen: " + zeile);
	}


	// ##### 5. Maskierung und Längengrenze des Vorlagennamens #####

	@Test
	void testSteuerzeichenImVorlagennamenWerdenSichtbarErsetzt() {
		// Ein Umbruch teilte die Konsolenausgabe in zwei Zeilen, von denen die zweite ohne Zeitstempel und Level steht - sie sähe wie ein eigener
		// Log-Eintrag aus und ließe sich fälschen.
		final String escape = String.valueOf((char) 27);
		parameter.reportvorlage = "Zeile1\nZeile2\rZeile3\tEnde" + escape + "[31m";

		final String zeile = zeile();

		assertTrue(zeile.contains("Vorlage 'Zeile1\\nZeile2\\rZeile3\\tEnde\\u001b[31m'"), zeile);
		assertFalse(zeile.contains("\n"), "Die Zeile darf keinen echten Umbruch enthalten: " + zeile);
		assertFalse(zeile.contains("\r"), "Die Zeile darf keinen echten Wagenrücklauf enthalten: " + zeile);
	}

	@Test
	void testEinVorlagennameAnDerLaengengrenzeBleibtVollstaendig() {
		parameter.reportvorlage = "x".repeat(120);

		assertTrue(zeile().contains("Vorlage '" + "x".repeat(120) + "'"), "Genau 120 Zeichen sind noch erlaubt.");
	}

	@Test
	void testEinLeererVorlagennameBleibtVonEinemFehlendenUnterscheidbar() {
		// Der leere Text ist der Vorgabewert des Datenobjekts. Er bezeichnet eine andere Anfrage als ein ausdrücklich gesetztes null.
		parameter.reportvorlage = "";

		assertTrue(zeile().contains("Vorlage '', Schuljahresabschnitt"), zeile());
	}

	@Test
	void testEinZeichenUeberDerGrenzeWirdBereitsGekuerzt() {
		parameter.reportvorlage = "x".repeat(121);

		assertTrue(zeile().contains("Vorlage '" + "x".repeat(117) + "...'"), zeile());
	}

	@Test
	void testDieMaskierungLaeuftVorDerKuerzung() {
		// Die Reihenfolge ist verbindlich: Die Maskierung verlängert den Text. Wer zuerst kürzt, hält die Grenze danach nicht mehr ein.
		parameter.reportvorlage = "x".repeat(119) + "\n";

		// Roh sind es 120 Zeichen, maskiert 121 - und damit eines zu viel.
		assertTrue(zeile().contains("Vorlage '" + "x".repeat(117) + "...'"), zeile());
	}

	@Test
	void testEinZuLangerVorlagennameWirdAufDieGrenzeGekuerzt() {
		// Die Grenze schließt das Auslassungszeichen ein, damit Umsetzung und Test dieselbe Zahl meinen.
		parameter.reportvorlage = "x".repeat(500);

		assertTrue(zeile().contains("Vorlage '" + "x".repeat(117) + "...'"), zeile());
	}

	@Test
	void testDieKuerzungZerschneidetKeinZeichenAusZweiCodeeinheiten() {
		// Die verbliebene Hälfte eines Zeichens erschiene in der Ausgabe als Ersatzzeichen und machte den Namen an seinem Ende unlesbar.
		parameter.reportvorlage = "x".repeat(116) + "😀".repeat(10);

		final String zeile = zeile();

		assertTrue(zeile.contains("Vorlage '" + "x".repeat(116) + "...'"), zeile);
		assertFalse(Character.isHighSurrogate(zeile.charAt(zeile.indexOf("...") - 1)), "Der Schnitt darf keine einzelne Codeeinheit hinterlassen: " + zeile);
	}


	// ##### 6. Ausgeschlossene Angaben erscheinen nicht #####

	@Test
	void testAusgeschlosseneAngabenErscheinenNichtImProtokoll() {
		// Das Log erreicht im Fehlerfall den Client. Der Ausschluss ist damit eine Zusage nach außen.
		parameter.eMailDaten.betreff = KENNWERT + "-BETREFF";
		parameter.eMailDaten.text = KENNWERT + "-TEXT";

		final ReportingReportvorlageParameter vorlagenparameter = new ReportingReportvorlageParameter();
		vorlagenparameter.name = KENNWERT + "-PARAMETERNAME";
		vorlagenparameter.wert = KENNWERT + "-PARAMETERWERT";
		final ReportingReportvorlageParameterGruppe parametergruppe = new ReportingReportvorlageParameterGruppe();
		parametergruppe.name = KENNWERT + "-PARAMETERGRUPPE";
		parametergruppe.beschreibung = KENNWERT + "-GRUPPENBESCHREIBUNG";
		parametergruppe.reportvorlageParameter.add(vorlagenparameter);
		parameter.reportvorlageParameterGruppen = new ArrayList<>(List.of(parametergruppe));

		final ReportingSortierungDefinitionGruppe sortierung = new ReportingSortierungDefinitionGruppe();
		sortierung.bezeichnung = KENNWERT + "-SORTIERUNG";
		sortierung.typ = KENNWERT + "-SORTIERTYP";
		parameter.sortierungDefinitionenGruppen = new ArrayList<>(List.of(sortierung));

		final ReportingFilterDefinitionGruppe filter = new ReportingFilterDefinitionGruppe();
		filter.bezeichnung = KENNWERT + "-FILTER";
		filter.typ = KENNWERT + "-FILTERTYP";
		parameter.filterDefinitionenGruppen = new ArrayList<>(List.of(filter));

		final String zeile = zeile();

		assertFalse(zeile.contains(KENNWERT), "Betreff, E-Mail-Text, freie Vorlagenparameter sowie Filter- und Sortierwerte gehören nicht ins Log: " + zeile);
	}

}
