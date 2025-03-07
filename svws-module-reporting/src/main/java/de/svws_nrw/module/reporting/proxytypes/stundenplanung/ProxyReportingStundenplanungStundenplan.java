package de.svws_nrw.module.reporting.proxytypes.stundenplanung;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungPausenzeit;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungUnterrichtsrasterstunde;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Raum und erweitert die Klasse {@link ReportingStundenplanungStundenplan}.
 */
public class ProxyReportingStundenplanungStundenplan extends ReportingStundenplanungStundenplan {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Der Manager für den Stundenplan. */
	@JsonIgnore
	private final StundenplanManager stundenplanManager;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStundenplanungStundenplan}.
	 *
	 * @param reportingRepository 	Repository für das Reporting.
	 * @param stundenplanManager	Der Manager für den Stundenplan.
	 */
	public ProxyReportingStundenplanungStundenplan(final ReportingRepository reportingRepository, final StundenplanManager stundenplanManager) {
		super("", "", "", -1, new ArrayList<>(), new ArrayList<>(), null, 0, new HashMap<>(), new ArrayList<>());

		this.reportingRepository = reportingRepository;
		this.stundenplanManager = stundenplanManager;

		if (this.stundenplanManager == null)
			return;

		super.id = this.stundenplanManager.stundenplanGetID();
		super.beschreibung = ersetzeNullBlankTrim(stundenplanManager.getBezeichnungStundenplan());
		super.gueltigAb = ersetzeNullBlankTrim(stundenplanManager.getGueltigAb());
		super.gueltigBis = ersetzeNullBlankTrim(stundenplanManager.getGueltigBis());
		super.schuljahresabschnitt = this.reportingRepository.schuljahresabschnitt(stundenplanManager.getIDSchuljahresabschnitt());
		super.wochenperiodizitaet = (this.stundenplanManager.getWochenTypModell() == 0) ? 1 : this.stundenplanManager.getWochenTypModell();
		for (int i = 1; i <= super.wochenperiodizitaet; i++) {
			super.mapWochenbezeichnungen.put(i, stundenplanManager.stundenplanGetWochenTypAsStringKurz(i));
		}

		super.setRaeume(this.stundenplanManager.raumGetMengeAsList().stream()
				.map(r -> (ReportingStundenplanungRaum) new ProxyReportingStundenplanungRaum(r, this))
				.toList());

		super.setRasterUnterrichteUndPausen(
				this.stundenplanManager.getListZeitraster().stream()
						.map(z -> (ReportingStundenplanungUnterrichtsrasterstunde) new ProxyReportingStundenplanungUnterrichtsrasterstunde(
								this.reportingRepository, z, this))
						.toList(),
				this.stundenplanManager.pausenzeitGetMengeAsList().stream()
						.map(pz -> (ReportingStundenplanungPausenzeit) new ProxyReportingStundenplanungPausenzeit(this.reportingRepository, pz, this))
						.toList());
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
