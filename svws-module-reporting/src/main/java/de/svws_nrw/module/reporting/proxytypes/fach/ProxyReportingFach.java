package de.svws_nrw.module.reporting.proxytypes.fach;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Fach und erweitert die Klasse {@link ReportingFach}.</p>
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
public class ProxyReportingFach extends ReportingFach {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten eines Fach- und GostFach-Objektes.
	 * @param reportingRepository Repository für die Reporting.
	 * @param fachDaten Fach-Daten-Objekt
	 * @param FachGostDaten GostFach-Daten-Objekt
	 */
	public ProxyReportingFach(final ReportingRepository reportingRepository, final FachDaten fachDaten, final GostFach FachGostDaten) {
		super(fachDaten.aufgabenfeld,
			fachDaten.aufZeugnis,
			fachDaten.bezeichnung,
			fachDaten.bezeichnungUeberweisungszeugnis,
			fachDaten.bezeichnungZeugnis,
			fachDaten.bilingualeSprache,
			fachDaten.holeAusAltenLernabschnitten,
			fachDaten.id,
			fachDaten.istFHRFach,
			false,
			false,
			fachDaten.istOberstufenFach,
			fachDaten.istNachpruefungErlaubt,
			fachDaten.istPruefungsordnungsRelevant,
			fachDaten.istSchriftlichBA,
			fachDaten.istSchriftlichZK,
			fachDaten.istSichtbar,
			fachDaten.kuerzel,
			fachDaten.maxZeichenInFachbemerkungen,
			fachDaten.sortierung,
			null);
		this.reportingRepository = reportingRepository;

		if (fachDaten.kuerzelStatistik != null && !fachDaten.kuerzelStatistik.isEmpty()) {
			super.setStatistikfach(ZulaessigesFach.getByKuerzelASD(fachDaten.kuerzelStatistik));
		}

		if (FachGostDaten != null) {
			this.setIstFremdsprache(FachGostDaten.istFremdsprache);
			this.setIstFremdSpracheNeuEinsetzend(FachGostDaten.istFremdSpracheNeuEinsetzend);
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
