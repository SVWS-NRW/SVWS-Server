package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungLehrerStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "Stundenplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextStundenplanungLehrerStundenplan extends HtmlContext<ReportingStundenplanungLehrerStundenplan>
		implements HtmlContextAufteilbar<HtmlContextStundenplanungLehrerStundenplan> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param stundenplan           Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe 		    Eine Liste von Lehrer-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	public HtmlContextStundenplanungLehrerStundenplan(final ReportingContext reportingContext, final ReportingStundenplanungStundenplan stundenplan,
			final List<Long> idsAusgabe) {
		super(reportingContext);
		erzeugeContext(stundenplan, idsAusgabe);
	}

	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung.
	 *
	 * @param stundenplan Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe  Eine Liste von Lehrer-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	private void erzeugeContext(final ReportingStundenplanungStundenplan stundenplan, final List<Long> idsAusgabe) {

		final List<ReportingStundenplanungLehrerStundenplan> stundenplaene = new ArrayList<>();
		this.reportingContext.repositoryLehrer().lehrer(idsAusgabe)
				.forEach(lehrer -> stundenplaene.add(new ReportingStundenplanungLehrerStundenplan(lehrer, stundenplan)));

		final List<ReportingLehrer> listeLehrkraefte = new ArrayList<>();
		stundenplaene.forEach(stundenplanungLehrerStundenplan -> listeLehrkraefte.add(stundenplanungLehrerStundenplan.lehrer()));
		String auflistungKuerzel = "";
		if (!listeLehrkraefte.isEmpty()) {
			auflistungKuerzel = listeLehrkraefte.stream().sorted(Comparator.comparing(ReportingLehrer::kuerzel))
					.map(ReportingLehrer::kuerzel).collect(Collectors.joining(","));
		}

		setContextData(stundenplaene);
		sortiereContextMitRegistry();

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("LehrerStundenplaene", getContextData());
		context.setVariable("LehrerStundenplaeneAuflistungKuerzel", auflistungKuerzel);

		super.setContext(context);
	}

	/**
	 * Teile diesen Context in eine Liste von Contexts auf, die jeweils auf eine ID filtern. Damit können Ausgaben pro ID erzeugt werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextStundenplanungLehrerStundenplan> getEinzelContexts() {
		final List<HtmlContextStundenplanungLehrerStundenplan> resultContexts = new ArrayList<>();

		for (final ReportingStundenplanungLehrerStundenplan stundenplan : getContextData()) {
			final List<Long> eineId = new ArrayList<>();
			eineId.add(stundenplan.lehrer().id());
			resultContexts.add(new HtmlContextStundenplanungLehrerStundenplan(this.reportingContext, stundenplan.stundenplan(), eineId));
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
		return getContextData().stream().map(plan -> plan.lehrer().id()).toList();
	}
}
