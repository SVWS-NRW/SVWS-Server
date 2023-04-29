package de.svws_nrw.db.dto.current.schild.benutzer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle BenutzerKompetenzen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOBenutzerKompetenzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID des Benutzers */
	public long Benutzer_ID;

	/** Die ID der zugeordneten Kompetenz */
	public long Kompetenz_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerKompetenzPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzerKompetenzPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerKompetenzPK.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param Kompetenz_ID   der Wert für das Attribut Kompetenz_ID
	 */
	public DTOBenutzerKompetenzPK(final long Benutzer_ID, final long Kompetenz_ID) {
		this.Benutzer_ID = Benutzer_ID;
		this.Kompetenz_ID = Kompetenz_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBenutzerKompetenzPK other = (DTOBenutzerKompetenzPK) obj;
		if (Benutzer_ID != other.Benutzer_ID)
			return false;
		return Kompetenz_ID == other.Kompetenz_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Benutzer_ID);

		result = prime * result + Long.hashCode(Kompetenz_ID);
		return result;
	}
}
