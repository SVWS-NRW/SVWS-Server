package de.nrw.schule.svws.db.dto.migration.schild.kurse;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle KursLehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOKursLehrerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Kurses zu denen der Lehrer gehört */
	public Long Kurs_ID;

	/** ID des Lehrers */
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKursLehrerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKursLehrerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKursLehrerPK.
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public MigrationDTOKursLehrerPK(final Long Kurs_ID, final Long Lehrer_ID) {
		if (Kurs_ID == null) { 
			throw new NullPointerException("Kurs_ID must not be null");
		}
		this.Kurs_ID = Kurs_ID;
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
		MigrationDTOKursLehrerPK other = (MigrationDTOKursLehrerPK) obj;
		if (Kurs_ID == null) {
			if (other.Kurs_ID != null)
				return false;
		} else if (!Kurs_ID.equals(other.Kurs_ID))
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
		result = prime * result + ((Kurs_ID == null) ? 0 : Kurs_ID.hashCode());

		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());
		return result;
	}
}