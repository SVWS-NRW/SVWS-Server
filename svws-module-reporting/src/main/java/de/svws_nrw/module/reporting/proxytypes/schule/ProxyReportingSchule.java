package de.svws_nrw.module.reporting.proxytypes.schule;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schule und erweitert die Klasse {@link ReportingSchule}.</p>
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
public class ProxyReportingSchule extends ReportingSchule {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Stammdaten der Schule aus dem Repository der Schule.
	 * @param reportingRepository Repository für die Reporting.
	 */
	public ProxyReportingSchule(final ReportingRepository reportingRepository) {
		super(null,
			reportingRepository.schulstammdaten().anzJGS_Jahr,
			reportingRepository.schulstammdaten().schuleAbschnitte.anzahlAbschnitte,
			reportingRepository.schulstammdaten().bezeichnung1,
			reportingRepository.schulstammdaten().bezeichnung2,
			reportingRepository.schulstammdaten().bezeichnung3,
			reportingRepository.schulstammdaten().schuleAbschnitte.abschnittBez,
			reportingRepository.schulstammdaten().schuleAbschnitte.bezAbschnitte,
			reportingRepository.schulstammdaten().dauerUnterrichtseinheit,
			reportingRepository.schulstammdaten().email,
			reportingRepository.schulstammdaten().fax,
			reportingRepository.schulstammdaten().hausnummer,
			reportingRepository.schulstammdaten().hausnummerZusatz,
			reportingRepository.schulstammdaten().ort,
			reportingRepository.schulstammdaten().plz,
			null,
			reportingRepository.schulstammdaten().schulform,
			reportingRepository.schulstammdaten().schulNr,
			reportingRepository.schulstammdaten().strassenname,
			reportingRepository.schulstammdaten().telefon,
			reportingRepository.schulstammdaten().webAdresse);

		    this.reportingRepository = reportingRepository;
			setSchuljahresabschnitte(this.reportingRepository.mapSchuljahresabschnitte().values().stream().map(a -> (ReportingSchuljahresabschnitt) new ProxyReportingSchuljahresabschnitt(a)).toList());
			setAktuellerSchuljahresabschnitt(new ProxyReportingSchuljahresabschnitt(this.reportingRepository.aktuellerSchuljahresabschnitt()));
	}



	// ##### Getter und Setter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}
}
