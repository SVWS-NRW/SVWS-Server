package de.nrw.schule.svws.db.dto.rev8.schild.klassen;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle KlassenartenKatalog_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOKlassenartenKatalogSchulformenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** die ID der Klassenart */
	public Long Klassenart_ID;

	/** das Kürzel der Schulform */
	public String Schulform_Kuerzel;

	/** das Kürzel der Schulgliederung bzw. des Bildungsganges. Leerer String, falls alle Gliederungen der Schulform gemeint sind */
	public String Schulgliederung_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKlassenartenKatalogSchulformenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKlassenartenKatalogSchulformenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKlassenartenKatalogSchulformenPK.
	 * @param Klassenart_ID   der Wert für das Attribut Klassenart_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 * @param Schulgliederung_Kuerzel   der Wert für das Attribut Schulgliederung_Kuerzel
	 */
	public Rev8DTOKlassenartenKatalogSchulformenPK(final Long Klassenart_ID, final String Schulform_Kuerzel, final String Schulgliederung_Kuerzel) {
		if (Klassenart_ID == null) { 
			throw new NullPointerException("Klassenart_ID must not be null");
		}
		this.Klassenart_ID = Klassenart_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
		if (Schulgliederung_Kuerzel == null) { 
			throw new NullPointerException("Schulgliederung_Kuerzel must not be null");
		}
		this.Schulgliederung_Kuerzel = Schulgliederung_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKlassenartenKatalogSchulformenPK other = (Rev8DTOKlassenartenKatalogSchulformenPK) obj;
		if (Klassenart_ID == null) {
			if (other.Klassenart_ID != null)
				return false;
		} else if (!Klassenart_ID.equals(other.Klassenart_ID))
			return false;

		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;

		if (Schulgliederung_Kuerzel == null) {
			if (other.Schulgliederung_Kuerzel != null)
				return false;
		} else if (!Schulgliederung_Kuerzel.equals(other.Schulgliederung_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Klassenart_ID == null) ? 0 : Klassenart_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());

		result = prime * result + ((Schulgliederung_Kuerzel == null) ? 0 : Schulgliederung_Kuerzel.hashCode());
		return result;
	}
}