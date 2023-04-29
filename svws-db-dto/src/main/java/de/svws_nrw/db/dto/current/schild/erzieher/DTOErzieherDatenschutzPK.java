package de.svws_nrw.db.dto.current.schild.erzieher;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle ErzieherDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOErzieherDatenschutzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ErzieherID des Datenschutzeintrags */
	public long ErzieherID;

	/** DatenschutzID des Eintrags */
	public long DatenschutzID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherDatenschutzPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOErzieherDatenschutzPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherDatenschutzPK.
	 * @param ErzieherID   der Wert für das Attribut ErzieherID
	 * @param DatenschutzID   der Wert für das Attribut DatenschutzID
	 */
	public DTOErzieherDatenschutzPK(final long ErzieherID, final long DatenschutzID) {
		this.ErzieherID = ErzieherID;
		this.DatenschutzID = DatenschutzID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOErzieherDatenschutzPK other = (DTOErzieherDatenschutzPK) obj;
		if (ErzieherID != other.ErzieherID)
			return false;
		return DatenschutzID == other.DatenschutzID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ErzieherID);

		result = prime * result + Long.hashCode(DatenschutzID);
		return result;
	}
}
