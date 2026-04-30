package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Klassen", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextKlassen extends HtmlContext<ReportingKlasse> implements HtmlContextAufteilbar<HtmlContextKlassen> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Klassen.
	 *
	 * @param reportingRepository 	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingKlassen		Liste der Klassen, die berücksichtigt werden sollen.
	 */
	public HtmlContextKlassen(final ReportingRepository reportingRepository, final List<ReportingKlasse> reportingKlassen) {
		super(reportingRepository);
		erzeugeContextFromKlassen(reportingKlassen);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Klassen-IDs.
	 *
	 * @param reportingRepository   Repository mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextKlassen(final ReportingRepository reportingRepository) {
		super(reportingRepository);
		erzeugeContextFromIds(this.reportingRepository.reportingParameter().idsHauptdaten());
	}


	/**
	 * Erzeugt den Context aus einer Liste von Klassen.
	 *
	 * @param reportingKlassen   	Liste der Klassen, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromKlassen(final List<ReportingKlasse> reportingKlassen) {

		setContextData(reportingKlassen);
		sortiereContextMitRegistry();

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Klassen", getContextData());

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Klassen-IDs.
	 *
	 * @param idsKlassen	Liste der IDs der Klassen, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromIds(final List<Long> idsKlassen) {

		setContextData(reportingRepository.repositoryLerngruppen().klassen(idsKlassen, false));
		sortiereContextMitRegistry();

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Klassen", getContextData());

		super.setContext(context);
	}


	/**
	 * Teile diesen Context mit allen Klassen in eine Liste von Contexts auf, die jeweils eine Klasse enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextKlassen> getEinzelContexts() {
		final List<HtmlContextKlassen> resultContexts = new ArrayList<>();

		for (final ReportingKlasse reportingKlasse : getContextData()) {
			final List<ReportingKlasse> eineKlasse = new ArrayList<>();
			eineKlasse.add(reportingKlasse);
			resultContexts.add(new HtmlContextKlassen(this.reportingRepository, eineKlasse));
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
		return getContextData().stream().map(ReportingKlasse::id).toList();
	}
}
