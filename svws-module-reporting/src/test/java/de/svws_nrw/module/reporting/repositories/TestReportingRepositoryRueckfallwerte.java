package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlassenunterricht;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKursunterricht;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.gost.kursplanung.ProxyReportingSchuelerGostKursplanungKursbelegung;
import de.svws_nrw.service.gost.GostServiceFactory;
import de.svws_nrw.service.gost.GostServiceFactoryBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Zugriffe, die bei einem Fehler einen Rückfallwert liefern - null, eine leere Liste - und die Ausgabe fortsetzen. Sie melden den Befund über die
 * Fassade und hinterlassen keinen {@code ERROR}-Eintrag: Dieses Level ist dem Abbruch vorbehalten und widerspräche dem Rückfallwert dieser Stellen.
 * <p>Der Fehler wird gezielt injiziert. Der gemockte Context liefert keine Datenbankverbindung, sodass jeder Datenzugriff scheitert; wo eine Stelle erst nach
 * einem gelungenen gesammelten Laden erreichbar ist, steuert ein Konstruktions-Mock die Datenzugriffsklasse. Geprüft wird die Meldung und nicht die
 * Deduplizierung - die leistet der Problemsammler, den der gemockte Context nicht besitzt.</p>
 */
class TestReportingRepositoryRueckfallwerte {

	/** Die ID des Schuljahresabschnitts, für den die Tests Daten anfordern. */
	private static final long ID_ABSCHNITT = 11L;

	/** Die ID des Jahrgangs, dessen Daten sich nicht laden lassen. */
	private static final long ID_JAHRGANG = 5L;

	/** Die ID der Lehrkraft, deren Unterricht sich nicht laden lässt. */
	private static final long ID_LEHRER = 42L;

	/** Die ID des Schülers, dessen GKL-Wahlen sich nicht laden lassen. */
	private static final long ID_SCHUELER = 17L;

	/** Die ID der Klausurvorgabe, die sich nicht laden lässt. */
	private static final long ID_KLAUSURVORGABE = 23L;

	/** Das Abiturjahr, dessen Fachwahlstatistik sich nicht laden lässt. */
	private static final int ABITURJAHR = 2025;

	/** Die ID der Klasse, deren Daten sich nicht laden lassen. */
	private static final long ID_KLASSE = 7L;

	/** Die ID des Kurses, dessen Daten sich nicht laden lassen. */
	private static final long ID_KURS = 9L;

	/** Der gemockte Context, den die Repositories erhalten. */
	private ReportingContext reportingContext;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		when(reportingContext.logger()).thenReturn(logger);
	}


	/**
	 * Prüft, dass genau ein hingenommenes Ausgabeproblem mit dem erwarteten Schlüssel gemeldet wurde.
	 *
	 * @param schluessel Der erwartete Schlüssel des Befundes.
	 */
	private void erwarteGemeldetesProblem(final ReportingProblemSchluessel schluessel) {
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(schluessel), anyString(), any());
	}

	/**
	 * Prüft, dass das Log keinen Eintrag mit dem Level ERROR enthält.
	 */
	private void erwarteKeinenFehlerImLog() {
		assertTrue(log.getLogData().stream().noneMatch(eintrag -> eintrag.getLevel() == LogLevel.ERROR),
				"Ein Zugriff mit Rückfallwert darf keinen ERROR-Eintrag hinterlassen: Dieses Level ist dem Abbruch vorbehalten.");
	}


	@Test
	void testNichtLadbareJahrgangsdatenWerdenAlsAusgabeproblemGemeldet() {
		// Der gesammelte Zugriff gelingt und liefert keinen Jahrgang; erst das Nachladen der einzelnen ID scheitert. Nur so ist die Stelle überhaupt
		// erreichbar - ein Fehler des gesammelten Zugriffs beendet den Report.
		final ReportingRepositoryKataloge repository = new ReportingRepositoryKataloge(reportingContext);
		try (MockedConstruction<DataJahrgangsdaten> dataJahrgangsdaten = mockConstruction(DataJahrgangsdaten.class, (dataMock, ctx) -> {
			when(dataMock.getAll()).thenReturn(List.of());
			when(dataMock.getById(ID_JAHRGANG)).thenThrow(new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehlerinjektion"));
		})) {
			assertNull(repository.jahrgang(ID_JAHRGANG), "Ohne Jahrgangsdaten bleibt der Wert leer, die Klasse erscheint weiterhin.");
		}

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingJahrgang.class, ID_JAHRGANG));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEinVerbindungsfehlerWirdAlsInfrastrukturstoerungGemeldet() {
		// Derselbe Weg, aber mit einer abgerissenen Verbindung als Ursache: Die Meldung trägt dann die abbrechende Ursache statt des Ladefehlers. Dass die
		// Fassade daraus einen Serverfehler macht, prüft TestReportingContextMeldefassade - der Mock hier kann nicht werfen.
		final ReportingRepositoryKataloge repository = new ReportingRepositoryKataloge(reportingContext);
		try (MockedConstruction<DataJahrgangsdaten> dataJahrgangsdaten = mockConstruction(DataJahrgangsdaten.class, (dataMock, ctx) -> {
			when(dataMock.getAll()).thenReturn(List.of());
			when(dataMock.getById(ID_JAHRGANG)).thenThrow(new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren.")));
		})) {
			assertNull(repository.jahrgang(ID_JAHRGANG));
		}

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.INFRASTRUKTURSTOERUNG),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingJahrgang.class, ID_JAHRGANG)), anyString(),
				any());
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testNichtLadbarerUnterrichtEinerLehrkraftWirdJeUnterrichtsartGemeldet() {
		final ReportingRepositoryLehrer repository = new ReportingRepositoryLehrer(reportingContext);

		assertTrue(repository.leistungsdatenAlsFachlehrerKlassenunterricht(ID_ABSCHNITT, ID_LEHRER).isEmpty());
		assertTrue(repository.leistungsdatenAlsFachlehrerKursunterricht(ID_ABSCHNITT, ID_LEHRER).isEmpty());

		// Zwei Schlüssel, weil Klassen- und Kursunterricht getrennt fehlen können: Ein gemeinsamer Schlüssel machte aus beiden Befunden einen.
		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingKlassenunterricht.class, ID_LEHRER));
		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingKursunterricht.class, ID_LEHRER));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testNichtLadbareKlassenEinesAbschnittsWerdenOhneIdGemeldet() {
		final ReportingRepositoryLerngruppen repository = new ReportingRepositoryLerngruppen(reportingContext);

		assertTrue(repository.klassenBySchuljahresabschnitt(ID_ABSCHNITT).isEmpty());

		// Der Zugriff gilt allen Klassen des Abschnitts; der Schlüssel führt deshalb keine ID und zählt einmal je Aufruf.
		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingKlasse.class));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testNichtLadbareGklWahlenWerdenAlsAusgabeproblemGemeldet() {
		// Der Rückfallwert ist ein leeres Wahl-Objekt. Ohne Meldung wäre ein gescheiterter Zugriff von einem Schüler ohne GKL-Wahlen nicht zu unterscheiden.
		final ReportingRepositorySchueler repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.idsGeladenerSchueler()).thenReturn(List.of());
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);
		final ReportingRepositoryGost repository = new ReportingRepositoryGost(reportingContext);

		assertNotNull(repository.gklWahl(ID_SCHUELER), "Der Schüler erscheint weiterhin, seine GKL-Wahlen bleiben leer.");

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(GostSchuelerGKLWahl.class, ID_SCHUELER));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testNichtLadbareKlausurvorgabenWerdenJeIdGemeldet() {
		// Der Rückfallwert ist die leere Map. Ohne Meldung sähe der Aufrufer denselben fehlenden Eintrag wie bei einer Vorgabe, die es nicht gibt.
		final ReportingRepositoryGost repository = new ReportingRepositoryGost(reportingContext);

		assertTrue(repository.klausurvorgaben(List.of(ID_KLAUSURVORGABE)).isEmpty());

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(GostKlausurvorgabe.class, ID_KLAUSURVORGABE));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEineFehlendeFachwahlDerKursbelegungWirdGemeldetUndAlsUngueltigAusgegeben() {
		// Der Rückfallwert ist die als ungültig markierte Belegung: Der Schüler erscheint weiterhin im Kurs. Ohne Meldung wäre die Datenlücke der Blockung
		// von einer regulär ungültigen Belegung nicht zu unterscheiden.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
		final GostBlockungsergebnisManager ergebnisManager = mock(GostBlockungsergebnisManager.class);
		when(ergebnisManager.getOfSchuelerOfKursFachwahl(ID_SCHUELER, ID_KURS)).thenThrow(new DeveloperNotificationException("Fehlerinjektion"));

		final var fachwahl = repository.fachwahlDatenOderMelde(ergebnisManager, ID_SCHUELER, ID_KURS);

		assertFalse(fachwahl.gueltig(), "Ohne Fachwahl erscheint die Kursbelegung als ungültig.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_VORHANDEN),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN),
				eq(ReportingProblemSchluessel.fuer(ProxyReportingSchuelerGostKursplanungKursbelegung.class, ID_SCHUELER)), anyString(), any());
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEntfernteKurszuordnungenUnbekannterSchuelerWerdenGemeldet() {
		// Die Zuordnungen stehen im gespeicherten Zwischenergebnis und entfallen beim Aufbau; ohne Meldung erschiene die Ausgabe als vollständig.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);

		repository.meldeEntfernteKurszuordnungen(ID_SCHUELER);

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_VORHANDEN),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN),
				eq(ReportingProblemSchluessel.fuer(ProxyReportingSchuelerGostKursplanungKursbelegung.class, ID_SCHUELER)), anyString(), eq(null));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEinAndererFehlerDerFachwahlAufloesungPropagiert() {
		// Allein die fehlende Fachwahl ist der definierte Rückfall-Fall; jeder andere Fehler würde sonst still als fehlende Fachwahl gedeutet.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
		final GostBlockungsergebnisManager ergebnisManager = mock(GostBlockungsergebnisManager.class);
		when(ergebnisManager.getOfSchuelerOfKursFachwahl(ID_SCHUELER, ID_KURS)).thenThrow(new IllegalStateException("Fehlerinjektion"));

		assertThrows(IllegalStateException.class, () -> repository.fachwahlDatenOderMelde(ergebnisManager, ID_SCHUELER, ID_KURS));
		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEineNichtLadbareFachwahlstatistikWirdBeimOptionalenZugriffGemeldet() {
		// Der Rückfallwert ist die leere Liste: Die Kurse des Blockungsergebnisses erscheinen weiterhin, allein ihre Statistikwerte fehlen. Ohne Meldung wäre
		// der Fehlschlag nicht von einem Jahrgang ohne Fachwahlen zu unterscheiden.
		final ReportingRepositoryGost repository = new ReportingRepositoryGost(reportingContext);

		try (MockedStatic<GostServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostServiceFactoryBuilder.class)) {
			final GostServiceFactory serviceFactory = mock(GostServiceFactory.class, RETURNS_DEEP_STUBS);
			serviceFactoryBuilder.when(GostServiceFactoryBuilder::getGostServiceFactory).thenReturn(serviceFactory);
			when(serviceFactory.getGostJahrgangFachwahlService().getFachwahlStatistik(ABITURJAHR))
					.thenThrow(new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehlerinjektion"));

			assertTrue(repository.fachwahlenOptional(ABITURJAHR).isEmpty());
		}

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(GostStatistikFachwahl.class, ABITURJAHR));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEinVerbindungsfehlerDerFachwahlstatistikTraegtDieAbbrechendeUrsache() {
		// Derselbe Weg mit abgerissener Verbindung: Die Meldung trägt die abbrechende Ursache. Dass die Fassade daraus einen Serverfehler macht, prüft
		// TestReportingContextMeldefassade - der Mock hier kann nicht werfen.
		final ReportingRepositoryGost repository = new ReportingRepositoryGost(reportingContext);

		try (MockedStatic<GostServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostServiceFactoryBuilder.class)) {
			final GostServiceFactory serviceFactory = mock(GostServiceFactory.class, RETURNS_DEEP_STUBS);
			serviceFactoryBuilder.when(GostServiceFactoryBuilder::getGostServiceFactory).thenReturn(serviceFactory);
			when(serviceFactory.getGostJahrgangFachwahlService().getFachwahlStatistik(ABITURJAHR))
					.thenThrow(new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
							new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren.")));

			assertTrue(repository.fachwahlenOptional(ABITURJAHR).isEmpty());
		}

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.INFRASTRUKTURSTOERUNG),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(GostStatistikFachwahl.class, ABITURJAHR)),
				anyString(), any());
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testNichtLadbareKurseEinesAbschnittsWerdenOhneIdGemeldet() {
		final ReportingRepositoryLerngruppen repository = new ReportingRepositoryLerngruppen(reportingContext);

		assertTrue(repository.kurseBySchuljahresabschnitt(ID_ABSCHNITT).isEmpty());

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingKurs.class));
		erwarteKeinenFehlerImLog();
	}


	// ##### Listen-Zugriffe der Repositories #####

	/**
	 * Verdrahtet den Filter-Service, den die Listen-Zugriffe für den Benutzerfilter abfragen. Der Mock liefert keine Filtergruppe, sodass ungefiltert
	 * ausgewählt wird.
	 */
	private void gebeFilterServiceVor() {
		when(reportingContext.filterService()).thenReturn(mock(ReportingFilterService.class));
	}

	@Test
	void testEineNichtLadbareLehrkraftFehltInDerListeUndWirdGemeldet() {
		// Der Listen-Zugriff gibt nur die Objekte heraus. Ohne die Meldung fehlte eine nicht ladbare Lehrkraft - etwa eine Klassenleitung - völlig still,
		// denn der Rückkanal des Ladefehlers unterdrückt dessen Logeintrag.
		gebeFilterServiceVor();
		final ReportingRepositoryLehrer repository = new ReportingRepositoryLehrer(reportingContext);

		assertTrue(repository.lehrer(List.of(ID_LEHRER)).isEmpty(), "Die nicht ladbare Lehrkraft fehlt in der Liste.");

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingLehrer.class, ID_LEHRER));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEinNichtLadbarerSchuelerFehltInDerListeUndWirdGemeldet() {
		gebeFilterServiceVor();
		final ReportingRepositorySchueler repository = new ReportingRepositorySchueler(reportingContext);

		assertTrue(repository.schueler(List.of(ID_SCHUELER)).isEmpty(), "Der nicht ladbare Schüler fehlt in der Liste.");

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingSchueler.class, ID_SCHUELER));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEineNichtLadbareKlasseFehltInDerListeUndWirdGemeldet() {
		gebeFilterServiceVor();
		final ReportingRepositoryLerngruppen repository = new ReportingRepositoryLerngruppen(reportingContext);

		assertTrue(repository.klassen(List.of(ID_KLASSE)).isEmpty(), "Die nicht ladbare Klasse fehlt in der Liste.");

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingKlasse.class, ID_KLASSE));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEinNichtLadbarerKursFehltInDerListeUndWirdGemeldet() {
		gebeFilterServiceVor();
		final ReportingRepositoryLerngruppen repository = new ReportingRepositoryLerngruppen(reportingContext);

		assertTrue(repository.kurse(List.of(ID_KURS)).isEmpty(), "Der nicht ladbare Kurs fehlt in der Liste.");

		erwarteGemeldetesProblem(ReportingProblemSchluessel.fuer(ReportingKurs.class, ID_KURS));
		erwarteKeinenFehlerImLog();
	}

	@Test
	void testEineIdOhneDatensatzWirdBeimListenzugriffNichtGemeldet() {
		// Die Gegenprobe: Der Zugriff gelingt und findet nichts. Das ist kein Ladefehler - eine Meldung machte aus jeder unbekannten ID ein Ausgabeproblem.
		gebeFilterServiceVor();
		try (MockedConstruction<DataKlassendaten> dataKlassendaten = mockConstruction(DataKlassendaten.class,
				(dataMock, ctx) -> when(dataMock.getDTOsByIds(any())).thenReturn(List.of()))) {
			final ReportingRepositoryLerngruppen repository = new ReportingRepositoryLerngruppen(reportingContext);
			assertTrue(repository.klassen(List.of(ID_KLASSE)).isEmpty());
		}

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
		erwarteKeinenFehlerImLog();
	}

}
