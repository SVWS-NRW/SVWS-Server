package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Lehrer", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextLehrer extends HtmlContext<ReportingLehrer> implements HtmlContextAufteilbar<HtmlContextLehrer> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Lehrern.
	 *
	 * @param reportingContext 	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingLehrer		Liste der Lehrer, die berücksichtigt werden sollen.
	 */
	public HtmlContextLehrer(final ReportingContext reportingContext, final List<ReportingLehrer> reportingLehrer) {
		super(reportingContext);
		erzeugeContextFromLehrer(reportingLehrer);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Lehrer-IDs.
	 *
	 * @param reportingContext   Context mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextLehrer(final ReportingContext reportingContext) {
		super(reportingContext);
		erzeugeContextFromIds(this.reportingContext.reportingParameter().idsHauptdaten());
	}


	/**
	 * Erzeugt den Context aus einer Liste von Lehrern.
	 *
	 * @param reportingLehrer   	Liste der Lehrer, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromLehrer(final List<ReportingLehrer> reportingLehrer) {

		setContextData(reportingLehrer);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Lehrer", getContextData());

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Lehrer-IDs.
	 *
	 * @param idsLehrer	Liste der IDs der Lehrer, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromIds(final List<Long> idsLehrer) {

		setContextData(this.reportingContext.repositoryLehrer().lehrer(idsLehrer));

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Lehrer", getContextData());

		super.setContext(context);
	}

	/**
	 * Teile diesen Context mit allen Lehrern in eine Liste von Contexts auf, die jeweils einen Lehrer enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextLehrer> getEinzelContexts() {
		final List<HtmlContextLehrer> resultContexts = new ArrayList<>();

		for (final ReportingLehrer reportingLehrer : getContextData()) {
			final List<ReportingLehrer> einLehrer = new ArrayList<>();
			einLehrer.add(reportingLehrer);
			resultContexts.add(new HtmlContextLehrer(this.reportingContext, einLehrer));
		}

		return resultContexts;
	}

	/**
	 * Liefert die IDs der Context.
	 *
	 * @return Liste der IDs der Context-Daten.
	 */
	@Override
	public List<Long> getIds() {
		return getContextData().stream().map(ReportingLehrer::id).toList();
	}
}
