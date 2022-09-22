package de.nrw.schule.svws.db.dto.rev8.schild.erzieher;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle ErzieherDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOErzieherDatenschutzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ErzieherID des Datenschutzeintrags */
	public Long ErzieherID;

	/** DatenschutzID des Eintrags */
	public Long DatenschutzID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOErzieherDatenschutzPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOErzieherDatenschutzPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOErzieherDatenschutzPK.
	 * @param ErzieherID   der Wert für das Attribut ErzieherID
	 * @param DatenschutzID   der Wert für das Attribut DatenschutzID
	 */
	public Rev8DTOErzieherDatenschutzPK(final Long ErzieherID, final Long DatenschutzID) {
		if (ErzieherID == null) { 
			throw new NullPointerException("ErzieherID must not be null");
		}
		this.ErzieherID = ErzieherID;
		if (DatenschutzID == null) { 
			throw new NullPointerException("DatenschutzID must not be null");
		}
		this.DatenschutzID = DatenschutzID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOErzieherDatenschutzPK other = (Rev8DTOErzieherDatenschutzPK) obj;
		if (ErzieherID == null) {
			if (other.ErzieherID != null)
				return false;
		} else if (!ErzieherID.equals(other.ErzieherID))
			return false;

		if (DatenschutzID == null) {
			if (other.DatenschutzID != null)
				return false;
		} else if (!DatenschutzID.equals(other.DatenschutzID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ErzieherID == null) ? 0 : ErzieherID.hashCode());

		result = prime * result + ((DatenschutzID == null) ? 0 : DatenschutzID.hashCode());
		return result;
	}
}