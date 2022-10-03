package de.nrw.schule.svws.db.dto.current.schild.schueler;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOSchuelerDatenschutzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Fremdschlüssel auf Tabelle Schueler */
	public Long Schueler_ID;

	/** Fremdschlüssel auf den Katalog der DSGVO-Merkmale */
	public Long Datenschutz_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerDatenschutzPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerDatenschutzPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerDatenschutzPK.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Datenschutz_ID   der Wert für das Attribut Datenschutz_ID
	 */
	public DTOSchuelerDatenschutzPK(final Long Schueler_ID, final Long Datenschutz_ID) {
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Datenschutz_ID == null) { 
			throw new NullPointerException("Datenschutz_ID must not be null");
		}
		this.Datenschutz_ID = Datenschutz_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerDatenschutzPK other = (DTOSchuelerDatenschutzPK) obj;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;

		if (Datenschutz_ID == null) {
			if (other.Datenschutz_ID != null)
				return false;
		} else if (!Datenschutz_ID.equals(other.Datenschutz_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());

		result = prime * result + ((Datenschutz_ID == null) ? 0 : Datenschutz_ID.hashCode());
		return result;
	}
}