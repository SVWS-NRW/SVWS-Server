package de.nrw.schule.svws.db.dto.rev8.views.schildintern;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Schildintern_Berufsebene.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOSchildInternBerufsebenenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die Berufsebene für Fachklassen */
	public Integer Berufsebene;

	/** Das Kürzel der Berufsebene */
	public String Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchildInternBerufsebenenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOSchildInternBerufsebenenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchildInternBerufsebenenPK.
	 * @param Berufsebene   der Wert für das Attribut Berufsebene
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public Rev8DTOSchildInternBerufsebenenPK(final Integer Berufsebene, final String Kuerzel) {
		if (Berufsebene == null) { 
			throw new NullPointerException("Berufsebene must not be null");
		}
		this.Berufsebene = Berufsebene;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchildInternBerufsebenenPK other = (Rev8DTOSchildInternBerufsebenenPK) obj;
		if (Berufsebene == null) {
			if (other.Berufsebene != null)
				return false;
		} else if (!Berufsebene.equals(other.Berufsebene))
			return false;

		if (Kuerzel == null) {
			if (other.Kuerzel != null)
				return false;
		} else if (!Kuerzel.equals(other.Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Berufsebene == null) ? 0 : Berufsebene.hashCode());

		result = prime * result + ((Kuerzel == null) ? 0 : Kuerzel.hashCode());
		return result;
	}
}