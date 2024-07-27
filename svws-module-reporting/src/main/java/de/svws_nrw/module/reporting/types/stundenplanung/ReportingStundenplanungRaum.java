package de.svws_nrw.module.reporting.types.stundenplanung;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Raum.</p>
 * <p>Sie enthält die Grunddaten eines Raumes, wie er im Stundenplan definiert wurde.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingStundenplanungRaum {

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


	// ##### Getter #####

	/**
	 * Die Beschreibung des Raumes.
	 * @return Inhalt des Feldes beschreibung
	 */
	public String beschreibung() {
		return beschreibung;
	}

	/**
	 * Die ID des Raumes, wenn dieser in einem Stundenplan zugeordnet wurde.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Optional: Die ID des Raumes im Katalog der Räume der Schule.
	 * @return Inhalt des Feldes idRaumKatalog
	 */
	public long idRaumKatalog() {
		return idRaumKatalog;
	}

	/**
	 * Optional: Die ID eines Stundenplans kann hier als ergänzende Information gespeichert werden.
	 * @return Inhalt des Feldes idStundenplan
	 */
	public long idStundenplan() {
		return idStundenplan;
	}

	/**
	 * Die Kapazität des Raumes (in Bezug auf die Anzahl der Schülerplätze).
	 * @return Inhalt des Feldes kapazitaet
	 */
	public int kapazitaet() {
		return kapazitaet;
	}

	/**
	 * Das Kürzel des Raumes.
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}
}
