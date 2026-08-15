package de.svws_nrw.module.reporting.diagnose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransientConnectionException;
import java.time.Duration;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Prüft die Einteilungen der {@link ReportingProblemursache}: welche Ursache die Ausgabe abbricht, welche einen gescheiterten Datenzugriff bezeichnet und
 * welche Ursache aus dem Fehler eines Zugriffs entsteht.
 * <p>An der ersten Einteilung hängt, ob aus einem Befund ein Serverfehler oder ein hingenommenes Ausgabeproblem wird; an der zweiten, welche Ursachen als
 * Fehlschlag eines Ladezustands zulässig sind. Die dritte entscheidet, ob eine abgerissene Datenbankverbindung als solche erkannt wird oder als fehlende
 * Angabe durchgeht.</p>
 */
class TestReportingProblemursache {

	@Test
	void testAlleinDieInfrastrukturstoerungBrichtAb() {
		// Die Grenze verläuft entlang der Ursache und nicht entlang der Anzahl betroffener Datensätze.
		for (final ReportingProblemursache ursache : ReportingProblemursache.values()) {
			assertEquals(ursache == ReportingProblemursache.INFRASTRUKTURSTOERUNG, ursache.istAbbruch(),
					"Nur eine Infrastrukturstörung bricht die Ausgabe ab: " + ursache.name());
		}
	}

	@Test
	void testNurDieBeidenLadefehlerBezeichnenEinenFehlschlag() {
		for (final ReportingProblemursache ursache : ReportingProblemursache.values()) {
			final boolean erwartet = (ursache == ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER)
					|| (ursache == ReportingProblemursache.INFRASTRUKTURSTOERUNG);
			assertEquals(erwartet, ursache.istFehlschlagUrsache(),
					"Die Einteilung der Ursache " + ursache.name() + " als Ursache eines Fehlschlags ist falsch.");
		}
	}

	@Test
	void testEinNichtVorhandenerDatensatzIstKeinFehlschlag() {
		// Die Ursache entsteht bei einem Datenzugriff und tritt als Ladezustand auf - der Zugriff hat aber einwandfrei gearbeitet und nichts gefunden.
		assertFalse(ReportingProblemursache.NICHT_VORHANDEN.istFehlschlagUrsache());
		assertFalse(ReportingProblemursache.NICHT_VORHANDEN.istAbbruch());
	}

	// ##### Die Ursache aus dem Fehler eines Zugriffs #####

	@Test
	void testEinAbgerissenerDatenbankzugriffIstEineInfrastrukturstoerung() {
		assertEquals(ReportingProblemursache.INFRASTRUKTURSTOERUNG,
				ReportingProblemursache.fuerLadefehler(new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren.")));
		assertEquals(ReportingProblemursache.INFRASTRUKTURSTOERUNG,
				ReportingProblemursache.fuerLadefehler(new SQLTransientConnectionException("Keine freie Verbindung im Pool.")));
		assertEquals(ReportingProblemursache.INFRASTRUKTURSTOERUNG, ReportingProblemursache.fuerLadefehler(new ConnectException("Verbindung abgelehnt.")));
	}

	@Test
	void testEinSqlFehlerAmDatensatzBleibtDatensatzbezogen() {
		// Gegenprobe: Ein verletzter Fremdschlüssel betrifft den einzelnen Datensatz und wird hingenommen.
		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER,
				ReportingProblemursache.fuerLadefehler(new SQLException("Der Fremdschlüssel ist verletzt.", "23000")));
	}

	@Test
	void testDerVerbindungsfehlerWirdAuchTiefInDerUrsachenketteErkannt() {
		// Der Datenzugriff reicht den Fehler des Treibers verpackt weiter; unverpackt kommt er in den Repositories nie an.
		final Exception fehler = new IllegalStateException("Der Zugriff ist gescheitert.",
				new RuntimeException("Die Abfrage ist gescheitert.", new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren.")));

		assertEquals(ReportingProblemursache.INFRASTRUKTURSTOERUNG, ReportingProblemursache.fuerLadefehler(fehler));
	}

	@Test
	void testEinGewoehnlicherZugriffsfehlerBleibtDatensatzbezogen() {
		// Der Default aus G-2a: Ein unsicherer Fall wird hingenommen, statt die ganze Ausgabe an einem einzelnen Datensatz scheitern zu lassen.
		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER,
				ReportingProblemursache.fuerLadefehler(new IllegalStateException("Der Datensatz ist beschädigt.")));
		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemursache.fuerLadefehler(new NullPointerException()));
		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER,
				ReportingProblemursache.fuerLadefehler(new IOException("Die Datei fehlt.")), "Ein Ein-/Ausgabefehler ist kein Verbindungsfehler.");
	}

	@Test
	void testOhneFehlerBleibtEsBeimDatensatzbezogenenLadefehler() {
		// Ein Zugriff kann ohne Ausnahme erfolglos bleiben - etwa wenn der Fehler-Marker im Cache ohne Rückkanal entstanden ist.
		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemursache.fuerLadefehler(null));
	}

	@Test
	void testEineImKreisVerketteteUrsacheBeendetDiePruefung() {
		// Die Kette wird nur begrenzt tief verfolgt. Ohne diese Grenze bliebe die Prüfung hier hängen und der Server antwortete nicht mehr.
		final Exception erster = new IllegalStateException("Erster Fehler.");
		final Exception zweiter = new IllegalStateException("Zweiter Fehler.", erster);
		erster.initCause(zweiter);

		assertTimeoutPreemptively(Duration.ofSeconds(5),
				() -> assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemursache.fuerLadefehler(erster)));
	}


	@Test
	void testEineAuswahlentscheidungIstKeinProblem() {
		// Ein Datensatz, den der Benutzerfilter ausschließt, fehlt, weil der Anwender es so wollte. Er gehört in das Ergebnis der Auswahl und nicht hierher -
		// sonst wird eine korrekt arbeitende Auswahl als unvollständige Ausgabe gemeldet.
		assertFalse(Arrays.stream(ReportingProblemursache.values()).map(Enum::name).anyMatch(name -> name.contains("FILTER")),
				"Die Ursachen des Moduls dürfen keine Filterung führen: " + Arrays.toString(ReportingProblemursache.values()));
	}

}
