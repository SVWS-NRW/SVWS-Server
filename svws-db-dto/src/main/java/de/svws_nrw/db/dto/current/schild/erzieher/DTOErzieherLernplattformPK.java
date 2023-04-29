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
	public long ErzieherID;

	/** ID der Lernplattform */
	public long LernplattformID;

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
	public DTOErzieherLernplattformPK(final long ErzieherID, final long LernplattformID) {
		this.ErzieherID = ErzieherID;
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
		if (ErzieherID != other.ErzieherID)
			return false;
		return LernplattformID == other.LernplattformID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ErzieherID);

		result = prime * result + Long.hashCode(LernplattformID);
		return result;
	}
}
