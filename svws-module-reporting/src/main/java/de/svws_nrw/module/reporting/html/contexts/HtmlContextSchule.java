package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingBenutzer;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Schule" und den Parametern zum Druck, um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextSchule extends HtmlContext<Object> {

 @Override
	public List<String> standardsortierung() {
		return new ArrayList<>();
	}

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextSchule(final ReportingRepository reportingRepository) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContext();
	}

	/**
	 * Erzeugt den Context für die Schule mit ihren Stammdaten und zusätzlichen einen Context mit den Druckparametern.
	 */
	private void erzeugeContext() {
		final Context context = new Context();

		context.setVariable("Schule", new ProxyReportingSchule(this.reportingRepository));
		context.setVariable("Benutzer", new ProxyReportingBenutzer(this.reportingRepository));
		context.setVariable("Parameter", this.reportingRepository.reportingParameter());

		super.setContext(context);
	}
}
