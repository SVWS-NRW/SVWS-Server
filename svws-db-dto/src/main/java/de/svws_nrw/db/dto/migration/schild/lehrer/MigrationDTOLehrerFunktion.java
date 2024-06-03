package de.svws_nrw.db.dto.migration.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerFunktionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerFunktionen")
@JsonPropertyOrder({"ID", "Lehrer_ID", "Abschnitt_ID", "Funktion_ID", "Jahr", "Abschnitt", "SchulnrEigner"})
public final class MigrationDTOLehrerFunktion {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrerFunktion e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Funktion_ID */
	public static final String QUERY_BY_FUNKTION_ID = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Funktion_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Funktion_ID */
	public static final String QUERY_LIST_BY_FUNKTION_ID = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Funktion_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.Abschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerFunktion e WHERE e.SchulnrEigner IN ?1";

	/** ID für den Eintrag für die schulinterne Funktion eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED: Lehrer-ID zu der die schulinterne Funktion gehört, in LehrerAbchnittsdaten enthalten */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** ID der schulinternen Funktion */
	@Column(name = "Funktion_ID")
	@JsonProperty
	public Long Funktion_ID;

	/** Schuljahr zu dem die schulinterne Funktion gehört */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt zu dem die schulinterne Funktion gehört */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerFunktion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerFunktion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerFunktion ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Funktion_ID   der Wert für das Attribut Funktion_ID
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOLehrerFunktion(final Long ID, final Long Lehrer_ID, final Long Abschnitt_ID, final Long Funktion_ID, final Integer Jahr, final Integer Abschnitt) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Lehrer_ID == null) {
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Funktion_ID == null) {
			throw new NullPointerException("Funktion_ID must not be null");
		}
		this.Funktion_ID = Funktion_ID;
		if (Jahr == null) {
			throw new NullPointerException("Jahr must not be null");
		}
		this.Jahr = Jahr;
		if (Abschnitt == null) {
			throw new NullPointerException("Abschnitt must not be null");
		}
		this.Abschnitt = Abschnitt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOLehrerFunktion other = (MigrationDTOLehrerFunktion) obj;
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
		return "MigrationDTOLehrerFunktion(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Funktion_ID=" + this.Funktion_ID + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
