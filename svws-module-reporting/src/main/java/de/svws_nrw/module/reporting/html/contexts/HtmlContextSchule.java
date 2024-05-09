package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import org.thymeleaf.context.Context;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "Schule", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextSchule extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 * @param reportingRepository	Das Repository der Schuldatenbank.
	 */
	public HtmlContextSchule(final ReportingRepository reportingRepository) {
		erzeugeContext(reportingRepository);
	}


	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *  @param reportingRepository	Das Repository der Schuldatenbank.
	 */
	private void erzeugeContext(final ReportingRepository reportingRepository) {
		final Context context = new Context();

		context.setVariable("Schule", new ProxyReportingSchule(reportingRepository));
		context.setVariable("Parameter", reportingRepository.reportingParameter());

		super.setContext(context);
	}

}
