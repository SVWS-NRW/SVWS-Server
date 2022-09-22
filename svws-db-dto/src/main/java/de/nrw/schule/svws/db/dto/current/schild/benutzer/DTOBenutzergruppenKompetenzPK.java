package de.nrw.schule.svws.db.dto.current.schild.benutzer;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle BenutzergruppenKompetenzen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOBenutzergruppenKompetenzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID der Benutzergruppe */
	public Long Gruppe_ID;

	/** Die ID der zugeordneten Kompetenz */
	public Long Kompetenz_ID;

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
	public DTOBenutzergruppenKompetenzPK(final Long Gruppe_ID, final Long Kompetenz_ID) {
		if (Gruppe_ID == null) { 
			throw new NullPointerException("Gruppe_ID must not be null");
		}
		this.Gruppe_ID = Gruppe_ID;
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
		DTOBenutzergruppenKompetenzPK other = (DTOBenutzergruppenKompetenzPK) obj;
		if (Gruppe_ID == null) {
			if (other.Gruppe_ID != null)
				return false;
		} else if (!Gruppe_ID.equals(other.Gruppe_ID))
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
		result = prime * result + ((Gruppe_ID == null) ? 0 : Gruppe_ID.hashCode());

		result = prime * result + ((Kompetenz_ID == null) ? 0 : Kompetenz_ID.hashCode());
		return result;
	}
}