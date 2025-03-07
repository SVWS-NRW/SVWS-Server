package de.svws_nrw.module.reporting.html.contexts;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Lehrer", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextLehrer extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Liste, die die im Context ermitteln Daten speichert und den Zugriff auf die Daten abseits des html-Templates ermöglicht. */
	@JsonIgnore
	private List<ReportingLehrer> lehrer = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Lehrern.
	 *
	 * @param reportingRepository 	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingLehrer		Liste der Lehrer, die berücksichtigt werden sollen.
	 */
	public HtmlContextLehrer(final ReportingRepository reportingRepository, final List<ReportingLehrer> reportingLehrer) {
		this.reportingRepository = reportingRepository;
		erzeugeContextFromLehrer(reportingLehrer);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Lehrer-IDs.
	 *
	 * @param reportingRepository   Repository mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextLehrer(final ReportingRepository reportingRepository) {
		this.reportingRepository = reportingRepository;
		erzeugeContextFromIds(this.reportingRepository.reportingParameter().idsHauptdaten);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Lehrern.
	 *
	 * @param reportingLehrer   	Liste der Lehrer, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromLehrer(final List<ReportingLehrer> reportingLehrer) {

		// Sortiere die übergebene Liste der Lehrer
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		reportingLehrer.sort(Comparator.comparing(ReportingLehrer::nachname, colGerman)
				.thenComparing(ReportingLehrer::vorname, colGerman)
				.thenComparing(ReportingLehrer::kuerzel, colGerman)
				.thenComparing(ReportingLehrer::id));

		lehrer = new ArrayList<>();
		lehrer.addAll(reportingLehrer);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Lehrer", lehrer);

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Lehrer-IDs.
	 *
	 * @param idsLehrer	Liste der IDs der Lehrer, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromIds(final List<Long> idsLehrer) {

		// Erzeuge nun die einzelnen Lehrerobjekte. Alle weiteren Daten werden später dynamisch nachgeladen.
		lehrer = this.reportingRepository.lehrer(idsLehrer);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Lehrer", lehrer);

		super.setContext(context);
	}


	/**
	 * Teile diesen Context mit allen Lehrern in eine Liste von Contexts auf, die jeweils einen Lehrer enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextLehrer> getEinzelLehrerContexts() {
		final List<HtmlContextLehrer> resultContexts = new ArrayList<>();

		for (final ReportingLehrer reportingLehrer : lehrer) {
			final List<ReportingLehrer> einLehrer = new ArrayList<>();
			einLehrer.add(reportingLehrer);
			resultContexts.add(new HtmlContextLehrer(this.reportingRepository, einLehrer));
		}

		return resultContexts;
	}
}
