package de.svws_nrw.module.reporting.types.stundenplanung;


import de.svws_nrw.core.types.Wochentag;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Zeitrasterstunde.
 */
public class ReportingStundenplanungZeitrasterstunde {

	/** Die ID der Zeitrasterstunde. */
	protected long id;

	/** Optional: Die ID eines Stundenplans kann hier als ergänzende Information gespeichert werden. */
	protected Long idStundenplan;

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde beginnt. NULL bedeutet "noch nicht definiert". */
	protected Integer stundenbeginn;

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde endet. NULL bedeutet "noch nicht definiert". */
	protected Integer stundenende;

	/** Die Nummer der Unterrichtsstunde an dem Wochentag. */
	protected int unterrichtstunde;

	/** Der {@link Wochentag} an dem der Unterricht stattfindet. */
	protected Wochentag wochentag;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param id				Die ID der Zeitrasterstunde.
	 * @param idStundenplan		Optional: Die ID eines Stundenplans kann hier als ergänzende Information gespeichert werden.
	 * @param stundenbeginn		Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde beginnt. NULL bedeutet "noch nicht definiert".
	 * @param stundenende		Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde endet. NULL bedeutet "noch nicht definiert".
	 * @param unterrichtstunde	Die Nummer der Unterrichtsstunde an dem Wochentag.
	 * @param wochentag			Der {@link Wochentag} an dem der Unterricht stattfindet.
	 */
	public ReportingStundenplanungZeitrasterstunde(final long id, final Long idStundenplan, final Integer stundenbeginn, final Integer stundenende,
			final int unterrichtstunde, final Wochentag wochentag) {
		this.id = id;
		this.idStundenplan = idStundenplan;
		this.stundenbeginn = stundenbeginn;
		this.stundenende = stundenende;
		this.unterrichtstunde = unterrichtstunde;
		this.wochentag = wochentag;
	}


	// ##### Getter #####

	/**
	 * Die ID der Zeitrasterstunde.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Optional: Die ID eines Stundenplans kann hier als ergänzende Information gespeichert werden.
	 *
	 * @return Inhalt des Feldes idStundenplan
	 */
	public Long idStundenplan() {
		return idStundenplan;
	}

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde beginnt. NULL bedeutet "noch nicht definiert".
	 *
	 * @return Inhalt des Feldes stundenbeginn
	 */
	public Integer stundenbeginn() {
		return stundenbeginn;
	}

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde endet. NULL bedeutet "noch nicht definiert".
	 *
	 * @return Inhalt des Feldes stundenende
	 */
	public Integer stundenende() {
		return stundenende;
	}

	/**
	 * Die Nummer der Unterrichtsstunde an dem Wochentag.
	 *
	 * @return Inhalt des Feldes unterrichtstunde
	 */
	public int unterrichtstunde() {
		return unterrichtstunde;
	}

	/**
	 * Der {@link Wochentag} an dem der Unterricht stattfindet.
	 *
	 * @return Inhalt des Feldes wochentag
	 */
	public Wochentag wochentag() {
		return wochentag;
	}
}
