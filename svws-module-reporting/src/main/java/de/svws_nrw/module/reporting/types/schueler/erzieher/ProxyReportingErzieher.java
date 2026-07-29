package de.svws_nrw.module.reporting.types.schueler.erzieher;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Erzieher und erweitert die Klasse {@link ReportingErzieher}.
 */
public class ProxyReportingErzieher extends ReportingErzieher {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingErzieher}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param erzieherStammdaten Stammdaten-Objekt aus der DB.
	 * @param reportingSchueler	Der Schüler, dem dieser Erzieher zugeordnet ist.
	 */
	public ProxyReportingErzieher(final ReportingContext reportingContext, final ErzieherStammdaten erzieherStammdaten,
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
				reportingContext.repositoryKataloge().ort(erzieherStammdaten.wohnortID),
				reportingContext.repositoryKataloge().ortsteil(erzieherStammdaten.ortsteilID));

		this.reportingContext = reportingContext;
		if (erzieherStammdaten.idErzieherArt != null) {
			super.art = this.reportingContext.repositoryKataloge().erzieherart(erzieherStammdaten.idErzieherArt);
		}
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
	public ReportingContext reportingContext() {
		return reportingContext;
	}
}
