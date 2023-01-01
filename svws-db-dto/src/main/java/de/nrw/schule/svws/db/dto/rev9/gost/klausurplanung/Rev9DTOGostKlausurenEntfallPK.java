package de.nrw.schule.svws.db.dto.rev9.gost.klausurplanung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Klausuren_Entfall.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev9DTOGostKlausurenEntfallPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Klausurtermins */
	public Long Termin_ID;

	/** ID des Kurses */
	public Long Kurs_ID;

	/** ID des Zeitrasters */
	public Long Zeitraster_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenEntfallPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOGostKlausurenEntfallPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenEntfallPK.
	 * @param Termin_ID   der Wert für das Attribut Termin_ID
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 */
	public Rev9DTOGostKlausurenEntfallPK(final Long Termin_ID, final Long Kurs_ID, final Long Zeitraster_ID) {
		if (Termin_ID == null) { 
			throw new NullPointerException("Termin_ID must not be null");
		}
		this.Termin_ID = Termin_ID;
		if (Kurs_ID == null) { 
			throw new NullPointerException("Kurs_ID must not be null");
		}
		this.Kurs_ID = Kurs_ID;
		if (Zeitraster_ID == null) { 
			throw new NullPointerException("Zeitraster_ID must not be null");
		}
		this.Zeitraster_ID = Zeitraster_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOGostKlausurenEntfallPK other = (Rev9DTOGostKlausurenEntfallPK) obj;
		if (Termin_ID == null) {
			if (other.Termin_ID != null)
				return false;
		} else if (!Termin_ID.equals(other.Termin_ID))
			return false;

		if (Kurs_ID == null) {
			if (other.Kurs_ID != null)
				return false;
		} else if (!Kurs_ID.equals(other.Kurs_ID))
			return false;

		if (Zeitraster_ID == null) {
			if (other.Zeitraster_ID != null)
				return false;
		} else if (!Zeitraster_ID.equals(other.Zeitraster_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Termin_ID == null) ? 0 : Termin_ID.hashCode());

		result = prime * result + ((Kurs_ID == null) ? 0 : Kurs_ID.hashCode());

		result = prime * result + ((Zeitraster_ID == null) ? 0 : Zeitraster_ID.hashCode());
		return result;
	}
}