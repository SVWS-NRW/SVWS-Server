package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Klassen", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextKlassen extends HtmlContext<ReportingKlasse> implements HtmlContextAufteilbar<HtmlContextKlassen> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Klassen.
	 *
	 * @param reportingContext 	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingKlassen		Liste der Klassen, die berücksichtigt werden sollen.
	 */
	public HtmlContextKlassen(final ReportingContext reportingContext, final List<ReportingKlasse> reportingKlassen) {
		super(reportingContext);
		setContextData(reportingKlassen);

		// Das Thymeleaf-Plugin von IntelliJ löst die Variablennamen in den Vorlagen nur auf, wenn es den Namen als String-Literal am setVariable-Aufruf
		// findet und den Typ aus den dort übergebenen Daten ablesen kann. Deshalb steht der Aufruf hier und nicht generisch in der Basisklasse:
		// So weiß die IDE, dass ${Klassen} eine List<ReportingKlasse> ist.
		final Context context = new Context();
		context.setVariable("Klassen", getContextData());
		setContext(context);
	}

	/**
	 * Teile diesen Context mit allen Klassen in eine Liste von Contexts auf, die jeweils eine Klasse enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextKlassen> getEinzelContexts() {
		final List<HtmlContextKlassen> resultContexts = new ArrayList<>();

		for (final ReportingKlasse reportingKlasse : getContextData()) {
			final List<ReportingKlasse> eineKlasse = new ArrayList<>();
			eineKlasse.add(reportingKlasse);
			resultContexts.add(new HtmlContextKlassen(this.reportingContext, eineKlasse));
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
		return getContextData().stream().map(ReportingKlasse::id).toList();
	}
}
