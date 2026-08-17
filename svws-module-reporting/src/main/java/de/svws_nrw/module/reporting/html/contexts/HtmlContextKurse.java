package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Kurse", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextKurse extends HtmlContext<ReportingKurs> implements HtmlContextAufteilbar<HtmlContextKurse> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Kursen.
	 *
	 * @param reportingContext 	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingKurse		Liste der Kurse, die berücksichtigt werden sollen.
	 */
	public HtmlContextKurse(final ReportingContext reportingContext, final List<ReportingKurs> reportingKurse) {
		super(reportingContext);
		// Die übergebene Liste wird gemäß der für ReportingKurs konfigurierten Sortierung (benutzerdefiniert oder Standard) sortiert,
		// damit beide Konstruktor-Pfade dasselbe Ordnungsverhalten zeigen.
		setContextDataSortiert(reportingKurse, ReportingKurs.SORTIERUNG, ReportingKurs.class);

		// Das Thymeleaf-Plugin von IntelliJ löst die Variablennamen in den Vorlagen nur auf, wenn es den Namen als String-Literal am setVariable-Aufruf
		// findet und den Typ aus den dort übergebenen Daten ablesen kann. Deshalb steht der Aufruf hier und nicht generisch in der Basisklasse:
		// So weiß die IDE, dass ${Kurse} eine List<ReportingKurs> ist.
		final Context context = new Context();
		context.setVariable("Kurse", getContextData());
		setContext(context);
	}

	/**
	 * Teile diesen Context mit allen Kursen in eine Liste von Contexts auf, die jeweils einen Kurs enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextKurse> getEinzelContexts() {
		final List<HtmlContextKurse> resultContexts = new ArrayList<>();

		for (final ReportingKurs reportingKurs : getContextData()) {
			final List<ReportingKurs> einKurs = new ArrayList<>();
			einKurs.add(reportingKurs);
			resultContexts.add(new HtmlContextKurse(this.reportingContext, einKurs));
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
		return getContextData().stream().map(ReportingKurs::id).toList();
	}
}
