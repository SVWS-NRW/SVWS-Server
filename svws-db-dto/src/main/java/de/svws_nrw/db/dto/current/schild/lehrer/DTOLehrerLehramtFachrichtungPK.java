package de.svws_nrw.db.dto.current.schild.lehrer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle LehrerLehramtFachr.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOLehrerLehramtFachrichtungPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** LehrerID zu der die Fachrichtung gehört */
	public long Lehrer_ID;

	/** Fachrichtungskürzel */
	public String FachrKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLehramtFachrichtungPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerLehramtFachrichtungPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLehramtFachrichtungPK.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param FachrKrz   der Wert für das Attribut FachrKrz
	 */
	public DTOLehrerLehramtFachrichtungPK(final long Lehrer_ID, final String FachrKrz) {
		this.Lehrer_ID = Lehrer_ID;
		if (FachrKrz == null) {
			throw new NullPointerException("FachrKrz must not be null");
		}
		this.FachrKrz = FachrKrz;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerLehramtFachrichtungPK other = (DTOLehrerLehramtFachrichtungPK) obj;
		if (Lehrer_ID != other.Lehrer_ID)
			return false;
		if (FachrKrz == null) {
			if (other.FachrKrz != null)
				return false;
		} else if (!FachrKrz.equals(other.FachrKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Lehrer_ID);

		result = prime * result + ((FachrKrz == null) ? 0 : FachrKrz.hashCode());
		return result;
	}
}
