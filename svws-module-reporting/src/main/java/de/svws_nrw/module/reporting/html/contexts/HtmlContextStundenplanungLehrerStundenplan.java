package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungLehrerStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "Stundenplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextStundenplanungLehrerStundenplan extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Die Stundenpläne dieses Contexts zu den übergebenen IDs für die Ausgabe. */
	@JsonIgnore
	private final List<ReportingStundenplanungLehrerStundenplan> stundenplaene = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param stundenplan           Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe 		    Eine Liste von Lehrer-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	public HtmlContextStundenplanungLehrerStundenplan(final ReportingRepository reportingRepository, final ReportingStundenplanungStundenplan stundenplan,
			final List<Long> idsAusgabe) {
		this.reportingRepository = reportingRepository;
		erzeugeContext(stundenplan, idsAusgabe);
	}

	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung.
	 *
	 * @param stundenplan Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe  Eine Liste von Lehrer-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	private void erzeugeContext(final ReportingStundenplanungStundenplan stundenplan, final List<Long> idsAusgabe) {

		this.reportingRepository.lehrer(idsAusgabe)
				.forEach(lehrer -> stundenplaene.add(new ReportingStundenplanungLehrerStundenplan(lehrer, stundenplan)));

		final List<ReportingLehrer> listeLehrkraefte = new ArrayList<>();
		stundenplaene.forEach(stundenplanungLehrerStundenplan -> listeLehrkraefte.add(stundenplanungLehrerStundenplan.lehrer()));
		String auflistungKuerzel = "";
		if (!listeLehrkraefte.isEmpty())
			auflistungKuerzel = listeLehrkraefte.stream().sorted(Comparator.comparing(ReportingLehrer::kuerzel))
					.map(ReportingLehrer::kuerzel).collect(Collectors.joining(","));

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("LehrerStundenplaene", stundenplaene);
		context.setVariable("LehrerStundenplaeneAuflistungKuerzel", auflistungKuerzel);

		super.setContext(context);
	}

	/**
	 * Teile diesen Context in eine Liste von Contexts auf, die jeweils auf eine ID filtern. Damit können Ausgaben pro ID erzeugt werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextStundenplanungLehrerStundenplan> getEinzelContexts() {
		final List<HtmlContextStundenplanungLehrerStundenplan> resultContexts = new ArrayList<>();

		for (final ReportingStundenplanungLehrerStundenplan stundenplan : this.stundenplaene) {
			final List<Long> eineId = new ArrayList<>();
			eineId.add(stundenplan.lehrer().id());
			resultContexts.add(new HtmlContextStundenplanungLehrerStundenplan(this.reportingRepository, stundenplan.stundenplan(), eineId));
		}

		return resultContexts;
	}
}
