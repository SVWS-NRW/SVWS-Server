package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

import java.util.List;

/**
 * Proxy-Klasse für die Schüler-Leistungsdaten-Matrix im Rahmen des Reportings.
 * Diese Klasse erweitert {@link ReportingSchuelerLeistungsdatenMatrix} und nutzt das
 * {@link ReportingContext}, um Sortierdefinitionen und Filter automatisch anzuwenden.
 */
public class ProxyReportingSchuelerLeistungsdatenMatrix extends ReportingSchuelerLeistungsdatenMatrix {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt eine neue Proxy-Matrix. Die Sortierung für Schüler und Leistungsdaten sowie der Filter
	 * der Leistungsdaten werden automatisch aus den im Repository hinterlegten Reporting-Parametern
	 * ermittelt. Die Spaltenreihenfolge der Fächer ergibt sich aus der Sortierung der Leistungsdaten.
	 *
	 * @param reportingContext  Das Repository für den Zugriff auf Parameter und Stammdaten.
	 * @param schueler             Die Liste der Schüler.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, für den die Daten ermittelt werden sollen.
	 */
	public ProxyReportingSchuelerLeistungsdatenMatrix(final ReportingContext reportingContext, final List<ReportingSchueler> schueler,
			final ReportingSchuljahresabschnitt schuljahresabschnitt) {

		super(schueler, schuljahresabschnitt,
				ComparatorFactory.buildComparator(reportingContext.sortierungService(), reportingContext.logger(),
						ReportingSchuelerLeistungsdaten.class.getSimpleName(), ReportingSchuelerLeistungsdaten.SORTIERUNG, true),
				ComparatorFactory.buildComparator(reportingContext.sortierungService(), reportingContext.logger(),
						ReportingSchueler.class.getSimpleName(), ReportingSchueler.SORTIERUNG, true),
				ReportingSchuelerLeistungsdaten.FILTER.bedingung(
						reportingContext.filterService().getFilter(ReportingSchuelerLeistungsdaten.class.getSimpleName()), null));

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
}
