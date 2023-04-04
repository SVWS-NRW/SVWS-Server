package de.svws_nrw.db.dto.current.schild.erzieher;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle ErzieherLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOErzieherLernplattformPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ErzieherID für den Lernplattform-Datensatz */
	public Long ErzieherID;

	/** ID der Lernplattform */
	public Long LernplattformID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherLernplattformPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOErzieherLernplattformPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherLernplattformPK.
	 * @param ErzieherID   der Wert für das Attribut ErzieherID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 */
	public DTOErzieherLernplattformPK(final Long ErzieherID, final Long LernplattformID) {
		if (ErzieherID == null) {
			throw new NullPointerException("ErzieherID must not be null");
		}
		this.ErzieherID = ErzieherID;
		if (LernplattformID == null) {
			throw new NullPointerException("LernplattformID must not be null");
		}
		this.LernplattformID = LernplattformID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOErzieherLernplattformPK other = (DTOErzieherLernplattformPK) obj;
		if (ErzieherID == null) {
			if (other.ErzieherID != null)
				return false;
		} else if (!ErzieherID.equals(other.ErzieherID))
			return false;

		if (LernplattformID == null) {
			if (other.LernplattformID != null)
				return false;
		} else if (!LernplattformID.equals(other.LernplattformID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ErzieherID == null) ? 0 : ErzieherID.hashCode());

		result = prime * result + ((LernplattformID == null) ? 0 : LernplattformID.hashCode());
		return result;
	}
}
