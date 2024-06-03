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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerAnrechnung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerAnrechnung")
@JsonPropertyOrder({"ID", "Lehrer_ID", "Abschnitt_ID", "AnrechnungsgrundKrz", "AnrechnungStd", "Jahr", "Abschnitt", "SchulnrEigner"})
public final class MigrationDTOLehrerAnrechnungsstunde {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnrechnungsgrundKrz */
	public static final String QUERY_BY_ANRECHNUNGSGRUNDKRZ = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.AnrechnungsgrundKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnrechnungsgrundKrz */
	public static final String QUERY_LIST_BY_ANRECHNUNGSGRUNDKRZ = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.AnrechnungsgrundKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnrechnungStd */
	public static final String QUERY_BY_ANRECHNUNGSTD = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.AnrechnungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnrechnungStd */
	public static final String QUERY_LIST_BY_ANRECHNUNGSTD = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.AnrechnungStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Abschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.SchulnrEigner IN ?1";

	/** ID für den Eintrag für die Anrechnungsstunden */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED: Lehrer-ID für die Anrechnungsstunden, in LehrerAbchnittsdaten enthalten */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Anrechnungsstundentext  für die Anrechnungsstunden */
	@Column(name = "AnrechnungsgrundKrz")
	@JsonProperty
	public String AnrechnungsgrundKrz;

	/** Zahl der Anrechnungsstunden für die Anrechnungsstunden */
	@Column(name = "AnrechnungStd")
	@JsonProperty
	public Double AnrechnungStd;

	/** Schuljahr für die Anrechnungsstunden */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnit für die Anrechnungsstunden */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerAnrechnungsstunde ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerAnrechnungsstunde() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerAnrechnungsstunde ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public MigrationDTOLehrerAnrechnungsstunde(final Long ID, final Long Lehrer_ID, final Long Abschnitt_ID) {
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
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOLehrerAnrechnungsstunde other = (MigrationDTOLehrerAnrechnungsstunde) obj;
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
		return "MigrationDTOLehrerAnrechnungsstunde(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", AnrechnungsgrundKrz=" + this.AnrechnungsgrundKrz + ", AnrechnungStd=" + this.AnrechnungStd + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
