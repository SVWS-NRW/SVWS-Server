package de.svws_nrw.module.reporting.html.contexts;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.reporting.ReportingVorlageParameter;
import de.svws_nrw.core.types.reporting.ReportingVorlageParameterTyp;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingVorlageParameterTypisiert;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingSchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Schule" und den Parametern zum Druck, um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextBasisdaten extends HtmlContext<Object> {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** In der Map werden zum Vorlage-Parameter die jeweiligen Werte gespeichert. */
	@JsonIgnore
	private final Map<String, Object> vorlageParameterWerte = new HashMap<>();


	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException Im Falle eines Fehlers beim Erzeugen des Contexts.
	 */
	public HtmlContextBasisdaten(final ReportingRepository reportingRepository) throws ApiOperationException {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContext();
	}

	/**
	 * Erzeugt den Context für die Schule mit ihren Stammdaten und zusätzlichen einen Context mit den Druckparametern.
	 *
	 * @throws ApiOperationException Im Falle eines Fehlers beim Erzeugen des Contexts.
	 */
	private void erzeugeContext() throws ApiOperationException {
		final Context context = new Context();

		context.setVariable("Schule", new ProxyReportingSchule(this.reportingRepository));
		context.setVariable("Benutzer", new ProxyReportingBenutzer(this.reportingRepository));
		context.setVariable("Parameter", this.reportingRepository.reportingParameter());

		// Baue die HashMap mit den übergebenen Vorlage-Parameter-Namen und ihren Werten auf, damit diese in den Templates direkt genutzt werden können.
		if ((this.reportingRepository.reportingParameter() != null) && (this.reportingRepository.reportingParameter().vorlageParameter() != null)) {
			for (final ReportingVorlageParameter reportingVorlageParameter : this.reportingRepository.reportingParameter().vorlageParameter()) {
				final ReportingVorlageParameterTypisiert<?> typisiert = erstelleTypisiertenParameter(reportingVorlageParameter);
				this.vorlageParameterWerte.put(typisiert.getName(), typisiert.getWert());
			}
		}
		context.setVariable("VorlageParameter", this.vorlageParameterWerte);

		super.setContext(context);
	}

	/**
	 * Erstellt einen typisierten Parameter basierend auf dem übergebenen ReportingVorlageParameter.
	 * Der Typ wird auf Grundlage des {@link ReportingVorlageParameterTyp} festgelegt.
	 * Bei einem Fehler während der Typisierung wird als Fallback ein String-Typ verwendet.
	 *
	 * @param reportingVorlageParameter der Vorlage-Parameter, aus dem der typisierte Parameter erstellt wird
	 *
	 * @return Ein Objekt der Klasse ReportingVorlageParameterTypisiert mit dem entsprechenden Typ
	 *         basierend auf dem Typ des übergebenen Parameters
	 *
	 * @throws ApiOperationException Im Falle eines Konvertierungsfehlers.
	 */
	private ReportingVorlageParameterTypisiert<?> erstelleTypisiertenParameter(final ReportingVorlageParameter reportingVorlageParameter)
			throws ApiOperationException {
		try {
			return switch (ReportingVorlageParameterTyp.getByID(reportingVorlageParameter.typ)) {
				case BOOLEAN -> new ReportingVorlageParameterTypisiert<Boolean>(reportingVorlageParameter);
				case INTEGER -> new ReportingVorlageParameterTypisiert<Integer>(reportingVorlageParameter);
				case LONG -> new ReportingVorlageParameterTypisiert<Long>(reportingVorlageParameter);
				case DECIMAL -> new ReportingVorlageParameterTypisiert<Double>(reportingVorlageParameter);
				case STRING -> new ReportingVorlageParameterTypisiert<String>(reportingVorlageParameter);
				default -> throw new ApiOperationException(Response.Status.BAD_REQUEST,
						"Ungültiger Typ für den Reporting-Vorlage-Parameter " + reportingVorlageParameter.name + " übergeben.");
			};
		} catch (final Exception e) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e, "Fehler bei der Typisierung der Vorlage-Parameter aufgetreten.");
		}
	}
}
