package de.svws_nrw.module.reporting.diagnose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Prüft den {@link ReportingProblemSammler}: Sammlung und Log entstehen in einem Schritt, dasselbe Problem zählt einmal, und ein hingenommenes Problem
 * hinterlässt niemals einen ERROR-Eintrag.
 * <p>Der letzte Punkt ist die schärfste Bedingung: {@link LogLevel#ERROR} ist dem Abbruch vorbehalten, und ein erfolgreicher Report hinterlässt keinen
 * solchen Eintrag. Ein Sammler, der ERROR schriebe, meldete jeden hingenommenen Befund als Abbruch.</p>
 */
class TestReportingProblemSammler {

	/** Die Überschrift des Abschnitts mit dem Stacktrace. Sie markiert einen vollständigen Fehlerblock im Log. */
	private static final String UEBERSCHRIFT_STACKTRACE = "### STACKTRACE:";

	/** Der Logger, in den der Sammler protokolliert. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;

	/** Der zu prüfende Sammler. */
	private ReportingProblemSammler sammler;


	/**
	 * Ein Fehler, dessen equals alle Instanzen gleichsetzt. Nur damit unterscheidet der Test die geforderte Objektidentität von einem Vergleich über
	 * equals: Gewöhnliche Exceptions erben das identitätsbasierte equals, sodass zwei Instanzen auch in einem gewöhnlichen Set getrennt blieben.
	 */
	private static final class GleichAussehenderFehler extends IllegalStateException {

		/** Die Serialisierungs-ID der Testklasse. */
		private static final long serialVersionUID = 1L;

		/**
		 * Erzeugt den Fehler mit einer festen Meldung.
		 */
		GleichAussehenderFehler() {
			super("Der Datensatz ist beschädigt.");
		}

		/**
		 * Setzt alle Instanzen dieser Klasse gleich.
		 *
		 * @param obj Das Vergleichsobjekt.
		 *
		 * @return true, wenn das Vergleichsobjekt dieselbe Klasse hat.
		 */
		@Override
		public boolean equals(final Object obj) {
			return obj instanceof GleichAussehenderFehler;
		}

		/**
		 * Liefert für alle Instanzen denselben Hashwert, passend zu {@link #equals(Object)}.
		 *
		 * @return Ein fester Wert.
		 */
		@Override
		public int hashCode() {
			return GleichAussehenderFehler.class.hashCode();
		}
	}


	@BeforeEach
	void setUp() {
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		sammler = new ReportingProblemSammler(logger);
	}


	/**
	 * Gibt die Texte der Log-Einträge des übergebenen Levels zurück.
	 *
	 * @param level Das Level, dessen Einträge gesucht sind.
	 *
	 * @return Die Texte der Einträge, ohne die Einrückung des Loggers.
	 */
	private List<String> eintraege(final LogLevel level) {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == level).map(eintrag -> eintrag.getText().strip()).toList();
	}

	/**
	 * Meldet ein ausgelassenes Schülerdatum zur übergebenen ID.
	 *
	 * @param idSchueler Die ID des Schülers.
	 */
	private void meldeFehlendenSchueler(final long idSchueler) {
		sammler.melde(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, idSchueler), "Der Schüler wurde ausgelassen.", null);
	}


	@Test
	void testEinGemeldetesProblemStehtInSammlungUndLog() {
		meldeFehlendenSchueler(42L);

		assertEquals(1, sammler.anzahl());
		assertFalse(sammler.istLeer());
		assertEquals(1, eintraege(LogLevel.WARNING).size(), "Sammlung und Log werden in einem Schritt befüllt.");
		assertTrue(eintraege(LogLevel.WARNING).getFirst().contains("ReportingSchueler 42"),
				"Die betroffene ID gehört ins Log - sie ist dort die einzige Spur, welcher Datensatz fehlt: " + eintraege(LogLevel.WARNING));
	}

	@Test
	void testEinHingenommenesProblemHinterlaesstKeinenFehler() {
		meldeFehlendenSchueler(42L);

		assertTrue(eintraege(LogLevel.ERROR).isEmpty(),
				"ERROR ist dem Abbruch vorbehalten; ein hingenommener Befund darf keinen solchen Eintrag hinterlassen: " + eintraege(LogLevel.ERROR));
	}

	@Test
	void testDasselbeProblemZaehltUndProtokolliertEinmal() {
		// Dieselbe fehlende ID wird im Verlauf eines Reports mehrfach aufgelöst - etwa je Klausurraum erneut.
		meldeFehlendenSchueler(42L);
		meldeFehlendenSchueler(42L);
		meldeFehlendenSchueler(42L);

		assertEquals(1, sammler.anzahl());
		assertEquals(1, eintraege(LogLevel.WARNING).size(), "Sammlung und Log sind gemeinsam dedupliziert: " + eintraege(LogLevel.WARNING));
	}

	@Test
	void testVerschiedeneDatensaetzeZaehlenGetrennt() {
		meldeFehlendenSchueler(42L);
		meldeFehlendenSchueler(43L);

		assertEquals(2, sammler.anzahl());
	}

	@Test
	void testDieselbeIdInVerschiedenenObjektartenZaehltGetrennt() {
		// Ohne die Objektart im Schlüssel fielen ein fehlender Schüler 42 und eine fehlende Klasse 42 zu einem Problem zusammen.
		sammler.melde(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Der Schüler wurde ausgelassen.", null);
		sammler.melde(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingKlasse.class, 42L), "Die Klasse wurde ausgelassen.", null);

		assertEquals(2, sammler.anzahl());
	}

	@Test
	void testZweiVerschiedeneProblemeAmSelbenObjektZaehlenGetrennt() {
		sammler.melde(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Die Lernabschnitte fehlen.", null);
		sammler.melde(ReportingProblemursache.NICHT_DARSTELLBAR, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Der QR-Code ist nicht darstellbar.", null);

		assertEquals(2, sammler.anzahl(), "Zwei fachlich verschiedene Probleme am selben Objekt sind zwei Probleme.");
	}

	@Test
	void testEinProblemOhneEinzelnenDatensatzZaehltEinmalJeAufruf() {
		// Nicht ladbare Stundenplandefinitionen betreffen die gesamte Ausgabe und hängen an keiner einzelnen ID.
		for (int i = 0; i < 3; i++) {
			sammler.melde(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
					ReportingProblemSchluessel.fuer(ReportingKlasse.class), "Die Stundenplandefinitionen konnten nicht ermittelt werden.", null);
		}

		assertEquals(1, sammler.anzahl());
		assertTrue(eintraege(LogLevel.WARNING).getFirst().contains("gesamter Aufruf"), "Der Schlüssel ohne ID weist das Problem als aufrufweit aus.");
	}

	@Test
	void testDerAusloesendeFehlerWirdMitStacktraceFestgehalten() {
		// Die meldende Stelle wirft nicht - dieser Eintrag ist die einzige Gelegenheit, Ursache und Stacktrace festzuhalten.
		final IllegalStateException fehler = new IllegalStateException("Der Datensatz ist beschädigt.");

		sammler.melde(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Der Schüler konnte nicht geladen werden.", fehler);

		assertTrue(eintraege(LogLevel.WARNING).contains(UEBERSCHRIFT_STACKTRACE), "Der Fehlerblock gehört vollständig ins Log.");
		assertTrue(eintraege(LogLevel.WARNING).contains("Der Datensatz ist beschädigt."), "Die Meldung der Ursache gehört ins Log.");
		assertTrue(eintraege(LogLevel.ERROR).isEmpty(), "Der gesamte Fehlerblock steht auf WARNING: " + eintraege(LogLevel.ERROR));
	}

	@Test
	void testOhneFehlerEntstehtKeinFehlerblock() {
		meldeFehlendenSchueler(42L);

		assertFalse(eintraege(LogLevel.WARNING).contains(UEBERSCHRIFT_STACKTRACE));
		assertTrue(eintraege(LogLevel.WARNING).stream().noneMatch(eintrag -> eintrag.contains("Fehler ohne Exception")),
				"Ein fehlender Datensatz ist kein Fehler; der Block wäre irreführend: " + eintraege(LogLevel.WARNING));
	}

	@Test
	void testDieselbeFehlerInstanzProtokolliertIhrenStacktraceNurEinmal() {
		// Dieselbe Fehler-Instanz reist mit mehreren Befunden - etwa eine nicht ladbare Lehrkraft, die als angefordertes Hauptobjekt ausgelassen und zusätzlich als
		// Klassenleitung gemeldet wird. Beide Befunde zählen; der Block aus Typ, Ursachenkette und Stacktrace stünde sonst je Befund erneut im Log.
		final IllegalStateException fehler = new IllegalStateException("Der Datensatz ist beschädigt.");

		sammler.melde(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Der Schüler konnte nicht geladen werden.", fehler);
		sammler.melde(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Die Daten des Schülers fehlen in der Ausgabe.", fehler);

		assertEquals(2, sammler.anzahl(), "Zwei Befunde bleiben zwei Probleme; die Deduplizierung betrifft allein den Fehlerblock.");
		assertEquals(1, eintraege(LogLevel.WARNING).stream().filter(UEBERSCHRIFT_STACKTRACE::equals).count(),
				"Der Stacktrace derselben Fehler-Instanz gehört nur einmal ins Log: " + eintraege(LogLevel.WARNING));
		assertTrue(eintraege(LogLevel.WARNING).stream().anyMatch(eintrag -> eintrag.contains("Fehlerdetails stehen beim ersten Eintrag")),
				"Der zweite Befund verweist auf den bereits protokollierten Fehlerblock: " + eintraege(LogLevel.WARNING));
	}

	@Test
	void testZweiGleichartigeFehlerBehaltenJeIhrenFehlerblock() {
		// Die Gegenprobe zur Objektidentität mit zwei Instanzen, deren equals sie gleichsetzt: Ein Vergleich über equals würfe sie zusammen und
		// unterschlüge den zweiten Stacktrace. Gewöhnliche Exceptions erben das identitätsbasierte equals und deckten diese Regression nicht auf.
		sammler.melde(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Der Schüler konnte nicht geladen werden.",
				new GleichAussehenderFehler());
		sammler.melde(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 43L), "Der Schüler konnte nicht geladen werden.",
				new GleichAussehenderFehler());

		assertEquals(2, eintraege(LogLevel.WARNING).stream().filter(UEBERSCHRIFT_STACKTRACE::equals).count(),
				"Verschiedene Fehler-Instanzen behalten je ihren eigenen Fehlerblock: " + eintraege(LogLevel.WARNING));
	}

	@Test
	void testEineInfrastrukturstoerungWirdNichtHingenommen() {
		// Sie bricht die Ausgabe ab. Sie hier zu melden hieße, einen nicht arbeitsfähigen Server als fehlende Daten auszugeben.
		assertThrows(IllegalArgumentException.class, () -> sammler.melde(ReportingProblemursache.INFRASTRUKTURSTOERUNG,
				ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Verbindung weg.", null));

		assertTrue(sammler.istLeer());
	}

	@Test
	void testEineInfrastrukturstoerungIstAuchDirektKeinProblem() {
		// Die Invariante hängt nicht am Sammler: Ein Ausgabeproblem mit abbrechender Ursache darf gar nicht erst entstehen.
		assertThrows(IllegalArgumentException.class, () -> new ReportingProblem(ReportingProblemursache.INFRASTRUKTURSTOERUNG,
				ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L)));
	}

	@Test
	void testDerSammlerVerlangtEinenLogger() {
		// Ohne Logger entstünde eine Sammlung ohne Logeinträge - beide Kanäle liefen genau dort auseinander, wo sie gemeinsam entstehen sollen.
		assertThrows(NullPointerException.class, () -> new ReportingProblemSammler(null));
	}

	@Test
	void testEineProxyKlasseZaehltAlsIhrReportingTyp() {
		// Zu jedem Reporting-Typ gehört ein ProxyReporting...; meldet eine Stelle objekt.getClass() und eine andere den Typ, wäre es sonst zweimal dasselbe
		// Problem.
		sammler.melde(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, 42L), "Der Schüler wurde ausgelassen.", null);
		sammler.melde(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ProxyReportingSchueler.class, 42L), "Der Schüler wurde ausgelassen.", null);

		assertEquals(1, sammler.anzahl(), "Die Proxy-Implementierung bezeichnet denselben Datensatz wie ihr Reporting-Typ.");
		assertEquals(ReportingSchueler.class, sammler.probleme().getFirst().schluessel().objektart(),
				"Im Schlüssel steht der Reporting-Typ, nicht seine Implementierung.");
	}

	@Test
	void testEineKlasseAusserhalbDerNamenskonventionBleibtUnveraendert() {
		// Der Schlüssel steht auch Datentypen offen, die keinen Reporting-Typ besitzen.
		assertEquals(String.class, ReportingProblemSchluessel.fuer(String.class, 1L).objektart());
	}

	@Test
	void testEineFremdeKlasseMitNamensanfangProxyBleibtUnveraendert() {
		// Das Präfix umfasst auch "Reporting". Ein bloßes "Proxy" ersetzte diese Klasse durch ihre Oberklasse und würfe damit fremde Objektarten zusammen.
		assertEquals(java.lang.reflect.Proxy.class, ReportingProblemSchluessel.fuer(java.lang.reflect.Proxy.class, 1L).objektart());
	}

	@Test
	void testDieGemeldetenProblemeSindUnveraenderlichUndInReihenfolge() {
		meldeFehlendenSchueler(42L);
		meldeFehlendenSchueler(43L);

		final List<ReportingProblem> probleme = sammler.probleme();

		assertEquals(42L, probleme.getFirst().schluessel().id());
		assertEquals(43L, probleme.getLast().schluessel().id());
		assertThrows(UnsupportedOperationException.class, () -> probleme.add(probleme.getFirst()),
				"Gemeldet wird ausschließlich über die Fassade des Contexts.");
	}

}
