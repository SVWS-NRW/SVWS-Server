package de.nrw.schule.svws.db.dto.migration.schild.benutzer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Logins.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOProtokollLoginPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Login Zeit */
	public String LI_LoginTime;

	/** UserID des Logins */
	public Long LI_UserID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOProtokollLoginPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOProtokollLoginPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOProtokollLoginPK.
	 * @param LI_LoginTime   der Wert für das Attribut LI_LoginTime
	 * @param LI_UserID   der Wert für das Attribut LI_UserID
	 */
	public MigrationDTOProtokollLoginPK(final String LI_LoginTime, final Long LI_UserID) {
		if (LI_LoginTime == null) { 
			throw new NullPointerException("LI_LoginTime must not be null");
		}
		this.LI_LoginTime = LI_LoginTime;
		if (LI_UserID == null) { 
			throw new NullPointerException("LI_UserID must not be null");
		}
		this.LI_UserID = LI_UserID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOProtokollLoginPK other = (MigrationDTOProtokollLoginPK) obj;
		if (LI_LoginTime == null) {
			if (other.LI_LoginTime != null)
				return false;
		} else if (!LI_LoginTime.equals(other.LI_LoginTime))
			return false;

		if (LI_UserID == null) {
			if (other.LI_UserID != null)
				return false;
		} else if (!LI_UserID.equals(other.LI_UserID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LI_LoginTime == null) ? 0 : LI_LoginTime.hashCode());

		result = prime * result + ((LI_UserID == null) ? 0 : LI_UserID.hashCode());
		return result;
	}
}