package de.svws_nrw.db.dto.current.schild.lehrer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle LehrerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOLehrerLernplattformPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** LehrerID für den Lernplattform-Datensatz */
	public long LehrerID;

	/** ID der Lernplattform */
	public long LernplattformID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLernplattformPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerLernplattformPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLernplattformPK.
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 */
	public DTOLehrerLernplattformPK(final long LehrerID, final long LernplattformID) {
		this.LehrerID = LehrerID;
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
		DTOLehrerLernplattformPK other = (DTOLehrerLernplattformPK) obj;
		if (LehrerID != other.LehrerID)
			return false;
		return LernplattformID == other.LernplattformID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(LehrerID);

		result = prime * result + Long.hashCode(LernplattformID);
		return result;
	}
}
