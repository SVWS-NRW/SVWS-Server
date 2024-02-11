package de.svws_nrw.module.reporting.proxytypes.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegungHalbjahr;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegungHalbjahr und erweitert die Klasse {@link ReportingGostAbiturFachbelegungHalbjahr}.</p>
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
public class ProxyReportingGostAbiturFachbelegungHalbjahr extends ReportingGostAbiturFachbelegungHalbjahr {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten aus der Datenbank.
	 * @param reportingRepository Repository für die Reporting.
	 * @param abiturFachbelegungHalbjahr Daten-Objekt der Halbjahresfachbelegungen aus der Datenbank
	 */
	public ProxyReportingGostAbiturFachbelegungHalbjahr(final ReportingRepository reportingRepository, final AbiturFachbelegungHalbjahr abiturFachbelegungHalbjahr) {
		super(abiturFachbelegungHalbjahr.biliSprache,
			abiturFachbelegungHalbjahr.block1gewertet,
			abiturFachbelegungHalbjahr.block1kursAufZeugnis,
			abiturFachbelegungHalbjahr.fehlstundenGesamt,
			abiturFachbelegungHalbjahr.fehlstundenUnentschuldigt,
			abiturFachbelegungHalbjahr.halbjahrKuerzel,
			abiturFachbelegungHalbjahr.schriftlich,
			abiturFachbelegungHalbjahr.kursartKuerzel,
			null,
			abiturFachbelegungHalbjahr.notenkuerzel,
			abiturFachbelegungHalbjahr.wochenstunden);
		this.reportingRepository = reportingRepository;

		super.setLehrer(
			new ProxyReportingLehrer(
				this.reportingRepository,
				this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(abiturFachbelegungHalbjahr.lehrer, l -> new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(abiturFachbelegungHalbjahr.lehrer))));
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}

}
