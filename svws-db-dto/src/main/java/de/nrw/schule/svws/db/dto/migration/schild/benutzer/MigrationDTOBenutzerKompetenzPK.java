package de.nrw.schule.svws.db.dto.migration.schild.benutzer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle BenutzerKompetenzen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOBenutzerKompetenzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID des Benutzers */
	public Long Benutzer_ID;

	/** Die ID der zugeordneten Kompetenz */
	public Long Kompetenz_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzerKompetenzPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBenutzerKompetenzPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzerKompetenzPK.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param Kompetenz_ID   der Wert für das Attribut Kompetenz_ID
	 */
	public MigrationDTOBenutzerKompetenzPK(final Long Benutzer_ID, final Long Kompetenz_ID) {
		if (Benutzer_ID == null) { 
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
		if (Kompetenz_ID == null) { 
			throw new NullPointerException("Kompetenz_ID must not be null");
		}
		this.Kompetenz_ID = Kompetenz_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBenutzerKompetenzPK other = (MigrationDTOBenutzerKompetenzPK) obj;
		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
			return false;

		if (Kompetenz_ID == null) {
			if (other.Kompetenz_ID != null)
				return false;
		} else if (!Kompetenz_ID.equals(other.Kompetenz_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());

		result = prime * result + ((Kompetenz_ID == null) ? 0 : Kompetenz_ID.hashCode());
		return result;
	}
}