package de.svws_nrw.module.reporting.proxytypes.schueler.erzieher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.erzieher.Erzieherart;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArt;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Erzieher-Art und erweitert die Klasse {@link ReportingErzieherArt}.
 */
public class ProxyReportingErzieherArt extends ReportingErzieherArt {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingErzieherArt}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param erzieherart Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingErzieherArt(final ReportingRepository reportingRepository, final Erzieherart erzieherart) {
		super(ersetzeNullDurchEmpty(erzieherart.bezeichnung),
				erzieherart.id,
				0
		);
		// TODO: Die Sortierung wird für alle auf 0 gesetzt, da bei der Erstellung dieser Klasse die im Data-Objekt noch fehlte. Wird diese dort ergänzt,
		//  muss sie auch hier ergänzt werden.
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


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
