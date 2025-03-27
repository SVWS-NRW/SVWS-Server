package de.svws_nrw.module.reporting.types.stundenplanung;


import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Pausenaufsicht im Rahmen der Stundenplanung.
 */
public class ReportingStundenplanungPausenaufsicht extends ReportingBaseType {

	/** Die ID der Pausenaufsicht */
	protected long id;

	/** Die Beschreibung des Aufsichtsbereichs, die dieser Pausenaufsicht zugeordnet ist. */
	protected String aufsichtsbereichBeschreibung;

	/** Das Kürzel des Aufsichtsbereichs, die dieser Pausenaufsicht zugeordnet ist. */
	protected String aufsichtsbereichKuerzel;

	/** Die ID des Aufsichtsbereichs, die dieser Pausenaufsicht zugeordnet ist. */
	protected long idAufsichtsbereich;

	/** Die Lehrkraft, die dieser Pausenaufsicht zugeordnet. */
	protected ReportingLehrer lehrkraft;

	/** Der Wochen-Typ bei der Unterscheidung von Mehrwochenplänen (-> 1, 2, ...) oder 0 */
	protected int wochentyp;

	/**
	 * Erzeugt eine neue Instanz der Klasse {@link ReportingStundenplanungPausenaufsicht}.
	 *
	 * @param id                            die ID der Pausenaufsicht
	 * @param aufsichtsbereichBeschreibung  die Beschreibung des Aufsichtsbereichs, der dieser Pausenaufsicht zugeordnet ist
	 * @param aufsichtsbereichKuerzel       das Kürzel des Aufsichtsbereichs, der dieser Pausenaufsicht zugeordnet ist
	 * @param idAufsichtsbereich            die ID des Aufsichtsbereichs, der dieser Pausenaufsicht zugeordnet ist
	 * @param lehrkraft                     die Lehrkraft, die dieser Pausenaufsicht zugeordnet ist
	 * @param wochentyp                     der Wochen-Typ bei der Unterscheidung von Mehrwochenplänen oder 0
	 */
	public ReportingStundenplanungPausenaufsicht(final long id, final String aufsichtsbereichBeschreibung, final String aufsichtsbereichKuerzel,
			final long idAufsichtsbereich, final ReportingLehrer lehrkraft, final int wochentyp) {
		this.id = id;
		this.aufsichtsbereichBeschreibung = aufsichtsbereichBeschreibung;
		this.aufsichtsbereichKuerzel = aufsichtsbereichKuerzel;
		this.idAufsichtsbereich = idAufsichtsbereich;
		this.lehrkraft = lehrkraft;
		this.wochentyp = wochentyp;
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
		if (!(obj instanceof final ReportingStundenplanungStundenplan other))
			return false;
		return (id == other.id);
	}

	// ##### Berechnete Methoden #####



	// ##### Getter #####
	/**
	 * Gibt die ID der Pausenaufsicht zurück.
	 *
	 * @return die ID der Pausenaufsicht.
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt die Beschreibung des Aufsichtsbereichs zurück, der dieser Pausenaufsicht zugeordnet ist.
	 *
	 * @return die Beschreibung des Aufsichtsbereichs.
	 */
	public String aufsichtsbereichBeschreibung() {
		return aufsichtsbereichBeschreibung;
	}

	/**
	 * Gibt das Kürzel des Aufsichtsbereichs zurück, der dieser Pausenaufsicht zugeordnet ist.
	 *
	 * @return das Kürzel des Aufsichtsbereichs.
	 */
	public String aufsichtsbereichKuerzel() {
		return aufsichtsbereichKuerzel;
	}

	/**
	 * Gibt die ID des Aufsichtsbereichs zurück, der dieser Pausenaufsicht zugeordnet ist.
	 *
	 * @return die ID des Aufsichtsbereichs.
	 */
	public long idAufsichtsbereich() {
		return idAufsichtsbereich;
	}

	/**
	 * Gibt die Lehrkraft zurück, die dieser Pausenaufsicht zugeordnet ist.
	 *
	 * @return die Lehrkraft.
	 */
	public ReportingLehrer lehrkraft() {
		return lehrkraft;
	}

	/**
	 * Gibt den Wochen-Typ bei der Unterscheidung von Mehrwochenplänen zurück.
	 *
	 * @return Der Wochen-Typ diesem Unterricht oder 0.
	 */
	 public int wochentyp() {
		return wochentyp;
	}
}
