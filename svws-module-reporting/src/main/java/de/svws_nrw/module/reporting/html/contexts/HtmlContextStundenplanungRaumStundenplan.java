package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaumStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "Stundenplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextStundenplanungRaumStundenplan extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Die Stundenpläne dieses Contexts zu den übergebenen IDs für die Ausgabe. */
	@JsonIgnore
	private final List<ReportingStundenplanungRaumStundenplan> stundenplaene = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param stundenplan           Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe 		    Eine Liste von Raum-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	public HtmlContextStundenplanungRaumStundenplan(final ReportingRepository reportingRepository, final ReportingStundenplanungStundenplan stundenplan,
			final List<Long> idsAusgabe) {
		this.reportingRepository = reportingRepository;
		erzeugeContext(stundenplan, idsAusgabe);
	}

	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung.
	 *
	 * @param stundenplan Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe  Eine Liste von Raum-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	private void erzeugeContext(final ReportingStundenplanungStundenplan stundenplan, final List<Long> idsAusgabe) {

		stundenplan.raeume(idsAusgabe).forEach(raum -> stundenplaene.add(new ReportingStundenplanungRaumStundenplan(raum, stundenplan)));

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("RaeumeStundenplaene", stundenplaene);

		super.setContext(context);
	}

	/**
	 * Teile diesen Context in eine Liste von Contexts auf, die jeweils auf eine ID filtern. Damit können Ausgaben pro ID erzeugt werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextStundenplanungRaumStundenplan> getEinzelContexts() {
		final List<HtmlContextStundenplanungRaumStundenplan> resultContexts = new ArrayList<>();

		for (final ReportingStundenplanungRaumStundenplan stundenplan : this.stundenplaene) {
			final List<Long> eineId = new ArrayList<>();
			eineId.add(stundenplan.raum().id());
			resultContexts.add(new HtmlContextStundenplanungRaumStundenplan(this.reportingRepository, stundenplan.stundenplan(), eineId));
		}

		return resultContexts;
	}
}
