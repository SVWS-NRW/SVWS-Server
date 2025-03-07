package de.svws_nrw.module.reporting.proxytypes.stundenplanung;

import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungUnterricht;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Raum und erweitert die Klasse {@link ReportingStundenplanungUnterricht}.
 */
public class ProxyReportingStundenplanungUnterricht extends ReportingStundenplanungUnterricht {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Der Stundenplan, zu dem dieser Unterricht gehört. */
	@JsonIgnore
	private final ReportingStundenplanungStundenplan stundenplan;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStundenplanungUnterricht}.
	 *
	 * @param reportingRepository  Repository für das Reporting.
	 * @param stundenplan	 	   Der Stundenplan, zu dieser der Unterricht gehört.
	 * @param unterricht		   Der Unterricht aus dem Stundenplan.
	 */
	public ProxyReportingStundenplanungUnterricht(final ReportingRepository reportingRepository, final ReportingStundenplanungStundenplan stundenplan,
			final StundenplanUnterricht unterricht) {
		super(unterricht.id, null, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), -1, null);

		this.reportingRepository = reportingRepository;
		this.stundenplan = stundenplan;

		if (this.stundenplan.schuljahresabschnitt() != null) {
			super.fach = this.stundenplan.schuljahresabschnitt().mapFaecher().get(unterricht.idFach);
			if (unterricht.idKurs != null) {
				super.kurs = this.stundenplan.schuljahresabschnitt().mapKurse().get(unterricht.idKurs);
				if (super.kurs() != null)
					super.klassen = super.kurs().klassen();
			} else {
				super.kurs = null;
				super.klassen =
						unterricht.klassen.stream().map(k -> this.stundenplan.schuljahresabschnitt().mapKlassen().get(k)).filter(Objects::nonNull).toList();
			}
		}

		super.lehrkraefte = unterricht.lehrer.stream()
				.filter(l -> this.reportingRepository.mapLehrerStammdaten().get(l) != null)
				.map(l -> (ReportingLehrer) new ProxyReportingLehrer(this.reportingRepository, this.reportingRepository.mapLehrerStammdaten().get(l)))
				.toList();

		if ((this.stundenplan.raeume() != null) && !this.stundenplan.raeume().isEmpty())
			super.raeume = unterricht.raeume.stream().map(this.stundenplan::raum).toList();

		// TODO: Schienen noch als Reporting-Objekt definieren und verwenden.
		super.schienen = unterricht.schienen;

		super.wochentyp = unterricht.wochentyp;

		super.stundeImUnterrichtsraster = this.stundenplan.unterrichtsrasterstunde(unterricht.idZeitraster);
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}

}
