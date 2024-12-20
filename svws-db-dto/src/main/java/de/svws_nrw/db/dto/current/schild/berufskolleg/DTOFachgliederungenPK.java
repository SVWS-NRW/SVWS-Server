package de.svws_nrw.db.dto.current.schild.berufskolleg;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Fach_Gliederungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOFachgliederungenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	public long Fach_ID;

	/** SGL für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	public String Gliederung;

	/** Fachklassen ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	public long Fachklasse_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachgliederungenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFachgliederungenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachgliederungenPK.
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Gliederung   der Wert für das Attribut Gliederung
	 * @param Fachklasse_ID   der Wert für das Attribut Fachklasse_ID
	 */
	public DTOFachgliederungenPK(final long Fach_ID, final String Gliederung, final long Fachklasse_ID) {
		this.Fach_ID = Fach_ID;
		if (Gliederung == null) {
			throw new NullPointerException("Gliederung must not be null");
		}
		this.Gliederung = Gliederung;
		this.Fachklasse_ID = Fachklasse_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOFachgliederungenPK other = (DTOFachgliederungenPK) obj;
		if (Fach_ID != other.Fach_ID)
			return false;
		if (Gliederung == null) {
			if (other.Gliederung != null)
				return false;
		} else if (!Gliederung.equals(other.Gliederung))
			return false;
		return Fachklasse_ID == other.Fachklasse_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Fach_ID);

		result = prime * result + ((Gliederung == null) ? 0 : Gliederung.hashCode());

		result = prime * result + Long.hashCode(Fachklasse_ID);
		return result;
	}
}
