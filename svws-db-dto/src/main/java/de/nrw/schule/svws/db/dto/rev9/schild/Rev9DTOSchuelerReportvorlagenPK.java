package de.nrw.schule.svws.db.dto.rev9.schild;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerReportvorlagen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev9DTOSchuelerReportvorlagenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** UserID des Users der die zugeordneten Reportvorlagen druckt */
	public Long User_ID;

	/** Pfad zur Reportvorlage */
	public String Reportvorlage;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerReportvorlagenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOSchuelerReportvorlagenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerReportvorlagenPK.
	 * @param User_ID   der Wert für das Attribut User_ID
	 * @param Reportvorlage   der Wert für das Attribut Reportvorlage
	 */
	public Rev9DTOSchuelerReportvorlagenPK(final Long User_ID, final String Reportvorlage) {
		if (User_ID == null) { 
			throw new NullPointerException("User_ID must not be null");
		}
		this.User_ID = User_ID;
		if (Reportvorlage == null) { 
			throw new NullPointerException("Reportvorlage must not be null");
		}
		this.Reportvorlage = Reportvorlage;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOSchuelerReportvorlagenPK other = (Rev9DTOSchuelerReportvorlagenPK) obj;
		if (User_ID == null) {
			if (other.User_ID != null)
				return false;
		} else if (!User_ID.equals(other.User_ID))
			return false;

		if (Reportvorlage == null) {
			if (other.Reportvorlage != null)
				return false;
		} else if (!Reportvorlage.equals(other.Reportvorlage))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((User_ID == null) ? 0 : User_ID.hashCode());

		result = prime * result + ((Reportvorlage == null) ? 0 : Reportvorlage.hashCode());
		return result;
	}
}