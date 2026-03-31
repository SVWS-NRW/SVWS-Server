package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_Schuelergruppen_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUvSchuelergruppeSchuelerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID der Schülergruppe als Fremdschlüssel auf die Tabelle UV_Schuelergruppen */
	public long Schuelergruppe_ID;

	/** Die ID des Schülers als Fremdschlüssel auf die Tabelle Schueler */
	public long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchuelergruppeSchuelerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvSchuelergruppeSchuelerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchuelergruppeSchuelerPK.
	 * @param Schuelergruppe_ID   der Wert für das Attribut Schuelergruppe_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOUvSchuelergruppeSchuelerPK(final long Schuelergruppe_ID, final long Schueler_ID) {
		this.Schuelergruppe_ID = Schuelergruppe_ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOUvSchuelergruppeSchuelerPK other = (DTOUvSchuelergruppeSchuelerPK) obj;
		if (Schuelergruppe_ID != other.Schuelergruppe_ID) {
			return false;
		}
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schuelergruppe_ID);

		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}
}
