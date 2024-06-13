package de.svws_nrw.db.dto.current.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerNotenmodulCredentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerNotenmodulCredentials")
@JsonPropertyOrder({"Lehrer_ID", "Initialkennwort", "PasswordHash"})
public final class DTOLehrerNotenmodulCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerNotenmodulCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.Lehrer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Initialkennwort */
	public static final String QUERY_BY_INITIALKENNWORT = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.Initialkennwort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Initialkennwort */
	public static final String QUERY_LIST_BY_INITIALKENNWORT = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.Initialkennwort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PasswordHash */
	public static final String QUERY_BY_PASSWORDHASH = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.PasswordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PasswordHash */
	public static final String QUERY_LIST_BY_PASSWORDHASH = "SELECT e FROM DTOLehrerNotenmodulCredentials e WHERE e.PasswordHash IN ?1";

	/** Die LehrerID des Lehrers, für den die Credentials gelten */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Initialkennwort für den Credential-Datensatz */
	@Column(name = "Initialkennwort")
	@JsonProperty
	public String Initialkennwort;

	/** Passwordhash für den Credential-Datensatz */
	@Column(name = "PasswordHash")
	@JsonProperty
	public String PasswordHash;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerNotenmodulCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerNotenmodulCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerNotenmodulCredentials ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Initialkennwort   der Wert für das Attribut Initialkennwort
	 * @param PasswordHash   der Wert für das Attribut PasswordHash
	 */
	public DTOLehrerNotenmodulCredentials(final long Lehrer_ID, final String Initialkennwort, final String PasswordHash) {
		this.Lehrer_ID = Lehrer_ID;
		if (Initialkennwort == null) {
			throw new NullPointerException("Initialkennwort must not be null");
		}
		this.Initialkennwort = Initialkennwort;
		if (PasswordHash == null) {
			throw new NullPointerException("PasswordHash must not be null");
		}
		this.PasswordHash = PasswordHash;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerNotenmodulCredentials other = (DTOLehrerNotenmodulCredentials) obj;
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
		return "DTOLehrerNotenmodulCredentials(Lehrer_ID=" + this.Lehrer_ID + ", Initialkennwort=" + this.Initialkennwort + ", PasswordHash=" + this.PasswordHash + ")";
	}

}
