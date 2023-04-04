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
	public Long Lehrer_ID;

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
	 * @param LehrbefKrz   der Wert für das Attribut LehrbefKrz
	 */
	public DTOLehrerLehramtBefaehigungPK(final Long Lehrer_ID, final String LehrbefKrz) {
		if (Lehrer_ID == null) {
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
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
		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
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
		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());

		result = prime * result + ((LehrbefKrz == null) ? 0 : LehrbefKrz.hashCode());
		return result;
	}
}
