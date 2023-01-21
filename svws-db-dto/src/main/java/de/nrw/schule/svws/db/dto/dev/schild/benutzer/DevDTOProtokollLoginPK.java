package de.nrw.schule.svws.db.dto.dev.schild.benutzer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Logins.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DevDTOProtokollLoginPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** UserID des Logins */
	public Long LI_UserID;

	/** Login Zeit */
	public String LI_LoginTime;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOProtokollLoginPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOProtokollLoginPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOProtokollLoginPK.
	 * @param LI_UserID   der Wert für das Attribut LI_UserID
	 * @param LI_LoginTime   der Wert für das Attribut LI_LoginTime
	 */
	public DevDTOProtokollLoginPK(final Long LI_UserID, final String LI_LoginTime) {
		if (LI_UserID == null) { 
			throw new NullPointerException("LI_UserID must not be null");
		}
		this.LI_UserID = LI_UserID;
		if (LI_LoginTime == null) { 
			throw new NullPointerException("LI_LoginTime must not be null");
		}
		this.LI_LoginTime = LI_LoginTime;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOProtokollLoginPK other = (DevDTOProtokollLoginPK) obj;
		if (LI_UserID == null) {
			if (other.LI_UserID != null)
				return false;
		} else if (!LI_UserID.equals(other.LI_UserID))
			return false;

		if (LI_LoginTime == null) {
			if (other.LI_LoginTime != null)
				return false;
		} else if (!LI_LoginTime.equals(other.LI_LoginTime))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LI_UserID == null) ? 0 : LI_UserID.hashCode());

		result = prime * result + ((LI_LoginTime == null) ? 0 : LI_LoginTime.hashCode());
		return result;
	}
}