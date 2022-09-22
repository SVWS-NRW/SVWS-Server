package de.nrw.schule.svws.db.dto.rev8.gost;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Schueler_Fachwahlen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOGostSchuelerFachbelegungenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Schülers in der Schuelertabelle */
	public Long Schueler_ID;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Faches in der Fächertabelle */
	public Long Fach_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostSchuelerFachbelegungenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOGostSchuelerFachbelegungenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostSchuelerFachbelegungenPK.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public Rev8DTOGostSchuelerFachbelegungenPK(final Long Schueler_ID, final Long Fach_ID) {
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOGostSchuelerFachbelegungenPK other = (Rev8DTOGostSchuelerFachbelegungenPK) obj;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;

		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());

		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());
		return result;
	}
}