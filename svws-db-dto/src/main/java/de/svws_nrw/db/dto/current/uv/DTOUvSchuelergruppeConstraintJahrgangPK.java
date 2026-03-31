package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_Schuelergruppen_Constraint_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUvSchuelergruppeConstraintJahrgangPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID der Schülergruppe */
	public long Schuelergruppe_ID;

	/** ID des zugeordneten Jahrgangs */
	public long Jahrgang_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchuelergruppeConstraintJahrgangPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvSchuelergruppeConstraintJahrgangPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchuelergruppeConstraintJahrgangPK.
	 * @param Schuelergruppe_ID   der Wert für das Attribut Schuelergruppe_ID
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 */
	public DTOUvSchuelergruppeConstraintJahrgangPK(final long Schuelergruppe_ID, final long Jahrgang_ID) {
		this.Schuelergruppe_ID = Schuelergruppe_ID;
		this.Jahrgang_ID = Jahrgang_ID;
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
		DTOUvSchuelergruppeConstraintJahrgangPK other = (DTOUvSchuelergruppeConstraintJahrgangPK) obj;
		if (Schuelergruppe_ID != other.Schuelergruppe_ID) {
			return false;
		}
		return Jahrgang_ID == other.Jahrgang_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schuelergruppe_ID);

		result = prime * result + Long.hashCode(Jahrgang_ID);
		return result;
	}
}
