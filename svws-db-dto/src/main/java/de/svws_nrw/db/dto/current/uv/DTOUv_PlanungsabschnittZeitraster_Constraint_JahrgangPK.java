package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	public long Planungsabschnitt_ID;

	/** ID des Zeitrasters des Planungsabschnitts */
	public long Zeitraster_ID;

	/** ID des zugeordneten Jahrgangs */
	public long Jahrgang_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK.
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 */
	public DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK(final long Planungsabschnitt_ID, final long Zeitraster_ID, final long Jahrgang_ID) {
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Zeitraster_ID = Zeitraster_ID;
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
		DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK other = (DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK) obj;
		if (Planungsabschnitt_ID != other.Planungsabschnitt_ID) {
			return false;
		}
		if (Zeitraster_ID != other.Zeitraster_ID) {
			return false;
		}
		return Jahrgang_ID == other.Jahrgang_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Planungsabschnitt_ID);

		result = prime * result + Long.hashCode(Zeitraster_ID);

		result = prime * result + Long.hashCode(Jahrgang_ID);
		return result;
	}
}
