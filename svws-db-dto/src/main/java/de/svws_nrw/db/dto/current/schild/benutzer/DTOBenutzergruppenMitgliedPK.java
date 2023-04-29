package de.svws_nrw.db.dto.current.schild.benutzer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle BenutzergruppenMitglieder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOBenutzergruppenMitgliedPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID der Benutzergruppe */
	public long Gruppe_ID;

	/** Die ID des Benutzers */
	public long Benutzer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzergruppenMitgliedPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzergruppenMitgliedPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzergruppenMitgliedPK.
	 * @param Gruppe_ID   der Wert für das Attribut Gruppe_ID
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 */
	public DTOBenutzergruppenMitgliedPK(final long Gruppe_ID, final long Benutzer_ID) {
		this.Gruppe_ID = Gruppe_ID;
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
		DTOBenutzergruppenMitgliedPK other = (DTOBenutzergruppenMitgliedPK) obj;
		if (Gruppe_ID != other.Gruppe_ID)
			return false;
		return Benutzer_ID == other.Benutzer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Gruppe_ID);

		result = prime * result + Long.hashCode(Benutzer_ID);
		return result;
	}
}
