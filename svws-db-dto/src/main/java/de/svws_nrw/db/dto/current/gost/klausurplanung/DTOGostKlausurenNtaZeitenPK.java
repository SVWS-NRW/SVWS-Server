package de.svws_nrw.db.dto.current.gost.klausurplanung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Klausuren_NtaZeiten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOGostKlausurenNtaZeitenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Schülers */
	public Long Schueler_ID;

	/** ID der Klausurvorgaben */
	public Long Vorgabe_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenNtaZeitenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenNtaZeitenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenNtaZeitenPK.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Vorgabe_ID   der Wert für das Attribut Vorgabe_ID
	 */
	public DTOGostKlausurenNtaZeitenPK(final Long Schueler_ID, final Long Vorgabe_ID) {
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Vorgabe_ID == null) { 
			throw new NullPointerException("Vorgabe_ID must not be null");
		}
		this.Vorgabe_ID = Vorgabe_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenNtaZeitenPK other = (DTOGostKlausurenNtaZeitenPK) obj;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;

		if (Vorgabe_ID == null) {
			if (other.Vorgabe_ID != null)
				return false;
		} else if (!Vorgabe_ID.equals(other.Vorgabe_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());

		result = prime * result + ((Vorgabe_ID == null) ? 0 : Vorgabe_ID.hashCode());
		return result;
	}
}