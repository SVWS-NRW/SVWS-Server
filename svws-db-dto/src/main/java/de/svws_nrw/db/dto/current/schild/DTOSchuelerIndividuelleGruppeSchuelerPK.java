package de.svws_nrw.db.dto.current.schild;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerListe_Inhalt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOSchuelerIndividuelleGruppeSchuelerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID der individuellen Schülerliste */
	public long Liste_ID;

	/** SchülerID des Schülers der zur individuellen Schülerliste gehört */
	public long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerIndividuelleGruppeSchuelerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerIndividuelleGruppeSchuelerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerIndividuelleGruppeSchuelerPK.
	 * @param Liste_ID   der Wert für das Attribut Liste_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerIndividuelleGruppeSchuelerPK(final long Liste_ID, final long Schueler_ID) {
		this.Liste_ID = Liste_ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerIndividuelleGruppeSchuelerPK other = (DTOSchuelerIndividuelleGruppeSchuelerPK) obj;
		if (Liste_ID != other.Liste_ID)
			return false;

		if (Schueler_ID != other.Schueler_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Liste_ID);

		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}
}
