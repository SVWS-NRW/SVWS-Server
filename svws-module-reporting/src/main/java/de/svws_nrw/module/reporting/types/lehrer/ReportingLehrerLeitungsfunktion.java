package de.svws_nrw.module.reporting.types.lehrer;

import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ LehrerFunktion.
 */
public class ReportingLehrerLeitungsfunktion extends ReportingBaseType {

	/** Die ID der Leitungsfunktion des Lehrers. (1 - Schulleitung, 2 - Stv. Schulleitung) */
	protected long idLeitungsfunktion;

	/** Die Bezeichnung der Leitungsfunktion, z. B. für Unterschriften. */
	protected String bezeichnung;

	/** Das Datum, mit welchem die Leitungsfunktion übernommen wurde. */
	protected String beginn;

	/** Das Datum, bis zu welchem die Leitungsfunktion übernommen wurde. */
	protected String ende;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param beginn             Das Datum, mit welchem die Leitungsfunktion übernommen wurde
	 * @param bezeichnung        Die Bezeichnung der Leitungsfunktion, z. B. für Unterschriften.
	 * @param ende               Das Datum, bis zu welchem die Leitungsfunktion übernommen wurde
	 * @param idLeitungsfunktion Die ID der Leitungsfunktion des Lehrers. (1 - Schulleitung, 2 - Stv. Schulleitung)
	 */
	public ReportingLehrerLeitungsfunktion(final String beginn, final String bezeichnung, final String ende, final long idLeitungsfunktion) {
		this.beginn = beginn;
		this.bezeichnung = bezeichnung;
		this.ende = ende;
		this.idLeitungsfunktion = idLeitungsfunktion;
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(idLeitungsfunktion);
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
		if (!(obj instanceof final ReportingLehrerLeitungsfunktion other))
			return false;
		return (idLeitungsfunktion == other.idLeitungsfunktion);
	}


	// ##### Berechnete Methoden #####

	/**
	 * Prüft, ob es sich bei der aktuellen Leitungsfunktion um die Schulleitung handelt.
	 *
	 * @return true, wenn die ID der Leitungsfunktion der Schulleitung (1) entspricht, sonst false
	 */
	public boolean istSchulleitungAktuell() {
		return ((idLeitungsfunktion == 1) && (ende == null));
	}

	/**
	 * Prüft, ob es sich bei der aktuellen Leitungsfunktion um die stellvertretende Schulleitung handelt.
	 *
	 * @return true, wenn die ID der Leitungsfunktion der stellvertretenden Schulleitung (2) entspricht, sonst false
	 */
	public boolean istStvSchulleitungAktuell() {
		return ((idLeitungsfunktion == 2) && (ende == null));
	}


	// ##### Getter #####

	/**
	 * Gibt die ID der Leitungsfunktion des Lehrers zurück.
	 *
	 * @return Die ID der Leitungsfunktion (1 - Schulleiter, 2 - Stv. Schulleiter)
	 */
	public long idLeitungsfunktion() {
		return idLeitungsfunktion;
	}

	/**
	 * Gibt die Bezeichnung der Leitungsfunktion zurück.
	 *
	 * @return Die Bezeichnung der Leitungsfunktion, z. B. für Unterschriften
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt das Datum zurück, mit welchem die Leitungsfunktion übernommen wurde.
	 *
	 * @return Das Datum des Beginns der Leitungsfunktion
	 */
	public String beginn() {
		return beginn;
	}

	/**
	 * Gibt das Datum zurück, bis zu welchem die Leitungsfunktion übernommen wurde.
	 *
	 * @return Das Datum des Endes der Leitungsfunktion
	 */
	public String ende() {
		return ende;
	}
}
