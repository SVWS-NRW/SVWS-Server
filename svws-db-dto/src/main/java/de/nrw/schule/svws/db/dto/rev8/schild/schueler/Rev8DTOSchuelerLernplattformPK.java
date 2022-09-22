package de.nrw.schule.svws.db.dto.rev8.schild.schueler;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOSchuelerLernplattformPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** SchülerID für den Lernplattform-Datensatz */
	public Long SchuelerID;

	/** ID der Lernplattform */
	public Long LernplattformID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuelerLernplattformPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOSchuelerLernplattformPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuelerLernplattformPK.
	 * @param SchuelerID   der Wert für das Attribut SchuelerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 */
	public Rev8DTOSchuelerLernplattformPK(final Long SchuelerID, final Long LernplattformID) {
		if (SchuelerID == null) { 
			throw new NullPointerException("SchuelerID must not be null");
		}
		this.SchuelerID = SchuelerID;
		if (LernplattformID == null) { 
			throw new NullPointerException("LernplattformID must not be null");
		}
		this.LernplattformID = LernplattformID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchuelerLernplattformPK other = (Rev8DTOSchuelerLernplattformPK) obj;
		if (SchuelerID == null) {
			if (other.SchuelerID != null)
				return false;
		} else if (!SchuelerID.equals(other.SchuelerID))
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
		result = prime * result + ((SchuelerID == null) ? 0 : SchuelerID.hashCode());

		result = prime * result + ((LernplattformID == null) ? 0 : LernplattformID.hashCode());
		return result;
	}
}