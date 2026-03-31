package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_StundenplanErgebnisse_Unterricht_Zeitraster.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUvStundenplanErgebnisUnterrichtZeitrasterPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Fremdschlüssel auf das Stundenplanergebnis */
	public long Ergebnis_ID;

	/** Fremdschlüssel auf den UV_Unterricht */
	public long Unterricht_ID;

	/** Fremdschlüssel auf den UV_ZeitrasterEintrag */
	public long ZeitrasterEintrag_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvStundenplanErgebnisUnterrichtZeitrasterPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvStundenplanErgebnisUnterrichtZeitrasterPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvStundenplanErgebnisUnterrichtZeitrasterPK.
	 * @param Ergebnis_ID   der Wert für das Attribut Ergebnis_ID
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param ZeitrasterEintrag_ID   der Wert für das Attribut ZeitrasterEintrag_ID
	 */
	public DTOUvStundenplanErgebnisUnterrichtZeitrasterPK(final long Ergebnis_ID, final long Unterricht_ID, final long ZeitrasterEintrag_ID) {
		this.Ergebnis_ID = Ergebnis_ID;
		this.Unterricht_ID = Unterricht_ID;
		this.ZeitrasterEintrag_ID = ZeitrasterEintrag_ID;
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
		DTOUvStundenplanErgebnisUnterrichtZeitrasterPK other = (DTOUvStundenplanErgebnisUnterrichtZeitrasterPK) obj;
		if (Ergebnis_ID != other.Ergebnis_ID) {
			return false;
		}
		if (Unterricht_ID != other.Unterricht_ID) {
			return false;
		}
		return ZeitrasterEintrag_ID == other.ZeitrasterEintrag_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Ergebnis_ID);

		result = prime * result + Long.hashCode(Unterricht_ID);

		result = prime * result + Long.hashCode(ZeitrasterEintrag_ID);
		return result;
	}
}
