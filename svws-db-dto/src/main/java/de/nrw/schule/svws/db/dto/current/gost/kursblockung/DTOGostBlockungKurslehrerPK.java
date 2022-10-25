package de.nrw.schule.svws.db.dto.current.gost.kursblockung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Blockung_Kurslehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOGostBlockungKurslehrerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Kurses */
	public Long Blockung_Kurs_ID;

	/** ID des Lehrers, welcher dem Kurs zugeordnet ist */
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungKurslehrerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungKurslehrerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungKurslehrerPK.
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOGostBlockungKurslehrerPK(final Long Blockung_Kurs_ID, final Long Lehrer_ID) {
		if (Blockung_Kurs_ID == null) { 
			throw new NullPointerException("Blockung_Kurs_ID must not be null");
		}
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
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
		DTOGostBlockungKurslehrerPK other = (DTOGostBlockungKurslehrerPK) obj;
		if (Blockung_Kurs_ID == null) {
			if (other.Blockung_Kurs_ID != null)
				return false;
		} else if (!Blockung_Kurs_ID.equals(other.Blockung_Kurs_ID))
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
		result = prime * result + ((Blockung_Kurs_ID == null) ? 0 : Blockung_Kurs_ID.hashCode());

		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());
		return result;
	}
}