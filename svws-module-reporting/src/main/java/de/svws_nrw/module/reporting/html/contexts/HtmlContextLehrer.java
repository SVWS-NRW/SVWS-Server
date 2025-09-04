package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Lehrer", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextLehrer extends HtmlContext<ReportingLehrer> {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Lehrern.
	 *
	 * @param reportingRepository 	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingLehrer		Liste der Lehrer, die berücksichtigt werden sollen.
	 */
 public HtmlContextLehrer(final ReportingRepository reportingRepository, final List<ReportingLehrer> reportingLehrer) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContextFromLehrer(reportingLehrer);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Lehrer-IDs.
	 *
	 * @param reportingRepository   Repository mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextLehrer(final ReportingRepository reportingRepository) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContextFromIds(this.reportingRepository.reportingParameter().idsHauptdaten);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Lehrern.
	 *
	 * @param reportingLehrer   	Liste der Lehrer, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromLehrer(final List<ReportingLehrer> reportingLehrer) {

		setContextData(reportingLehrer);
		sortiereContextMitRegistry();

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Lehrer", getContextData());

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Lehrer-IDs.
	 *
	 * @param idsLehrer	Liste der IDs der Lehrer, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromIds(final List<Long> idsLehrer) {

		setContextData(this.reportingRepository.lehrer(idsLehrer));
		sortiereContextMitRegistry();

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Lehrer", getContextData());

		super.setContext(context);
	}


	/**
	 * Teile diesen Context mit allen Lehrern in eine Liste von Contexts auf, die jeweils einen Lehrer enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextLehrer> getEinzelContexts() {
		final List<HtmlContextLehrer> resultContexts = new ArrayList<>();

  for (final ReportingLehrer reportingLehrer : getContextData()) {
			final List<ReportingLehrer> einLehrer = new ArrayList<>();
			einLehrer.add(reportingLehrer);
			resultContexts.add(new HtmlContextLehrer(this.reportingRepository, einLehrer));
		}

		return resultContexts;
	}
}
