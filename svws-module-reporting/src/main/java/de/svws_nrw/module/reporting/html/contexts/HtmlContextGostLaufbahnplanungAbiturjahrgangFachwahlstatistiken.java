package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ProxyReportingGostFachwahlstatistikenAbiturjahrgang;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ReportingGostFachwahlstatistik;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ReportingGostFachwahlstatistikenAbiturjahrgang;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ReportingGostFachwahlstatistikHalbjahr;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "GostFachwahlstatistik", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken extends HtmlContext<Object> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingContext    Context mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(final ReportingContext reportingContext) {
		super(reportingContext);
		erzeugeContext();
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 */
	private void erzeugeContext() {

		// In den idsHauptdaten der Reporting-Parameter werden das Abiturjahr und evtl. GostHalbjahres-IDs (0 = EF.1 bis 5 = Q2.2) übergeben.
		// In den idsDetails der Reporting-Parameter liegen die Ids der Fächer.
		// Hier werden die Daten NICHT validiert. Die Daten aus den Parametern müssen vorab validiert worden sein (HtmlFactory).
		final int abiturjahr = reportingContext.reportingParameter().idsHauptdaten().stream().filter(Objects::nonNull).toList().getFirst().intValue();
		final List<Long> idsFaecher = reportingContext.reportingParameter().idsDetaildaten().stream().filter(Objects::nonNull).toList();
		final List<Integer> idsGostHalbjahre = new ArrayList<>();

		for (int i = 1; i < reportingContext.reportingParameter().idsHauptdaten().size(); i = i + 1) {
			idsGostHalbjahre.add(reportingContext.reportingParameter().idsHauptdaten().get(i).intValue());
		}
		// Sind keine GostHalbjahre angegeben, so erfolgt die Ausgabe aller Halbjahre. Ergänze sie daher in der Liste
		if (idsGostHalbjahre.isEmpty()) {
			Arrays.stream(GostHalbjahr.values()).forEach(ghj -> idsGostHalbjahre.add(ghj.id));
		}

		// Objekt mit allen Fachwahlstatistiken als Basis für die Inhalte des Contexts erzeugen.
		final ReportingGostFachwahlstatistikenAbiturjahrgang proxyReportingGostFachwahlstatistikenAbiturjahrgang =
				new ProxyReportingGostFachwahlstatistikenAbiturjahrgang(this.reportingContext, abiturjahr);

		// Objekte mit verschiedenen Fachwahlstatistiken für den Context erzeugen.
		final List<ReportingGostFachwahlstatistik> fachwahlstatistiken =
				proxyReportingGostFachwahlstatistikenAbiturjahrgang.fachwahlstatistiken();
		final List<ReportingGostFachwahlstatistik> fachwahlstatistikenAuswahlNachFaechern =
				proxyReportingGostFachwahlstatistikenAbiturjahrgang.fachwahlstatistikenByIds(idsFaecher);
		final List<ReportingGostFachwahlstatistikHalbjahr> fachwahlstatistikenAuswahlNachFaechernHalbjahren =
				proxyReportingGostFachwahlstatistikenAbiturjahrgang.fachwahlstatistikenHalbjahreByIds(idsFaecher, idsGostHalbjahre);

		// Die Zähleinheit dieses Datenaufbaus sind die Fächer, über die die Vorlage iteriert - und das ist die vollständige Liste. Die beiden nach Fächern
		// eingeschränkten Listen stehen zwar im Context, werden von keiner Vorlage gelesen; aus ihnen gezählt, meldete der Header eine Auswahl, während die
		// Ausgabe alle Fächer zeigt. Die Zahlen entstehen erst hier und werden deshalb hier gemeldet.
		final int anzahlFaecher = fachwahlstatistiken.size();
		reportingContext.meldeAusgabeumfang(new ReportingAusgabeumfang(anzahlFaecher, anzahlFaecher, anzahlFaecher == 0));

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
