package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerLeistungsdatenMatrix;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdatenMatrix;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Schüler", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextSchueler extends HtmlContext<ReportingSchueler> implements HtmlContextAufteilbar<HtmlContextSchueler> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Schülern.
	 *
	 * @param reportingContext 	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingSchueler		Liste der Schüler, die berücksichtigt werden sollen.
	 */
	public HtmlContextSchueler(final ReportingContext reportingContext, final List<ReportingSchueler> reportingSchueler) {
		super(reportingContext);
		setContextData(reportingSchueler);

		// Das Thymeleaf-Plugin von IntelliJ löst die Variablennamen in den Vorlagen nur auf, wenn es den Namen als String-Literal am setVariable-Aufruf
		// findet und den Typ aus den dort übergebenen Daten ablesen kann. Deshalb steht der Aufruf hier und nicht generisch in der Basisklasse:
		// So weiß die IDE, dass ${Schueler} eine List<ReportingSchueler> ist.
		final Context context = new Context();
		context.setVariable("Schueler", getContextData());
		setContext(context);
	}

	/**
	 * Erstellt eine Leistungsdaten-Matrix für die Schüler in diesem Context basierend auf dem ausgewählten Schuljahresabschnitt.
	 *
	 * @return Die Leistungsdaten-Matrix für die Schüler dieses Contexts.
	 */
	public ReportingSchuelerLeistungsdatenMatrix schuelerLeistungsdatenMatrix() {
		return new ProxyReportingSchuelerLeistungsdatenMatrix(this.reportingContext, this.getContextData(),
				this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt());
	}

	/**
	 * Teile diesen Context mit allen Schülern in eine Liste von Contexts auf, die jeweils einen Schüler enthalten, um eine schülerbezogene Ausgabe zu
	 * ermöglichen.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextSchueler> getEinzelContexts() {
		final List<HtmlContextSchueler> resultContexts = new ArrayList<>();

		for (final ReportingSchueler reportingSchueler : getContextData()) {
			final List<ReportingSchueler> einSchueler = new ArrayList<>();
			einSchueler.add(reportingSchueler);
			resultContexts.add(new HtmlContextSchueler(this.reportingContext, einSchueler));
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
		return getContextData().stream().map(ReportingSchueler::id).toList();
	}
}
