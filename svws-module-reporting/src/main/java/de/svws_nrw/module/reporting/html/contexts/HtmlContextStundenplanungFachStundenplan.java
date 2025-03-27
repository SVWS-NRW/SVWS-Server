package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungFachStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "Stundenplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextStundenplanungFachStundenplan extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Die Stundenpläne dieses Contexts zu den übergebenen IDs für die Ausgabe. */
	@JsonIgnore
	private final List<ReportingStundenplanungFachStundenplan> stundenplaene = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param stundenplan           Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe 		    Eine Liste von Fach-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	public HtmlContextStundenplanungFachStundenplan(final ReportingRepository reportingRepository, final ReportingStundenplanungStundenplan stundenplan,
			final List<Long> idsAusgabe) {
		this.reportingRepository = reportingRepository;
		erzeugeContext(stundenplan, idsAusgabe);
	}

	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung.
	 *
	 * @param stundenplan Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe  Eine Liste von Fach-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	private void erzeugeContext(final ReportingStundenplanungStundenplan stundenplan, final List<Long> idsAusgabe) {

		stundenplan.schuljahresabschnitt().faecher(idsAusgabe).forEach(fach -> stundenplaene.add(new ReportingStundenplanungFachStundenplan(fach, stundenplan)));

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("FaecherStundenplaene", stundenplaene);

		super.setContext(context);
	}

	/**
	 * Teile diesen Context in eine Liste von Contexts auf, die jeweils auf eine ID filtern. Damit können Ausgaben pro ID erzeugt werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextStundenplanungFachStundenplan> getEinzelContexts() {
		final List<HtmlContextStundenplanungFachStundenplan> resultContexts = new ArrayList<>();

		for (final ReportingStundenplanungFachStundenplan stundenplan : this.stundenplaene) {
			final List<Long> eineId = new ArrayList<>();
			eineId.add(stundenplan.fach().id());
			resultContexts.add(new HtmlContextStundenplanungFachStundenplan(this.reportingRepository, stundenplan.stundenplan(), eineId));
		}

		return resultContexts;
	}
}
