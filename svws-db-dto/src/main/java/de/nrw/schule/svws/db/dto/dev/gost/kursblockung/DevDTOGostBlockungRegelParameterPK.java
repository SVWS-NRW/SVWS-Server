package de.nrw.schule.svws.db.dto.dev.gost.kursblockung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Blockung_Regelparameter.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DevDTOGostBlockungRegelParameterPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Regel-Parameters */
	public Long Regel_ID;

	/** Die Nummer des Parameters der Regel, beginnend bei 1 */
	public Integer Nummer;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostBlockungRegelParameterPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostBlockungRegelParameterPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostBlockungRegelParameterPK.
	 * @param Regel_ID   der Wert für das Attribut Regel_ID
	 * @param Nummer   der Wert für das Attribut Nummer
	 */
	public DevDTOGostBlockungRegelParameterPK(final Long Regel_ID, final Integer Nummer) {
		if (Regel_ID == null) { 
			throw new NullPointerException("Regel_ID must not be null");
		}
		this.Regel_ID = Regel_ID;
		if (Nummer == null) { 
			throw new NullPointerException("Nummer must not be null");
		}
		this.Nummer = Nummer;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOGostBlockungRegelParameterPK other = (DevDTOGostBlockungRegelParameterPK) obj;
		if (Regel_ID == null) {
			if (other.Regel_ID != null)
				return false;
		} else if (!Regel_ID.equals(other.Regel_ID))
			return false;

		if (Nummer == null) {
			if (other.Nummer != null)
				return false;
		} else if (!Nummer.equals(other.Nummer))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Regel_ID == null) ? 0 : Regel_ID.hashCode());

		result = prime * result + ((Nummer == null) ? 0 : Nummer.hashCode());
		return result;
	}
}