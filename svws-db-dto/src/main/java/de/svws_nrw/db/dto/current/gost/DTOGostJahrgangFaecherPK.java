package de.svws_nrw.db.dto.current.gost;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Jahrgang_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostJahrgangFaecherPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schuljahr, in welchem der Jahrgang das Abitur macht */
	public int Abi_Jahrgang;

	/** ID des Faches in der Fächertabelle */
	public long Fach_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFaecherPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangFaecherPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFaecherPK.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOGostJahrgangFaecherPK(final int Abi_Jahrgang, final long Fach_ID) {
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangFaecherPK other = (DTOGostJahrgangFaecherPK) obj;
		if (Abi_Jahrgang != other.Abi_Jahrgang)
			return false;
		return Fach_ID == other.Fach_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Abi_Jahrgang);

		result = prime * result + Long.hashCode(Fach_ID);
		return result;
	}
}
