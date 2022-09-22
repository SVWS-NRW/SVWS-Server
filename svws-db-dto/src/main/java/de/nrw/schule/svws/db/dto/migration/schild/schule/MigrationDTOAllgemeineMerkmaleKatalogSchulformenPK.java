package de.nrw.schule.svws.db.dto.migration.schild.schule;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle AllgemeineMerkmaleKatalog_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOAllgemeineMerkmaleKatalogSchulformenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** die ID des allgemeinen Merkmals bei Schulen und/oder Schülern */
	public Long Merkmal_ID;

	/** das Kürzel des allgemeinen Merkmals bei Schulen und/oder Schülern */
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAllgemeineMerkmaleKatalogSchulformenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAllgemeineMerkmaleKatalogSchulformenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAllgemeineMerkmaleKatalogSchulformenPK.
	 * @param Merkmal_ID   der Wert für das Attribut Merkmal_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public MigrationDTOAllgemeineMerkmaleKatalogSchulformenPK(final Long Merkmal_ID, final String Schulform_Kuerzel) {
		if (Merkmal_ID == null) { 
			throw new NullPointerException("Merkmal_ID must not be null");
		}
		this.Merkmal_ID = Merkmal_ID;
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
		MigrationDTOAllgemeineMerkmaleKatalogSchulformenPK other = (MigrationDTOAllgemeineMerkmaleKatalogSchulformenPK) obj;
		if (Merkmal_ID == null) {
			if (other.Merkmal_ID != null)
				return false;
		} else if (!Merkmal_ID.equals(other.Merkmal_ID))
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
		result = prime * result + ((Merkmal_ID == null) ? 0 : Merkmal_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}
}