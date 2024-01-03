package de.svws_nrw.db.dto.current.gost.klausurplanung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Klausuren_SchuelerklausurenTermine_Raumstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID des Schuelerklausurtermins */
	public long Schuelerklausurtermin_ID;

	/** ID der Klausurraumstunde */
	public long Raumstunde_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK.
	 * @param Schuelerklausurtermin_ID   der Wert für das Attribut Schuelerklausurtermin_ID
	 * @param Raumstunde_ID   der Wert für das Attribut Raumstunde_ID
	 */
	public DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK(final long Schuelerklausurtermin_ID, final long Raumstunde_ID) {
		this.Schuelerklausurtermin_ID = Schuelerklausurtermin_ID;
		this.Raumstunde_ID = Raumstunde_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK other = (DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK) obj;
		if (Schuelerklausurtermin_ID != other.Schuelerklausurtermin_ID)
			return false;
		return Raumstunde_ID == other.Raumstunde_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schuelerklausurtermin_ID);

		result = prime * result + Long.hashCode(Raumstunde_ID);
		return result;
	}
}
