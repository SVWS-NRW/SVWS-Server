package de.svws_nrw.db.dto.current.gost.klausurplanung;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Gost_Klausuren_Schuelerklausuren_Raeume_Stunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOGostKlausurenSchuelerklausurenRaeumeStundenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID der Schuelerklausur */
	public long Schuelerklausur_ID;

	/** ID der Klausurraumstunde */
	public long KlausurRaumStunde_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenRaeumeStundenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenSchuelerklausurenRaeumeStundenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenRaeumeStundenPK.
	 * @param Schuelerklausur_ID   der Wert für das Attribut Schuelerklausur_ID
	 * @param KlausurRaumStunde_ID   der Wert für das Attribut KlausurRaumStunde_ID
	 */
	public DTOGostKlausurenSchuelerklausurenRaeumeStundenPK(final long Schuelerklausur_ID, final long KlausurRaumStunde_ID) {
		this.Schuelerklausur_ID = Schuelerklausur_ID;
		this.KlausurRaumStunde_ID = KlausurRaumStunde_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenSchuelerklausurenRaeumeStundenPK other = (DTOGostKlausurenSchuelerklausurenRaeumeStundenPK) obj;
		if (Schuelerklausur_ID != other.Schuelerklausur_ID)
			return false;
		return KlausurRaumStunde_ID == other.KlausurRaumStunde_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schuelerklausur_ID);

		result = prime * result + Long.hashCode(KlausurRaumStunde_ID);
		return result;
	}
}
