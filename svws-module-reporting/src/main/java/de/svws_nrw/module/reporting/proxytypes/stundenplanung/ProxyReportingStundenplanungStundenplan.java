package de.svws_nrw.module.reporting.proxytypes.stundenplanung;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungZeitrasterstunde;


/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Raum und erweitert die Klasse {@link ReportingStundenplanungStundenplan}.</p>
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
public class ProxyReportingStundenplanungStundenplan extends ReportingStundenplanungStundenplan {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 * @param reportingRepository 	Repository für die Reporting.
	 * @param stundenplan 			Das Stundenplan-Objekt mit den gesammelten Daten des Stundenplanes.
	 */
	public ProxyReportingStundenplanungStundenplan(final ReportingRepository reportingRepository, final Stundenplan stundenplan) {
		super(stundenplan.bezeichnungStundenplan,
				stundenplan.gueltigAb,
				stundenplan.gueltigBis,
				stundenplan.id,
				new ArrayList<>(),
				null,
				new ArrayList<>());

		this.reportingRepository = reportingRepository;

		if (!this.reportingRepository.mapStundenplaene().containsKey(stundenplan.id))
			this.reportingRepository.mapStundenplaene().put(stundenplan.id, stundenplan);

		if (this.reportingRepository.mapSchuljahresabschnitte().containsKey(stundenplan.idSchuljahresabschnitt)) {
			super.schuljahresabschnitt = new ProxyReportingSchuljahresabschnitt(
					this.reportingRepository.mapSchuljahresabschnitte().get(stundenplan.idSchuljahresabschnitt));
		}

		// Räume und Zeitraster werden per Lasy-loading nachgeladen.
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}


	/**
	 * Eine Liste aller Räume im Stundenplan.
	 * @return Inhalt des Feldes raeume
	 */
	@Override
	public List<ReportingStundenplanungRaum> raeume() {
		if (super.raeume().isEmpty()) {
			super.raeume().addAll(this.reportingRepository.mapStundenplaene().get(this.id).raeume.stream()
					.map(r -> (ReportingStundenplanungRaum) new ProxyReportingStundenplanungRaum(r, this.id)).toList());
		}
		return super.raeume();
	}

	/**
	 * Eine Liste aller Räume im Stundenplan.
	 * @return Inhalt des Feldes raeume
	 */
	@Override
	public List<ReportingStundenplanungZeitrasterstunde> zeitraster() {
		if (super.zeitraster().isEmpty()) {
			super.zeitraster().addAll(this.reportingRepository.mapStundenplaene().get(this.id).zeitraster.stream()
					.map(z -> (ReportingStundenplanungZeitrasterstunde) new ProxyReportingStundenplanungZeitrasterstunde(z, this.id)).toList());
		}
		return super.zeitraster();
	}

}
