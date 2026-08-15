package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungFachStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "Stundenplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextStundenplanungFachStundenplan extends HtmlContext<ReportingStundenplanungFachStundenplan>
		implements HtmlContextAufteilbar<HtmlContextStundenplanungFachStundenplan> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param stundenplan           Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe 		    Eine Liste von Fach-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	public HtmlContextStundenplanungFachStundenplan(final ReportingContext reportingContext, final ReportingStundenplanungStundenplan stundenplan,
			final List<Long> idsAusgabe) {
		super(reportingContext);
		erzeugeContext(stundenplan, idsAusgabe);
	}

	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung.
	 *
	 * @param stundenplan Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe  Eine Liste von Fach-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	private void erzeugeContext(final ReportingStundenplanungStundenplan stundenplan, final List<Long> idsAusgabe) {

		final List<ReportingStundenplanungFachStundenplan> stundenplaene = new ArrayList<>();
		// Ohne aufgelösten Schuljahresabschnitt gibt es keine Fächer zum Stundenplan; die Ausgabe bleibt dann leer, statt mit einer NPE abzubrechen.
		final ReportingSchuljahresabschnitt schuljahresabschnitt = stundenplan.schuljahresabschnitt();
		if (schuljahresabschnitt != null) {
			schuljahresabschnitt.faecher(idsAusgabe)
					.forEach(fach -> stundenplaene.add(new ReportingStundenplanungFachStundenplan(fach, stundenplan)));
		}

		setContextDataSortiert(stundenplaene, ReportingStundenplanungFachStundenplan.SORTIERUNG, ReportingStundenplanungFachStundenplan.class);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("FaecherStundenplaene", getContextData());

		super.setContext(context);
	}

	/**
	 * Teile diesen Context in eine Liste von Contexts auf, die jeweils auf eine ID filtern. Damit können Ausgaben pro ID erzeugt werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextStundenplanungFachStundenplan> getEinzelContexts() {
		final List<HtmlContextStundenplanungFachStundenplan> resultContexts = new ArrayList<>();

		for (final ReportingStundenplanungFachStundenplan stundenplan : getContextData()) {
			final List<Long> eineId = new ArrayList<>();
			eineId.add(stundenplan.fach().id());
			resultContexts.add(new HtmlContextStundenplanungFachStundenplan(this.reportingContext, stundenplan.stundenplan(), eineId));
		}

		return resultContexts;
	}
}
