package de.svws_nrw.module.reporting.proxytypes.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegung;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegung und erweitert die Klasse {@link ReportingGostAbiturFachbelegung}.</p>
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
public class ProxyReportingGostAbiturFachbelegung extends ReportingGostAbiturFachbelegung {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten aus der Datenbank.
	 * @param reportingRepository Repository für die Reporting.
	 * @param abiturFachbelegung Daten-Objekt der Fachbelegungen aus der Datenbank
	 */
	public ProxyReportingGostAbiturFachbelegung(final ReportingRepository reportingRepository, final AbiturFachbelegung abiturFachbelegung) {
		super(abiturFachbelegung.abiturFach,
			abiturFachbelegung.block1NotenpunkteDurchschnitt,
			abiturFachbelegung.block1PunktSumme,
			abiturFachbelegung.block2MuendlichePruefungAbweichung,
			abiturFachbelegung.block2MuendlichePruefungBestehen,
			abiturFachbelegung.block2MuendlichePruefungFreiwillig,
			abiturFachbelegung.block2MuendlichePruefungNotenKuerzel,
			abiturFachbelegung.block2MuendlichePruefungReihenfolge,
			abiturFachbelegung.block2NotenKuerzelPruefung,
			null,
			abiturFachbelegung.block2Punkte,
			abiturFachbelegung.block2PunkteZwischenstand,
			null,
			null,
			abiturFachbelegung.letzteKursart);
		this.reportingRepository = reportingRepository;

		super.setBlock2Pruefer(
			new ProxyReportingLehrer(
				this.reportingRepository,
				this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(abiturFachbelegung.block2Pruefer, l -> new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(abiturFachbelegung.block2Pruefer))));

	 	super.setFach(this.reportingRepository.mapReportingFaecher().get(abiturFachbelegung.fachID));

		for (int i = 0; i < abiturFachbelegung.belegungen.length; i++) {
			if (abiturFachbelegung.belegungen[i] != null)
				super.halbjahresbelegungen()[i] = new ProxyReportingGostAbiturFachbelegungHalbjahr(this.reportingRepository, abiturFachbelegung.belegungen[i]);
			else
				super.halbjahresbelegungen()[i] = null;
		}
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}
}
