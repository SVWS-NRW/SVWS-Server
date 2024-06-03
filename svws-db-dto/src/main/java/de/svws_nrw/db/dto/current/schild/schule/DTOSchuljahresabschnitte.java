package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schuljahresabschnitte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schuljahresabschnitte")
@JsonPropertyOrder({"ID", "Jahr", "Abschnitt", "VorigerAbschnitt_ID", "FolgeAbschnitt_ID"})
public final class DTOSchuljahresabschnitte {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuljahresabschnitte e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Abschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VorigerAbschnitt_ID */
	public static final String QUERY_BY_VORIGERABSCHNITT_ID = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.VorigerAbschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VorigerAbschnitt_ID */
	public static final String QUERY_LIST_BY_VORIGERABSCHNITT_ID = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.VorigerAbschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FolgeAbschnitt_ID */
	public static final String QUERY_BY_FOLGEABSCHNITT_ID = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.FolgeAbschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FolgeAbschnitt_ID */
	public static final String QUERY_LIST_BY_FOLGEABSCHNITT_ID = "SELECT e FROM DTOSchuljahresabschnitte e WHERE e.FolgeAbschnitt_ID IN ?1";

	/** ID des Schuljahresabschnittes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Schuljahr des Schuljahresabschnitts (z.B. 2012 für 2012/13) */
	@Column(name = "Jahr")
	@JsonProperty
	public int Jahr;

	/** Abschnitt des Schuljahresabschnitts */
	@Column(name = "Abschnitt")
	@JsonProperty
	public int Abschnitt;

	/** ID des vorigen Schuljahresabschnitts */
	@Column(name = "VorigerAbschnitt_ID")
	@JsonProperty
	public Long VorigerAbschnitt_ID;

	/** ID des nachfolgenden Schuljahresabschnitts */
	@Column(name = "FolgeAbschnitt_ID")
	@JsonProperty
	public Long FolgeAbschnitt_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuljahresabschnitte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuljahresabschnitte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuljahresabschnitte ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public DTOSchuljahresabschnitte(final long ID, final int Jahr, final int Abschnitt) {
		this.ID = ID;
		this.Jahr = Jahr;
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
		DTOSchuljahresabschnitte other = (DTOSchuljahresabschnitte) obj;
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
		return "DTOSchuljahresabschnitte(ID=" + this.ID + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", VorigerAbschnitt_ID=" + this.VorigerAbschnitt_ID + ", FolgeAbschnitt_ID=" + this.FolgeAbschnitt_ID + ")";
	}

}
