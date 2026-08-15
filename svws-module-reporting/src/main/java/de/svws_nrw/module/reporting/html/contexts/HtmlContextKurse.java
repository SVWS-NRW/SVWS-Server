package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;


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
		erzeugeContextSortiert("Kurse", reportingKurse, ReportingKurs.SORTIERUNG, ReportingKurs.class);
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
