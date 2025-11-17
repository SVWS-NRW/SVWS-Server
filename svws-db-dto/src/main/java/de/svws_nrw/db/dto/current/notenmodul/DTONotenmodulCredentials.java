package de.svws_nrw.db.dto.current.notenmodul;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Notenmodul_Credentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Notenmodul_Credentials")
@JsonPropertyOrder({"idLehrer", "initialkennwort", "passwordHash"})
public final class DTONotenmodulCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTONotenmodulCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLehrer */
	public static final String QUERY_BY_IDLEHRER = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLehrer */
	public static final String QUERY_LIST_BY_IDLEHRER = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes initialkennwort */
	public static final String QUERY_BY_INITIALKENNWORT = "SELECT e FROM DTONotenmodulCredentials e WHERE e.initialkennwort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes initialkennwort */
	public static final String QUERY_LIST_BY_INITIALKENNWORT = "SELECT e FROM DTONotenmodulCredentials e WHERE e.initialkennwort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes passwordHash */
	public static final String QUERY_BY_PASSWORDHASH = "SELECT e FROM DTONotenmodulCredentials e WHERE e.passwordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes passwordHash */
	public static final String QUERY_LIST_BY_PASSWORDHASH = "SELECT e FROM DTONotenmodulCredentials e WHERE e.passwordHash IN ?1";

	/** Die LehrerID des Lehrers, für den die Credentials gelten */
	@Id
	@Column(name = "idLehrer")
	@JsonProperty
	public long idLehrer;

	/** Initialkennwort für den Credential-Datensatz */
	@Column(name = "initialkennwort")
	@JsonProperty
	public String initialkennwort;

	/** Passwordhash für den Credential-Datensatz */
	@Column(name = "passwordHash")
	@JsonProperty
	public String passwordHash;

	/**
	 * Erstellt ein neues Objekt der Klasse DTONotenmodulCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTONotenmodulCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTONotenmodulCredentials ohne eine Initialisierung der Attribute.
	 * @param idLehrer   der Wert für das Attribut idLehrer
	 * @param initialkennwort   der Wert für das Attribut initialkennwort
	 * @param passwordHash   der Wert für das Attribut passwordHash
	 */
	public DTONotenmodulCredentials(final long idLehrer, final String initialkennwort, final String passwordHash) {
		this.idLehrer = idLehrer;
		if (initialkennwort == null) {
			throw new NullPointerException("initialkennwort must not be null");
		}
		this.initialkennwort = initialkennwort;
		if (passwordHash == null) {
			throw new NullPointerException("passwordHash must not be null");
		}
		this.passwordHash = passwordHash;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTONotenmodulCredentials other = (DTONotenmodulCredentials) obj;
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
		return "DTONotenmodulCredentials(idLehrer=" + this.idLehrer + ", initialkennwort=" + this.initialkennwort + ", passwordHash=" + this.passwordHash + ")";
	}

}
