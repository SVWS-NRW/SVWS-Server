package de.svws_nrw.module.reporting.types.lerngruppen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingSchueler;
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

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt einen Proxy-Klassenunterricht aus einer Klasse und Fachinformationen.
	 *
	 * @param reportingRepository Das Repository für das Reporting.
	 * @param klasse Die Klasse, in der der Unterricht stattfindet
	 * @param fach Das unterrichtete Fach
	 * @param bewertenderLehrer Der Lehrer, der diesen Unterricht bewertet.
	 * @param fachlehrer Liste der Fachlehrer des Klassenunterrichts.
	 * @param wochenstundenFachlehrer Map der Wochenstunden pro Lehrer
	 * @param schueler Die Liste der Schüler, die dem Unterricht zugeordnet werden sollen. Ist die Liste null/empty, dann werden alle Schüler der Klasse gesetzt.
	 * @param wochenstundenSchueler Wochenstunden für die Schüler
	 * @param mapSchuelerLeistungsdaten Eine Map, die die Leistungsdaten zu diesem Unterricht zur ID des Schülers speichert
	 */
	public ProxyReportingKlassenunterricht(final ReportingRepository reportingRepository, final @NotNull ReportingKlasse klasse, final @NotNull ReportingFach fach,
			final ReportingLehrer bewertenderLehrer, final List<ReportingLehrer> fachlehrer, final Map<Long, Double> wochenstundenFachlehrer,
			final List<ReportingSchueler> schueler, final int wochenstundenSchueler, final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten) {
		super(klasse, fach, bewertenderLehrer, fachlehrer, wochenstundenFachlehrer, schueler, wochenstundenSchueler, mapSchuelerLeistungsdaten);
		this.reportingRepository = reportingRepository;
	}

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}

	/**
	 * Gibt die Schüler der Lerngruppe zurück.
	 * Die Liste wird anhand der im Reporting-Repository konfigurierten Sortierung sortiert.
	 *
	 * @return Die Liste der Schüler der Lerngruppe.
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		final List<ReportingSchueler> schueler = super.schueler();
		final Comparator<ReportingSchueler> comparator = ComparatorFactory.buildOptionalComparator(reportingRepository, ReportingSchueler.class.getSimpleName(),
				SortierungRegistryReportingSchueler.sortierungRegistry()).orElse(null);
		if (comparator != null)
			return schueler.stream().sorted(comparator).toList();
		return schueler;
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
