package de.svws_nrw.db.dto.current.gost;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Jahrgang_Sprachenfolge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostJahrgangSprachenfolgePK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: Schuljahr, in welchem der Jahrgang das Abitur macht */
	public int Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: Atomares Sprachkürzel des Sprach-Faches */
	public String Sprache;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangSprachenfolgePK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangSprachenfolgePK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangSprachenfolgePK.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Sprache   der Wert für das Attribut Sprache
	 */
	public DTOGostJahrgangSprachenfolgePK(final int Abi_Jahrgang, final String Sprache) {
		this.Abi_Jahrgang = Abi_Jahrgang;
		if (Sprache == null) {
			throw new NullPointerException("Sprache must not be null");
		}
		this.Sprache = Sprache;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangSprachenfolgePK other = (DTOGostJahrgangSprachenfolgePK) obj;
		if (Abi_Jahrgang != other.Abi_Jahrgang)
			return false;
		if (Sprache == null) {
			if (other.Sprache != null)
				return false;
		} else if (!Sprache.equals(other.Sprache))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Abi_Jahrgang);

		result = prime * result + ((Sprache == null) ? 0 : Sprache.hashCode());
		return result;
	}
}
