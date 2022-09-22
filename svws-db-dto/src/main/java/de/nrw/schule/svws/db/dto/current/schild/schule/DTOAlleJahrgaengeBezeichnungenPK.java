package de.nrw.schule.svws.db.dto.current.schild.schule;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Jahrgaenge_Bezeichnungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOAlleJahrgaengeBezeichnungenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID des Jahrgangs */
	public Long Jahrgang_ID;

	/** das Kürzel der Schulform, für welche die Bezeichnung gültig ist */
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleJahrgaengeBezeichnungenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAlleJahrgaengeBezeichnungenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleJahrgaengeBezeichnungenPK.
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public DTOAlleJahrgaengeBezeichnungenPK(final Long Jahrgang_ID, final String Schulform_Kuerzel) {
		if (Jahrgang_ID == null) { 
			throw new NullPointerException("Jahrgang_ID must not be null");
		}
		this.Jahrgang_ID = Jahrgang_ID;
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
		DTOAlleJahrgaengeBezeichnungenPK other = (DTOAlleJahrgaengeBezeichnungenPK) obj;
		if (Jahrgang_ID == null) {
			if (other.Jahrgang_ID != null)
				return false;
		} else if (!Jahrgang_ID.equals(other.Jahrgang_ID))
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
		result = prime * result + ((Jahrgang_ID == null) ? 0 : Jahrgang_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}
}