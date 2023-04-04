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
	public Long Schuelerklausur_ID;

	/** ID der Klausurraumstunde */
	public Long KlausurRaumStunde_ID;

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
	public DTOGostKlausurenSchuelerklausurenRaeumeStundenPK(final Long Schuelerklausur_ID, final Long KlausurRaumStunde_ID) {
		if (Schuelerklausur_ID == null) {
			throw new NullPointerException("Schuelerklausur_ID must not be null");
		}
		this.Schuelerklausur_ID = Schuelerklausur_ID;
		if (KlausurRaumStunde_ID == null) {
			throw new NullPointerException("KlausurRaumStunde_ID must not be null");
		}
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
		if (Schuelerklausur_ID == null) {
			if (other.Schuelerklausur_ID != null)
				return false;
		} else if (!Schuelerklausur_ID.equals(other.Schuelerklausur_ID))
			return false;

		if (KlausurRaumStunde_ID == null) {
			if (other.KlausurRaumStunde_ID != null)
				return false;
		} else if (!KlausurRaumStunde_ID.equals(other.KlausurRaumStunde_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schuelerklausur_ID == null) ? 0 : Schuelerklausur_ID.hashCode());

		result = prime * result + ((KlausurRaumStunde_ID == null) ? 0 : KlausurRaumStunde_ID.hashCode());
		return result;
	}
}
