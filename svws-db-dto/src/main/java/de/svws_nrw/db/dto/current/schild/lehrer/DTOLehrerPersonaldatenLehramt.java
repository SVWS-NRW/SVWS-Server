package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerPersonaldatenLehramt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerPersonaldatenLehramt")
@JsonPropertyOrder({"id", "idLehrer", "idKatalogLehramt", "idAnerkennungsgrund"})
public final class DTOLehrerPersonaldatenLehramt {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerPersonaldatenLehramt e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLehrer */
	public static final String QUERY_BY_IDLEHRER = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLehrer */
	public static final String QUERY_LIST_BY_IDLEHRER = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idKatalogLehramt */
	public static final String QUERY_BY_IDKATALOGLEHRAMT = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.idKatalogLehramt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idKatalogLehramt */
	public static final String QUERY_LIST_BY_IDKATALOGLEHRAMT = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.idKatalogLehramt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAnerkennungsgrund */
	public static final String QUERY_BY_IDANERKENNUNGSGRUND = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.idAnerkennungsgrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAnerkennungsgrund */
	public static final String QUERY_LIST_BY_IDANERKENNUNGSGRUND = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.idAnerkennungsgrund IN ?1";

	/** Eine eindeutige ID für den Eintrag zum Lehramt eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** Die ID des Lehrers zu der das Lehramt gehört */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long idLehrer;

	/** Die ID des Lehramtes aus dem zugehörigen Statistik-Katalog */
	@Column(name = "Lehramt_Katalog_ID")
	@JsonProperty
	public Long idKatalogLehramt;

	/** Die ID der Lehramts-Anerkennung aus dem zugehörigen Statistik-Katalog */
	@Column(name = "LehramtAnerkennung_Katalog_ID")
	@JsonProperty
	public Long idAnerkennungsgrund;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerPersonaldatenLehramt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerPersonaldatenLehramt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerPersonaldatenLehramt ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idLehrer   der Wert für das Attribut idLehrer
	 */
	public DTOLehrerPersonaldatenLehramt(final long id, final long idLehrer) {
		this.id = id;
		this.idLehrer = idLehrer;
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
		DTOLehrerPersonaldatenLehramt other = (DTOLehrerPersonaldatenLehramt) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLehrerPersonaldatenLehramt(id=" + this.id + ", idLehrer=" + this.idLehrer + ", idKatalogLehramt=" + this.idKatalogLehramt + ", idAnerkennungsgrund=" + this.idAnerkennungsgrund + ")";
	}

}
