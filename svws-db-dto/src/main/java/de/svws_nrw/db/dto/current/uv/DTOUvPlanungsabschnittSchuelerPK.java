package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_Planungsabschnitte_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUvPlanungsabschnittSchuelerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	public long Planungsabschnitt_ID;

	/** Die ID des Schülers als Fremdschlüssel auf die Tabelle Schueler */
	public long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvPlanungsabschnittSchuelerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvPlanungsabschnittSchuelerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvPlanungsabschnittSchuelerPK.
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOUvPlanungsabschnittSchuelerPK(final long Planungsabschnitt_ID, final long Schueler_ID) {
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
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
		DTOUvPlanungsabschnittSchuelerPK other = (DTOUvPlanungsabschnittSchuelerPK) obj;
		if (Planungsabschnitt_ID != other.Planungsabschnitt_ID) {
			return false;
		}
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Planungsabschnitt_ID);

		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}
}
