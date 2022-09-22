package de.nrw.schule.svws.db.dto.current.schild.schule;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Herkunftsart_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOHerkunftsartSchulformenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** die ID der Herkunftsart */
	public Long Herkunftsart_ID;

	/** das Kürzel der Schulform */
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftsartSchulformenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOHerkunftsartSchulformenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftsartSchulformenPK.
	 * @param Herkunftsart_ID   der Wert für das Attribut Herkunftsart_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public DTOHerkunftsartSchulformenPK(final Long Herkunftsart_ID, final String Schulform_Kuerzel) {
		if (Herkunftsart_ID == null) { 
			throw new NullPointerException("Herkunftsart_ID must not be null");
		}
		this.Herkunftsart_ID = Herkunftsart_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOHerkunftsartSchulformenPK other = (DTOHerkunftsartSchulformenPK) obj;
		if (Herkunftsart_ID == null) {
			if (other.Herkunftsart_ID != null)
				return false;
		} else if (!Herkunftsart_ID.equals(other.Herkunftsart_ID))
			return false;

		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Herkunftsart_ID == null) ? 0 : Herkunftsart_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}
}