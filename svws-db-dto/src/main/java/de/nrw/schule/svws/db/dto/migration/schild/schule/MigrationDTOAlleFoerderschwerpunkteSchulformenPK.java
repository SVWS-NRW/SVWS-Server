package de.nrw.schule.svws.db.dto.migration.schild.schule;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Foerderschwerpunkte_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOAlleFoerderschwerpunkteSchulformenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** die ID des Förderschwerpunktes */
	public Long Foerderschwerpunkt_ID;

	/** das Kürzel der Schulform */
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleFoerderschwerpunkteSchulformenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAlleFoerderschwerpunkteSchulformenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleFoerderschwerpunkteSchulformenPK.
	 * @param Foerderschwerpunkt_ID   der Wert für das Attribut Foerderschwerpunkt_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public MigrationDTOAlleFoerderschwerpunkteSchulformenPK(final Long Foerderschwerpunkt_ID, final String Schulform_Kuerzel) {
		if (Foerderschwerpunkt_ID == null) { 
			throw new NullPointerException("Foerderschwerpunkt_ID must not be null");
		}
		this.Foerderschwerpunkt_ID = Foerderschwerpunkt_ID;
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
		MigrationDTOAlleFoerderschwerpunkteSchulformenPK other = (MigrationDTOAlleFoerderschwerpunkteSchulformenPK) obj;
		if (Foerderschwerpunkt_ID == null) {
			if (other.Foerderschwerpunkt_ID != null)
				return false;
		} else if (!Foerderschwerpunkt_ID.equals(other.Foerderschwerpunkt_ID))
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
		result = prime * result + ((Foerderschwerpunkt_ID == null) ? 0 : Foerderschwerpunkt_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}
}