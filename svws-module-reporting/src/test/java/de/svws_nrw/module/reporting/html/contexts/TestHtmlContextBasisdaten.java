package de.svws_nrw.module.reporting.html.contexts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Typisierung der Vorlage-Parameter in {@link HtmlContextBasisdaten}.
 * <p>Den Typ eines Vorlage-Parameters gibt die SOLL-Struktur der Reportvorlage vor; aus dem Request stammen allein die Werte, die der
 * {@code ReportingParameterBuilder} über den Namen zuordnet. Ein nicht auflösbarer Typ ist damit ein Fehler im Katalog und wird als
 * {@code INTERNAL_SERVER_ERROR} gemeldet. Dasselbe gilt für einen Wert, der sich nicht in den Typ wandeln lässt: Er stammt aus der Vorlagendefinition
 * auf dem Server, denn Werte aus Request und Konfiguration prüft der {@code ReportingParameterBuilder} vorher auf Typkonformität. Welcher Parameter
 * betroffen ist, gehört ins Log und nicht in die Meldung: Der interne Name sagt dem Anwender nichts, und die Bezeichnung für die Oberfläche darf leer
 * sein.</p>
 * <p>Der {@link ReportingContext} ist gemockt, weil sein einziger Konstruktor alle Repositories aufbaut und dafür eine Datenbankverbindung benötigt.</p>
 */
class TestHtmlContextBasisdaten {

	/** Der gemockte Context, den der Basisdaten-Context in den Tests erhält. */
	private ReportingContext reportingContext;

	/** Die gemockten Reporting-Parameter, die die Vorlage-Parameter tragen. */
	private ReportingParameterTypisiert reportingParameter;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		reportingParameter = mock(ReportingParameterTypisiert.class);
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		when(reportingContext.logger()).thenReturn(logger);
		when(reportingContext.repositorySchule()).thenReturn(mock(ReportingRepositorySchule.class));
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
	}

	/**
	 * Gibt die Texte der Logeinträge des übergebenen Levels zurück, ohne die Einrückung des Loggers.
	 *
	 * @param level Das Level, dessen Einträge gesucht sind.
	 *
	 * @return Die Texte in der Reihenfolge ihres Auftretens.
	 */
	private List<String> eintraege(final LogLevel level) {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == level).map(eintrag -> eintrag.getText().strip()).toList();
	}


	/**
	 * Erzeugt einen Vorlage-Parameter mit dem angegebenen Namen und Typ.
	 *
	 * @param name Der Name des Parameters.
	 * @param typ  Die ID des Parametertyps.
	 *
	 * @return Der Vorlage-Parameter.
	 */
	private static ReportingReportvorlageParameter vorlageParameter(final String name, final int typ) {
		final ReportingReportvorlageParameter parameter = new ReportingReportvorlageParameter();
		parameter.name = name;
		parameter.typ = typ;
		parameter.wert = "Testwert";
		return parameter;
	}


	@Test
	void testEinNichtAufloesbarerParametertypIstEinInternerFehler() {
		when(reportingParameter.reportvorlageParameter())
				.thenReturn(List.of(vorlageParameter("Zeugnisdatum", ReportingReportvorlageParameterTyp.UNDEFINED.getId())));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> new HtmlContextBasisdaten(reportingContext));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("### FEHLER: Ein Vorlagenparameter hat keinen bekannten Typ.", aoe.getBody(),
				"Die eigene Meldung darf nicht von der allgemeinen Fehlerbehandlung ersetzt werden.");
		assertTrue(eintraege(LogLevel.ERROR).contains("Vorlagenparameter: Zeugnisdatum"),
				"Welcher Parameter betroffen ist, gehört ins Log: " + eintraege(LogLevel.ERROR));
	}

	@Test
	void testEinNichtLesbarerParameterwertIstEinInternerFehler() {
		// Der Wert stammt aus der Vorlagendefinition auf dem Server; Werte aus Request und Konfiguration prüft der ParameterBuilder vorher auf Typkonformität.
		final ReportingReportvorlageParameter parameter = vorlageParameter("Zeugnisdatum", ReportingReportvorlageParameterTyp.INTEGER.getId());
		parameter.wert = "kein Integer";
		when(reportingParameter.reportvorlageParameter()).thenReturn(List.of(parameter));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> new HtmlContextBasisdaten(reportingContext));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("### FEHLER: Ein Vorlagenparameter des Reports ist auf dem Server ungültig.", aoe.getBody(),
				"Die eigene Meldung darf nicht von der allgemeinen Fehlerbehandlung ersetzt werden.");
		assertTrue(eintraege(LogLevel.ERROR).contains("Vorlagenparameter: Zeugnisdatum"),
				"Welcher Parameter betroffen ist, gehört ins Log: " + eintraege(LogLevel.ERROR));
	}

	@Test
	void testEinUnbekannterParametertypIstEinInternerFehler() {
		// Eine ID außerhalb des Enums löst getByID zu UNDEFINED auf und läuft in denselben Zweig; der Name steht trotzdem im Log.
		when(reportingParameter.reportvorlageParameter()).thenReturn(List.of(vorlageParameter("Zeugnisdatum", 4711)));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> new HtmlContextBasisdaten(reportingContext));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("### FEHLER: Ein Vorlagenparameter hat keinen bekannten Typ.", aoe.getBody());
		assertTrue(eintraege(LogLevel.ERROR).contains("Vorlagenparameter: Zeugnisdatum"),
				"Welcher Parameter betroffen ist, gehört ins Log: " + eintraege(LogLevel.ERROR));
	}

	@Test
	void testEinGueltigerParametertypWirdTypisiert() {
		when(reportingParameter.reportvorlageParameter())
				.thenReturn(List.of(vorlageParameter("Zeugnisdatum", ReportingReportvorlageParameterTyp.STRING.getId())));

		assertDoesNotThrow(() -> new HtmlContextBasisdaten(reportingContext));
	}

	@Test
	void testOhneVorlageParameterEntstehtDerContextFehlerfrei() {
		when(reportingParameter.reportvorlageParameter()).thenReturn(List.of());

		assertDoesNotThrow(() -> new HtmlContextBasisdaten(reportingContext));
	}

}
