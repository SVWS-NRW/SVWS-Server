package de.svws_nrw.module.reporting.html.contexts;

import org.thymeleaf.context.Context;

import de.svws_nrw.module.reporting.html.base.HtmlContext;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "Schule", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextSchule extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Das Repository der Schuldatenbank.
	 */
	public HtmlContextSchule(final ReportingRepository reportingRepository) {
		erzeugeContext(reportingRepository);
	}


	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 *  @param reportingRepository	Das Repository der Schuldatenbank.
	 */
	private void erzeugeContext(final ReportingRepository reportingRepository) {
		final Context context = new Context();

		final ProxyReportingSchule proxyReportingSchule = new ProxyReportingSchule(reportingRepository);
		context.setVariable("Schule", proxyReportingSchule);

		super.setContext(context);
	}

}
