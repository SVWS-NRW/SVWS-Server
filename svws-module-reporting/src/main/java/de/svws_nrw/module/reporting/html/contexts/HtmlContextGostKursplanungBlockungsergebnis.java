package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import org.thymeleaf.context.Context;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKursplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKursplanungBlockungsergebnis extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public HtmlContextGostKursplanungBlockungsergebnis(final ReportingRepository reportingRepository) throws ApiOperationException {
		erzeugeContext(reportingRepository);
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void erzeugeContext(final ReportingRepository reportingRepository) throws ApiOperationException {

		final Long idBlockungsergebnis = reportingRepository.reportingParameter().idsHauptdaten.getFirst();

		final GostBlockungsergebnis blockungsergebnis = DataGostBlockungsergebnisse.getErgebnisFromID(reportingRepository.conn(), idBlockungsergebnis);
		final GostBlockungsdatenManager datenManager =
				DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(reportingRepository.conn(), blockungsergebnis.blockungID);

		final ReportingGostKursplanungBlockungsergebnis proxyReportingGostKursplanungBlockungsergebnis =
				new ProxyReportingGostKursplanungBlockungsergebnis(reportingRepository, blockungsergebnis, datenManager);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Blockungsergebnis", proxyReportingGostKursplanungBlockungsergebnis);

		super.setContext(context);
	}
}
