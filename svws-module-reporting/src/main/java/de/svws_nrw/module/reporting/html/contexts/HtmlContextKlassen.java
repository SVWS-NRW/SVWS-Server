package de.svws_nrw.module.reporting.html.contexts;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
public final class HtmlContextKlassen extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Liste, die die im Context ermitteln Daten speichert und den Zugriff auf die Daten abseits des html-Templates ermöglicht. */
	@JsonIgnore
	private ArrayList<ReportingKlasse> klassen = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Klassen.
	 *
	 * @param reportingRepository 	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingKlassen		Liste der Klassen, die berücksichtigt werden sollen.
	 */
	public HtmlContextKlassen(final ReportingRepository reportingRepository, final List<ReportingKlasse> reportingKlassen) {
		this.reportingRepository = reportingRepository;
		erzeugeContextFromKlassen(reportingKlassen);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Kurs-IDs.
	 *
	 * @param reportingRepository   Repository mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextKlassen(final ReportingRepository reportingRepository) throws ApiOperationException {
		this.reportingRepository = reportingRepository;
		erzeugeContextFromIds(this.reportingRepository.reportingParameter().idsHauptdaten);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Klassen.
	 *
	 * @param reportingKlassen   	Liste der Klassen, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromKlassen(final List<ReportingKlasse> reportingKlassen) {

		// Sortiere die übergebene Liste der Klassen
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		reportingKlassen.sort(Comparator.comparing(ReportingKlasse::sortierung)
				.thenComparing(ReportingKlasse::kuerzel, colGerman));

		klassen = new ArrayList<>();
		klassen.addAll(reportingKlassen);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Klassen", klassen);

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Klassen-IDs.
	 *
	 * @param idsKlassen	Liste der IDs der Klassen, die berücksichtigt werden sollen.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void erzeugeContextFromIds(final List<Long> idsKlassen) throws ApiOperationException {

		// Erzeuge Maps, damit auch später leicht auf die Kursdaten zugegriffen
		// werden kann.
		final Map<Long, ReportingKlasse> mapKlassen = new HashMap<>();
		for (final Long idKlasse : idsKlassen) {
			if (reportingRepository.mapKlassen().containsKey(idKlasse))
				mapKlassen.put(idKlasse, reportingRepository.mapKlassen().get(idKlasse));
			else {
				// ID der Klassen ist bekannt, aber er wurde noch nicht aus der DB geladen. Lade dessen Daten und lade dabei alle Klassen des Lernabschnitts.
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

		// Die Klassen bzw. ihre IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe sollten
		// sie aber Sortierreihenfolge sein.
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		klassen.addAll(mapKlassen.values().stream().sorted(Comparator.comparing(ReportingKlasse::sortierung)
				.thenComparing(ReportingKlasse::kuerzel, colGerman)).toList());

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Klassen", klassen);

		super.setContext(context);
	}


	/**
	 * Teile diesen Context mit allen Klassen in eine Liste von Contexts auf, die jeweils einen Kurs enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextKlassen> getEinzelKlasseContexts() {
		final List<HtmlContextKlassen> resultContexts = new ArrayList<>();

		for (final ReportingKlasse reportingKlasse : klassen) {
			final List<ReportingKlasse> eineKlasse = new ArrayList<>();
			eineKlasse.add(reportingKlasse);
			resultContexts.add(new HtmlContextKlassen(this.reportingRepository, eineKlasse));
		}

		return resultContexts;
	}
}
