package de.svws_nrw.db.dto.current.schild;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerReportvorlagen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOSchuelerReportvorlagenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerReportvorlagen")
@JsonPropertyOrder({"User_ID", "Reportvorlage", "Schueler_IDs"})
public final class DTOSchuelerReportvorlagen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerReportvorlagen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerReportvorlagen e WHERE e.User_ID = ?1 AND e.Reportvorlage = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerReportvorlagen e WHERE e.User_ID IS NOT NULL AND e.Reportvorlage IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes User_ID */
	public static final String QUERY_BY_USER_ID = "SELECT e FROM DTOSchuelerReportvorlagen e WHERE e.User_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes User_ID */
	public static final String QUERY_LIST_BY_USER_ID = "SELECT e FROM DTOSchuelerReportvorlagen e WHERE e.User_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Reportvorlage */
	public static final String QUERY_BY_REPORTVORLAGE = "SELECT e FROM DTOSchuelerReportvorlagen e WHERE e.Reportvorlage = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Reportvorlage */
	public static final String QUERY_LIST_BY_REPORTVORLAGE = "SELECT e FROM DTOSchuelerReportvorlagen e WHERE e.Reportvorlage IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_IDs */
	public static final String QUERY_BY_SCHUELER_IDS = "SELECT e FROM DTOSchuelerReportvorlagen e WHERE e.Schueler_IDs = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_IDs */
	public static final String QUERY_LIST_BY_SCHUELER_IDS = "SELECT e FROM DTOSchuelerReportvorlagen e WHERE e.Schueler_IDs IN ?1";

	/** UserID des Users der die zugeordneten Reportvorlagen druckt */
	@Id
	@Column(name = "User_ID")
	@JsonProperty
	public long User_ID;

	/** Pfad zur Reportvorlage */
	@Id
	@Column(name = "Reportvorlage")
	@JsonProperty
	public String Reportvorlage;

	/** SchülerID für die gedruckt werden soll (temporär) */
	@Column(name = "Schueler_IDs")
	@JsonProperty
	public String Schueler_IDs;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerReportvorlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerReportvorlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerReportvorlagen ohne eine Initialisierung der Attribute.
	 * @param User_ID   der Wert für das Attribut User_ID
	 */
	public DTOSchuelerReportvorlagen(final long User_ID) {
		this.User_ID = User_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerReportvorlagen other = (DTOSchuelerReportvorlagen) obj;
		if (User_ID != other.User_ID)
			return false;
		if (Reportvorlage == null) {
			if (other.Reportvorlage != null)
				return false;
		} else if (!Reportvorlage.equals(other.Reportvorlage))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(User_ID);

		result = prime * result + ((Reportvorlage == null) ? 0 : Reportvorlage.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerReportvorlagen(User_ID=" + this.User_ID + ", Reportvorlage=" + this.Reportvorlage + ", Schueler_IDs=" + this.Schueler_IDs + ")";
	}

}
