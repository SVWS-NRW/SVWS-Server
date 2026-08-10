package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryStundenplan;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Statuszuordnung des Initializers, für den der Stundenplan das angeforderte Hauptdatum ist.
 * <p>Zwei Ursachen führen im Repository zu demselben Ergebnis {@code null} beziehungsweise zu einem Wurf, dürfen am API-Rand aber nicht denselben Status
 * ergeben: Eine ID, zu der es keine Definition gibt, ist {@code NOT_FOUND}; nicht ladbare Definitionen sind ein Serverproblem.</p>
 * <p>Der {@link ReportingContext} wird gemockt, weil sein Konstruktor sämtliche Repositories aufbaut und dafür eine Datenbankverbindung benötigt. Das
 * Repository der Stundenpläne ist dagegen <b>echt</b> - gerade sein Zusammenspiel mit dem Initializer ist Gegenstand des Tests.</p>
 */
class TestHtmlContextInitializerStundenplan {

	/** Die ID, unter der ein Stundenplan angefordert wird. */
	private static final long ID_STUNDENPLAN = 42L;

	/** Der gemockte Context, den der Initializer erhält. */
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

		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
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
		verdrahteRepository(null, new IllegalStateException("Die Verbindung zur Datenbank wurde unterbrochen."));
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
	 * Gibt die Texte der Log-Einträge des übergebenen Levels zurück.
	 *
	 * @param level Das Level, dessen Einträge gesucht sind.
	 *
	 * @return Die Texte der Einträge, ohne die Einrückung des Loggers.
	 */
	private List<String> eintraege(final LogLevel level) {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == level).map(eintrag -> eintrag.getText().strip()).toList();
	}


	@Test
	void testEineNichtVorhandeneStundenplanIdErgibtNotFound() {
		repositoryOhneDefinitionen();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> initializer().init());

		assertEquals(Status.NOT_FOUND, aoe.getStatus(), "Der einzeln adressierte Stundenplan existiert nicht; das ist kein Serverfehler.");
	}

	@Test
	void testNichtLadbareDefinitionenErgebenEinenServerfehler() {
		repositoryMitLadefehler();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> initializer().init());

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(),
				"Ein technischer Ladefehler darf nicht als „Stundenplan existiert nicht“ beim Client ankommen.");
		assertTrue(eintraege(LogLevel.ERROR).stream().anyMatch(eintrag -> eintrag.contains("Stundenplandefinitionen")),
				"Die Quelle des Fehlers benennt das Repository: " + eintraege(LogLevel.ERROR));
	}

}
