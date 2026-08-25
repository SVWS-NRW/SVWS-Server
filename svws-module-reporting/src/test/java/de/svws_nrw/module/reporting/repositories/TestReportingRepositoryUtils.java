package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;

/**
 * Tests für das Erzeugen von Reporting-Listen in {@link ReportingRepositoryUtils}, mit Schwerpunkt auf dem Verhalten nach einem fehlgeschlagenen Laden.
 * <p>Schlägt sowohl das gesammelte als auch das anschließende einzelne Laden fehl, hinterlegt
 * {@code ladeFehlendeWerteInRepositoryMap} einen Fehler-Marker {@code put(id, null)} in der Stammdaten-Map. Dieser Marker verhindert Endlosschleifen bei
 * erneuter Anfrage, darf aber nicht als geladener Datensatz durchgehen — sonst wird der Ersteller mit {@code null}-Stammdaten aufgerufen.</p>
 * <p>Der Ersteller in diesen Tests greift deshalb bewusst auf ein Feld der Stammdaten zu. Genau das tun auch die produktiven Ersteller: Bei Klassen und
 * Kursen wird {@code daten.idSchuljahresabschnitt} gelesen, bei Lehrkräften und Schülern werden die Stammdaten an den Proxy-Konstruktor gereicht.</p>
 * <p>Die Tests brauchen keine Datenbank; Loader und Ersteller sind einfache Lambdas.</p>
 */
class TestReportingRepositoryUtils {

	/** Die Bezeichnung des Datentyps für die Log-Meldungen der Utility. */
	private static final String DATENTYP = "Testdaten";

	/** Die Stammdaten-Map, die die Utility befüllt und aus der der Ersteller liest. */
	private Map<Long, Stammdaten> mapStammdaten;

	/** Die Cache-Map der erzeugten Reporting-Objekte. */
	private Map<Long, Reportingobjekt> mapReportingObjekte;

	/** Der Logger, den die Utility für ihre Meldungen erhält. */
	private Logger logger;

	/** Die Fehler gescheiterter Ladevorgänge, wie sie ein Repository über die Lebensdauer seines Caches hält. */
	private Map<Long, Exception> ladefehler;


	/**
	 * Minimale Stammdaten, wie sie ein Loader liefert.
	 *
	 * @param id   Die ID des Datensatzes.
	 * @param name Ein fachliches Feld, auf das der Ersteller zugreift.
	 */
	private record Stammdaten(long id, String name) {
		// Nur Träger der Testdaten.
	}

	/**
	 * Das aus den Stammdaten erzeugte Reporting-Objekt.
	 *
	 * @param id   Die ID des Datensatzes.
	 * @param name Der aus den Stammdaten übernommene Name.
	 */
	private record Reportingobjekt(long id, String name) {
		// Nur Träger des Ergebnisses.
	}


	@BeforeEach
	void setUp() {
		mapStammdaten = new HashMap<>();
		mapReportingObjekte = new HashMap<>();
		logger = new Logger();
		ladefehler = new HashMap<>();
	}


	/**
	 * Erzeugt einen Loader, der für die angeforderten IDs Stammdaten liefert und dabei für die angegebenen IDs eine Ausnahme wirft.
	 *
	 * @param idsMitFehler Die IDs, für die das Laden fehlschlägt.
	 *
	 * @return Der Loader für den Aufruf der Utility.
	 */
	private static Function<List<Long>, List<Stammdaten>> loaderMitFehlerFuer(final List<Long> idsMitFehler) {
		return angeforderteIds -> {
			for (final Long id : angeforderteIds) {
				if (idsMitFehler.contains(id)) {
					throw new IllegalStateException("Laden für ID " + id + " fehlgeschlagen.");
				}
			}
			final List<Stammdaten> ergebnis = new ArrayList<>();
			for (final Long id : angeforderteIds) {
				ergebnis.add(new Stammdaten(id, "Name-" + id));
			}
			return ergebnis;
		};
	}

	/**
	 * Ruft die Utility mit dem übergebenen Loader auf. Der Ersteller greift dabei auf ein Feld der Stammdaten zu.
	 *
	 * @param ids    Die IDs, für die Reporting-Objekte erzeugt werden sollen.
	 * @param loader Der Loader für die Stammdaten.
	 *
	 * @return Die erzeugte Liste der Reporting-Objekte.
	 */
	private List<Reportingobjekt> erstelleListe(final List<Long> ids, final Function<List<Long>, List<Stammdaten>> loader) {
		return waehleAus(ids, loader, null).objekte();
	}


	@Test
	void testAlleIdsLadbarErgibtVollstaendigesErgebnis() {
		final List<Reportingobjekt> ergebnis = erstelleListe(List.of(1L, 2L), loaderMitFehlerFuer(List.of()));

		assertEquals(List.of(new Reportingobjekt(1L, "Name-1"), new Reportingobjekt(2L, "Name-2")), ergebnis);
	}

	@Test
	void testFehlgeschlagenerBulkLoadMitErfolgreichenEinzelLoadsErgibtVollstaendigesErgebnis() {
		// Der Loader scheitert nur, solange beide IDs zusammen angefragt werden. Der Rückfall auf einzelne Ladevorgänge liefert dann beide Datensätze.
		final Function<List<Long>, List<Stammdaten>> loader = angeforderteIds -> {
			if (angeforderteIds.size() > 1) {
				throw new IllegalStateException("Gesammeltes Laden fehlgeschlagen.");
			}
			final long id = angeforderteIds.getFirst();
			return List.of(new Stammdaten(id, "Name-" + id));
		};

		final List<Reportingobjekt> ergebnis = erstelleListe(List.of(1L, 2L), loader);

		assertEquals(List.of(new Reportingobjekt(1L, "Name-1"), new Reportingobjekt(2L, "Name-2")), ergebnis);
	}

	@Test
	void testEineTechnischNichtLadbareIdWirdAusgelassen() {
		// Für ID 2 scheitern gesammeltes und einzelnes Laden. Die Utility hinterlegt dafür den Fehler-Marker put(2, null).
		final List<Reportingobjekt> ergebnis = erstelleListe(List.of(1L, 2L), loaderMitFehlerFuer(List.of(2L)));

		assertEquals(List.of(new Reportingobjekt(1L, "Name-1")), ergebnis);
		assertTrue(mapStammdaten.containsKey(2L), "Der Fehler-Marker muss erhalten bleiben, damit die ID nicht erneut geladen wird.");
	}

	@Test
	void testEineFachlichNichtVorhandeneIdWirdAusgelassen() {
		// Der Loader liefert für ID 2 schlicht keinen Datensatz, ohne zu scheitern.
		final Function<List<Long>, List<Stammdaten>> loader = angeforderteIds -> {
			final List<Stammdaten> ergebnis = new ArrayList<>();
			for (final Long id : angeforderteIds) {
				if (id != 2L) {
					ergebnis.add(new Stammdaten(id, "Name-" + id));
				}
			}
			return ergebnis;
		};

		final List<Reportingobjekt> ergebnis = erstelleListe(List.of(1L, 2L), loader);

		assertEquals(List.of(new Reportingobjekt(1L, "Name-1")), ergebnis);
	}

	@Test
	void testAlleIdsTechnischNichtLadbarErgibtEineLeereListe() {
		final List<Reportingobjekt> ergebnis = erstelleListe(List.of(1L, 2L), loaderMitFehlerFuer(List.of(1L, 2L)));

		assertEquals(List.of(), ergebnis);
	}

	@Test
	void testEinFehlerMarkerAusEinemFrueherenAufrufWirdNichtAlsDatensatzGewertet() {
		// Der Marker steht bereits in der Map. Ein späterer Aufruf darf ihn nicht erneut zu laden versuchen und nicht als Datensatz behandeln.
		mapStammdaten.put(2L, null);

		final List<Reportingobjekt> ergebnis = erstelleListe(List.of(1L, 2L), loaderMitFehlerFuer(List.of()));

		assertEquals(List.of(new Reportingobjekt(1L, "Name-1")), ergebnis);
	}


	// ##### Die Auswahl: welche IDs fehlen und warum #####

	/**
	 * Ruft die Auswahl mit dem übergebenen Loader und Filter auf.
	 *
	 * @param ids    Die IDs, für die Reporting-Objekte erzeugt werden sollen.
	 * @param loader Der Loader für die Stammdaten.
	 * @param filter Das Filter-Predicate oder {@code null}.
	 *
	 * @return Das Auswahlergebnis.
	 */
	private ReportingAuswahlergebnis<Reportingobjekt> waehleAus(final List<Long> ids, final Function<List<Long>, List<Stammdaten>> loader,
			final Predicate<Reportingobjekt> filter) {
		final LongFunction<Reportingobjekt> ersteller = id -> new Reportingobjekt(id, mapStammdaten.get(id).name());
		return ReportingRepositoryUtils.waehleAus(ids, mapStammdaten, mapReportingObjekte, loader, ersteller,
				Stammdaten::id, Comparator.comparingLong(Reportingobjekt::id), filter, DATENTYP, logger, ladefehler);
	}

	@Test
	void testEineTechnischNichtLadbareIdGiltAlsGescheiterterLadevorgang() {
		// Der Fehler-Marker put(2, null) unterscheidet den gescheiterten Ladevorgang von einem Datensatz, den es schlicht nicht gibt.
		final ReportingAuswahlergebnis<Reportingobjekt> auswahl = waehleAus(List.of(1L, 2L), loaderMitFehlerFuer(List.of(2L)), null);

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, auswahl.ausgelassen().get(2L).ursache());
	}

	@Test
	void testEinVerbindungsfehlerGiltAlsInfrastrukturstoerung() {
		// Fehlerinjektion: Der Treiber meldet eine verlorene Verbindung, verpackt in den Fehler des Zugriffs. Ein nicht mehr arbeitsfähiger Server darf nicht
		// als ausgelassener Datensatz durchgehen - aus diesem Zustand entsteht der Abbruch.
		final Function<List<Long>, List<Stammdaten>> loader = angeforderteIds -> {
			throw new IllegalStateException("Der Zugriff ist gescheitert.", new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren."));
		};

		final ReportingAuswahlergebnis<Reportingobjekt> auswahl = waehleAus(List.of(1L), loader, null);

		assertEquals(ReportingProblemursache.INFRASTRUKTURSTOERUNG, auswahl.ausgelassen().get(1L).ursache());
	}

	@Test
	void testDerAusloesendeFehlerErreichtDenLadezustand() {
		// Ohne ihn schriebe die spätere Meldung eine Warnung ohne Fehlertyp und ohne Stacktrace - und da die meldende Stelle nicht wirft, wäre beides verloren.
		final ReportingAuswahlergebnis<Reportingobjekt> auswahl = waehleAus(List.of(1L, 2L), loaderMitFehlerFuer(List.of(2L)), null);

		final Exception fehler = auswahl.ausgelassen().get(2L).fehler();
		assertEquals("Laden für ID 2 fehlgeschlagen.", (fehler == null) ? null : fehler.getMessage());
	}

	@Test
	void testDerFehlerUeberlebtDenZweitenZugriffAufDieselbeId() {
		// Der Fehler-Marker im Cache verhindert jeden weiteren Ladeversuch. Läge der Fehler nur im Aufruf, wüsste der zweite Zugriff zwar noch, dass das Laden
		// scheiterte, aber nicht mehr woran - und die Meldung stünde ohne Ursache und Stacktrace im Log.
		waehleAus(List.of(1L, 2L), loaderMitFehlerFuer(List.of(2L)), null);

		final ReportingAuswahlergebnis<Reportingobjekt> zweiterZugriff = waehleAus(List.of(1L, 2L), loaderMitFehlerFuer(List.of(2L)), null);

		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, zweiterZugriff.ausgelassen().get(2L).ursache());
		final Exception fehler = zweiterZugriff.ausgelassen().get(2L).fehler();
		assertEquals("Laden für ID 2 fehlgeschlagen.", (fehler == null) ? null : fehler.getMessage());
	}

	@Test
	void testDerEinzelfehlerWirdNichtZusaetzlichProtokolliert() {
		// Der Fehler gehört in die Meldung des Ausgabeproblems - gemeinsam dedupliziert. Ein eigener Eintrag hier stünde außerhalb dieser Deduplizierung und
		// führte denselben Fehler zweimal im Log.
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);

		waehleAus(List.of(1L, 2L), loaderMitFehlerFuer(List.of(2L)), null);

		assertTrue(log.getLogData().stream().noneMatch(eintrag -> eintrag.getText().contains("Fehler beim einzelnen Laden")),
				"Der Einzelfehler wird festgehalten statt protokolliert.");
		assertTrue(log.getLogData().stream().anyMatch(eintrag -> eintrag.getText().contains("Fehler beim gesammelten Laden")),
				"Der Fehler des gesammelten Zugriffs bleibt im Log: Er ist kein Ausgabeproblem, solange die Einzelzugriffe gelingen.");
	}

	@Test
	void testEineFachlichNichtVorhandeneIdGiltAlsNichtVorhanden() {
		// Der Loader liefert für ID 2 schlicht keinen Datensatz, ohne zu scheitern - es gibt sie nicht.
		final Function<List<Long>, List<Stammdaten>> loader = angeforderteIds -> {
			final List<Stammdaten> ergebnis = new ArrayList<>();
			for (final Long id : angeforderteIds) {
				if (id != 2L) {
					ergebnis.add(new Stammdaten(id, "Name-" + id));
				}
			}
			return ergebnis;
		};

		final ReportingAuswahlergebnis<Reportingobjekt> auswahl = waehleAus(List.of(1L, 2L), loader, null);

		assertEquals(ReportingProblemursache.NICHT_VORHANDEN, auswahl.ausgelassen().get(2L).ursache());
	}

	@Test
	void testDerComparatorSiehtKeineAusgefiltertenObjekte() {
		// Die Reporting-Comparatoren greifen auf nachladende Getter zu - etwa den Lernabschnitt eines Schülers oder das Fach eines Kurses. Würde nach dem
		// Sortieren gefiltert, löste die Sortierung diese Zugriffe auch für Datensätze aus, die der Anwender gar nicht sehen will.
		final List<Long> verglichen = new ArrayList<>();
		final Comparator<Reportingobjekt> comparator = (a, b) -> {
			verglichen.add(a.id());
			verglichen.add(b.id());
			return Long.compare(a.id(), b.id());
		};
		final LongFunction<Reportingobjekt> ersteller = id -> new Reportingobjekt(id, mapStammdaten.get(id).name());

		ReportingRepositoryUtils.waehleAus(List.of(1L, 2L, 3L), mapStammdaten, mapReportingObjekte, loaderMitFehlerFuer(List.of()), ersteller,
				Stammdaten::id, comparator, objekt -> objekt.id() != 2L, DATENTYP, logger, ladefehler);

		assertFalse(verglichen.contains(2L), "Der Comparator darf für ein ausgefiltertes Objekt nicht aufgerufen werden: " + verglichen);
	}

	@Test
	void testEinAusgefilterterDatensatzErscheintNichtUnterDenAusgelassenen() {
		// Er fehlt, weil der Anwender ihn nicht sehen will - das ist kein Ausgabeproblem und darf keines melden.
		final ReportingAuswahlergebnis<Reportingobjekt> auswahl = waehleAus(List.of(1L, 2L), loaderMitFehlerFuer(List.of()), objekt -> objekt.id() != 2L);

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
		assertEquals(List.of(2L), auswahl.idsAusgefiltert());
		assertTrue(auswahl.ausgelassen().isEmpty());
	}

	@Test
	void testEineNegativeIdWirdWieEineUnbekannteBehandelt() {
		// Eine negative ID benennt keinen Datensatz; sie fällt heute stillschweigend aus der Anfrage.
		final ReportingAuswahlergebnis<Reportingobjekt> auswahl = waehleAus(List.of(-1L), loaderMitFehlerFuer(List.of()), null);

		assertEquals(List.of(-1L), auswahl.idsAngefordert());
		assertEquals(ReportingProblemursache.NICHT_VORHANDEN, auswahl.ausgelassen().get(-1L).ursache());
		assertTrue(auswahl.bewusstLeer());
	}

	@Test
	void testDoppelteUndLeereIdsWerdenVorDerAuswahlEntfernt() {
		final ReportingAuswahlergebnis<Reportingobjekt> auswahl = waehleAus(Arrays.asList(1L, 1L, null), loaderMitFehlerFuer(List.of()), null);

		assertEquals(List.of(1L), auswahl.idsAngefordert());
		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
	}

	@Test
	void testFallenAlleDatensaetzeWegIstDieAuswahlBewusstLeer() {
		final ReportingAuswahlergebnis<Reportingobjekt> auswahl = waehleAus(List.of(1L, 2L), loaderMitFehlerFuer(List.of(1L, 2L)), null);

		assertTrue(auswahl.objekte().isEmpty());
		assertTrue(auswahl.bewusstLeer(), "Aus einer leeren Auswahl entsteht eine Ausgabe ohne Datensätze, kein Serverfehler.");
	}


	// ##### Listen-Caches mit Rückkanal für den Einzelfehler #####

	/**
	 * Erzeugt einen Loader für Listen-Daten, der für die angegebenen IDs eine Ausnahme wirft und sonst je ID einen Eintrag liefert.
	 *
	 * @param idsMitFehler Die IDs, für die das Laden fehlschlägt.
	 *
	 * @return Der Loader für den Aufruf der Utility.
	 */
	private static Function<List<Long>, Map<Long, List<String>>> listenLoaderMitFehlerFuer(final List<Long> idsMitFehler) {
		return angeforderteIds -> {
			for (final Long id : angeforderteIds) {
				if (idsMitFehler.contains(id)) {
					throw new IllegalStateException("Laden für ID " + id + " fehlgeschlagen.");
				}
			}
			final Map<Long, List<String>> ergebnis = new HashMap<>();
			for (final Long id : angeforderteIds) {
				ergebnis.put(id, List.of("Eintrag-" + id));
			}
			return ergebnis;
		};
	}

	@Test
	void testDerEinzelfehlerEinerListeWirdFestgehaltenStattProtokolliert() {
		// Bei Listen steht anschließend die leere Liste im Cache. Ohne Rückkanal wäre der Fehler damit spurlos, sobald niemand den Logeintrag liest.
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		final Map<Long, List<String>> cache = new HashMap<>();

		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(List.of(1L, 2L), cache, listenLoaderMitFehlerFuer(List.of(2L)), DATENTYP, logger,
				ladefehler);

		assertEquals(List.of("Eintrag-1"), cache.get(1L));
		assertEquals(List.of(), cache.get(2L), "Konsumenten sehen nie null, auch nicht nach einem gescheiterten Zugriff.");
		assertEquals("Laden für ID 2 fehlgeschlagen.", ladefehler.get(2L).getMessage());
		assertFalse(ladefehler.containsKey(1L), "Eine geladene ID hinterlässt keinen Fehler.");
		assertTrue(log.getLogData().stream().noneMatch(eintrag -> eintrag.getText().contains("Fehler beim einzelnen Laden")),
				"Der Einzelfehler gehört in die Meldung des Ausgabeproblems und nicht in einen eigenen Logeintrag.");
		assertTrue(log.getLogData().stream().anyMatch(eintrag -> eintrag.getText().contains("Fehler beim gesammelten Laden")),
				"Der Fehler des gesammelten Zugriffs bleibt im Log: Er ist kein Ausgabeproblem, solange die Einzelzugriffe gelingen.");
	}

	@Test
	void testOhneRueckkanalBleibtDerEinzelfehlerEinerListeImLog() {
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		final Map<Long, List<String>> cache = new HashMap<>();

		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(List.of(1L, 2L), cache, listenLoaderMitFehlerFuer(List.of(2L)), DATENTYP, logger);

		assertEquals(List.of(), cache.get(2L));
		assertTrue(log.getLogData().stream().anyMatch(eintrag -> eintrag.getText().contains("Fehler beim einzelnen Laden")),
				"Ohne Rückkanal ist der Logeintrag die einzige Spur des Fehlers und bleibt deshalb erhalten.");
	}

	@Test
	void testDerFehlerEinerListeUeberlebtDenZweitenZugriffAufDieselbeId() {
		final Map<Long, List<String>> cache = new HashMap<>();
		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(List.of(2L), cache, listenLoaderMitFehlerFuer(List.of(2L)), DATENTYP, logger, ladefehler);

		// Der zweite Zugriff lädt nicht erneut: Der Cache-Eintrag verhindert das. Der Fehler steht deshalb nur noch im Rückkanal des Repositories.
		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(List.of(2L), cache, listenLoaderMitFehlerFuer(List.of()), DATENTYP, logger, ladefehler);

		assertEquals(List.of(), cache.get(2L), "Ein erneuter Ladeversuch findet nicht statt.");
		assertEquals("Laden für ID 2 fehlgeschlagen.", ladefehler.get(2L).getMessage());
	}

}
