package de.svws_nrw.db.dto.current.gost.kursblockung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostBlockungZwischenergebnisKursSchuelerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Zwischenergebnisses */
	public Long Zwischenergebnis_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Kurses */
	public Long Blockung_Kurs_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Schülers */
	public Long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnisKursSchuelerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungZwischenergebnisKursSchuelerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnisKursSchuelerPK.
	 * @param Zwischenergebnis_ID   der Wert für das Attribut Zwischenergebnis_ID
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOGostBlockungZwischenergebnisKursSchuelerPK(final Long Zwischenergebnis_ID, final Long Blockung_Kurs_ID, final Long Schueler_ID) {
		if (Zwischenergebnis_ID == null) {
			throw new NullPointerException("Zwischenergebnis_ID must not be null");
		}
		this.Zwischenergebnis_ID = Zwischenergebnis_ID;
		if (Blockung_Kurs_ID == null) {
			throw new NullPointerException("Blockung_Kurs_ID must not be null");
		}
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungZwischenergebnisKursSchuelerPK other = (DTOGostBlockungZwischenergebnisKursSchuelerPK) obj;
		if (Zwischenergebnis_ID == null) {
			if (other.Zwischenergebnis_ID != null)
				return false;
		} else if (!Zwischenergebnis_ID.equals(other.Zwischenergebnis_ID))
			return false;

		if (Blockung_Kurs_ID == null) {
			if (other.Blockung_Kurs_ID != null)
				return false;
		} else if (!Blockung_Kurs_ID.equals(other.Blockung_Kurs_ID))
			return false;

		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Zwischenergebnis_ID == null) ? 0 : Zwischenergebnis_ID.hashCode());

		result = prime * result + ((Blockung_Kurs_ID == null) ? 0 : Blockung_Kurs_ID.hashCode());

		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());
		return result;
	}
}
