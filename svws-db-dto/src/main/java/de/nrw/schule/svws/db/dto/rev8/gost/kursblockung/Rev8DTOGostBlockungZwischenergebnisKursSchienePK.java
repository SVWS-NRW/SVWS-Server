package de.nrw.schule.svws.db.dto.rev8.gost.kursblockung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schienen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOGostBlockungZwischenergebnisKursSchienePK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Kursblockung der Gymnasialen Oberstufe - Kurs-Schienen-Zuordnung eines Zwischenergebnisses einer Blockung: ID des Zwischenergebnisses */
	public Long Zwischenergebnis_ID;

	/** Kursblockung der Gymnasialen Oberstufe - Kurs-Schienen-Zuordnung eines Zwischenergebnisses einer Blockung: ID des Kurses */
	public Long Blockung_Kurs_ID;

	/** Kursblockung der Gymnasialen Oberstufe - Kurs-Schienen-Zuordnung eines Zwischenergebnisses einer Blockung: ID der Schiene aus der Blockung */
	public Long Schienen_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostBlockungZwischenergebnisKursSchienePK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOGostBlockungZwischenergebnisKursSchienePK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostBlockungZwischenergebnisKursSchienePK.
	 * @param Zwischenergebnis_ID   der Wert für das Attribut Zwischenergebnis_ID
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Schienen_ID   der Wert für das Attribut Schienen_ID
	 */
	public Rev8DTOGostBlockungZwischenergebnisKursSchienePK(final Long Zwischenergebnis_ID, final Long Blockung_Kurs_ID, final Long Schienen_ID) {
		if (Zwischenergebnis_ID == null) { 
			throw new NullPointerException("Zwischenergebnis_ID must not be null");
		}
		this.Zwischenergebnis_ID = Zwischenergebnis_ID;
		if (Blockung_Kurs_ID == null) { 
			throw new NullPointerException("Blockung_Kurs_ID must not be null");
		}
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
		if (Schienen_ID == null) { 
			throw new NullPointerException("Schienen_ID must not be null");
		}
		this.Schienen_ID = Schienen_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOGostBlockungZwischenergebnisKursSchienePK other = (Rev8DTOGostBlockungZwischenergebnisKursSchienePK) obj;
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

		if (Schienen_ID == null) {
			if (other.Schienen_ID != null)
				return false;
		} else if (!Schienen_ID.equals(other.Schienen_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Zwischenergebnis_ID == null) ? 0 : Zwischenergebnis_ID.hashCode());

		result = prime * result + ((Blockung_Kurs_ID == null) ? 0 : Blockung_Kurs_ID.hashCode());

		result = prime * result + ((Schienen_ID == null) ? 0 : Schienen_ID.hashCode());
		return result;
	}
}