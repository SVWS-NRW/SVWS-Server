package de.svws_nrw.db.dto.current.gost.kursblockung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Blockung_Regelparameter.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostBlockungRegelParameterPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Regel-Parameters */
	public long Regel_ID;

	/** Die Nummer des Parameters der Regel, beginnend bei 1 */
	public int Nummer;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungRegelParameterPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungRegelParameterPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungRegelParameterPK.
	 * @param Regel_ID   der Wert für das Attribut Regel_ID
	 * @param Nummer   der Wert für das Attribut Nummer
	 */
	public DTOGostBlockungRegelParameterPK(final long Regel_ID, final int Nummer) {
		this.Regel_ID = Regel_ID;
		this.Nummer = Nummer;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungRegelParameterPK other = (DTOGostBlockungRegelParameterPK) obj;
		if (Regel_ID != other.Regel_ID)
			return false;
		return Nummer == other.Nummer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Regel_ID);

		result = prime * result + Integer.hashCode(Nummer);
		return result;
	}
}
