package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterricht;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, dass ein Ladefehler der Stundenplandefinitionen nur dort zum Fehler wird, wo der Stundenplan das angeforderte Hauptobjekt ist.
 * <p>Der {@link ReportingContext} erzeugt das Repository für jeden Report, auch für Ausgaben ohne Stundenplanbezug. Ein Wurf im Konstruktor reißt diese
 * Ausgaben mit; ein Log-Eintrag mit {@link LogLevel#ERROR} wäre dort ebenso falsch, denn dieses Level ist dem Abbruch vorbehalten. Der Nachweis über das
 * fehlende ERROR ist deshalb Gegenstand mehrerer Tests.</p>
 * <p>Das Laden der Definitionen erfolgt über den statischen Aufruf {@link DataStundenplanListe#getStundenplaeneAktiv}; er ist die einzige Naht, an der sich
 * ein Ladefehler ohne Datenbank herstellen lässt.</p>
 */
class TestReportingRepositoryStundenplan {

	/** Die Meldung der Ursache, die das Laden der Definitionen scheitern lässt. */
	private static final String MELDUNG_URSACHE = "Die Verbindung zur Datenbank wurde unterbrochen.";

	/** Die ID des Stundenplans, dessen Daten die Tests anfordern. */
	private static final long ID_STUNDENPLAN = 7L;

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
	 * Erzeugt ein Repository, dessen Definitionen genau einen Stundenplan zur übergebenen ID führen. Nur für eine ID mit Definition erreicht der Zugriff
	 * überhaupt die Datenbank.
	 *
	 * @param idStundenplan Die ID des definierten Stundenplans.
	 *
	 * @return Das Repository mit dieser einen Definition.
	 */
	private ReportingRepositoryStundenplan repositoryMitDefinitionZu(final long idStundenplan) {
		final StundenplanListeEintrag definition = new StundenplanListeEintrag();
		definition.id = idStundenplan;
		definition.gueltigAb = "2026-08-01";
		definition.gueltigBis = "2027-07-31";
		dataStundenplanListe.when(() -> DataStundenplanListe.getStundenplaeneAktiv(any(), any())).thenReturn(new ArrayList<>(List.of(definition)));
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
				"Beim Initialisieren ist die Bedeutung des Fehlers unbekannt; ein ERROR-Eintrag behauptete einen Abbruch, den es nicht gibt.");
		assertNull(repository.stundenplan("2026-08-10"), "Der optionale Zugriff liefert nach einem Ladefehler null.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingStundenplanungStundenplan.class)),
				anyString(), any());
	}

	@Test
	void testDerOptionaleZugriffMeldetDenLadefehlerJeZugriffUeberDieFassade() {
		// Der Klausurplan erzeugt je Raum einen Proxy und fragt den Stundenplan zum Termin jedes Mal erneut ab. Die Deduplizierung gleicher Befunde
		// leistet der Problemsammler der Fassade über den Schlüssel.
		final ReportingRepositoryStundenplan repository = repositoryMitLadefehler();

		assertNull(repository.stundenplan("2026-08-10"));
		assertNull(repository.stundenplan("2026-08-11"));

		verify(reportingContext, times(2)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingStundenplanungStundenplan.class)),
				anyString(), any());
	}

	@Test
	void testOhneStundenplanZumDatumWirdDerFehlendeOptionaleWertGemeldet() {
		// Kein Plan zum Datum ist ein fachlicher Befund: Die abhängigen Angaben bleiben leer, der Schlüssel dedupliziert je Datum.
		final ReportingRepositoryStundenplan repository = repositoryMitDefinitionZu(ID_STUNDENPLAN);

		assertNull(repository.stundenplan("2020-01-15"), "Außerhalb aller Gültigkeitszeiträume gibt es keinen Stundenplan zum Datum.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.OPTIONALER_WERT_FEHLT),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN),
				eq(ReportingProblemSchluessel.fuer(ReportingStundenplanungStundenplan.class, 20200115L)), anyString(), eq(null));
	}

	@Test
	void testOhneVerwertbaresDatumEntstehtKeineWarnung() {
		// Ein Klausurtermin muss kein Datum tragen. Ohne Datum werden keine Stundenplandaten benötigt, der Ladefehler bleibt für diese Ausgabe folgenlos.
		final ReportingRepositoryStundenplan repository = repositoryMitLadefehler();

		assertNull(repository.stundenplan(""), "Ohne Datum gibt es keinen Stundenplan zum Termin.");
		assertNull(repository.stundenplan((String) null), "Ohne Datum gibt es keinen Stundenplan zum Termin.");

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testDerStrikteZugriffMeldetEinenServerfehler() {
		final ReportingRepositoryStundenplan repository = repositoryMitLadefehler();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.stundenplan(42L));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(), "Ein nicht ladbares Hauptobjekt ist ein Serverproblem, kein NOT_FOUND.");
		assertTrue(ursachen(aoe).contains(MELDUNG_URSACHE), "Die ursprüngliche Ursache bleibt in der Kette erhalten: " + ursachen(aoe));
	}

	@Test
	void testEinGescheiterterZugriffAufDenStundenplanWirdGemeldet() {
		// Ohne die Meldung liefert der Zugriff dasselbe null wie für einen Stundenplan, den es nicht gibt - der gescheiterte Zugriff bliebe damit spurlos.
		final ReportingRepositoryStundenplan repository = repositoryOhneDefinitionen();

		try (MockedConstruction<DataStundenplan> dataStundenplan = mockConstruction(DataStundenplan.class,
				(dataMock, ctx) -> when(dataMock.getById(ID_STUNDENPLAN)).thenThrow(new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehlerinjektion")))) {
			assertNull(repository.manager(ID_STUNDENPLAN), "Der Stundenplan bleibt leer, die übrige Ausgabe entsteht.");
		}

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN),
				eq(ReportingProblemSchluessel.fuer(ReportingStundenplanungStundenplan.class, ID_STUNDENPLAN)), anyString(), any());
		assertTrue(eintraege(LogLevel.ERROR).isEmpty(), "Der Zugriff liefert einen Rückfallwert und darf die Ausgabe nicht über das Log beenden.");
	}

	@Test
	void testEinNichtVorhandenerStundenplanWirdNichtGemeldet() {
		// Gegenprobe: Der Datenzugriff meldet einen Stundenplan, den es nicht gibt, mit NOT_FOUND. Der Zugriff hat einwandfrei gearbeitet - das ist eine
		// fachliche Auskunft und kein Ausgabeproblem.
		final ReportingRepositoryStundenplan repository = repositoryOhneDefinitionen();

		try (MockedConstruction<DataStundenplan> dataStundenplan = mockConstruction(DataStundenplan.class, (dataMock, ctx) -> when(
				dataMock.getById(ID_STUNDENPLAN)).thenThrow(new ApiOperationException(Status.NOT_FOUND, "Kein Stundenplan zur ID gefunden.")))) {
			assertNull(repository.manager(ID_STUNDENPLAN));
		}

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testFehlendeTeildatenEinesVorhandenenStundenplansWerdenGemeldet() {
		// Den Stundenplan gibt es; erst das Nachladen seiner Teildaten scheitert. Würde der Status NOT_FOUND auch hier als "gibt es nicht" gelesen, verschwände
		// diese Störung spurlos - deshalb gilt diese Lesart allein für den Zugriff auf den Stundenplan selbst.
		final ReportingRepositoryStundenplan repository = repositoryOhneDefinitionen();

		try (MockedConstruction<DataStundenplan> dataStundenplan = mockConstruction(DataStundenplan.class,
				(dataMock, ctx) -> when(dataMock.getById(ID_STUNDENPLAN)).thenReturn(new Stundenplan()));
				MockedStatic<DataStundenplanUnterricht> dataStundenplanUnterricht = mockStatic(DataStundenplanUnterricht.class)) {
			dataStundenplanUnterricht.when(() -> DataStundenplanUnterricht.getUnterrichte(any(), anyLong()))
					.thenThrow(new ApiOperationException(Status.NOT_FOUND, "Keine Unterrichte zum Stundenplan gefunden."));

			assertNull(repository.manager(ID_STUNDENPLAN));
		}

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN),
				eq(ReportingProblemSchluessel.fuer(ReportingStundenplanungStundenplan.class, ID_STUNDENPLAN)), anyString(), any());
	}

	@Test
	void testEinUnvollstaendigerStundenplanWirdGemeldet() {
		// Der Datenzugriff liefert null, wenn es den Stundenplan zwar gibt, sein Schuljahresabschnitt aber fehlt. Diese Inkonsistenz ist ein Datenproblem und
		// darf nicht wie ein nicht vorhandener Stundenplan durchgehen.
		final ReportingRepositoryStundenplan repository = repositoryOhneDefinitionen();

		try (MockedConstruction<DataStundenplan> dataStundenplan = mockConstruction(DataStundenplan.class,
				(dataMock, ctx) -> when(dataMock.getById(ID_STUNDENPLAN)).thenReturn(null))) {
			assertNull(repository.manager(ID_STUNDENPLAN));
		}

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN),
				eq(ReportingProblemSchluessel.fuer(ReportingStundenplanungStundenplan.class, ID_STUNDENPLAN)), anyString(), any());
	}

	@Test
	void testEinVorhandenerAberNichtLadbarerStundenplanErgibtEinenServerfehler() {
		// Der strikte Zugriff darf einen vorhandenen Stundenplan, dessen Daten fehlen, nicht wie eine unbekannte ID beantworten: Der Anwender suchte sonst
		// nach einem Datensatz, den es gibt.
		final ReportingRepositoryStundenplan repository = repositoryMitDefinitionZu(ID_STUNDENPLAN);
		final ApiOperationException ursache = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Unterrichtsdaten sind nicht lesbar.");

		try (MockedConstruction<DataStundenplan> dataStundenplan = mockConstruction(DataStundenplan.class,
				(dataMock, ctx) -> when(dataMock.getById(ID_STUNDENPLAN)).thenThrow(ursache))) {
			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.stundenplan(ID_STUNDENPLAN));

			assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
			assertSame(ursache, aoe.getCause(), "Die Ursache gehört in den Abbruch; die Abschlussgrenze protokolliert sie mit Stacktrace.");
		}
	}

	@Test
	void testEinWaehrendDesAufrufsGeloeschterStundenplanErgibtEinenServerfehler() {
		// Die Definitionsliste dieses Aufrufs führt den Stundenplan, der Einzelzugriff findet ihn nicht mehr - etwa nach einem parallelen Löschen. Für den
		// strikten Zugriff zählt das Weltbild des Aufrufs: Die Schule führt den Plan, sein Fehlen ist ein Serverproblem und kein "gibt es nicht".
		final ReportingRepositoryStundenplan repository = repositoryMitDefinitionZu(ID_STUNDENPLAN);
		final ApiOperationException ursache = new ApiOperationException(Status.NOT_FOUND, "Kein Stundenplan zur ID gefunden.");

		try (MockedConstruction<DataStundenplan> dataStundenplan = mockConstruction(DataStundenplan.class,
				(dataMock, ctx) -> when(dataMock.getById(ID_STUNDENPLAN)).thenThrow(ursache))) {
			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.stundenplan(ID_STUNDENPLAN));

			assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
			assertSame(ursache, aoe.getCause(), "Die Ursache gehört in den Abbruch; die Abschlussgrenze protokolliert sie mit Stacktrace.");
		}
		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEineIdOhneDefinitionBleibtOhneStundenplan() {
		// Gegenprobe: Zu dieser ID gibt es keinen aktiven Stundenplan. Der strikte Zugriff liefert null, und der Aufrufer macht daraus sein NOT_FOUND.
		final ReportingRepositoryStundenplan repository = repositoryMitDefinitionZu(ID_STUNDENPLAN);

		assertNull(assertDoesNotThrow(() -> repository.stundenplan(ID_STUNDENPLAN + 1)));
	}

	@Test
	void testLeereDefinitionenSindKeinLadefehler() {
		final ReportingRepositoryStundenplan repository = repositoryOhneDefinitionen();

		assertNull(repository.stundenplan(42L), "Ohne passende Definition liefert der strikte Zugriff null; daraus erzeugt der Initializer sein NOT_FOUND.");
		assertNull(repository.stundenplan("2026-08-10"), "Der optionale Zugriff liefert ebenfalls null.");
		assertTrue(eintraege(LogLevel.ERROR).isEmpty(), "Eine geladene, aber leere Liste ist ein zulässiger Datenwert und kein Fehler.");
		verify(reportingContext, never()).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER), any(), any(), anyString(), any());
	}

}
