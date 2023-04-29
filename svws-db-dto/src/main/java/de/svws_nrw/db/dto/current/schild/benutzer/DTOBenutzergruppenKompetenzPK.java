package de.svws_nrw.db.dto.current.schild.benutzer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle BenutzergruppenKompetenzen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOBenutzergruppenKompetenzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID der Benutzergruppe */
	public long Gruppe_ID;

	/** Die ID der zugeordneten Kompetenz */
	public long Kompetenz_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzergruppenKompetenzPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzergruppenKompetenzPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzergruppenKompetenzPK.
	 * @param Gruppe_ID   der Wert für das Attribut Gruppe_ID
	 * @param Kompetenz_ID   der Wert für das Attribut Kompetenz_ID
	 */
	public DTOBenutzergruppenKompetenzPK(final long Gruppe_ID, final long Kompetenz_ID) {
		this.Gruppe_ID = Gruppe_ID;
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
		DTOBenutzergruppenKompetenzPK other = (DTOBenutzergruppenKompetenzPK) obj;
		if (Gruppe_ID != other.Gruppe_ID)
			return false;

		if (Kompetenz_ID != other.Kompetenz_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Gruppe_ID);

		result = prime * result + Long.hashCode(Kompetenz_ID);
		return result;
	}
}
