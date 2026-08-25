package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.diagnose.ReportingLadezustand;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryLerngruppen;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryStundenplan;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft den Initializer, für den der Stundenplan das angeforderte Hauptobjekt ist: die Statuszuordnung des Stundenplan-Ladens und die Auswahl der
 * Hauptdaten je Sichtweise.
 * <p>Drei Ursachen sehen im Repository ähnlich aus, dürfen am API-Rand aber nicht denselben Status ergeben: Eine ID, zu der es keine Definition gibt, ist
 * {@code NOT_FOUND}; nicht ladbare Definitionen und ein vorhandener Stundenplan mit nicht ladbaren Daten sind Serverprobleme.</p>
 * <p>Der {@link ReportingContext} wird gemockt, weil sein Konstruktor sämtliche Repositories aufbaut und dafür eine Datenbankverbindung benötigt. Für die
 * Status-Tests ist das Repository der Stundenpläne <b>echt</b> - gerade sein Zusammenspiel mit dem Initializer ist Gegenstand dieser Tests. Für die
 * Auswahl-Tests ist es gemockt: Dort zählt allein, dass der Stundenplan geladen ist.</p>
 */
class TestHtmlContextInitializerStundenplan {

	/** Die ID, unter der ein Stundenplan angefordert wird. */
	private static final long ID_STUNDENPLAN = 42L;

	/** Die Meldung des Fehlers, an dem das Laden der Stundenplandefinitionen scheitert. */
	private static final String MELDUNG_LADEFEHLER = "Die Verbindung zur Datenbank wurde unterbrochen.";

	/** Der gemockte Context, den der Initializer erhält. */
	private ReportingContext reportingContext;

	/** Die gemockten typisierten Parameter des Requests. */
	private ReportingParameterTypisiert reportingParameter;

	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		// Der Initializer hält seinen Ablauf auf DEBUG fest; ohne Logger liefe er in eine NPE. Ausgewertet wird das Log nicht.
		when(reportingContext.logger()).thenReturn(new Logger());

		reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.reportVorlage()).thenReturn(ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN);
		when(reportingParameter.idHauptdatenObjekt()).thenReturn(ID_STUNDENPLAN);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
	}


	/**
	 * Verdrahtet den Context mit einem echten Repository, dessen Definitionen sich laden lassen, aber keinen Eintrag enthalten.
	 */
	private void repositoryOhneDefinitionen() {
		verdrahteRepository(new ArrayList<>(), null);
	}

	/**
	 * Verdrahtet den Context mit einem echten Repository, dessen Definitionen sich nicht laden lassen.
	 */
	private void repositoryMitLadefehler() {
		verdrahteRepository(null, new IllegalStateException(MELDUNG_LADEFEHLER));
	}

	/**
	 * Verdrahtet den Context mit einem echten Repository, dessen Definitionen den angeforderten Stundenplan führen. Erst damit erreicht der Zugriff die
	 * Datenbank.
	 */
	private void verdrahteRepositoryMitDefinition() {
		final StundenplanListeEintrag definition = new StundenplanListeEintrag();
		definition.id = ID_STUNDENPLAN;
		definition.gueltigAb = "2026-08-01";
		definition.gueltigBis = "2027-07-31";
		verdrahteRepository(new ArrayList<>(List.of(definition)), null);
	}

	/**
	 * Erzeugt das echte Repository mit dem angegebenen Ladeergebnis und hinterlegt es im gemockten Context.
	 * <p>Das Repository entsteht <b>vor</b> dem Stubben des Getters: Innerhalb eines laufenden {@code when(…)} greift der Mock des statischen Ladeaufrufs
	 * nicht, das Repository liefe dann gegen die fehlende Datenbankverbindung.</p>
	 *
	 * @param definitionen Die zu ladenden Definitionen; wird nur ausgewertet, wenn kein Ladefehler übergeben ist.
	 * @param ladefehler   Der Fehler, mit dem das Laden scheitert, oder null.
	 */
	private void verdrahteRepository(final List<StundenplanListeEintrag> definitionen, final RuntimeException ladefehler) {
		final ReportingRepositoryStundenplan repository;
		try (MockedStatic<DataStundenplanListe> dataStundenplanListe = mockStatic(DataStundenplanListe.class)) {
			if (ladefehler != null) {
				dataStundenplanListe.when(() -> DataStundenplanListe.getStundenplaeneAktiv(any(), any())).thenThrow(ladefehler);
			} else {
				dataStundenplanListe.when(() -> DataStundenplanListe.getStundenplaeneAktiv(any(), any())).thenReturn(definitionen);
			}
			repository = new ReportingRepositoryStundenplan(reportingContext);
		}
		when(reportingContext.repositoryStundenplan()).thenReturn(repository);
	}

	/**
	 * Erzeugt den Initializer der Sichtweise „Klassen“ über die Registry. Die fünf Sichtweisen der Stundenplanung teilen sich diesen Initializer und
	 * unterscheiden sich allein in der Prüfung ihrer Hauptdaten-IDs, die erst nach dem Laden des Stundenplans erfolgt.
	 *
	 * @return Der Initializer für den Test.
	 */
	private HtmlContextInitializer initializer() {
		return HtmlContextInitializerRegistry.aufbauOderNull(ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN)
				.initializer(reportingContext, new HashMap<>());
	}

	/**
	 * Gibt die Meldungen der Ursachenkette eines Fehlers zurück, von der äußersten Ursache bis zur innersten.
	 *
	 * @param exception Der Fehler, dessen Kette gelesen wird.
	 *
	 * @return Die Meldungen der Ursachen.
	 */
	private static List<String> ursachen(final Exception exception) {
		final List<String> meldungen = new ArrayList<>();
		for (Throwable ursache = exception.getCause(); ursache != null; ursache = ursache.getCause()) {
			meldungen.add(String.valueOf(ursache.getMessage()));
		}
		return meldungen;
	}


	@Test
	void testEineNichtVorhandeneStundenplanIdErgibtNotFound() {
		repositoryOhneDefinitionen();

		final HtmlContextInitializer initializer = initializer();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, initializer::init);

		assertEquals(Status.NOT_FOUND, aoe.getStatus(), "Der einzeln adressierte Stundenplan existiert nicht; das ist kein Serverfehler.");
	}

	@Test
	void testEinVorhandenerAberNichtLadbarerStundenplanErgibtEinenServerfehler() {
		// Die Definition führt den Stundenplan, seine Daten sind aber nicht lesbar. Ein NOT_FOUND schickte den Anwender auf die Suche nach einem Datensatz,
		// den es gibt.
		verdrahteRepositoryMitDefinition();

		try (MockedConstruction<DataStundenplan> dataStundenplan = mockConstruction(DataStundenplan.class, (dataMock, ctx) -> when(
				dataMock.getById(ID_STUNDENPLAN)).thenThrow(new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Daten sind nicht lesbar.")))) {
			final HtmlContextInitializer initializer = initializer();

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, initializer::init);

			assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		}
	}

	@Test
	void testNichtLadbareDefinitionenErgebenEinenServerfehler() {
		repositoryMitLadefehler();

		final HtmlContextInitializer initializer = initializer();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, initializer::init);

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(),
				"Ein technischer Ladefehler darf nicht als „Stundenplan existiert nicht“ beim Client ankommen.");
		assertTrue(ursachen(aoe).contains(MELDUNG_LADEFEHLER), "Die ursprüngliche Ursache bleibt in der Kette erhalten: " + ursachen(aoe));
	}


	// ##### Auswahl der Hauptdaten je Sichtweise #####

	/**
	 * Erzeugt einen minimalen Stundenplan ohne Raster für die Tests der Auswahl.
	 *
	 * @param raeume               Die Räume des Stundenplans oder null.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt des Stundenplans oder null.
	 *
	 * @return Der Stundenplan.
	 */
	private static ReportingStundenplanungStundenplan stundenplan(final List<ReportingStundenplanungRaum> raeume,
			final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		return new ReportingStundenplanungStundenplan("", null, null, ID_STUNDENPLAN, null, raeume, schuljahresabschnitt, 1, null, null);
	}

	/**
	 * Verdrahtet den Context mit einem gemockten Stundenplan-Repository, das den angeforderten Stundenplan liefert.
	 *
	 * @param stundenplan Der Stundenplan, den das Repository liefern soll.
	 */
	private void gebeGeladenenStundenplanVor(final ReportingStundenplanungStundenplan stundenplan) {
		final ReportingRepositoryStundenplan repositoryStundenplan = mock(ReportingRepositoryStundenplan.class);
		when(repositoryStundenplan.stundenplan(ID_STUNDENPLAN)).thenReturn(stundenplan);
		when(reportingContext.repositoryStundenplan()).thenReturn(repositoryStundenplan);
	}

	/**
	 * Verdrahtet das Lerngruppen-Repository mit dem übergebenen Ergebnis der Klassen-Auswahl und die Hauptdaten-IDs des Requests.
	 *
	 * @param ids     Die angeforderten Klassen-IDs.
	 * @param auswahl Das Ergebnis, das die Auswahl liefern soll.
	 */
	private void gebeKlassenAuswahlVor(final List<Long> ids, final ReportingAuswahlergebnis<ReportingKlasse> auswahl) {
		final ReportingRepositoryLerngruppen repositoryLerngruppen = mock(ReportingRepositoryLerngruppen.class);
		when(repositoryLerngruppen.waehleKlassenAus(ids)).thenReturn(auswahl);
		when(repositoryLerngruppen.klassen(anyList())).thenReturn(List.of());
		when(reportingContext.repositoryLerngruppen()).thenReturn(repositoryLerngruppen);
		when(reportingParameter.idsHauptdaten()).thenReturn(ids);
	}

	/**
	 * Führt die Auswahl der übergebenen Sichtweise gegen den Stundenplan aus - der Weg, den der Initializer nach dem Laden des Stundenplans nimmt.
	 *
	 * @param datenContext Der Datenaufbau der Sichtweise.
	 * @param stundenplan  Der geladene Stundenplan.
	 * @param ids          Die angeforderten Hauptdaten-IDs.
	 *
	 * @return Das Auswahlergebnis.
	 */
	private ReportingAuswahlergebnis<?> auswahlDerSichtweise(final ReportingReportvorlageDatenContext datenContext,
			final ReportingStundenplanungStundenplan stundenplan, final List<Long> ids) {
		final HtmlContextAufbauStundenplan<?, ?> aufbau = (HtmlContextAufbauStundenplan<?, ?>) HtmlContextInitializerRegistry.aufbauOderNull(datenContext);
		return aufbau.auswahl().waehle(reportingContext, stundenplan, ids);
	}


	@Test
	void testEineUnbekannteKlassenIdWirdAusgelassenStattAbgewiesen() {
		gebeGeladenenStundenplanVor(stundenplan(null, null));
		gebeKlassenAuswahlVor(List.of(1L, 2L), ReportingAuswahlergebnis.aus(List.of(1L, 2L), Map.of(1L, mock(ReportingKlasse.class)),
				Map.of(2L, ReportingLadezustand.nichtVorhanden()), List.of()));

		assertDoesNotThrow(() -> initializer().init(), "Eine unbekannte ID unter mehreren beendet den Report nicht mehr.");

		verify(reportingContext).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_VORHANDEN), eq(ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN),
				eq(ReportingProblemSchluessel.fuer(ReportingKlasse.class, 2L)), anyString(), any());
	}

	@Test
	void testEineAuswahlOhneVerbleibendeDatensaetzeMeldetDieZulaessigLeereAusgabe() {
		gebeGeladenenStundenplanVor(stundenplan(null, null));
		gebeKlassenAuswahlVor(List.of(1L), ReportingAuswahlergebnis.aus(List.of(1L), Map.of(),
				Map.of(1L, ReportingLadezustand.nichtVorhanden()), List.of()));
		final HtmlContextInitializer initializer = initializer();

		assertDoesNotThrow(initializer::init);

		// Bleibt kein Datensatz übrig, rechtfertigt allein das gemeldete Kennzeichen eine Ausgabe ohne Dokument.
		verify(reportingContext).meldeAusgabeumfang(new ReportingAusgabeumfang(1, 0, true));
	}

	@Test
	void testEineLeereIdListeBleibtEinClientfehler() {
		gebeGeladenenStundenplanVor(stundenplan(null, null));
		gebeKlassenAuswahlVor(List.of(), ReportingAuswahlergebnis.aus(List.of(), Map.of(), Map.of(), List.of()));

		final HtmlContextInitializer initializer = initializer();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, initializer::init);

		assertEquals(Status.BAD_REQUEST, aoe.getStatus(), "Eine im Request leere ID-Liste fordert nichts an; das bleibt ein Clientfehler.");
		assertEquals("### FEHLER: Es wurden keine Klassen ausgewählt. Für die Ausgabe ist mindestens ein Datensatz auszuwählen.",
				aoe.getBody(), "Der Anwender wählt Klassen aus, keine IDs.");
	}

	@Test
	void testDieRaumAuswahlLaesstUnbekannteRaumIdsAus() {
		final ReportingStundenplanungRaum raum = new ReportingStundenplanungRaum("", 1L, null, null, 30, "R1");
		final ReportingStundenplanungStundenplan stundenplan = stundenplan(List.of(raum), null);

		final ReportingAuswahlergebnis<?> auswahl = auswahlDerSichtweise(ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM, stundenplan,
				List.of(1L, 99L));

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
		assertEquals(Set.of(99L), auswahl.ausgelassen().keySet());
		assertEquals(ReportingProblemursache.NICHT_VORHANDEN, auswahl.ausgelassen().get(99L).ursache(),
				"Ein Raum, den der Stundenplan nicht führt, ist kein Ladefehler.");
	}

	@Test
	void testDieFachAuswahlLaesstUnbekannteFachIdsAus() {
		final ReportingSchuljahresabschnitt schuljahresabschnitt = new ReportingSchuljahresabschnitt(11L, 2026, 1, null, null, null, null,
				Map.of(1L, mock(ReportingFach.class)), null, null, null, null);
		final ReportingStundenplanungStundenplan stundenplan = stundenplan(null, schuljahresabschnitt);

		final ReportingAuswahlergebnis<?> auswahl = auswahlDerSichtweise(ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH, stundenplan,
				List.of(1L, 99L));

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
		assertEquals(Set.of(99L), auswahl.ausgelassen().keySet());
	}

	@Test
	void testDieFachAuswahlOhneSchuljahresabschnittLaesstAlleIdsAus() {
		// Ein Stundenplan ohne aufgelösten Schuljahresabschnitt kennt keine Fächer. Die Auswahl bleibt bewusst leer und wird gemeldet, statt dass der
		// Aufbau an der fehlenden Referenz scheitert.
		final ReportingStundenplanungStundenplan stundenplan = stundenplan(null, null);

		final ReportingAuswahlergebnis<?> auswahl = auswahlDerSichtweise(ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH, stundenplan, List.of(1L));

		assertTrue(auswahl.idsAusgewaehlt().isEmpty());
		assertTrue(auswahl.bewusstLeer());
	}

}
