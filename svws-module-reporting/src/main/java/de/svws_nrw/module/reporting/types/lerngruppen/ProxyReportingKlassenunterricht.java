package de.svws_nrw.module.reporting.types.lerngruppen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import jakarta.validation.constraints.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Proxy-Klasse für einen Klassenunterricht im Rahmen des Reportings.
 * Erweitert die Klasse {@link ReportingKlassenunterricht}.
 */
public class ProxyReportingKlassenunterricht extends ReportingKlassenunterricht {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt einen Proxy-Klassenunterricht aus einer Klasse und Fachinformationen.
	 *
	 * @param reportingContext Das Repository für das Reporting.
	 * @param klasse Die Klasse, in der der Unterricht stattfindet
	 * @param fach Das unterrichtete Fach
	 * @param bewertenderLehrer Der Lehrer, der diesen Unterricht bewertet.
	 * @param fachlehrer Liste der Fachlehrer des Klassenunterrichts.
	 * @param wochenstundenFachlehrer Map der Wochenstunden pro Lehrer
	 * @param schueler Die Liste der Schüler, die dem Unterricht zugeordnet werden sollen. Ist die Liste null/empty, dann werden alle Schüler der Klasse gesetzt.
	 * @param wochenstundenSchueler Wochenstunden für die Schüler
	 * @param mapSchuelerLeistungsdaten Eine Map, die die Leistungsdaten zu diesem Unterricht zur ID des Schülers speichert
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ProxyReportingKlassenunterricht(final ReportingContext reportingContext, final @NotNull ReportingKlasse klasse,
			final @NotNull ReportingFach fach, final ReportingLehrer bewertenderLehrer, final List<ReportingLehrer> fachlehrer,
			final Map<Long, Double> wochenstundenFachlehrer, final List<ReportingSchueler> schueler, final int wochenstundenSchueler,
			final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten) {
		super(klasse, fach, bewertenderLehrer, fachlehrer, wochenstundenFachlehrer, schueler, wochenstundenSchueler, mapSchuelerLeistungsdaten);
		this.reportingContext = reportingContext;
	}

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}

	/**
	 * Gibt die Schüler der Lerngruppe zurück.
	 * Die Liste wird anhand der im Reporting-Context konfigurierten Sortierung sortiert.
	 *
	 * @return Die Liste der Schüler der Lerngruppe.
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		final List<ReportingSchueler> schueler = super.schueler();

		// Prüfe, ob Service und Logger abrufbar sind. Andernfalls würden Standardsortierungen verwendet werden.
		final ReportingSortierungService sortierungService = (this.reportingContext != null) ? this.reportingContext.sortierungService() : null;
		final Logger logger = (this.reportingContext != null) ? this.reportingContext.logger() : null;

		final Comparator<ReportingSchueler> comparator =
				ComparatorFactory.buildComparator(sortierungService, logger, ReportingSchueler.class.getSimpleName(),
						ReportingSchueler.SORTIERUNG, true);
		return schueler.stream().sorted(comparator).toList();
	}

	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    Ergibt true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}
}
