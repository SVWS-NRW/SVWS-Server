package de.svws_nrw.db.dto.current.schild.schueler;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOSchuelerLernplattformPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** SchülerID für den Lernplattform-Datensatz */
	public long SchuelerID;

	/** ID der Lernplattform */
	public long LernplattformID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLernplattformPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerLernplattformPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLernplattformPK.
	 * @param SchuelerID   der Wert für das Attribut SchuelerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 */
	public DTOSchuelerLernplattformPK(final long SchuelerID, final long LernplattformID) {
		this.SchuelerID = SchuelerID;
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
		DTOSchuelerLernplattformPK other = (DTOSchuelerLernplattformPK) obj;
		if (SchuelerID != other.SchuelerID)
			return false;
		return LernplattformID == other.LernplattformID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(SchuelerID);

		result = prime * result + Long.hashCode(LernplattformID);
		return result;
	}
}
