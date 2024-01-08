package de.svws_nrw.module.reporting.proxytypes.lehrer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer und erweitert die Klasse {@link ReportingLehrer}.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 * 		<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 * 			enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 * 			wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 * 			Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingLehrer extends ReportingLehrer {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 *
	 * @param reportingRepository Repository für die Reporting.
	 * @param lehrerStammdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingLehrer(final ReportingRepository reportingRepository, final LehrerStammdaten lehrerStammdaten) {
		super(lehrerStammdaten.amtsbezeichnung,
			lehrerStammdaten.anrede,
			lehrerStammdaten.emailDienstlich,
			lehrerStammdaten.emailPrivat,
			lehrerStammdaten.foto,
			lehrerStammdaten.geburtsdatum,
			Geschlecht.fromValue(lehrerStammdaten.geschlecht),
			lehrerStammdaten.hausnummer,
			lehrerStammdaten.hausnummerZusatz,
			lehrerStammdaten.id,
			lehrerStammdaten.kuerzel,
			lehrerStammdaten.nachname,
			lehrerStammdaten.personalTyp,
			Nationalitaeten.getByDESTATIS(lehrerStammdaten.staatsangehoerigkeitID),
			lehrerStammdaten.strassenname,
			lehrerStammdaten.telefon,
			lehrerStammdaten.telefonMobil,
			lehrerStammdaten.titel,
			lehrerStammdaten.vorname,
			lehrerStammdaten.wohnortID != null ? reportingRepository.katalogOrte().get(lehrerStammdaten.wohnortID) : null,
			"",
			lehrerStammdaten.ortsteilID != null ? reportingRepository.katalogOrtsteile().get(lehrerStammdaten.ortsteilID) : null,
			"");

		this.reportingRepository = reportingRepository;
		super.setWohnortname(super.wohnort() != null ? super.wohnort().ortsname : "");
		super.setWohnortsteilname(super.wohnortsteil() != null ? super.wohnortsteil().ortsteil : "");

		// Füge Stammdaten des Lehrers für weitere Verwendung in der Map im Repository hinzu.
		reportingRepository.mapLehrerStammdaten().putIfAbsent(super.id(), lehrerStammdaten);
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}
}
