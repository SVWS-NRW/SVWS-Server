package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import jakarta.ws.rs.core.Response.Status;
import org.thymeleaf.context.Context;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKursplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKursplanungBlockungsergebnis extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public HtmlContextGostKursplanungBlockungsergebnis(final DBEntityManager conn, final ReportingRepository reportingRepository) throws ApiOperationException {
		erzeugeContext(conn, reportingRepository);
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void erzeugeContext(final DBEntityManager conn, final ReportingRepository reportingRepository) throws ApiOperationException {

		// ####### Daten validieren. Wirft eine Exception bei Fehlern, andernfalls werden die Manager für die Blockung erzeugt. ###############################

		if (conn == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Datenbankverbindung ungültig.");

		if (reportingRepository.reportingParameter().idsHauptdaten == null || reportingRepository.reportingParameter().idsHauptdaten.isEmpty() || reportingRepository.reportingParameter().idsHauptdaten.getFirst() == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Ungültige Blockungsergebnis-ID übergeben.");

		final Long idBlockungsergebnis = reportingRepository.reportingParameter().idsHauptdaten.getFirst();

		// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e, "Keine Schule oder Schule ohne GOSt gefunden.");
		}

		final ReportingGostKursplanungBlockungsergebnis blockungsergebnis = new ProxyReportingGostKursplanungBlockungsergebnis(reportingRepository, idBlockungsergebnis);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Blockungsergebnis", blockungsergebnis);
		context.setVariable("Parameter", reportingRepository.reportingParameter());

		super.setContext(context);
	}
}
