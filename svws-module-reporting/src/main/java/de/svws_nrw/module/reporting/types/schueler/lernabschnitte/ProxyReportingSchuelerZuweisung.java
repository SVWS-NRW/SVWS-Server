package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisung;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Zuweisung und erweitert die Klasse {@link ReportingSchuelerZuweisung}.
 */
public class ProxyReportingSchuelerZuweisung extends ReportingSchuelerZuweisung {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerZuweisung}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param dto                 Das DTO mit den Datenbank-Daten.
	 * @param lernabschnitt       Der Lernabschnitt des Schülers, dem diese Zuweisung zugeordnet ist.
	 */
	public ProxyReportingSchuelerZuweisung(final ReportingContext reportingContext, final DTOSchuelerZuweisung dto,
			final ReportingSchuelerLernabschnitt lernabschnitt) {
		super(lernabschnitt.schuljahresabschnitt().fach(dto.Fach_ID),
				ersetzeNullBlankTrim(dto.Kursart),
				lernabschnitt);

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
