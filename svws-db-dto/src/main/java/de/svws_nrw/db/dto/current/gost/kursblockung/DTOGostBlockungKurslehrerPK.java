package de.svws_nrw.db.dto.current.gost.kursblockung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Blockung_Kurslehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostBlockungKurslehrerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Kurses */
	public long Blockung_Kurs_ID;

	/** ID des Lehrers, welcher dem Kurs zugeordnet ist */
	public long Lehrer_ID;

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
	public DTOGostBlockungKurslehrerPK(final long Blockung_Kurs_ID, final long Lehrer_ID) {
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
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
		DTOGostBlockungKurslehrerPK other = (DTOGostBlockungKurslehrerPK) obj;
		if (Blockung_Kurs_ID != other.Blockung_Kurs_ID)
			return false;

		if (Lehrer_ID != other.Lehrer_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Blockung_Kurs_ID);

		result = prime * result + Long.hashCode(Lehrer_ID);
		return result;
	}
}
