package de.svws_nrw.db.dto.migration.schild.lehrer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle LehrerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class MigrationDTOLehrerLernplattformPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** LehrerID für den Lernplattform-Datensatz */
	public Long LehrerID;

	/** ID der Lernplattform */
	public Long LernplattformID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLernplattformPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerLernplattformPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLernplattformPK.
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 */
	public MigrationDTOLehrerLernplattformPK(final Long LehrerID, final Long LernplattformID) {
		if (LehrerID == null) {
			throw new NullPointerException("LehrerID must not be null");
		}
		this.LehrerID = LehrerID;
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
		MigrationDTOLehrerLernplattformPK other = (MigrationDTOLehrerLernplattformPK) obj;
		if (LehrerID == null) {
			if (other.LehrerID != null)
				return false;
		} else if (!LehrerID.equals(other.LehrerID))
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
		result = prime * result + ((LehrerID == null) ? 0 : LehrerID.hashCode());

		result = prime * result + ((LernplattformID == null) ? 0 : LernplattformID.hashCode());
		return result;
	}
}
