package de.svws_nrw.db.dto.migration.schild.berufskolleg;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerZuweisungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOSchuelerZuweisungPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerZuweisungen")
@JsonPropertyOrder({"Abschnitt_ID", "Fach_ID", "Kursart", "SchulnrEigner"})
public final class MigrationDTOSchuelerZuweisung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerZuweisung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.Abschnitt_ID = ?1 AND e.Fach_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.Abschnitt_ID IS NOT NULL AND e.Fach_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursart */
	public static final String QUERY_BY_KURSART = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursart */
	public static final String QUERY_LIST_BY_KURSART = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerZuweisung e WHERE e.SchulnrEigner IN ?1";

	/** LernabschnittsID  der Zuweisung (E G Kurse GE und PS SK) */
	@Id
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** FachID der Zuweisung */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Kursart der Zuweisung */
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerZuweisung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerZuweisung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerZuweisung ohne eine Initialisierung der Attribute.
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public MigrationDTOSchuelerZuweisung(final Long Abschnitt_ID, final Long Fach_ID) {
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Fach_ID == null) {
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerZuweisung other = (MigrationDTOSchuelerZuweisung) obj;
		if (Abschnitt_ID == null) {
			if (other.Abschnitt_ID != null)
				return false;
		} else if (!Abschnitt_ID.equals(other.Abschnitt_ID))
			return false;
		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Abschnitt_ID == null) ? 0 : Abschnitt_ID.hashCode());

		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerZuweisung(Abschnitt_ID=" + this.Abschnitt_ID + ", Fach_ID=" + this.Fach_ID + ", Kursart=" + this.Kursart + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
