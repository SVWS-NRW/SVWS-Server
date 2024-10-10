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
 * Diese Klasse dient als DTO für die Datenbanktabelle TimestampsLehrerNotenmodulCredentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TimestampsLehrerNotenmodulCredentials")
@JsonPropertyOrder({"Lehrer_ID", "tsPasswordHash"})
public final class DTOTimestampsLehrerNotenmodulCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTimestampsLehrerNotenmodulCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTimestampsLehrerNotenmodulCredentials e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTimestampsLehrerNotenmodulCredentials e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTimestampsLehrerNotenmodulCredentials e WHERE e.Lehrer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOTimestampsLehrerNotenmodulCredentials e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOTimestampsLehrerNotenmodulCredentials e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsPasswordHash */
	public static final String QUERY_BY_TSPASSWORDHASH = "SELECT e FROM DTOTimestampsLehrerNotenmodulCredentials e WHERE e.tsPasswordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsPasswordHash */
	public static final String QUERY_LIST_BY_TSPASSWORDHASH = "SELECT e FROM DTOTimestampsLehrerNotenmodulCredentials e WHERE e.tsPasswordHash IN ?1";

	/** die ID des Lehrers */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Der Zeitstempel der letzten Änderung an dem Password-Hash der Lehrer-Notenmodul-Credentials. */
	@Column(name = "tsPasswordHash")
	@JsonProperty
	public String tsPasswordHash;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsLehrerNotenmodulCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTimestampsLehrerNotenmodulCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsLehrerNotenmodulCredentials ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param tsPasswordHash   der Wert für das Attribut tsPasswordHash
	 */
	public DTOTimestampsLehrerNotenmodulCredentials(final long Lehrer_ID, final String tsPasswordHash) {
		this.Lehrer_ID = Lehrer_ID;
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
		DTOTimestampsLehrerNotenmodulCredentials other = (DTOTimestampsLehrerNotenmodulCredentials) obj;
		return Lehrer_ID == other.Lehrer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Lehrer_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOTimestampsLehrerNotenmodulCredentials(Lehrer_ID=" + this.Lehrer_ID + ", tsPasswordHash=" + this.tsPasswordHash + ")";
	}

}
