package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.proxytypes.gost.ProxyReportingGostFachwahlstatistikenAbiturjahrgang;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistik;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistikenAbiturjahrgang;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistikHalbjahr;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "GostFachwahlstatistik", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken extends HtmlContext<Object> {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository    Repository mit Parametern, Logger und Daten zum Reporting.
	 */
 	public HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(final ReportingRepository reportingRepository) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContext();
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 */
	private void erzeugeContext() {

		// In den idsHauptdaten der Reporting-Parameter werden das Abiturjahr und evtl. GostHalbjahres-IDs (0 = EF.1 bis 5 = Q2.2) übergeben.
		// In den idsDetails der Reporting-Parameter liegen die Ids der Fächer.
		// Hier werden die Daten NICHT validiert. Die Daten aus den Parametern müssen vorab validiert worden sein (ReportingValidierung).
		final int abiturjahr = reportingRepository.reportingParameter().idsHauptdaten.stream().filter(Objects::nonNull).toList().getFirst().intValue();
		final List<Long> idsFaecher = reportingRepository.reportingParameter().idsDetaildaten.stream().filter(Objects::nonNull).toList();
		final List<Integer> idsGostHalbjahre = new ArrayList<>();

		for (int i = 1; i < reportingRepository.reportingParameter().idsHauptdaten.size(); i = i + 1) {
			idsGostHalbjahre.add(reportingRepository.reportingParameter().idsHauptdaten.get(i).intValue());
		}
		// Sind keine GostHalbjahre angegeben, so erfolgt die Ausgabe aller Halbjahre. Ergänze sie daher in der Liste
		if (idsGostHalbjahre.isEmpty()) {
			Arrays.stream(GostHalbjahr.values()).forEach(ghj -> idsGostHalbjahre.add(ghj.id));
		}

		// Objekt mit allen Fachwahlstatistiken als Basis für die Inhalte des Contexts erzeugen.
		final ReportingGostFachwahlstatistikenAbiturjahrgang proxyReportingGostFachwahlstatistikenAbiturjahrgang =
				new ProxyReportingGostFachwahlstatistikenAbiturjahrgang(this.reportingRepository, abiturjahr);

		// Objekte mit verschiedenen Fachwahlstatistiken für den Context erzeugen.
		final List<ReportingGostFachwahlstatistik> fachwahlstatistiken =
				proxyReportingGostFachwahlstatistikenAbiturjahrgang.fachwahlstatistiken();
		final List<ReportingGostFachwahlstatistik> fachwahlstatistikenAuswahlNachFaechern =
				proxyReportingGostFachwahlstatistikenAbiturjahrgang.fachwahlstatistikenByIds(idsFaecher);
		final List<ReportingGostFachwahlstatistikHalbjahr> fachwahlstatistikenAuswahlNachFaechernHalbjahren =
				proxyReportingGostFachwahlstatistikenAbiturjahrgang.fachwahlstatistikenHalbjahreByIds(idsFaecher, idsGostHalbjahre);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();

		context.setVariable("GostLaufbahnplanungAbiturjahrgangFachwahlstatistikenAbiturjahr", abiturjahr);
		context.setVariable("GostLaufbahnplanungAbiturjahrgangFachwahlstatistiken", fachwahlstatistiken);
		context.setVariable("GostLaufbahnplanungAbiturjahrgangFachwahlstatistikenAuswahlNachFaechern", fachwahlstatistikenAuswahlNachFaechern);
		context.setVariable("GostLaufbahnplanungAbiturjahrgangFachwahlstatistikenAuswahlNachFaechernHalbjahren",
				fachwahlstatistikenAuswahlNachFaechernHalbjahren);

		super.setContext(context);
	}
}
