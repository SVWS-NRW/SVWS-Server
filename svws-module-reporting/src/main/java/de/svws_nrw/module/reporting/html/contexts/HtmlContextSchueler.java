package de.svws_nrw.module.reporting.html.contexts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Schüler", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextSchueler extends HtmlContext<ReportingSchueler> {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Schülern.
	 *
	 * @param reportingRepository 	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingSchueler		Liste der Schüler, die berücksichtigt werden sollen.
	 */
	public HtmlContextSchueler(final ReportingRepository reportingRepository, final List<ReportingSchueler> reportingSchueler) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContextFromSchueler(reportingSchueler);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Schüler-IDs.
	 *
	 * @param reportingRepository   Repository mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextSchueler(final ReportingRepository reportingRepository) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContextFromIds(this.reportingRepository.reportingParameter().idsHauptdaten);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Schülern.
	 *
	 * @param reportingSchueler   	Liste der Schüler, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromSchueler(final List<ReportingSchueler> reportingSchueler) {

		setContextData(reportingSchueler);
		sortiereContextMitRegistry();

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Schueler", getContextData());

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Schüler-IDs.
	 *
	 * @param idsSchueler	Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromIds(final List<Long> idsSchueler) {

		// Erzeuge Maps, damit auch später leicht auf die Schülerdaten zugegriffen werden kann.
		final Map<Long, SchuelerStammdaten> mapSchueler = new HashMap<>();
		final List<Long> fehlendeSchueler = new ArrayList<>();
		for (final Long idSchueler : idsSchueler) {
			if (reportingRepository.mapSchuelerStammdaten().containsKey(idSchueler))
				mapSchueler.put(idSchueler, reportingRepository.mapSchuelerStammdaten().get(idSchueler));
			else
				fehlendeSchueler.add(idSchueler);
		}
		if (!fehlendeSchueler.isEmpty()) {
			final List<SchuelerStammdaten> fehlendeSchuelerStammdaten;
			try {
				fehlendeSchuelerStammdaten = (new DataSchuelerStammdaten(reportingRepository.conn())).getListByIds(fehlendeSchueler);
			} catch (final ApiOperationException e) {
				throw new DeveloperNotificationException(e.getMessage());
			}
			fehlendeSchuelerStammdaten.forEach(s -> this.reportingRepository.mapSchuelerStammdaten().putIfAbsent(s.id, s));
			mapSchueler.putAll(fehlendeSchuelerStammdaten.stream().collect(Collectors.toMap(s -> s.id, s -> s)));
		}

		// Erzeuge nun die einzelnen Schülerobjekte. Alle weiteren Daten werden später dynamisch nachgeladen.
		final List<ReportingSchueler> temp = new ArrayList<>();

		for (final Long schuelerID : mapSchueler.values().stream().map(s -> s.id).toList()) {
			final ProxyReportingSchueler proxyReportingSchueler = new ProxyReportingSchueler(reportingRepository, mapSchueler.get(schuelerID));
			temp.add(proxyReportingSchueler);
			this.reportingRepository.mapSchueler().put(schuelerID, proxyReportingSchueler);
		}

		setContextData(temp);
		sortiereContextMitRegistry();

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Schueler", getContextData());

		super.setContext(context);
	}



	/**
	 * Teile diesen Context mit allen Schülern in eine Liste von Contexts auf, die jeweils einen Schüler enthalten, um eine schülerbezogene Ausgabe zu
	 * ermöglichen.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextSchueler> getEinzelContexts() {
		final List<HtmlContextSchueler> resultContexts = new ArrayList<>();

		for (final ReportingSchueler reportingSchueler : getContextData()) {
			final List<ReportingSchueler> einSchueler = new ArrayList<>();
			einSchueler.add(reportingSchueler);
			resultContexts.add(new HtmlContextSchueler(this.reportingRepository, einSchueler));
		}

		return resultContexts;
	}
}
