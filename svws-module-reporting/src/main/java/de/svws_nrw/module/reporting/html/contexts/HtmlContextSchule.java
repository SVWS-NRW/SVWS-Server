package de.svws_nrw.module.reporting.html.contexts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Schule" und den Parametern zum Druck, um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextSchule extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextSchule(final ReportingRepository reportingRepository) {
		this.reportingRepository = reportingRepository;
		erzeugeContext();
	}

	/**
	 * Erzeugt den Context für die Schule mit ihren Stammdaten und zusätzlichen einen Context mit den Druckparametern.
	 */
	private void erzeugeContext() {
		final Context context = new Context();

		context.setVariable("Schule", new ProxyReportingSchule(this.reportingRepository));
		context.setVariable("Parameter", this.reportingRepository.reportingParameter());

		super.setContext(context);
	}
}
