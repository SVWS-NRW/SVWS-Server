package de.svws_nrw.module.reporting.html.contexts;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Kurse", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextKurse extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Liste, die die im Context ermitteln Daten speichert und den Zugriff auf die Daten abseits des html-Templates ermöglicht. */
	@JsonIgnore
	private ArrayList<ReportingKurs> kurse = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Kursen.
	 *
	 * @param reportingRepository 	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingKurse		Liste der Kurse, die berücksichtigt werden sollen.
	 */
	public HtmlContextKurse(final ReportingRepository reportingRepository, final List<ReportingKurs> reportingKurse) {
		this.reportingRepository = reportingRepository;
		erzeugeContextFromKurse(reportingKurse);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Kurs-IDs.
	 *
	 * @param reportingRepository   Repository mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextKurse(final ReportingRepository reportingRepository) throws ApiOperationException {
		this.reportingRepository = reportingRepository;
		erzeugeContextFromIds(this.reportingRepository.reportingParameter().idsHauptdaten);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Kursen.
	 *
	 * @param reportingKurse   	Liste der Kurse, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromKurse(final List<ReportingKurs> reportingKurse) {

		// Sortiere die übergebene Liste der Kurse
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		reportingKurse.sort(Comparator.comparing(ReportingKurs::kuerzel, colGerman));

		kurse = new ArrayList<>();
		kurse.addAll(reportingKurse);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Kurse", kurse);

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Kurs-IDs.
	 *
	 * @param idsKurse	Liste der IDs der Kurse, die berücksichtigt werden sollen.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void erzeugeContextFromIds(final List<Long> idsKurse) throws ApiOperationException {

		// Erzeuge Maps, damit auch später leicht auf die Kursdaten zugegriffen werden kann.
		final Map<Long, ReportingKurs> mapKurse = new HashMap<>();
		for (final Long idKurs : idsKurse) {
			if (reportingRepository.mapKurse().containsKey(idKurs))
				mapKurse.put(idKurs, reportingRepository.mapKurse().get(idKurs));
			else {
				// ID des Kurses ist bekannt, aber er wurde noch nicht aus der DB geladen. Lade dessen Daten und lade dabei alle Kurses des Lernabschnitts.
				final KursDaten kursDaten;
				try {
					kursDaten = DataKurse.getKursdaten(reportingRepository.conn(), idKurs);
					mapKurse.put(idKurs, this.reportingRepository.schuljahresabschnitt(kursDaten.idSchuljahresabschnitt).kurs(idKurs));
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"FEHLER: Fehler bei der Ermittlung der Daten für des Kurses %s.".formatted(idKurs), e, reportingRepository.logger(), LogLevel.ERROR,
							0);
				}
			}
		}

		// Die Kurse bzw. ihre IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe sollten
		// sie aber Sortierreihenfolge sein.
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		kurse.addAll(mapKurse.values().stream().sorted(Comparator.comparing(ReportingKurs::kuerzel, colGerman)).toList());

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Kurse", kurse);

		super.setContext(context);
	}


	/**
	 * Teile diesen Context mit allen Kursen in eine Liste von Contexts auf, die jeweils einen Kurs enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextKurse> getEinzelContexts() {
		final List<HtmlContextKurse> resultContexts = new ArrayList<>();

		for (final ReportingKurs reportingKurs : kurse) {
			final List<ReportingKurs> einKurs = new ArrayList<>();
			einKurs.add(reportingKurs);
			resultContexts.add(new HtmlContextKurse(this.reportingRepository, einKurs));
		}

		return resultContexts;
	}
}
