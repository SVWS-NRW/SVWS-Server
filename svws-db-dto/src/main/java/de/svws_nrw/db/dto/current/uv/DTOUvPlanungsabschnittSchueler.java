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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Planungsabschnitte_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOUvPlanungsabschnittSchuelerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Planungsabschnitte_Schueler")
@JsonPropertyOrder({"Planungsabschnitt_ID", "Schueler_ID", "Jahrgang_ID", "Klasse_ID"})
public final class DTOUvPlanungsabschnittSchueler {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvPlanungsabschnittSchueler e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Planungsabschnitt_ID = ?1 AND e.Schueler_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Planungsabschnitt_ID IS NOT NULL AND e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klasse_ID */
	public static final String QUERY_BY_KLASSE_ID = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Klasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klasse_ID */
	public static final String QUERY_LIST_BY_KLASSE_ID = "SELECT e FROM DTOUvPlanungsabschnittSchueler e WHERE e.Klasse_ID IN ?1";

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Id
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/** Die ID des Schülers als Fremdschlüssel auf die Tabelle Schueler */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** ID des Jahrgangs der dem Schüler zugeordnet wird */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public long Jahrgang_ID;

	/** Klassen_ID des Schülers für den Planungsabschnitt */
	@Column(name = "Klasse_ID")
	@JsonProperty
	public long Klasse_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvPlanungsabschnittSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvPlanungsabschnittSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvPlanungsabschnittSchueler ohne eine Initialisierung der Attribute.
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 * @param Klasse_ID   der Wert für das Attribut Klasse_ID
	 */
	public DTOUvPlanungsabschnittSchueler(final long Planungsabschnitt_ID, final long Schueler_ID, final long Jahrgang_ID, final long Klasse_ID) {
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Schueler_ID = Schueler_ID;
		this.Jahrgang_ID = Jahrgang_ID;
		this.Klasse_ID = Klasse_ID;
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
		DTOUvPlanungsabschnittSchueler other = (DTOUvPlanungsabschnittSchueler) obj;
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


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvPlanungsabschnittSchueler(Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ", Schueler_ID=" + this.Schueler_ID + ", Jahrgang_ID=" + this.Jahrgang_ID + ", Klasse_ID=" + this.Klasse_ID + ")";
	}

}
