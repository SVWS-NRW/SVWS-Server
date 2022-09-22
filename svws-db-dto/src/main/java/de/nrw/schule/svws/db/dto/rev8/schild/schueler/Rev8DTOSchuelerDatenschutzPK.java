package de.nrw.schule.svws.db.dto.rev8.schild.schueler;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOSchuelerDatenschutzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Fremdschlüssel auf den Katalog der DSGVO-Merkmale */
	public Long Datenschutz_ID;

	/** Fremdschlüssel auf Tabelle Schueler */
	public Long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuelerDatenschutzPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOSchuelerDatenschutzPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuelerDatenschutzPK.
	 * @param Datenschutz_ID   der Wert für das Attribut Datenschutz_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public Rev8DTOSchuelerDatenschutzPK(final Long Datenschutz_ID, final Long Schueler_ID) {
		if (Datenschutz_ID == null) { 
			throw new NullPointerException("Datenschutz_ID must not be null");
		}
		this.Datenschutz_ID = Datenschutz_ID;
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchuelerDatenschutzPK other = (Rev8DTOSchuelerDatenschutzPK) obj;
		if (Datenschutz_ID == null) {
			if (other.Datenschutz_ID != null)
				return false;
		} else if (!Datenschutz_ID.equals(other.Datenschutz_ID))
			return false;

		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Datenschutz_ID == null) ? 0 : Datenschutz_ID.hashCode());

		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());
		return result;
	}
}