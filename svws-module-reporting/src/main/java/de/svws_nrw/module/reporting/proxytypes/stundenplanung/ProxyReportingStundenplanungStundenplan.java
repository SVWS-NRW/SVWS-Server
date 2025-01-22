package de.svws_nrw.module.reporting.proxytypes.stundenplanung;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungZeitrasterstunde;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Raum und erweitert die Klasse {@link ReportingStundenplanungStundenplan}.
 */
public class ProxyReportingStundenplanungStundenplan extends ReportingStundenplanungStundenplan {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStundenplanungStundenplan}.
	 *
	 * @param reportingRepository 	Repository für das Reporting.
	 * @param stundenplan 			Das Stundenplan-Objekt mit den gesammelten Daten des Stundenplanes.
	 */
	public ProxyReportingStundenplanungStundenplan(final ReportingRepository reportingRepository, final Stundenplan stundenplan) {
		super(ersetzeNullBlankTrim(stundenplan.bezeichnungStundenplan),
				ersetzeNullBlankTrim(stundenplan.gueltigAb),
				ersetzeNullBlankTrim(stundenplan.gueltigBis),
				stundenplan.id,
				new ArrayList<>(),
				null,
				new ArrayList<>());

		this.reportingRepository = reportingRepository;

		if (!this.reportingRepository.mapStundenplaene().containsKey(stundenplan.id))
			this.reportingRepository.mapStundenplaene().put(stundenplan.id, stundenplan);

		super.schuljahresabschnitt = this.reportingRepository.schuljahresabschnitt(stundenplan.idSchuljahresabschnitt);

		// Räume und Zeitraster werden per lazy-loading nachgeladen.
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}


	/**
	 * Eine Liste aller Räume im Stundenplan.
	 *
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
	 *
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
