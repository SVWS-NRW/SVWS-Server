package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Lehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Lehrer")
@JsonPropertyOrder({"ID", "K_Lehrer_ID", "Kuerzel", "Nachname", "Vorname"})
public final class DTOUvLehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvLehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvLehrer e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes K_Lehrer_ID */
	public static final String QUERY_BY_K_LEHRER_ID = "SELECT e FROM DTOUvLehrer e WHERE e.K_Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes K_Lehrer_ID */
	public static final String QUERY_LIST_BY_K_LEHRER_ID = "SELECT e FROM DTOUvLehrer e WHERE e.K_Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOUvLehrer e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOUvLehrer e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nachname */
	public static final String QUERY_BY_NACHNAME = "SELECT e FROM DTOUvLehrer e WHERE e.Nachname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nachname */
	public static final String QUERY_LIST_BY_NACHNAME = "SELECT e FROM DTOUvLehrer e WHERE e.Nachname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vorname */
	public static final String QUERY_BY_VORNAME = "SELECT e FROM DTOUvLehrer e WHERE e.Vorname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vorname */
	public static final String QUERY_LIST_BY_VORNAME = "SELECT e FROM DTOUvLehrer e WHERE e.Vorname IN ?1";

	/** ID des Lehrers im Planungsabschnitt (generiert, planungsspezifisch) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer */
	@Column(name = "K_Lehrer_ID")
	@JsonProperty
	public Long K_Lehrer_ID;

	/** Lehrer-Kürzel für eine lesbare eindeutige Identifikation des Lehrers */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Der Nachname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Nachname")
	@JsonProperty
	public String Nachname;

	/** Der Vorname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt. */
	@Column(name = "Vorname")
	@JsonProperty
	public String Vorname;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvLehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvLehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvLehrer ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOUvLehrer(final long ID) {
		this.ID = ID;
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
		DTOUvLehrer other = (DTOUvLehrer) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvLehrer(ID=" + this.ID + ", K_Lehrer_ID=" + this.K_Lehrer_ID + ", Kuerzel=" + this.Kuerzel + ", Nachname=" + this.Nachname + ", Vorname=" + this.Vorname + ")";
	}

}
