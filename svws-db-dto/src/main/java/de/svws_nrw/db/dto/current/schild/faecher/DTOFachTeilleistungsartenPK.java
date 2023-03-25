package de.svws_nrw.db.dto.current.schild.faecher;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle EigeneSchule_FachTeilleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOFachTeilleistungsartenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die eindeutige ID der Teilleistungsart – verweist auf die Teilleistungsart */
	public Long Teilleistungsart_ID;

	/** Die eindeutige ID des Faches – verweist auf das Fach */
	public Long Fach_ID;

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
	public DTOFachTeilleistungsartenPK(final Long Teilleistungsart_ID, final Long Fach_ID, final String Kursart) {
		if (Teilleistungsart_ID == null) { 
			throw new NullPointerException("Teilleistungsart_ID must not be null");
		}
		this.Teilleistungsart_ID = Teilleistungsart_ID;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
		if (Kursart == null) { 
			throw new NullPointerException("Kursart must not be null");
		}
		this.Kursart = Kursart;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOFachTeilleistungsartenPK other = (DTOFachTeilleistungsartenPK) obj;
		if (Teilleistungsart_ID == null) {
			if (other.Teilleistungsart_ID != null)
				return false;
		} else if (!Teilleistungsart_ID.equals(other.Teilleistungsart_ID))
			return false;

		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
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
		result = prime * result + ((Teilleistungsart_ID == null) ? 0 : Teilleistungsart_ID.hashCode());

		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());

		result = prime * result + ((Kursart == null) ? 0 : Kursart.hashCode());
		return result;
	}
}