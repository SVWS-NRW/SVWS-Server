package de.svws_nrw.module.reporting.proxytypes.schueler.erzieher;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieher;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Erzieher und erweitert die Klasse {@link ReportingErzieher}.
 */
public class ProxyReportingErzieher extends ReportingErzieher {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingErzieher}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param erzieherStammdaten Stammdaten-Objekt aus der DB.
	 * @param reportingSchueler	Der Schüler, dem dieser Erzieher zugeordnet ist.
	 */
	public ProxyReportingErzieher(final ReportingRepository reportingRepository, final ErzieherStammdaten erzieherStammdaten,
			final ReportingSchueler reportingSchueler) {
		super(ersetzeNullBlankTrim(erzieherStammdaten.anrede),
				null,
				ersetzeNullBlankTrim(erzieherStammdaten.bemerkungen),
				ersetzeNullBlankTrim(erzieherStammdaten.eMail),
				"",
				erzieherStammdaten.erhaeltAnschreiben,
				"",
				"",
				"",
				"",
				null,
				ersetzeNullBlankTrim(erzieherStammdaten.hausnummer),
				ersetzeNullBlankTrim(erzieherStammdaten.hausnummerZusatz),
				erzieherStammdaten.id,
				ersetzeNullBlankTrim(erzieherStammdaten.nachname),
				reportingSchueler,
				Nationalitaeten.getByDESTATIS(erzieherStammdaten.staatsangehoerigkeitID),
				null,
				ersetzeNullBlankTrim(erzieherStammdaten.strassenname),
				"",
				"",
				ersetzeNullBlankTrim(erzieherStammdaten.titel),
				ersetzeNullBlankTrim(erzieherStammdaten.vorname),
				ersetzeNullBlankTrim(erzieherStammdaten.vorname),
				(erzieherStammdaten.wohnortID != null) ? reportingRepository.katalogOrte().get(erzieherStammdaten.wohnortID) : null,
				(erzieherStammdaten.ortsteilID != null) ? reportingRepository.katalogOrtsteile().get(erzieherStammdaten.ortsteilID) : null);

		this.reportingRepository = reportingRepository;
		if (erzieherStammdaten.idErzieherArt != null)
			super.art = this.reportingRepository.mapReportingErzieherarten().get(erzieherStammdaten.idErzieherArt);
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
