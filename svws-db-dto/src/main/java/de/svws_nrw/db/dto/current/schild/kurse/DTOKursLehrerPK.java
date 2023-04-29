package de.svws_nrw.db.dto.current.schild.kurse;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle KursLehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOKursLehrerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Kurses zu denen der Lehrer gehört */
	public long Kurs_ID;

	/** ID des Lehrers */
	public long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursLehrerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKursLehrerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursLehrerPK.
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOKursLehrerPK(final long Kurs_ID, final long Lehrer_ID) {
		this.Kurs_ID = Kurs_ID;
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
		DTOKursLehrerPK other = (DTOKursLehrerPK) obj;
		if (Kurs_ID != other.Kurs_ID)
			return false;

		if (Lehrer_ID != other.Lehrer_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Kurs_ID);

		result = prime * result + Long.hashCode(Lehrer_ID);
		return result;
	}
}
