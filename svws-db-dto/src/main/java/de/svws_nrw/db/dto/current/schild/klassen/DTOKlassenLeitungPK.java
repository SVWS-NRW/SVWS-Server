package de.svws_nrw.db.dto.current.schild.klassen;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle KlassenLehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOKlassenLeitungPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID der Klasse */
	public long Klassen_ID;

	/** ID des Lehrers */
	public long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKlassenLeitungPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKlassenLeitungPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKlassenLeitungPK.
	 * @param Klassen_ID   der Wert für das Attribut Klassen_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOKlassenLeitungPK(final long Klassen_ID, final long Lehrer_ID) {
		this.Klassen_ID = Klassen_ID;
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKlassenLeitungPK other = (DTOKlassenLeitungPK) obj;
		if (Klassen_ID != other.Klassen_ID)
			return false;

		if (Lehrer_ID != other.Lehrer_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Klassen_ID);

		result = prime * result + Long.hashCode(Lehrer_ID);
		return result;
	}
}
