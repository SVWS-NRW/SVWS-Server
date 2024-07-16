package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurtermin und erweitert die Klasse {@link ReportingGostKlausurplanungKlausurtermin}.</p>
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
 *  	<li>Die Proxy-Klasse können zudem auf den Klausurplan {@link ReportingGostKlausurplanungKlausurplan} zugreifen. Drin ist wieder der
 *  		Zugriff auf das Repository {@link ReportingRepository} möglich.
 *  		Im ersteren kann auf Klausurmanager zugegriffen werden, um darüber Daten nachladen zu können.
 *  		Das zweite enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 *    		wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 *    		Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingGostKlausurplanungKlausurtermin extends ReportingGostKlausurplanungKlausurtermin {

	/** Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört. */
	@JsonIgnore
	private final ReportingGostKlausurplanungKlausurplan reportingGostKlausurplanungKlausurplan;

	/**
	 * Erstellt ein neues Reporting-Objekt.
	 * @param gostKlausurtermin							Der GostKlausurtermin mit den Daten zum Klausurtermin.
	 * @param reportingGostKlausurplanungKlausurplan	Der Manager zum Klausurplan.
	 */
	public ProxyReportingGostKlausurplanungKlausurtermin(final GostKlausurtermin gostKlausurtermin,
			final ReportingGostKlausurplanungKlausurplan reportingGostKlausurplanungKlausurplan) {
		super(gostKlausurtermin.bemerkung,
				gostKlausurtermin.bezeichnung,
				gostKlausurtermin.datum,
				GostHalbjahr.fromID(gostKlausurtermin.halbjahr),
				gostKlausurtermin.id,
				gostKlausurtermin.istHaupttermin,
				gostKlausurtermin.nachschreiberZugelassen,
				gostKlausurtermin.quartal,
				gostKlausurtermin.startzeit);

		this.reportingGostKlausurplanungKlausurplan = reportingGostKlausurplanungKlausurplan;
	}

}
