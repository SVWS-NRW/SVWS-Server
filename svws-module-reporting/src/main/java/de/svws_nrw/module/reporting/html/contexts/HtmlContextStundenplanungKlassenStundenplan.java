package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungKlasseStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "Stundenplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextStundenplanungKlassenStundenplan extends HtmlContext<ReportingStundenplanungKlasseStundenplan> {

	@Override
	public List<String> standardsortierung() {
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(methodenreferenzToString(ReportingStundenplanungKlasseStundenplan::klasse) + "." + methodenreferenzToString(ReportingKlasse::kuerzel));
		return standardSort;
	}

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param stundenplan           Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe 		    Eine Liste von Klassen-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	public HtmlContextStundenplanungKlassenStundenplan(final ReportingRepository reportingRepository, final ReportingStundenplanungStundenplan stundenplan,
			final List<Long> idsAusgabe) {
		super(reportingRepository, false);
		this.reportingRepository = reportingRepository;
		erzeugeContext(stundenplan, idsAusgabe);
	}

	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung.
	 *
	 * @param stundenplan Der Stundenplan, mit dem die Stundenpläne zu den IDs der Ausgabe erstellt werden sollen.
	 * @param idsAusgabe  Eine Liste von Klassen-IDs, für die die Ausgabe erzeugt werden soll.
	 */
	private void erzeugeContext(final ReportingStundenplanungStundenplan stundenplan, final List<Long> idsAusgabe) {

		final List<ReportingStundenplanungKlasseStundenplan> stundenplaene = new ArrayList<>();
		this.reportingRepository.klassen(idsAusgabe)
				.forEach(klasse -> stundenplaene.add(new ReportingStundenplanungKlasseStundenplan(klasse, stundenplan)));

		setContextData(stundenplaene);
		sortiereContext();

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("KlassenStundenplaene", getContextData());

		super.setContext(context);
	}

	/**
	 * Teile diesen Context in eine Liste von Contexts auf, die jeweils auf eine ID filtern. Damit können Ausgaben pro ID erzeugt werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextStundenplanungKlassenStundenplan> getEinzelContexts() {
		final List<HtmlContextStundenplanungKlassenStundenplan> resultContexts = new ArrayList<>();

		for (final ReportingStundenplanungKlasseStundenplan stundenplan : getContextData()) {
			final List<Long> eineId = new ArrayList<>();
			eineId.add(stundenplan.klasse().id());
			resultContexts.add(new HtmlContextStundenplanungKlassenStundenplan(this.reportingRepository, stundenplan.stundenplan(), eineId));
		}

		return resultContexts;
	}
}
