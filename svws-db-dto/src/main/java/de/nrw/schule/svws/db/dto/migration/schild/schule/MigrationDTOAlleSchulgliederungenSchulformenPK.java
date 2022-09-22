package de.nrw.schule.svws.db.dto.migration.schild.schule;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schulgliederungen_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOAlleSchulgliederungenSchulformenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** die ID der Schulgliederung */
	public Long Schulgliederung_ID;

	/** das Kürzel der Schulform */
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleSchulgliederungenSchulformenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAlleSchulgliederungenSchulformenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleSchulgliederungenSchulformenPK.
	 * @param Schulgliederung_ID   der Wert für das Attribut Schulgliederung_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public MigrationDTOAlleSchulgliederungenSchulformenPK(final Long Schulgliederung_ID, final String Schulform_Kuerzel) {
		if (Schulgliederung_ID == null) { 
			throw new NullPointerException("Schulgliederung_ID must not be null");
		}
		this.Schulgliederung_ID = Schulgliederung_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAlleSchulgliederungenSchulformenPK other = (MigrationDTOAlleSchulgliederungenSchulformenPK) obj;
		if (Schulgliederung_ID == null) {
			if (other.Schulgliederung_ID != null)
				return false;
		} else if (!Schulgliederung_ID.equals(other.Schulgliederung_ID))
			return false;

		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schulgliederung_ID == null) ? 0 : Schulgliederung_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}
}