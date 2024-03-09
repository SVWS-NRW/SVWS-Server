package de.svws_nrw.module.reporting.proxytypes.gost.kursplanung;

import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
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
	 * @param gostHalbjahr Das GostHalbjahr, für die die fachwahlstatistik erstellt wird.
	 * @param gostStatistikFachwahl Wahlstatistik für ein Fach der GOSt über alle Halbjahre.
	 * @param ergebnisManager Der Manager des Blockungsergebnisses aus der Kursplanung, zu dem die Fachwahlstatistik gehört.
	 */
	public ProxyReportingGostKursplanungFachwahlstatistik(final ReportingRepository reportingRepository, final GostHalbjahr gostHalbjahr, final GostStatistikFachwahl gostStatistikFachwahl, final GostBlockungsergebnisManager ergebnisManager) {
		super(0,
			0,
			0,
			null,
			gostStatistikFachwahl.wahlenAB3,
			gostStatistikFachwahl.wahlenAB4,
			gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenGK,
			gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenGKMuendlich,
			gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenGKSchriftlich,
			gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenLK,
			gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenZK);

		final ReportingFach reportingFach = reportingRepository.mapReportingFaecher().get(gostStatistikFachwahl.id);
		super.setFach(reportingFach);

		super.setDifferenzKursgroessenGK(ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.GK.id) + ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.PJK.id) + ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.VTF.id));
		super.setDifferenzKursgroessenGK(ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.LK.id));
		super.setDifferenzKursgroessenGK(ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.ZK.id));
	}
}
