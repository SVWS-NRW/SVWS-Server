package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisung;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Zuweisung und erweitert die Klasse {@link ReportingSchuelerZuweisung}.
 */
public class ProxyReportingSchuelerZuweisung extends ReportingSchuelerZuweisung {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerZuweisung}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param dto                 Das DTO mit den Datenbank-Daten.
	 * @param lernabschnitt       Der Lernabschnitt des Schülers, dem diese Zuweisung zugeordnet ist.
	 */
	public ProxyReportingSchuelerZuweisung(final ReportingRepository reportingRepository, final DTOSchuelerZuweisung dto,
			final ReportingSchuelerLernabschnitt lernabschnitt) {
		super(lernabschnitt.schuljahresabschnitt().fach(dto.Fach_ID),
				ersetzeNullBlankTrim(dto.Kursart),
				lernabschnitt);

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
