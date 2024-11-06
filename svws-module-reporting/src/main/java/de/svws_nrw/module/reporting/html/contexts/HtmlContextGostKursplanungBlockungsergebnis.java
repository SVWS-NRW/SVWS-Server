package de.svws_nrw.module.reporting.html.contexts;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "GostKursplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextGostKursplanungBlockungsergebnis extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository		Repository mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextGostKursplanungBlockungsergebnis(final ReportingRepository reportingRepository) throws ApiOperationException {
		this.reportingRepository = reportingRepository;
		erzeugeContext();
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void erzeugeContext() throws ApiOperationException {

		final Long idBlockungsergebnis = this.reportingRepository.reportingParameter().idsHauptdaten.getFirst();

		final GostBlockungsergebnis blockungsergebnis = DataGostBlockungsergebnisse.getErgebnisFromID(this.reportingRepository.conn(), idBlockungsergebnis);
		final GostBlockungsdatenManager datenManager =
				DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(this.reportingRepository.conn(), blockungsergebnis.blockungID);

		final ReportingGostKursplanungBlockungsergebnis proxyReportingGostKursplanungBlockungsergebnis =
				new ProxyReportingGostKursplanungBlockungsergebnis(this.reportingRepository, blockungsergebnis, datenManager);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("GostBlockungsergebnis", proxyReportingGostKursplanungBlockungsergebnis);

		super.setContext(context);
	}
}
