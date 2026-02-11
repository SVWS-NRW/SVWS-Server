package de.svws_nrw.module.reporting.types.lerngruppen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingSchueler;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import jakarta.validation.constraints.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Proxy-Klasse für einen Unterricht eines Kurses im Rahmen des Reportings.
 * Erweitert die Klasse {@link ReportingKursunterricht}.
 */
public class ProxyReportingKursunterricht extends ReportingKursunterricht {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt einen Proxy-Kursunterricht aus einer Klasse und Fachinformationen.
	 *
	 * @param reportingRepository Das Repository für das Reporting.
	 * @param kurs Der Kurs, in dem der Unterricht stattfindet
	 * @param bewertenderLehrer Der Lehrer, der diesen Unterricht bewertet.
	 * @param mapSchuelerLeistungsdaten Eine Map, die die Leistungsdaten zu diesem Unterricht zur ID des Schülers speichert.
	 */
	public ProxyReportingKursunterricht(final ReportingRepository reportingRepository, final @NotNull ReportingKurs kurs, final ReportingLehrer bewertenderLehrer,
			final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten) {
		super(kurs, bewertenderLehrer, mapSchuelerLeistungsdaten);
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
