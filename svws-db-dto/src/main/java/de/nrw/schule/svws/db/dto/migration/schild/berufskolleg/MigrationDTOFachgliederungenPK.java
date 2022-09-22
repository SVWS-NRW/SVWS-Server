package de.nrw.schule.svws.db.dto.migration.schild.berufskolleg;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Fach_Gliederungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOFachgliederungenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	public Long Fach_ID;

	/** Fachklassen ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	public Long Fachklasse_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachgliederungenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFachgliederungenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachgliederungenPK.
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Fachklasse_ID   der Wert für das Attribut Fachklasse_ID
	 */
	public MigrationDTOFachgliederungenPK(final Long Fach_ID, final Long Fachklasse_ID) {
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
		if (Fachklasse_ID == null) { 
			throw new NullPointerException("Fachklasse_ID must not be null");
		}
		this.Fachklasse_ID = Fachklasse_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFachgliederungenPK other = (MigrationDTOFachgliederungenPK) obj;
		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;

		if (Fachklasse_ID == null) {
			if (other.Fachklasse_ID != null)
				return false;
		} else if (!Fachklasse_ID.equals(other.Fachklasse_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());

		result = prime * result + ((Fachklasse_ID == null) ? 0 : Fachklasse_ID.hashCode());
		return result;
	}
}