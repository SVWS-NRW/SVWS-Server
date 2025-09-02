package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Klassen", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextKlassen extends HtmlContext<ReportingKlasse> {

	@Override
	public List<String> standardsortierung() {
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(methodenreferenzToString(ReportingKlasse::sortierung));
		standardSort.add(methodenreferenzToString(ReportingKlasse::kuerzel));
		return standardSort;
	}

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Klassen.
	 *
	 * @param reportingRepository 	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingKlassen		Liste der Klassen, die berücksichtigt werden sollen.
	 */
	public HtmlContextKlassen(final ReportingRepository reportingRepository, final List<ReportingKlasse> reportingKlassen) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContextFromKlassen(reportingKlassen);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Klassen-IDs.
	 *
	 * @param reportingRepository   Repository mit Parametern, Logger und Daten zum Reporting.
	 */
	public HtmlContextKlassen(final ReportingRepository reportingRepository) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		erzeugeContextFromIds(this.reportingRepository.reportingParameter().idsHauptdaten);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Klassen.
	 *
	 * @param reportingKlassen   	Liste der Klassen, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromKlassen(final List<ReportingKlasse> reportingKlassen) {

		setContextData(reportingKlassen);
		sortiereContext();

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

		// Erzeuge Maps, damit auch später leicht auf die Klassendaten zugegriffen werden kann.
		final Map<Long, ReportingKlasse> mapKlassen = new HashMap<>();
		for (final Long idKlasse : idsKlassen) {
			if (reportingRepository.mapKlassen().containsKey(idKlasse))
				mapKlassen.put(idKlasse, reportingRepository.mapKlassen().get(idKlasse));
			else {
				// Die ID der Klasse ist bekannt, aber sie wurde noch nicht aus der DB geladen. Lade dessen Daten und lade dabei alle Klassen des Lernabschnitts.
				final KlassenDaten klassenDaten;
				try {
					klassenDaten = new DataKlassendaten(reportingRepository.conn()).getById(idKlasse);
					mapKlassen.put(idKlasse, this.reportingRepository.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt).klasse(idKlasse));
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"FEHLER: Fehler bei der Ermittlung der Daten für des Klassen %s.".formatted(idKlasse), e, reportingRepository.logger(),
							LogLevel.ERROR,
							0);
				}
			}
		}

		setContextData(mapKlassen.values().stream().toList());
		sortiereContext();

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
	public List<HtmlContextKlassen> getEinzelContexts() {
		final List<HtmlContextKlassen> resultContexts = new ArrayList<>();

		for (final ReportingKlasse reportingKlasse : getContextData()) {
			final List<ReportingKlasse> eineKlasse = new ArrayList<>();
			eineKlasse.add(reportingKlasse);
			resultContexts.add(new HtmlContextKlassen(this.reportingRepository, eineKlasse));
		}

		return resultContexts;
	}
}
