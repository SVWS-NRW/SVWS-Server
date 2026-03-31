package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_StundenplanErgebnisse_Unterricht_Raeume.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUvStundenplanErgebnisUnterrichtRaumPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Fremdschlüssel auf das Stundenplanergebnis */
	public long Ergebnis_ID;

	/** Fremdschlüssel auf den UV_Unterricht */
	public long Unterricht_ID;

	/** Fremdschlüssel auf den UV_Raeume */
	public long Raum_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvStundenplanErgebnisUnterrichtRaumPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvStundenplanErgebnisUnterrichtRaumPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvStundenplanErgebnisUnterrichtRaumPK.
	 * @param Ergebnis_ID   der Wert für das Attribut Ergebnis_ID
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param Raum_ID   der Wert für das Attribut Raum_ID
	 */
	public DTOUvStundenplanErgebnisUnterrichtRaumPK(final long Ergebnis_ID, final long Unterricht_ID, final long Raum_ID) {
		this.Ergebnis_ID = Ergebnis_ID;
		this.Unterricht_ID = Unterricht_ID;
		this.Raum_ID = Raum_ID;
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
		DTOUvStundenplanErgebnisUnterrichtRaumPK other = (DTOUvStundenplanErgebnisUnterrichtRaumPK) obj;
		if (Ergebnis_ID != other.Ergebnis_ID) {
			return false;
		}
		if (Unterricht_ID != other.Unterricht_ID) {
			return false;
		}
		return Raum_ID == other.Raum_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Ergebnis_ID);

		result = prime * result + Long.hashCode(Unterricht_ID);

		result = prime * result + Long.hashCode(Raum_ID);
		return result;
	}
}
