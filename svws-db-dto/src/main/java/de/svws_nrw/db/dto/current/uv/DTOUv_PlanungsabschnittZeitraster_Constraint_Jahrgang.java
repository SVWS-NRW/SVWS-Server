package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge")
@JsonPropertyOrder({"Planungsabschnitt_ID", "Zeitraster_ID", "Jahrgang_ID"})
public final class DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e WHERE e.Planungsabschnitt_ID = ?1 AND e.Zeitraster_ID = ?2 AND e.Jahrgang_ID = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e WHERE e.Planungsabschnitt_ID IS NOT NULL AND e.Zeitraster_ID IS NOT NULL AND e.Jahrgang_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zeitraster_ID */
	public static final String QUERY_BY_ZEITRASTER_ID = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e WHERE e.Zeitraster_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zeitraster_ID */
	public static final String QUERY_LIST_BY_ZEITRASTER_ID = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e WHERE e.Zeitraster_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e WHERE e.Jahrgang_ID IN ?1";

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Id
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/** ID des Zeitrasters des Planungsabschnitts */
	@Id
	@Column(name = "Zeitraster_ID")
	@JsonProperty
	public long Zeitraster_ID;

	/** ID des zugeordneten Jahrgangs */
	@Id
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public long Jahrgang_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang ohne eine Initialisierung der Attribute.
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 */
	public DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang(final long Planungsabschnitt_ID, final long Zeitraster_ID, final long Jahrgang_ID) {
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
		DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang other = (DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang) obj;
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


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang(Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ", Zeitraster_ID=" + this.Zeitraster_ID + ", Jahrgang_ID=" + this.Jahrgang_ID + ")";
	}

}
