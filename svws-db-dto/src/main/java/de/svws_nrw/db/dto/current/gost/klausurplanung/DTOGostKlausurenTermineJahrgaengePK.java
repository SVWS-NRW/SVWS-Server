package de.svws_nrw.db.dto.current.gost.klausurplanung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Klausuren_Termine_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostKlausurenTermineJahrgaengePK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Termin_ID des Klausurtermins */
	public long Termin_ID;

	/** Der Abiturjahrgang, der zum Klausurtermin zugelassen werden soll. */
	public int Abi_Jahrgang;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermineJahrgaengePK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenTermineJahrgaengePK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermineJahrgaengePK.
	 * @param Termin_ID   der Wert für das Attribut Termin_ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 */
	public DTOGostKlausurenTermineJahrgaengePK(final long Termin_ID, final int Abi_Jahrgang) {
		this.Termin_ID = Termin_ID;
		this.Abi_Jahrgang = Abi_Jahrgang;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenTermineJahrgaengePK other = (DTOGostKlausurenTermineJahrgaengePK) obj;
		if (Termin_ID != other.Termin_ID)
			return false;
		return Abi_Jahrgang == other.Abi_Jahrgang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Termin_ID);

		result = prime * result + Integer.hashCode(Abi_Jahrgang);
		return result;
	}
}
