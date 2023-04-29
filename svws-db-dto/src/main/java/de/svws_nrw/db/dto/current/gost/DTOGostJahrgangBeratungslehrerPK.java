package de.svws_nrw.db.dto.current.gost;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Jahrgang_Beratungslehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostJahrgangBeratungslehrerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht  */
	public int Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: ID des Beratungslehrers in der Lehrertabelle */
	public long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangBeratungslehrerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangBeratungslehrerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangBeratungslehrerPK.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOGostJahrgangBeratungslehrerPK(final int Abi_Jahrgang, final long Lehrer_ID) {
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangBeratungslehrerPK other = (DTOGostJahrgangBeratungslehrerPK) obj;
		if (Abi_Jahrgang != other.Abi_Jahrgang)
			return false;

		if (Lehrer_ID != other.Lehrer_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Abi_Jahrgang);

		result = prime * result + Long.hashCode(Lehrer_ID);
		return result;
	}
}
