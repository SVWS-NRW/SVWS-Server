package de.nrw.schule.svws.db.dto.migration.schild.lehrer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle LehrerDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOLehrerDatenschutzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** LehrerID des Datenschutzeintrags */
	public Long LehrerID;

	/** DatenschutzID des Eintrags */
	public Long DatenschutzID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerDatenschutzPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerDatenschutzPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerDatenschutzPK.
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 * @param DatenschutzID   der Wert für das Attribut DatenschutzID
	 */
	public MigrationDTOLehrerDatenschutzPK(final Long LehrerID, final Long DatenschutzID) {
		if (LehrerID == null) { 
			throw new NullPointerException("LehrerID must not be null");
		}
		this.LehrerID = LehrerID;
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
		MigrationDTOLehrerDatenschutzPK other = (MigrationDTOLehrerDatenschutzPK) obj;
		if (LehrerID == null) {
			if (other.LehrerID != null)
				return false;
		} else if (!LehrerID.equals(other.LehrerID))
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
		result = prime * result + ((LehrerID == null) ? 0 : LehrerID.hashCode());

		result = prime * result + ((DatenschutzID == null) ? 0 : DatenschutzID.hashCode());
		return result;
	}
}