package de.svws_nrw.module.reporting.proxytypes.gost.kursplanung;

import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungFachwahlstatistik;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungFachwahlstatistik und erweitert die Klasse {@link ReportingGostKursplanungFachwahlstatistik}.</p>
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
 *  	<li>Die Proxy-Klasse können zudem auf das Blockungsergebnis {@link ReportingGostKursplanungFachwahlstatistik} zugreifen. Drin ist wieder der
 *  		Zugriff auf das Repository {@link ReportingRepository} möglich. Im ersteren kann auf die Ergebnis- und Datenmanager der
 *  		Blockung zugegriffen werden, um darüber Daten nachladen zu können.
 *  		Das zweite enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 *    		wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 *    		Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingGostKursplanungFachwahlstatistik extends ReportingGostKursplanungFachwahlstatistik {


    /**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param reportingRepository Repository für die Reporting
	 * @param reportingGostKursplanungBlockungsergebnis Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört.
	 * @param gostStatistikFachwahl Wahlstatistik für ein Fach der GOSt über alle Halbjahre.
	 */
	public ProxyReportingGostKursplanungFachwahlstatistik(final ReportingRepository reportingRepository, final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis, final GostStatistikFachwahl gostStatistikFachwahl) {
		super(reportingRepository.mapReportingFaecher().get(gostStatistikFachwahl.id),
			gostStatistikFachwahl.wahlenAB3,
			gostStatistikFachwahl.wahlenAB4,
			gostStatistikFachwahl.fachwahlen[reportingGostKursplanungBlockungsergebnis.gostHalbjahr().id].wahlenGK,
			gostStatistikFachwahl.fachwahlen[reportingGostKursplanungBlockungsergebnis.gostHalbjahr().id].wahlenGKMuendlich,
			gostStatistikFachwahl.fachwahlen[reportingGostKursplanungBlockungsergebnis.gostHalbjahr().id].wahlenGKSchriftlich,
			gostStatistikFachwahl.fachwahlen[reportingGostKursplanungBlockungsergebnis.gostHalbjahr().id].wahlenLK,
			gostStatistikFachwahl.fachwahlen[reportingGostKursplanungBlockungsergebnis.gostHalbjahr().id].wahlenZK);
	}
}
