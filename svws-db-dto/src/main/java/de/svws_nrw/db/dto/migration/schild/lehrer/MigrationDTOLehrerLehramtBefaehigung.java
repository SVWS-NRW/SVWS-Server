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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerLehramtLehrbef.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOLehrerLehramtBefaehigungPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerLehramtLehrbef")
@JsonPropertyOrder({"Lehrer_ID", "LehramtKrz", "LehrbefKrz", "LehrbefAnerkennungKrz", "SchulnrEigner"})
public final class MigrationDTOLehrerLehramtBefaehigung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID = ?1 AND e.LehramtKrz = ?2 AND e.LehrbefKrz = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID IS NOT NULL AND e.LehramtKrz IS NOT NULL AND e.LehrbefKrz IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehramtKrz */
	public static final String QUERY_BY_LEHRAMTKRZ = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.LehramtKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehramtKrz */
	public static final String QUERY_LIST_BY_LEHRAMTKRZ = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.LehramtKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrbefKrz */
	public static final String QUERY_BY_LEHRBEFKRZ = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.LehrbefKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrbefKrz */
	public static final String QUERY_LIST_BY_LEHRBEFKRZ = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.LehrbefKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrbefAnerkennungKrz */
	public static final String QUERY_BY_LEHRBEFANERKENNUNGKRZ = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.LehrbefAnerkennungKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrbefAnerkennungKrz */
	public static final String QUERY_LIST_BY_LEHRBEFANERKENNUNGKRZ = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.LehrbefAnerkennungKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerLehramtBefaehigung e WHERE e.SchulnrEigner IN ?1";

	/** LehrerID zu der die Lehrbefähigung gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Lehramtskürzel */
	@Id
	@Column(name = "LehramtKrz")
	@JsonProperty
	public String LehramtKrz;

	/** Kürzel der Lehrbefähigung */
	@Id
	@Column(name = "LehrbefKrz")
	@JsonProperty
	public String LehrbefKrz;

	/** Kürzel der LehrbefähigungsAnerkennung */
	@Column(name = "LehrbefAnerkennungKrz")
	@JsonProperty
	public String LehrbefAnerkennungKrz;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLehramtBefaehigung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerLehramtBefaehigung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLehramtBefaehigung ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public MigrationDTOLehrerLehramtBefaehigung(final Long Lehrer_ID) {
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
		MigrationDTOLehrerLehramtBefaehigung other = (MigrationDTOLehrerLehramtBefaehigung) obj;
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
		if (LehrbefKrz == null) {
			if (other.LehrbefKrz != null)
				return false;
		} else if (!LehrbefKrz.equals(other.LehrbefKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());

		result = prime * result + ((LehramtKrz == null) ? 0 : LehramtKrz.hashCode());

		result = prime * result + ((LehrbefKrz == null) ? 0 : LehrbefKrz.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOLehrerLehramtBefaehigung(Lehrer_ID=" + this.Lehrer_ID + ", LehramtKrz=" + this.LehramtKrz + ", LehrbefKrz=" + this.LehrbefKrz + ", LehrbefAnerkennungKrz=" + this.LehrbefAnerkennungKrz + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
