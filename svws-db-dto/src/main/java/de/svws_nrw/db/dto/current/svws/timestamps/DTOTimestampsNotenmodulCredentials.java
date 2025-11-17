package de.svws_nrw.db.dto.current.svws.timestamps;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TimestampsNotenmodulCredentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TimestampsNotenmodulCredentials")
@JsonPropertyOrder({"idLehrer", "tsPasswordHash"})
public final class DTOTimestampsNotenmodulCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTimestampsNotenmodulCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLehrer */
	public static final String QUERY_BY_IDLEHRER = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLehrer */
	public static final String QUERY_LIST_BY_IDLEHRER = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsPasswordHash */
	public static final String QUERY_BY_TSPASSWORDHASH = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsPasswordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsPasswordHash */
	public static final String QUERY_LIST_BY_TSPASSWORDHASH = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsPasswordHash IN ?1";

	/** die ID des Lehrers */
	@Id
	@Column(name = "idLehrer")
	@JsonProperty
	public long idLehrer;

	/** Der Zeitstempel der letzten Änderung an dem Password-Hash der Notenmodul-Credentials. */
	@Column(name = "tsPasswordHash")
	@JsonProperty
	public String tsPasswordHash;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsNotenmodulCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTimestampsNotenmodulCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsNotenmodulCredentials ohne eine Initialisierung der Attribute.
	 * @param idLehrer   der Wert für das Attribut idLehrer
	 * @param tsPasswordHash   der Wert für das Attribut tsPasswordHash
	 */
	public DTOTimestampsNotenmodulCredentials(final long idLehrer, final String tsPasswordHash) {
		this.idLehrer = idLehrer;
		if (tsPasswordHash == null) {
			throw new NullPointerException("tsPasswordHash must not be null");
		}
		this.tsPasswordHash = tsPasswordHash;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOTimestampsNotenmodulCredentials other = (DTOTimestampsNotenmodulCredentials) obj;
		return idLehrer == other.idLehrer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(idLehrer);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOTimestampsNotenmodulCredentials(idLehrer=" + this.idLehrer + ", tsPasswordHash=" + this.tsPasswordHash + ")";
	}

}
