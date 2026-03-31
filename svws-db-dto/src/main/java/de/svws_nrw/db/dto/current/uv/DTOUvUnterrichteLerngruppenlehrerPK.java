package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_Unterrichte_Lerngruppenlehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUvUnterrichteLerngruppenlehrerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des UV_Unterrichts */
	public long Unterricht_ID;

	/** ID des Lehrers, welcher der Lerngruppe zugeordnet ist */
	public long LerngruppenLehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvUnterrichteLerngruppenlehrerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvUnterrichteLerngruppenlehrerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvUnterrichteLerngruppenlehrerPK.
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param LerngruppenLehrer_ID   der Wert für das Attribut LerngruppenLehrer_ID
	 */
	public DTOUvUnterrichteLerngruppenlehrerPK(final long Unterricht_ID, final long LerngruppenLehrer_ID) {
		this.Unterricht_ID = Unterricht_ID;
		this.LerngruppenLehrer_ID = LerngruppenLehrer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOUvUnterrichteLerngruppenlehrerPK other = (DTOUvUnterrichteLerngruppenlehrerPK) obj;
		if (Unterricht_ID != other.Unterricht_ID) {
			return false;
		}
		return LerngruppenLehrer_ID == other.LerngruppenLehrer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Unterricht_ID);

		result = prime * result + Long.hashCode(LerngruppenLehrer_ID);
		return result;
	}
}
