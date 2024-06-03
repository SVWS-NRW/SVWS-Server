package de.svws_nrw.db.dto.migration.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerLehramt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOLehrerLehramtPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerLehramt")
@JsonPropertyOrder({"Lehrer_ID", "LehramtKrz", "LehramtAnerkennungKrz", "SchulnrEigner"})
public final class MigrationDTOLehrerLehramt {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrerLehramt e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.Lehrer_ID = ?1 AND e.LehramtKrz = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.Lehrer_ID IS NOT NULL AND e.LehramtKrz IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehramtKrz */
	public static final String QUERY_BY_LEHRAMTKRZ = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.LehramtKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehramtKrz */
	public static final String QUERY_LIST_BY_LEHRAMTKRZ = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.LehramtKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehramtAnerkennungKrz */
	public static final String QUERY_BY_LEHRAMTANERKENNUNGKRZ = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.LehramtAnerkennungKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehramtAnerkennungKrz */
	public static final String QUERY_LIST_BY_LEHRAMTANERKENNUNGKRZ = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.LehramtAnerkennungKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerLehramt e WHERE e.SchulnrEigner IN ?1";

	/** LehrerID zu der das Lehramt gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Lehramtskürzel */
	@Id
	@Column(name = "LehramtKrz")
	@JsonProperty
	public String LehramtKrz;

	/** Lehramts-Anerkennung-Kürzel */
	@Column(name = "LehramtAnerkennungKrz")
	@JsonProperty
	public String LehramtAnerkennungKrz;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLehramt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerLehramt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLehramt ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public MigrationDTOLehrerLehramt(final Long Lehrer_ID) {
		if (Lehrer_ID == null) {
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOLehrerLehramt other = (MigrationDTOLehrerLehramt) obj;
		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;
		if (LehramtKrz == null) {
			if (other.LehramtKrz != null)
				return false;
		} else if (!LehramtKrz.equals(other.LehramtKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());

		result = prime * result + ((LehramtKrz == null) ? 0 : LehramtKrz.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOLehrerLehramt(Lehrer_ID=" + this.Lehrer_ID + ", LehramtKrz=" + this.LehramtKrz + ", LehramtAnerkennungKrz=" + this.LehramtAnerkennungKrz + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
