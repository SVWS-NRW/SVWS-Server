package de.svws_nrw.db.dto.current.gost;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Schueler_Fachwahlen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostSchuelerFachbelegungenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Schülers in der Schuelertabelle */
	public long Schueler_ID;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Faches in der Fächertabelle */
	public long Fach_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchuelerFachbelegungenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostSchuelerFachbelegungenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchuelerFachbelegungenPK.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOGostSchuelerFachbelegungenPK(final long Schueler_ID, final long Fach_ID) {
		this.Schueler_ID = Schueler_ID;
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
		DTOGostSchuelerFachbelegungenPK other = (DTOGostSchuelerFachbelegungenPK) obj;
		if (Schueler_ID != other.Schueler_ID)
			return false;

		if (Fach_ID != other.Fach_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);

		result = prime * result + Long.hashCode(Fach_ID);
		return result;
	}
}
