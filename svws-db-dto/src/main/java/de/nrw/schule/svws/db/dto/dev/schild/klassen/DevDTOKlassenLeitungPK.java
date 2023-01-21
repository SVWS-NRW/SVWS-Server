package de.nrw.schule.svws.db.dto.dev.schild.klassen;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle KlassenLehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DevDTOKlassenLeitungPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID der Klasse */
	public Long Klassen_ID;

	/** ID des Lehrers */
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKlassenLeitungPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOKlassenLeitungPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKlassenLeitungPK.
	 * @param Klassen_ID   der Wert für das Attribut Klassen_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DevDTOKlassenLeitungPK(final Long Klassen_ID, final Long Lehrer_ID) {
		if (Klassen_ID == null) { 
			throw new NullPointerException("Klassen_ID must not be null");
		}
		this.Klassen_ID = Klassen_ID;
		if (Lehrer_ID == null) { 
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOKlassenLeitungPK other = (DevDTOKlassenLeitungPK) obj;
		if (Klassen_ID == null) {
			if (other.Klassen_ID != null)
				return false;
		} else if (!Klassen_ID.equals(other.Klassen_ID))
			return false;

		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Klassen_ID == null) ? 0 : Klassen_ID.hashCode());

		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());
		return result;
	}
}