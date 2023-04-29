package de.svws_nrw.db.dto.current.schild.faecher;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle EigeneSchule_FachTeilleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOFachTeilleistungsartenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die eindeutige ID der Teilleistungsart – verweist auf die Teilleistungsart */
	public long Teilleistungsart_ID;

	/** Die eindeutige ID des Faches – verweist auf das Fach */
	public long Fach_ID;

	/** Gibt an, bei welcher Kursart die Teilleistungsart zugeordnet werden soll */
	public String Kursart;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachTeilleistungsartenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFachTeilleistungsartenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachTeilleistungsartenPK.
	 * @param Teilleistungsart_ID   der Wert für das Attribut Teilleistungsart_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Kursart   der Wert für das Attribut Kursart
	 */
	public DTOFachTeilleistungsartenPK(final long Teilleistungsart_ID, final long Fach_ID, final String Kursart) {
		this.Teilleistungsart_ID = Teilleistungsart_ID;
		this.Fach_ID = Fach_ID;
		if (Kursart == null) {
			throw new NullPointerException("Kursart must not be null");
		}
		this.Kursart = Kursart;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOFachTeilleistungsartenPK other = (DTOFachTeilleistungsartenPK) obj;
		if (Teilleistungsart_ID != other.Teilleistungsart_ID)
			return false;

		if (Fach_ID != other.Fach_ID)
			return false;

		if (Kursart == null) {
			if (other.Kursart != null)
				return false;
		} else if (!Kursart.equals(other.Kursart))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Teilleistungsart_ID);

		result = prime * result + Long.hashCode(Fach_ID);

		result = prime * result + ((Kursart == null) ? 0 : Kursart.hashCode());
		return result;
	}
}
