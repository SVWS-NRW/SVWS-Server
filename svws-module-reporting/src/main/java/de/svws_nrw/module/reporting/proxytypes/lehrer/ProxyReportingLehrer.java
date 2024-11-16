package de.svws_nrw.module.reporting.proxytypes.lehrer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer und erweitert die Klasse {@link ReportingLehrer}.
 */
public class ProxyReportingLehrer extends ReportingLehrer {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingLehrer}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param lehrerStammdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingLehrer(final ReportingRepository reportingRepository, final LehrerStammdaten lehrerStammdaten) {
		super(lehrerStammdaten.amtsbezeichnung,
				lehrerStammdaten.anrede,
				lehrerStammdaten.emailPrivat,
				lehrerStammdaten.emailDienstlich,
				lehrerStammdaten.foto,
				lehrerStammdaten.geburtsdatum,
				"",
				"",
				"",
				Geschlecht.fromValue(lehrerStammdaten.geschlecht),
				lehrerStammdaten.hausnummer,
				lehrerStammdaten.hausnummerZusatz,
				lehrerStammdaten.id,
				lehrerStammdaten.kuerzel,
				lehrerStammdaten.nachname,
				lehrerStammdaten.personalTyp,
				Nationalitaeten.getByDESTATIS(lehrerStammdaten.staatsangehoerigkeitID),
				null,
				lehrerStammdaten.strassenname,
				lehrerStammdaten.telefon,
				lehrerStammdaten.telefonMobil,
				lehrerStammdaten.titel,
				lehrerStammdaten.vorname,
				lehrerStammdaten.vorname,
				(lehrerStammdaten.wohnortID != null) ? reportingRepository.katalogOrte().get(lehrerStammdaten.wohnortID) : null,
				(lehrerStammdaten.ortsteilID != null) ? reportingRepository.katalogOrtsteile().get(lehrerStammdaten.ortsteilID) : null);

		this.reportingRepository = reportingRepository;

		// Füge Stammdaten des Lehrers für weitere Verwendung in der Map im Repository hinzu.
		reportingRepository.mapLehrerStammdaten().putIfAbsent(super.id(), lehrerStammdaten);

		ersetzeStringNullDurchEmpty(this, false);
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
