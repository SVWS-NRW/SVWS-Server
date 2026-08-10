package de.svws_nrw.module.reporting.html.contexts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Typisierung der Vorlage-Parameter in {@link HtmlContextBasisdaten}.
 * <p>Den Typ eines Vorlage-Parameters gibt die SOLL-Struktur der Reportvorlage vor; aus dem Request stammen allein die Werte, die der
 * {@code ReportingParameterBuilder} über den Namen zuordnet. Ein nicht auflösbarer Typ ist damit ein Fehler im Katalog und muss als
 * {@code INTERNAL_SERVER_ERROR} mit dem Namen des betroffenen Parameters gemeldet werden.</p>
 * <p>Der {@link ReportingContext} ist gemockt, weil sein einziger Konstruktor alle Repositories aufbaut und dafür eine Datenbankverbindung benötigt.</p>
 */
class TestHtmlContextBasisdaten {

	/** Der gemockte Context, den der Basisdaten-Context in den Tests erhält. */
	private ReportingContext reportingContext;

	/** Die gemockten Reporting-Parameter, die die Vorlage-Parameter tragen. */
	private ReportingParameterTypisiert reportingParameter;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingContext.repositorySchule()).thenReturn(mock(ReportingRepositorySchule.class));
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
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
		assertEquals("Der Reporting-Vorlage-Parameter Zeugnisdatum besitzt keinen auflösbaren Typ.", aoe.getBody(),
				"Die Meldung muss den betroffenen Parameter benennen und darf nicht von der allgemeinen Fehlerbehandlung ersetzt werden.");
	}

	@Test
	void testEinUnbekannterParametertypIstEinInternerFehler() {
		when(reportingParameter.reportvorlageParameter()).thenReturn(List.of(vorlageParameter("Zeugnisdatum", 4711)));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> new HtmlContextBasisdaten(reportingContext));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
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
