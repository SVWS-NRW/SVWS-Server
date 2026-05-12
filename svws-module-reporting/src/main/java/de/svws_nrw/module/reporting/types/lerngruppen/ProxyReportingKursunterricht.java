package de.svws_nrw.module.reporting.types.lerngruppen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
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
// SonarQube möchte, dass die Vererbung maximal 5 Stufen besitzt. Hier kann die Warnung ignoriert werden, da die Proxy-Klasse die letzte Ebene darstellt und
// im Wesentlichen nur der Initialisierung über ein Kurs-Objekt dient. Des Weiteren lässt sich die schulische Realität in der Struktur der Unterrichtsgruppen
// nicht weiter vereinfachen.
@SuppressWarnings("java:S110")
public class ProxyReportingKursunterricht extends ReportingKursunterricht {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt einen Proxy-Kursunterricht aus einer Klasse und Fachinformationen.
	 *
	 * @param reportingContext Das Repository für das Reporting.
	 * @param kurs Der Kurs, in dem der Unterricht stattfindet
	 * @param bewertenderLehrer Der Lehrer, der diesen Unterricht bewertet.
	 * @param mapSchuelerLeistungsdaten Eine Map, die die Leistungsdaten zu diesem Unterricht zur ID des Schülers speichert.
	 */
	public ProxyReportingKursunterricht(final ReportingContext reportingContext, final @NotNull ReportingKurs kurs,
			final ReportingLehrer bewertenderLehrer, final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten) {
		super(kurs, bewertenderLehrer, mapSchuelerLeistungsdaten);
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
				ComparatorFactory.buildOptionalComparator(
						sortierungService,
						logger,
						ReportingSchueler.class.getSimpleName(),
						SortierungRegistryReportingSchueler.sortierungRegistry()).orElse(null);
		if (comparator != null) {
			return schueler.stream().sorted(comparator).toList();
		}
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
