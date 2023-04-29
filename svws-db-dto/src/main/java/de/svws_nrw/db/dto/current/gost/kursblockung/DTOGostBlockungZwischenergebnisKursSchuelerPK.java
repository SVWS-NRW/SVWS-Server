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
	public long Zwischenergebnis_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Kurses */
	public long Blockung_Kurs_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Schülers */
	public long Schueler_ID;

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
	public DTOGostBlockungZwischenergebnisKursSchuelerPK(final long Zwischenergebnis_ID, final long Blockung_Kurs_ID, final long Schueler_ID) {
		this.Zwischenergebnis_ID = Zwischenergebnis_ID;
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
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
		if (Zwischenergebnis_ID != other.Zwischenergebnis_ID)
			return false;
		if (Blockung_Kurs_ID != other.Blockung_Kurs_ID)
			return false;
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Zwischenergebnis_ID);

		result = prime * result + Long.hashCode(Blockung_Kurs_ID);

		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}
}
