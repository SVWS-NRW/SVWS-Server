package de.nrw.schule.svws.db.dto.current.gost;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Jahrgang_Fachkombinationen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOGostJahrgangFachkombinationenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht */
	public Integer Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: ID des Datensatzes der nicht möglichen Fächerkombination */
	public String ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFachkombinationenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangFachkombinationenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFachkombinationenPK.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOGostJahrgangFachkombinationenPK(final Integer Abi_Jahrgang, final String ID) {
		if (Abi_Jahrgang == null) { 
			throw new NullPointerException("Abi_Jahrgang must not be null");
		}
		this.Abi_Jahrgang = Abi_Jahrgang;
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangFachkombinationenPK other = (DTOGostJahrgangFachkombinationenPK) obj;
		if (Abi_Jahrgang == null) {
			if (other.Abi_Jahrgang != null)
				return false;
		} else if (!Abi_Jahrgang.equals(other.Abi_Jahrgang))
			return false;

		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Abi_Jahrgang == null) ? 0 : Abi_Jahrgang.hashCode());

		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}
}