package de.nrw.schule.svws.db.dto.rev8.schild.schule;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schulgliederungen_Abschluesse_Berufsbildend.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOAlleSchulgliederungenAbschluesseBerufsbildendPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** die ID der Schulgliederung */
	public Long Schulgliederung_ID;

	/** die ID der Art des berufsbildenden Abschlusses */
	public String Abschluss_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOAlleSchulgliederungenAbschluesseBerufsbildendPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOAlleSchulgliederungenAbschluesseBerufsbildendPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOAlleSchulgliederungenAbschluesseBerufsbildendPK.
	 * @param Schulgliederung_ID   der Wert für das Attribut Schulgliederung_ID
	 * @param Abschluss_Kuerzel   der Wert für das Attribut Abschluss_Kuerzel
	 */
	public Rev8DTOAlleSchulgliederungenAbschluesseBerufsbildendPK(final Long Schulgliederung_ID, final String Abschluss_Kuerzel) {
		if (Schulgliederung_ID == null) { 
			throw new NullPointerException("Schulgliederung_ID must not be null");
		}
		this.Schulgliederung_ID = Schulgliederung_ID;
		if (Abschluss_Kuerzel == null) { 
			throw new NullPointerException("Abschluss_Kuerzel must not be null");
		}
		this.Abschluss_Kuerzel = Abschluss_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOAlleSchulgliederungenAbschluesseBerufsbildendPK other = (Rev8DTOAlleSchulgliederungenAbschluesseBerufsbildendPK) obj;
		if (Schulgliederung_ID == null) {
			if (other.Schulgliederung_ID != null)
				return false;
		} else if (!Schulgliederung_ID.equals(other.Schulgliederung_ID))
			return false;

		if (Abschluss_Kuerzel == null) {
			if (other.Abschluss_Kuerzel != null)
				return false;
		} else if (!Abschluss_Kuerzel.equals(other.Abschluss_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schulgliederung_ID == null) ? 0 : Schulgliederung_ID.hashCode());

		result = prime * result + ((Abschluss_Kuerzel == null) ? 0 : Abschluss_Kuerzel.hashCode());
		return result;
	}
}