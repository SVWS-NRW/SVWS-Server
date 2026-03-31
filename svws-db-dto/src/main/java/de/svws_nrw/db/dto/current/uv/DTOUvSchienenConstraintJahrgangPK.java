package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_Schienen_Constraint_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUvSchienenConstraintJahrgangPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** ID der Schiene */
	public long Schiene_ID;

	/** ID des zugeordneten Jahrgangs */
	public long Jahrgang_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchienenConstraintJahrgangPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvSchienenConstraintJahrgangPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchienenConstraintJahrgangPK.
	 * @param Schiene_ID   der Wert für das Attribut Schiene_ID
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 */
	public DTOUvSchienenConstraintJahrgangPK(final long Schiene_ID, final long Jahrgang_ID) {
		this.Schiene_ID = Schiene_ID;
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
		DTOUvSchienenConstraintJahrgangPK other = (DTOUvSchienenConstraintJahrgangPK) obj;
		if (Schiene_ID != other.Schiene_ID) {
			return false;
		}
		return Jahrgang_ID == other.Jahrgang_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schiene_ID);

		result = prime * result + Long.hashCode(Jahrgang_ID);
		return result;
	}
}
