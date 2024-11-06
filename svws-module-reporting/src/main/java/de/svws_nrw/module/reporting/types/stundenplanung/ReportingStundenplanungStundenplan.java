package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.List;

import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Stundenplan.
 */
public class ReportingStundenplanungStundenplan {

	/** Die Beschreibung des Stundenplans. */
	protected String beschreibung;

	/** Datum, ab dem der Stundenplan gültig ist. */
	protected String gueltigAb;

	/** Datum, bis zu dem der Stundenplan gültig ist. */
	protected String gueltigBis;

	/** Die ID des Stundenplans. */
	protected Long id;

	/** Eine Liste aller Räume im Stundenplan. */
	protected List<ReportingStundenplanungRaum> raeume;

	/** Der Schuljahresabschnitt, dem dieser Stundenplan zugeordnet ist. */
	protected ReportingSchuljahresabschnitt schuljahresabschnitt;

	/** Das Zeitraster dieses Stundenplanes. */
	protected List<ReportingStundenplanungZeitrasterstunde> zeitraster;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param beschreibung			Die Beschreibung des Stundenplans
	 * @param gueltigAb				Datum, ab dem der Stundenplan gültig ist.
	 * @param gueltigBis			Datum, bis zu dem der Stundenplan gültig ist.
	 * @param id					Die ID des Stundenplans.
	 * @param raeume				Eine Liste aller Räume im Stundenplan.
	 * @param schuljahresabschnitt	Der Schuljahresabschnitt, dem dieser Stundenplan zugeordnet ist.
	 * @param zeitraster 			Das Zeitraster dieses Stundenplanes.
	 */
	public ReportingStundenplanungStundenplan(final String beschreibung, final String gueltigAb, final String gueltigBis, final Long id,
			final List<ReportingStundenplanungRaum> raeume, final ReportingSchuljahresabschnitt schuljahresabschnitt,
			final List<ReportingStundenplanungZeitrasterstunde> zeitraster) {
		this.beschreibung = beschreibung;
		this.gueltigAb = gueltigAb;
		this.gueltigBis = gueltigBis;
		this.id = id;
		this.raeume = raeume;
		this.schuljahresabschnitt = schuljahresabschnitt;
		this.zeitraster = zeitraster;
	}


	// ##### Berechnete Methode #####

	/**
	 * Prüft, ob das übergebene Datum im Zeitbereich des Stundenplans liegt.
	 *
	 * @param datum Das zu prüfende Datum im Format yyyy-mm-dd
	 *
	 * @return true, wenn das Datum im Zeitbereich des Stundenplans liegt, sonst false. Ist nur eine Gültigkeitsangabe im Plan gesetzt, wird nur gegen diese geprüft.
	 */
	public boolean istDatumImStundenplan(final String datum) {
		if ((datum == null) || (datum.length() != 10))
			return false;
		if ((gueltigAb != null) && (gueltigBis != null))
			return (datum.compareTo(gueltigAb) >= 0) && (datum.compareTo(gueltigBis) <= 0);
		else if (gueltigAb != null)
			return (datum.compareTo(gueltigAb) >= 0);
		else if (gueltigBis != null)
			return (datum.compareTo(gueltigBis) <= 0);

		return false;
	}

	/**
	 * Gibt den Raum zur übergebenen ID aus dem Stundenplan zurück.
	 *
	 * @param idRaumStundenplan Die ID des Raumes im Stundenplan.
	 *
	 * @return Raum zur ID oder null, wenn es zu ID keinen Raum im Stundenplan gibt.
	 */
	public ReportingStundenplanungRaum raum(final Long idRaumStundenplan) {
		if (idRaumStundenplan == null)
			return null;
		return this.raeume.stream().filter(r -> idRaumStundenplan.equals(r.id)).findFirst().orElse(null);
	}

	/**
	 * Gibt die Stunde im Zeitraster zur übergebenen ID des Zeitrastereintrags aus dem Stundenplan zurück.
	 *
	 * @param id Die ID des Zeitrastereintrags im Stundenplan.
	 *
	 * @return Stunde im Zeitraster zur ID oder null, wenn es zu ID keinem Zeitrastereintrag im Stundenplan gibt.
	 */
	public ReportingStundenplanungZeitrasterstunde zeitrasterstunde(final Long id) {
		if (id == null)
			return null;
		return this.zeitraster.stream().filter(z -> id.equals(z.id)).findFirst().orElse(null);
	}

	/**
	 * Gibt die Stunde im Zeitraster zum übergebenen Tag und zur übergebenen Stunde aus dem Stundenplan zurück.
	 *
	 * @param wochentag 		Der Wochentag der Unterrichtsstunde aus dem Zeitraster
	 * @param unterrichtstunde 	Die Unterrichtstunde am übergebenen Wochentag.
	 *
	 * @return Stunde im Zeitraster oder null, wenn es zu ID keinem Zeitrastereintrag im Stundenplan gibt.
	 */
	public ReportingStundenplanungZeitrasterstunde zeitrasterstunde(final Wochentag wochentag, final int unterrichtstunde) {
		if ((wochentag == null) || (unterrichtstunde < 0))
			return null;
		return this.zeitraster.stream().filter(z -> ((wochentag == z.wochentag) && (unterrichtstunde == z.unterrichtstunde))).findFirst().orElse(null);
	}



	// ##### Getter #####

	/**
	 * Die Beschreibung des Stundenplans.
	 *
	 * @return Inhalt des Feldes beschreibung
	 */
	public String beschreibung() {
		return beschreibung;
	}

	/**
	 * Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return Inhalt des Feldes gueltigAb
	 */
	public String gueltigAb() {
		return gueltigAb;
	}

	/**
	 * Datum, bis zu dem der Stundenplan gültig ist.
	 *
	 * @return Inhalt des Feldes gueltigBis
	 */
	public String gueltigBis() {
		return gueltigBis;
	}

	/**
	 * Die ID des Stundenplans.
	 *
	 * @return Inhalt des Feldes id
	 */
	public Long id() {
		return id;
	}

	/**
	 * Eine Liste aller Räume im Stundenplan.
	 *
	 * @return Inhalt des Feldes raeume
	 */
	public List<ReportingStundenplanungRaum> raeume() {
		return raeume;
	}

	/**
	 * Der Schuljahresabschnitt, dem dieser Stundenplan zugeordnet ist.
	 *
	 * @return Inhalt des Feldes schuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return schuljahresabschnitt;
	}

	/**
	 * Das Zeitraster dieses Stundenplanes.
	 *
	 * @return Inhalt des Feldes zeitraster
	 */
	public List<ReportingStundenplanungZeitrasterstunde> zeitraster() {
		return zeitraster;
	}
}
