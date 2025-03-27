package de.svws_nrw.module.reporting.types.stundenplanung;


import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Raum.
 */
public class ReportingStundenplanungRaum extends ReportingBaseType {

	/** Die Beschreibung des Raumes. */
	protected String beschreibung;

	/** Optional: Die ID des Raumes, wenn dieser in einem Stundenplan zugeordnet wurde. */
	protected long id;

	/** Optional: Die ID des Raumes im Katalog der Räume der Schule. */
	protected Long idRaumKatalog;

	/** Optional: Die ID eines Stundenplans kann hier als ergänzende Information gespeichert werden. */
	protected Long idStundenplan;

	/** Die Kapazität des Raumes (in Bezug auf die Anzahl der Schülerplätze). */
	protected int kapazitaet;

	/** Das Kürzel des Raumes. */
	protected String kuerzel;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param beschreibung					Die Beschreibung des Raumes.
	 * @param id							Die ID des Raumes, wenn dieser in einem Stundenplan zugeordnet wurde.
	 * @param idRaumKatalog					Optional: Die ID des Raumes im Katalog der Räume der Schule.
	 * @param idStundenplan					Optional: Die ID eines Stundenplans kann hier als ergänzende Information gespeichert werden.
	 * @param kapazitaet					Die Kapazität des Raumes (in Bezug auf die Anzahl der Schülerplätze).
	 * @param kuerzel						Das Kürzel des Raumes.
	 */
	public ReportingStundenplanungRaum(final String beschreibung, final long id, final Long idRaumKatalog, final Long idStundenplan, final int kapazitaet,
			final String kuerzel) {
		this.beschreibung = beschreibung;
		this.id = id;
		this.idRaumKatalog = idRaumKatalog;
		this.idStundenplan = idStundenplan;
		this.kapazitaet = kapazitaet;
		this.kuerzel = kuerzel;
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingStundenplanungRaum other))
			return false;
		return (id == other.id);
	}


	// ##### Getter #####

	/**
	 * Die Beschreibung des Raumes.
	 *
	 * @return Inhalt des Feldes beschreibung
	 */
	public String beschreibung() {
		return beschreibung;
	}

	/**
	 * Die ID des Raumes, wenn dieser in einem Stundenplan zugeordnet wurde.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Optional: Die ID des Raumes im Katalog der Räume der Schule.
	 *
	 * @return Inhalt des Feldes idRaumKatalog
	 */
	public long idRaumKatalog() {
		return idRaumKatalog;
	}

	/**
	 * Optional: Die ID eines Stundenplans kann hier als ergänzende Information gespeichert werden.
	 *
	 * @return Inhalt des Feldes idStundenplan
	 */
	public long idStundenplan() {
		return idStundenplan;
	}

	/**
	 * Die Kapazität des Raumes (in Bezug auf die Anzahl der Schülerplätze).
	 *
	 * @return Inhalt des Feldes kapazitaet
	 */
	public int kapazitaet() {
		return kapazitaet;
	}

	/**
	 * Das Kürzel des Raumes.
	 *
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}
}
