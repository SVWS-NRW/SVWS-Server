package de.svws_nrw.module.reporting.proxytypes.lehrer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schule.Schulleitung;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrerLeitungsfunktion;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ LehrerLeitungsfunktion und erweitert die Klasse {@link ReportingLehrerLeitungsfunktion}.
 */
public class ProxyReportingLehrerLeitungsfunktion extends ReportingLehrerLeitungsfunktion {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingLehrerLeitungsfunktion}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param schulleitung        Die Schulleitungsdaten.
	 */
	public ProxyReportingLehrerLeitungsfunktion(final ReportingRepository reportingRepository, final Schulleitung schulleitung) {
		super(schulleitung.beginn, schulleitung.bezeichnung, schulleitung.ende, schulleitung.idLeitungsfunktion);
		this.reportingRepository = reportingRepository;
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


	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}

}
