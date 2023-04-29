package de.svws_nrw.db.dto.current.schild.berufskolleg;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerZuweisungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOSchuelerZuweisungPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** LernabschnittsID  der Zuweisung (E G Kurse GE und PS SK) */
	public long Abschnitt_ID;

	/** FachID der Zuweisung */
	public long Fach_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerZuweisungPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerZuweisungPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerZuweisungPK.
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOSchuelerZuweisungPK(final long Abschnitt_ID, final long Fach_ID) {
		this.Abschnitt_ID = Abschnitt_ID;
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
		DTOSchuelerZuweisungPK other = (DTOSchuelerZuweisungPK) obj;
		if (Abschnitt_ID != other.Abschnitt_ID)
			return false;

		if (Fach_ID != other.Fach_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Abschnitt_ID);

		result = prime * result + Long.hashCode(Fach_ID);
		return result;
	}
}
