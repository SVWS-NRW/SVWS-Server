package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.LongFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.Logger;

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
		final LongFunction<Reportingobjekt> ersteller = id -> new Reportingobjekt(id, mapStammdaten.get(id).name());
		return ReportingRepositoryUtils.erstelleReportingListe(ids, mapStammdaten, mapReportingObjekte, loader, ersteller,
				stammdaten -> stammdaten.id(), Comparator.comparingLong(Reportingobjekt::id), null, DATENTYP, logger);
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

}
