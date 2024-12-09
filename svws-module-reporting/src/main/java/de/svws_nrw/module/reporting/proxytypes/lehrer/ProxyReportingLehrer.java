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
		super(ersetzeNullDurchEmpty(lehrerStammdaten.amtsbezeichnung),
				ersetzeNullDurchEmpty(lehrerStammdaten.anrede),
				ersetzeNullDurchEmpty(lehrerStammdaten.emailPrivat),
				ersetzeNullDurchEmpty(lehrerStammdaten.emailDienstlich),
				lehrerStammdaten.foto,
				ersetzeNullDurchEmpty(lehrerStammdaten.geburtsdatum),
				"",
				"",
				"",
				Geschlecht.fromValue(lehrerStammdaten.geschlecht),
				ersetzeNullDurchEmpty(lehrerStammdaten.hausnummer),
				ersetzeNullDurchEmpty(lehrerStammdaten.hausnummerZusatz),
				lehrerStammdaten.id,
				ersetzeNullDurchEmpty(lehrerStammdaten.kuerzel),
				ersetzeNullDurchEmpty(lehrerStammdaten.nachname),
				ersetzeNullDurchEmpty(lehrerStammdaten.personalTyp),
				Nationalitaeten.getByDESTATIS(lehrerStammdaten.staatsangehoerigkeitID),
				null,
				ersetzeNullDurchEmpty(lehrerStammdaten.strassenname),
				ersetzeNullDurchEmpty(lehrerStammdaten.telefon),
				ersetzeNullDurchEmpty(lehrerStammdaten.telefonMobil),
				ersetzeNullDurchEmpty(lehrerStammdaten.titel),
				ersetzeNullDurchEmpty(lehrerStammdaten.vorname),
				ersetzeNullDurchEmpty(lehrerStammdaten.vorname),
				(lehrerStammdaten.wohnortID != null) ? reportingRepository.katalogOrte().get(lehrerStammdaten.wohnortID) : null,
				(lehrerStammdaten.ortsteilID != null) ? reportingRepository.katalogOrtsteile().get(lehrerStammdaten.ortsteilID) : null);

		this.reportingRepository = reportingRepository;

		// Füge Stammdaten des Lehrers für weitere Verwendung in der Map im Repository hinzu.
		reportingRepository.mapLehrerStammdaten().putIfAbsent(super.id(), lehrerStammdaten);
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
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
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
