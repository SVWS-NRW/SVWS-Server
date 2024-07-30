package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

		// In den idsHauptdaten der Reporting-Parameter werden im Wechsel das Abiturjahr und des GostHlabjahr (0 = EF.1 bis 5 = Q2.2) übergeben.
		// Hier werden die Daten NICHT valdiert. Die Daten aus den Paramatern müssen vorab validiert worden sein (ReportingValidierung).
		final List<Long> parameterDaten = reportingRepository.reportingParameter().idsHauptdaten.stream().filter(Objects::nonNull).toList();
		final List<Integer> abiturjahrgaenge = new ArrayList<>();
		final List<Integer> gostHalbjahre = new ArrayList<>();

		for (int i = 0; i < parameterDaten.size(); i = i + 2) {
			abiturjahrgaenge.add(Math.toIntExact(parameterDaten.get(i)));
			gostHalbjahre.add(Math.toIntExact(parameterDaten.get(i + 1)));
		}

		// TODO: Parameter von DataGostKlausuren.getAllData sollten eine Liste fassen können, statt einem einzigen Jahrgang.
		final GostKlausurenMetaDataCollection allData = DataGostKlausuren.getAllData(reportingRepository.conn(), abiturjahrgaenge.getFirst(),
				GostHalbjahr.fromID(gostHalbjahre.getFirst()));
		final GostKursklausurManager gostKlausurManager = new GostKursklausurManager(allData);

		final ReportingGostKlausurplanungKlausurplan gostKlausurplan =
				new ProxyReportingGostKlausurplanungKlausurplan(reportingRepository, gostKlausurManager);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("GostKlausurplan", gostKlausurplan);

		super.setContext(context);
	}
}
