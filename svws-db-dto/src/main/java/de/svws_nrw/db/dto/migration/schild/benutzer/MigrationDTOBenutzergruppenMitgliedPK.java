package de.svws_nrw.db.dto.migration.schild.benutzer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle BenutzergruppenMitglieder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class MigrationDTOBenutzergruppenMitgliedPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID der Benutzergruppe */
	public Long Gruppe_ID;

	/** Die ID des Benutzers */
	public Long Benutzer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzergruppenMitgliedPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBenutzergruppenMitgliedPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzergruppenMitgliedPK.
	 * @param Gruppe_ID   der Wert für das Attribut Gruppe_ID
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 */
	public MigrationDTOBenutzergruppenMitgliedPK(final Long Gruppe_ID, final Long Benutzer_ID) {
		if (Gruppe_ID == null) {
			throw new NullPointerException("Gruppe_ID must not be null");
		}
		this.Gruppe_ID = Gruppe_ID;
		if (Benutzer_ID == null) {
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBenutzergruppenMitgliedPK other = (MigrationDTOBenutzergruppenMitgliedPK) obj;
		if (Gruppe_ID == null) {
			if (other.Gruppe_ID != null)
				return false;
		} else if (!Gruppe_ID.equals(other.Gruppe_ID))
			return false;
		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Gruppe_ID == null) ? 0 : Gruppe_ID.hashCode());

		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());
		return result;
	}
}
