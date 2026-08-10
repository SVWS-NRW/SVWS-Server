package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, dass ein Ladefehler der Stundenplandefinitionen nur dort zum Fehler wird, wo der Stundenplan das angeforderte Hauptdatum ist.
 * <p>Der {@link ReportingContext} erzeugt das Repository für jeden Report, auch für Ausgaben ohne Stundenplanbezug. Ein Wurf im Konstruktor reißt diese
 * Ausgaben mit; ein Log-Eintrag mit {@link LogLevel#ERROR} tut dasselbe, da die ReportingFactory das gesammelte Log darauf prüft und die Ausgabe auch ohne
 * Wurf abbricht. Der Nachweis über das fehlende ERROR ist deshalb Gegenstand mehrerer Tests.</p>
 * <p>Das Laden der Definitionen erfolgt über den statischen Aufruf {@link DataStundenplanListe#getStundenplaeneAktiv}; er ist die einzige Naht, an der sich
 * ein Ladefehler ohne Datenbank herstellen lässt.</p>
 */
class TestReportingRepositoryStundenplan {

	/** Die Meldung der Ursache, die das Laden der Definitionen scheitern lässt. */
	private static final String MELDUNG_URSACHE = "Die Verbindung zur Datenbank wurde unterbrochen.";

	/** Die Überschrift des Abschnitts mit dem Stacktrace. Sie markiert einen vollständigen Fehlerblock im Log. */
	private static final String UEBERSCHRIFT_STACKTRACE = "### STACKTRACE:";

	/** Der Logger, in den das Repository protokolliert. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;

	/** Der Context, mit dem das Repository erzeugt wird. */
	private ReportingContext reportingContext;

	/** Die Naht für das Laden der Definitionen. */
	private MockedStatic<DataStundenplanListe> dataStundenplanListe;


	@BeforeEach
	void setUp() {
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);
		dataStundenplanListe = mockStatic(DataStundenplanListe.class);
	}

	@AfterEach
	void tearDown() {
		dataStundenplanListe.close();
	}


	/**
	 * Erzeugt ein Repository, dessen Definitionen sich nicht laden lassen.
	 *
	 * @return Das Repository mit gespeichertem Ladefehler.
	 */
	private ReportingRepositoryStundenplan repositoryMitLadefehler() {
		dataStundenplanListe.when(() -> DataStundenplanListe.getStundenplaeneAktiv(any(), any()))
				.thenThrow(new IllegalStateException(MELDUNG_URSACHE));
		return new ReportingRepositoryStundenplan(reportingContext);
	}

	/**
	 * Erzeugt ein Repository, dessen Definitionen geladen werden konnten, aber keinen Eintrag enthalten.
	 *
	 * @return Das Repository mit leerer Definitionsliste.
	 */
	private ReportingRepositoryStundenplan repositoryOhneDefinitionen() {
		dataStundenplanListe.when(() -> DataStundenplanListe.getStundenplaeneAktiv(any(), any())).thenReturn(new ArrayList<>());
		return new ReportingRepositoryStundenplan(reportingContext);
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
	 * Gibt die Meldungen der Ursachenkette der übergebenen Exception zurück.
	 *
	 * @param exception Die Exception, deren Ursachen gesucht sind.
	 *
	 * @return Die Meldungen der Ursachen, von der äußersten zur innersten.
	 */
	private static List<String> ursachen(final Exception exception) {
		final List<String> meldungen = new ArrayList<>();
		for (Throwable ursache = exception.getCause(); ursache != null; ursache = ursache.getCause()) {
			meldungen.add(String.valueOf(ursache.getMessage()));
		}
		return meldungen;
	}


	@Test
	void testEinLadefehlerBrichtDieKonstruktionNichtAb() {
		final ReportingRepositoryStundenplan repository = repositoryMitLadefehler();

		assertTrue(eintraege(LogLevel.ERROR).isEmpty(),
				"Beim Initialisieren ist die Bedeutung des Fehlers unbekannt; ein ERROR-Eintrag bräche auch fachfremde Reports ab.");
		assertTrue(eintraege(LogLevel.WARNING).isEmpty(), "Auch eine Warnung ist beim Initialisieren zu früh: Erst der Zugriff kennt die Bedeutung.");
		assertNull(repository.stundenplan("2026-08-10"), "Der optionale Zugriff liefert nach einem Ladefehler null.");
		assertTrue(eintraege(LogLevel.ERROR).isEmpty(), "Der optionale Zugriff stellt eine Lücke dar und protokolliert deshalb höchstens WARNING.");
		assertFalse(eintraege(LogLevel.WARNING).isEmpty(), "Da der optionale Zugriff nicht wirft, ist seine Warnung die einzige Spur des Fehlers.");
	}

	@Test
	void testDerOptionaleZugriffProtokolliertHoechstensEinmal() {
		// Der Klausurplan erzeugt je Raum einen Proxy und fragt den Stundenplan zum Termin jedes Mal erneut ab.
		final ReportingRepositoryStundenplan repository = repositoryMitLadefehler();

		assertNull(repository.stundenplan("2026-08-10"));
		assertNull(repository.stundenplan("2026-08-11"));

		assertEquals(1, eintraege(LogLevel.WARNING).stream().filter(UEBERSCHRIFT_STACKTRACE::equals).count(),
				"Derselbe Ladefehler darf nicht je Zugriff erneut mit Ursache und Stacktrace im Log stehen: " + eintraege(LogLevel.WARNING));
	}

	@Test
	void testOhneVerwertbaresDatumEntstehtKeineWarnung() {
		// Ein Klausurtermin muss kein Datum tragen. Ohne Datum werden keine Stundenplandaten benötigt, der Ladefehler bleibt für diese Ausgabe folgenlos.
		final ReportingRepositoryStundenplan repository = repositoryMitLadefehler();

		assertNull(repository.stundenplan(""), "Ohne Datum gibt es keinen Stundenplan zum Termin.");
		assertNull(repository.stundenplan((String) null), "Ohne Datum gibt es keinen Stundenplan zum Termin.");

		assertTrue(eintraege(LogLevel.WARNING).isEmpty(),
				"Eine Warnung über nicht benötigte Daten wäre für den Leser des Logs irreführend: " + eintraege(LogLevel.WARNING));
	}

	@Test
	void testDerStrikteZugriffMeldetEinenServerfehler() {
		final ReportingRepositoryStundenplan repository = repositoryMitLadefehler();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.stundenplan(42L));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(), "Ein nicht ladbares Hauptdatum ist ein Serverproblem, kein NOT_FOUND.");
		assertTrue(ursachen(aoe).contains(MELDUNG_URSACHE), "Die ursprüngliche Ursache bleibt in der Kette erhalten: " + ursachen(aoe));
		assertEquals(1, eintraege(LogLevel.ERROR).size(),
				"Das Repository trägt allein die Angabe bei, welcher Zugriff scheitert; den Block aus Typ, Ursachen und Stacktrace gibt die oberste Ebene "
						+ "aus: " + eintraege(LogLevel.ERROR));
	}

	@Test
	void testLeereDefinitionenSindKeinLadefehler() {
		final ReportingRepositoryStundenplan repository = repositoryOhneDefinitionen();

		assertNull(repository.stundenplan(42L), "Ohne passende Definition liefert der strikte Zugriff null; daraus erzeugt der Initializer sein NOT_FOUND.");
		assertNull(repository.stundenplan("2026-08-10"), "Der optionale Zugriff liefert ebenfalls null.");
		assertTrue(eintraege(LogLevel.ERROR).isEmpty(), "Eine geladene, aber leere Liste ist ein zulässiger Datenwert und kein Fehler.");
		assertTrue(eintraege(LogLevel.WARNING).isEmpty(), "Ohne Ladefehler gibt es auch nichts zu warnen.");
	}

}
