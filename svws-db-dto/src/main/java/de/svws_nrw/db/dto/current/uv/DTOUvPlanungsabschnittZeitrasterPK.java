package de.svws_nrw.db.dto.current.uv;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle UV_Planungsabschnitte_Zeitraster.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOUvPlanungsabschnittZeitrasterPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	public long Planungsabschnitt_ID;

	/** ID des Zeitrasters des Planungsabschnitts */
	public long Zeitraster_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvPlanungsabschnittZeitrasterPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvPlanungsabschnittZeitrasterPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvPlanungsabschnittZeitrasterPK.
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 */
	public DTOUvPlanungsabschnittZeitrasterPK(final long Planungsabschnitt_ID, final long Zeitraster_ID) {
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Zeitraster_ID = Zeitraster_ID;
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
		DTOUvPlanungsabschnittZeitrasterPK other = (DTOUvPlanungsabschnittZeitrasterPK) obj;
		if (Planungsabschnitt_ID != other.Planungsabschnitt_ID) {
			return false;
		}
		return Zeitraster_ID == other.Zeitraster_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Planungsabschnitt_ID);

		result = prime * result + Long.hashCode(Zeitraster_ID);
		return result;
	}
}
