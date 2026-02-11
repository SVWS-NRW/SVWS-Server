package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

import java.util.List;

/**
 * Proxy-Klasse für die Schüler-Leistungsdaten-Matrix im Rahmen des Reportings.
 * Diese Klasse erweitert {@link ReportingSchuelerLeistungsdatenMatrix} und nutzt das
 * {@link ReportingRepository}, um Sortierdefinitionen und Filter automatisch anzuwenden.
 */
public class ProxyReportingSchuelerLeistungsdatenMatrix extends ReportingSchuelerLeistungsdatenMatrix {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt eine neue Proxy-Matrix. Die Sortierung für Schüler und Fächer wird automatisch
	 * aus den im Repository hinterlegten Reporting-Parametern ermittelt.
	 *
	 * @param reportingRepository  Das Repository für den Zugriff auf Parameter und Stammdaten.
	 * @param schueler             Die Liste der Schüler.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, für den die Daten ermittelt werden sollen.
	 */
	public ProxyReportingSchuelerLeistungsdatenMatrix(final ReportingRepository reportingRepository, final List<ReportingSchueler> schueler,
			final ReportingSchuljahresabschnitt schuljahresabschnitt) {

		super(schueler, schuljahresabschnitt,
				reportingRepository.getSortierungsAttribute(ReportingFach.class.getSimpleName(), true),
				reportingRepository.getSortierungsAttribute(ReportingSchueler.class.getSimpleName(), true),
				reportingRepository.getFilter(ReportingFach.class.getSimpleName(), null));

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
}
