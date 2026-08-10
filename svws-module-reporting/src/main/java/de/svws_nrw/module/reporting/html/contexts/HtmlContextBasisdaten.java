package de.svws_nrw.module.reporting.html.contexts;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingVorlageParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Schule" und den Parametern zum Druck, um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextBasisdaten extends HtmlContext<Object> {

	/** In der Map werden zum Vorlage-Parameter die jeweiligen Werte gespeichert. */
	@JsonIgnore
	private final Map<String, Object> reportvorlageParameterWerte = new HashMap<>();


	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException Im Falle eines Fehlers beim Erzeugen des Contexts.
	 */
	public HtmlContextBasisdaten(final ReportingContext reportingContext) throws ApiOperationException {
		super(reportingContext);
		erzeugeContext();
	}

	/**
	 * Erzeugt den Context für die Schule mit ihren Stammdaten und zusätzlichen einen Context mit den Druckparametern.
	 *
	 * @throws ApiOperationException Im Falle eines Fehlers beim Erzeugen des Contexts.
	 */
	private void erzeugeContext() throws ApiOperationException {
		final Context context = new Context();

		context.setVariable("Schule", this.reportingContext.repositorySchule().schule());
		context.setVariable("Benutzer", this.reportingContext.benutzer());
		context.setVariable("Parameter", this.reportingContext.reportingParameter());

		// Baue die HashMap mit den übergebenen Vorlage-Parameter-Namen und ihren Werten auf, damit diese in den Templates direkt genutzt werden können.
		if ((this.reportingContext.reportingParameter() != null) && (this.reportingContext.reportingParameter().reportvorlageParameter() != null)) {
			for (final ReportingReportvorlageParameter reportingReportVorlageParameter : this.reportingContext.reportingParameter()
					.reportvorlageParameter()) {
				final ReportingVorlageParameterTypisiert<?> typisiert = erstelleTypisiertenParameter(reportingReportVorlageParameter);
				this.reportvorlageParameterWerte.put(typisiert.getName(), typisiert.getWert());
			}
		}
		context.setVariable("VorlageParameter", this.reportvorlageParameterWerte);

		super.setContext(context);
	}

	/**
	 * Erstellt einen typisierten Parameter basierend auf dem übergebenen ReportingVorlageParameter.
	 * Der Typ wird auf Grundlage des {@link ReportingReportvorlageParameterTyp} festgelegt.
	 * Bei einem Fehler während der Typisierung wird als Fallback ein String-Typ verwendet.
	 *
	 * @param reportingReportVorlageParameter der Vorlage-Parameter, aus dem der typisierte Parameter erstellt wird
	 *
	 * @return Ein Objekt der Klasse ReportingVorlageParameterTypisiert mit dem entsprechenden Typ
	 *         basierend auf dem Typ des übergebenen Parameters
	 *
	 * @throws ApiOperationException Im Falle eines Konvertierungsfehlers. Der Typ eines Vorlage-Parameters stammt aus der SOLL-Struktur der Reportvorlage;
	 *                               der Request liefert allein die Werte. Ein nicht auflösbarer Typ ist damit ein Katalogfehler und
	 *                               {@code INTERNAL_SERVER_ERROR}.
	 */
	private ReportingVorlageParameterTypisiert<?> erstelleTypisiertenParameter(final ReportingReportvorlageParameter reportingReportVorlageParameter)
			throws ApiOperationException {
		try {
			return switch (ReportingReportvorlageParameterTyp.getByID(reportingReportVorlageParameter.typ)) {
				case BOOLEAN, INTEGER, LONG, DECIMAL, STRING -> new ReportingVorlageParameterTypisiert<>(reportingReportVorlageParameter);
				default -> throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
						"Der Reporting-Vorlage-Parameter " + reportingReportVorlageParameter.name + " besitzt keinen auflösbaren Typ.");
			};
		} catch (final ApiOperationException e) {
			// Die Meldung oben benennt den betroffenen Parameter; der allgemeine Catch ersetzt sie sonst durch eine Angabe ohne Namen.
			throw e;
		} catch (final Exception e) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e, "Fehler bei der Typisierung der Vorlage-Parameter aufgetreten.");
		}
	}
}
