package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenMetaDataCollection;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import org.thymeleaf.context.Context;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKlausurplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKlausurplanungKlausurplan extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public HtmlContextGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository) throws ApiOperationException {
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

		// TODO: Parameter von DataGostKlausuren.getAllData richtig setzen (am besten ausgewählter Schuljahresabschnitt). Siehe dann auch Validierung.
		//  Begründung: Die Methode DataGostKlausuren.getAllData wertet die zwei Parameter Abiturjahr und GOSt-Halbjahr nicht aus und es werden alle
		//  Klausurdaten zurückgegeben.
		final GostKlausurenMetaDataCollection allData = DataGostKlausuren.getAllData(reportingRepository.conn(), 0, GostHalbjahr.EF1);
		final GostKursklausurManager gostKlausurManager = new GostKursklausurManager(allData);

		final ReportingGostKlausurplanungKlausurplan gostKlausurplan =
				new ProxyReportingGostKlausurplanungKlausurplan(reportingRepository, gostKlausurManager);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("GostKlausurplan", gostKlausurplan);

		super.setContext(context);
	}
}
