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
		setContextData(reportingLehrer);

		// Das Thymeleaf-Plugin von IntelliJ löst die Variablennamen in den Vorlagen nur auf, wenn es den Namen als String-Literal am setVariable-Aufruf
		// findet und den Typ aus den dort übergebenen Daten ablesen kann. Deshalb steht der Aufruf hier und nicht generisch in der Basisklasse:
		// So weiß die IDE, dass ${Lehrer} eine List<ReportingLehrer> ist.
		final Context context = new Context();
		context.setVariable("Lehrer", getContextData());
		setContext(context);
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
