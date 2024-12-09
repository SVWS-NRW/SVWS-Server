package de.svws_nrw.module.reporting.types.schueler.erzieher;

import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Erzieher-Art.
 */
public class ReportingErzieherArt extends ReportingBaseType {

	/** Bezeichnung der Erzieher-Art */
	protected String bezeichnung;

	/** id der Erzieher-Art */
	protected long id;

	/** Der Wert der Sortierung der Erzieher-Art. */
	protected Integer sortierung;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param bezeichnung	Bezeichnung der Erzieher-Art
	 * @param id			id der Erzieher-Art
	 * @param sortierung	Der Wert der Sortierung der Erzieher-Art.
	 */
	public ReportingErzieherArt(final String bezeichnung, final long id, final Integer sortierung) {
		this.bezeichnung = bezeichnung;
		this.id = id;
		this.sortierung = sortierung;
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
		if (!(obj instanceof final ReportingErzieherArt other))
			return false;
		return (id == other.id);
	}


// ##### Getter #####
	/**
	 * Bezeichnung der Erzieher-Art
	 *
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * ID der Erzieher-Art
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Der Wert der Sortierung der Erzieher-Art.
	 *
	 * @return Inhalt des Feldes sortierung
	 */
	public Integer sortierung() {
		return sortierung;
	}
}
