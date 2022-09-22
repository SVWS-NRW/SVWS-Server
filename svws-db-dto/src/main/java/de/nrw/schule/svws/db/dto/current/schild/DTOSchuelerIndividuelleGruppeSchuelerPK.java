package de.nrw.schule.svws.db.dto.current.schild;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerListe_Inhalt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOSchuelerIndividuelleGruppeSchuelerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID der individuellen Schülerliste */
	public Long Liste_ID;

	/** SchülerID des Schülers der zur individuellen Schülerliste gehört */
	public Long Schueler_ID;

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
	public DTOSchuelerIndividuelleGruppeSchuelerPK(final Long Liste_ID, final Long Schueler_ID) {
		if (Liste_ID == null) { 
			throw new NullPointerException("Liste_ID must not be null");
		}
		this.Liste_ID = Liste_ID;
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
		DTOSchuelerIndividuelleGruppeSchuelerPK other = (DTOSchuelerIndividuelleGruppeSchuelerPK) obj;
		if (Liste_ID == null) {
			if (other.Liste_ID != null)
				return false;
		} else if (!Liste_ID.equals(other.Liste_ID))
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
		result = prime * result + ((Liste_ID == null) ? 0 : Liste_ID.hashCode());

		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());
		return result;
	}
}