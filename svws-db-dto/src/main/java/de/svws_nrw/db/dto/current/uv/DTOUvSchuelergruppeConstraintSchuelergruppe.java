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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Schuelergruppen_Constraint_Schuelergruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOUvSchuelergruppeConstraintSchuelergruppePK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Schuelergruppen_Constraint_Schuelergruppen")
@JsonPropertyOrder({"Schuelergruppe_ID", "Schuelergruppe_Vaild_ID", "Planungsabschnitt_ID"})
public final class DTOUvSchuelergruppeConstraintSchuelergruppe {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e WHERE e.Schuelergruppe_ID = ?1 AND e.Schuelergruppe_Vaild_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e WHERE e.Schuelergruppe_ID IS NOT NULL AND e.Schuelergruppe_Vaild_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuelergruppe_ID */
	public static final String QUERY_BY_SCHUELERGRUPPE_ID = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e WHERE e.Schuelergruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuelergruppe_ID */
	public static final String QUERY_LIST_BY_SCHUELERGRUPPE_ID = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e WHERE e.Schuelergruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuelergruppe_Vaild_ID */
	public static final String QUERY_BY_SCHUELERGRUPPE_VAILD_ID = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e WHERE e.Schuelergruppe_Vaild_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuelergruppe_Vaild_ID */
	public static final String QUERY_LIST_BY_SCHUELERGRUPPE_VAILD_ID = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e WHERE e.Schuelergruppe_Vaild_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvSchuelergruppeConstraintSchuelergruppe e WHERE e.Planungsabschnitt_ID IN ?1";

	/** ID der Schülergruppe */
	@Id
	@Column(name = "Schuelergruppe_ID")
	@JsonProperty
	public long Schuelergruppe_ID;

	/** ID der Schülergruppe, deren Schüler dieser Schülergruppe angehören dürfen */
	@Id
	@Column(name = "Schuelergruppe_Vaild_ID")
	@JsonProperty
	public long Schuelergruppe_Vaild_ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchuelergruppeConstraintSchuelergruppe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvSchuelergruppeConstraintSchuelergruppe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchuelergruppeConstraintSchuelergruppe ohne eine Initialisierung der Attribute.
	 * @param Schuelergruppe_ID   der Wert für das Attribut Schuelergruppe_ID
	 * @param Schuelergruppe_Vaild_ID   der Wert für das Attribut Schuelergruppe_Vaild_ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 */
	public DTOUvSchuelergruppeConstraintSchuelergruppe(final long Schuelergruppe_ID, final long Schuelergruppe_Vaild_ID, final long Planungsabschnitt_ID) {
		this.Schuelergruppe_ID = Schuelergruppe_ID;
		this.Schuelergruppe_Vaild_ID = Schuelergruppe_Vaild_ID;
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
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
		DTOUvSchuelergruppeConstraintSchuelergruppe other = (DTOUvSchuelergruppeConstraintSchuelergruppe) obj;
		if (Schuelergruppe_ID != other.Schuelergruppe_ID) {
			return false;
		}
		return Schuelergruppe_Vaild_ID == other.Schuelergruppe_Vaild_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schuelergruppe_ID);

		result = prime * result + Long.hashCode(Schuelergruppe_Vaild_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvSchuelergruppeConstraintSchuelergruppe(Schuelergruppe_ID=" + this.Schuelergruppe_ID + ", Schuelergruppe_Vaild_ID=" + this.Schuelergruppe_Vaild_ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ")";
	}

}
