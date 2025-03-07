package de.svws_nrw.module.reporting.proxytypes.stundenplanung;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungUnterricht;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungUnterrichtsrasterstunde;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Zeitrasterstunde und erweitert die Klasse {@link ReportingStundenplanungUnterrichtsrasterstunde}.
 */
public class ProxyReportingStundenplanungUnterrichtsrasterstunde extends ReportingStundenplanungUnterrichtsrasterstunde {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Der Stundenplan-Manager, zu dem dieses Zeitraster-Element gehört. */
	@JsonIgnore
	private StundenplanManager stundenplanManager;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStundenplanungUnterrichtsrasterstunde}.
	 *
	 * @param reportingRepository Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param zeitraster		  Die Zeitrasterstunde aus dem Stundenplan.
	 * @param stundenplan         Der Stundenplan, zudem diese Zeitrasterstunde gehört.
	 */
	public ProxyReportingStundenplanungUnterrichtsrasterstunde(final ReportingRepository reportingRepository,
			final StundenplanZeitraster zeitraster, final ReportingStundenplanungStundenplan stundenplan) {
		super(zeitraster.id,
				stundenplan,
				zeitraster.stundenbeginn,
				zeitraster.stundenende,
				zeitraster.unterrichtstunde,
				null,
				new ArrayList<>());

		this.reportingRepository = reportingRepository;
		this.stundenplanManager = this.reportingRepository.stundenplanManager(stundenplan.id());

		if (stundenplanManager == null)
			return;

		try {
			// Prüfe, ob die ID des Zeitrasters zum Stundenplan-Manager passt.
			stundenplanManager.zeitrasterGetByIdOrException(zeitraster.id);
			// Setze den Wochentag, wenn es passt.
			super.wochentag = Wochentag.fromIDorException(zeitraster.wochentag);
		} catch (final Exception e) {
			this.stundenplanManager = null;
		}
	}


	// ##### Hash und Equals ud Compare-Methoden #####

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


	// ##### Überladene Methoden #####

	/**
	 * Liefert die Liste der Unterrichte einer Klasse für einen spezifischen Wochentyp.
	 * Wenn keine Unterrichte für die Klasse und den angegebenen Wochentyp vorhanden sind,
	 * werden sie aus dem zugehörigen Manager geladen und zwischengespeichert.
	 *
	 * @param idKlasse               Die ID der Klasse, für den die Unterrichte ermittelt werden sollen.
	 * @param wochentyp              Der gewünschte Wochentyp (z. B. 1 oder 2).
	 * @param inklusiveWochentyp0    Gibt an, ob auch Unterrichte des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste von Unterrichte der Klasse für den angegebenen Wochentyp.
	 *         Kann leer sein, wenn keine Unterrichte vorhanden sind.
	 */
	@Override
	public List<ReportingStundenplanungUnterricht> unterrichteKlasse(final long idKlasse, final int wochentyp,
			final boolean inklusiveWochentyp0) {
		// Wenn noch keine Unterrichte zur Klasse in dieser Zeitrasterstunde vorliegen, lade sie aus dem Manager.
		if ((this.stundenplanManager != null) && super.unterrichteKlasse(idKlasse, wochentyp, inklusiveWochentyp0).isEmpty()) {
			final List<ReportingStundenplanungUnterricht> result =
					stundenplanManager.unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idKlasse, this.wochentag.id,
							this.stundeImUnterrichtsraster, wochentyp, inklusiveWochentyp0).stream()
							.map(u -> (ReportingStundenplanungUnterricht) new ProxyReportingStundenplanungUnterricht(this.reportingRepository,
									this.stundenplan, u))
							.toList();
			super.addUnterrichte(result);
		}

		return super.unterrichteKlasse(idKlasse, wochentyp, inklusiveWochentyp0);
	}

	/**
	 * Liefert die Liste der Unterrichte eines Lehrers für einen spezifischen Wochentyp.
	 * Wenn keine Unterrichte für den Lehrer und den angegebenen Wochentyp vorhanden sind,
	 * werden sie aus dem zugehörigen Manager geladen und zwischengespeichert.
	 *
	 * @param idLehrer               Die ID des Lehrers, für den die Unterrichte ermittelt werden sollen.
	 * @param wochentyp              Der gewünschte Wochentyp (z. B. 1 oder 2).
	 * @param inklusiveWochentyp0    Gibt an, ob auch Unterrichte des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste von Unterrichte des Lehrers für den angegebenen Wochentyp.
	 *         Kann leer sein, wenn keine Unterrichte vorhanden sind.
	 */
	@Override
	public List<ReportingStundenplanungUnterricht> unterrichteLehrkraft(final long idLehrer, final int wochentyp,
			final boolean inklusiveWochentyp0) {
		// Wenn noch keine Unterrichte zum Lehrer in dieser Zeitrasterstunde vorliegen, lade sie aus dem Manager.
		if ((this.stundenplanManager != null) && super.unterrichteLehrkraft(idLehrer, wochentyp, inklusiveWochentyp0).isEmpty()) {
			final List<ReportingStundenplanungUnterricht> result =
					stundenplanManager.unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrer, this.wochentag.id,
							this.stundeImUnterrichtsraster, wochentyp, inklusiveWochentyp0).stream()
							.map(u -> (ReportingStundenplanungUnterricht) new ProxyReportingStundenplanungUnterricht(this.reportingRepository,
									this.stundenplan, u))
							.toList();
			super.addUnterrichte(result);
		}

		return super.unterrichteLehrkraft(idLehrer, wochentyp, inklusiveWochentyp0);
	}
}
