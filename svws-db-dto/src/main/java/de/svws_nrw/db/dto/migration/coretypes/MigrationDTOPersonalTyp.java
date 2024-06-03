package de.svws_nrw.db.dto.migration.coretypes;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle PersonalTypen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "PersonalTypen")
@JsonPropertyOrder({"ID", "Kuerzel", "Bezeichnung", "gueltigVon", "gueltigBis"})
public final class MigrationDTOPersonalTyp {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOPersonalTyp e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.gueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.gueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.gueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM MigrationDTOPersonalTyp e WHERE e.gueltigBis IN ?1";

	/** ID des Personal-Typs */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Kürzel des Personal-Typs */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Die (Lang-)Bezeichnung des Personal-Typs */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Der Datensatz ist gültig ab dem Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Der Datensatz ist gültig bis zu dem Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOPersonalTyp ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOPersonalTyp() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOPersonalTyp ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOPersonalTyp(final Long ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOPersonalTyp other = (MigrationDTOPersonalTyp) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOPersonalTyp(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
