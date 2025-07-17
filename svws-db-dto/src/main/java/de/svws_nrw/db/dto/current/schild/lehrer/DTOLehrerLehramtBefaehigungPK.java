package de.svws_nrw.db.dto.current.schild.lehrer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle LehrerLehramtLehrbef.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOLehrerLehramtBefaehigungPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** LehrerID zu der die Lehrbefähigung gehört */
	public long Lehrer_ID;

	/** Lehramtskürzel */
	public String LehramtKrz;

	/** Kürzel der Lehrbefähigung */
	public String LehrbefKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLehramtBefaehigungPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerLehramtBefaehigungPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLehramtBefaehigungPK.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param LehramtKrz   der Wert für das Attribut LehramtKrz
	 * @param LehrbefKrz   der Wert für das Attribut LehrbefKrz
	 */
	public DTOLehrerLehramtBefaehigungPK(final long Lehrer_ID, final String LehramtKrz, final String LehrbefKrz) {
		this.Lehrer_ID = Lehrer_ID;
		if (LehramtKrz == null) {
			throw new NullPointerException("LehramtKrz must not be null");
		}
		this.LehramtKrz = LehramtKrz;
		if (LehrbefKrz == null) {
			throw new NullPointerException("LehrbefKrz must not be null");
		}
		this.LehrbefKrz = LehrbefKrz;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerLehramtBefaehigungPK other = (DTOLehrerLehramtBefaehigungPK) obj;
		if (Lehrer_ID != other.Lehrer_ID)
			return false;
		if (LehramtKrz == null) {
			if (other.LehramtKrz != null)
				return false;
		} else if (!LehramtKrz.equals(other.LehramtKrz))
			return false;
		if (LehrbefKrz == null) {
			if (other.LehrbefKrz != null)
				return false;
		} else if (!LehrbefKrz.equals(other.LehrbefKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Lehrer_ID);

		result = prime * result + ((LehramtKrz == null) ? 0 : LehramtKrz.hashCode());

		result = prime * result + ((LehrbefKrz == null) ? 0 : LehrbefKrz.hashCode());
		return result;
	}
}
