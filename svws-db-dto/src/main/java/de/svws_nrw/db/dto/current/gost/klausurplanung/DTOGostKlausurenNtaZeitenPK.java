package de.svws_nrw.db.dto.current.gost.klausurplanung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Klausuren_NtaZeiten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostKlausurenNtaZeitenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Schülers */
	public long Schueler_ID;

	/** ID der Klausurvorgaben */
	public long Vorgabe_ID;

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
	public DTOGostKlausurenNtaZeitenPK(final long Schueler_ID, final long Vorgabe_ID) {
		this.Schueler_ID = Schueler_ID;
		this.Vorgabe_ID = Vorgabe_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenNtaZeitenPK other = (DTOGostKlausurenNtaZeitenPK) obj;
		if (Schueler_ID != other.Schueler_ID)
			return false;

		if (Vorgabe_ID != other.Vorgabe_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);

		result = prime * result + Long.hashCode(Vorgabe_ID);
		return result;
	}
}
