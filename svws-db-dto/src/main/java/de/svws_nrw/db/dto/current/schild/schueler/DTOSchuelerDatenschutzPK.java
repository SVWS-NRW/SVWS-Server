package de.svws_nrw.db.dto.current.schild.schueler;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOSchuelerDatenschutzPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Fremdschlüssel auf Tabelle Schueler */
	public long Schueler_ID;

	/** Fremdschlüssel auf den Katalog der DSGVO-Merkmale */
	public long Datenschutz_ID;

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
	public DTOSchuelerDatenschutzPK(final long Schueler_ID, final long Datenschutz_ID) {
		this.Schueler_ID = Schueler_ID;
		this.Datenschutz_ID = Datenschutz_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerDatenschutzPK other = (DTOSchuelerDatenschutzPK) obj;
		if (Schueler_ID != other.Schueler_ID)
			return false;

		if (Datenschutz_ID != other.Datenschutz_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);

		result = prime * result + Long.hashCode(Datenschutz_ID);
		return result;
	}
}
