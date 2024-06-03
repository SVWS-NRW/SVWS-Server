package de.svws_nrw.db.dto.migration.svws.auth;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Credentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Credentials")
@JsonPropertyOrder({"ID", "Benutzername", "BenutzernamePseudonym", "Initialkennwort", "PasswordHash", "RSAPublicKey", "RSAPrivateKey", "AES"})
public final class MigrationDTOCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOCredentials e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOCredentials e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOCredentials e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOCredentials e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOCredentials e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzername */
	public static final String QUERY_BY_BENUTZERNAME = "SELECT e FROM MigrationDTOCredentials e WHERE e.Benutzername = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzername */
	public static final String QUERY_LIST_BY_BENUTZERNAME = "SELECT e FROM MigrationDTOCredentials e WHERE e.Benutzername IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BenutzernamePseudonym */
	public static final String QUERY_BY_BENUTZERNAMEPSEUDONYM = "SELECT e FROM MigrationDTOCredentials e WHERE e.BenutzernamePseudonym = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BenutzernamePseudonym */
	public static final String QUERY_LIST_BY_BENUTZERNAMEPSEUDONYM = "SELECT e FROM MigrationDTOCredentials e WHERE e.BenutzernamePseudonym IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Initialkennwort */
	public static final String QUERY_BY_INITIALKENNWORT = "SELECT e FROM MigrationDTOCredentials e WHERE e.Initialkennwort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Initialkennwort */
	public static final String QUERY_LIST_BY_INITIALKENNWORT = "SELECT e FROM MigrationDTOCredentials e WHERE e.Initialkennwort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PasswordHash */
	public static final String QUERY_BY_PASSWORDHASH = "SELECT e FROM MigrationDTOCredentials e WHERE e.PasswordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PasswordHash */
	public static final String QUERY_LIST_BY_PASSWORDHASH = "SELECT e FROM MigrationDTOCredentials e WHERE e.PasswordHash IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSAPublicKey */
	public static final String QUERY_BY_RSAPUBLICKEY = "SELECT e FROM MigrationDTOCredentials e WHERE e.RSAPublicKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSAPublicKey */
	public static final String QUERY_LIST_BY_RSAPUBLICKEY = "SELECT e FROM MigrationDTOCredentials e WHERE e.RSAPublicKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSAPrivateKey */
	public static final String QUERY_BY_RSAPRIVATEKEY = "SELECT e FROM MigrationDTOCredentials e WHERE e.RSAPrivateKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSAPrivateKey */
	public static final String QUERY_LIST_BY_RSAPRIVATEKEY = "SELECT e FROM MigrationDTOCredentials e WHERE e.RSAPrivateKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AES */
	public static final String QUERY_BY_AES = "SELECT e FROM MigrationDTOCredentials e WHERE e.AES = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AES */
	public static final String QUERY_LIST_BY_AES = "SELECT e FROM MigrationDTOCredentials e WHERE e.AES IN ?1";

	/** ID des Datensatzes für die SVWS internen Account-Credentials */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Benutzername für den Credential-Datensatz */
	@Column(name = "Benutzername")
	@JsonProperty
	public String Benutzername;

	/** Der pseudonymisierte Benutzername für den Credential-Datensatz */
	@Column(name = "BenutzernamePseudonym")
	@JsonProperty
	public String BenutzernamePseudonym;

	/** Initialkennwort für den Credential-Datensatz */
	@Column(name = "Initialkennwort")
	@JsonProperty
	public String Initialkennwort;

	/** Passwordhash für den Credential-Datensatz */
	@Column(name = "PasswordHash")
	@JsonProperty
	public String PasswordHash;

	/** RSAPublicKey für den Credential-Datensatz */
	@Column(name = "RSAPublicKey")
	@JsonProperty
	public String RSAPublicKey;

	/** RSAPrivateKey für den Credential-Datensatz */
	@Column(name = "RSAPrivateKey")
	@JsonProperty
	public String RSAPrivateKey;

	/** AES-Schlüssel für den Credential-Datensatz */
	@Column(name = "AES")
	@JsonProperty
	public String AES;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOCredentials ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Benutzername   der Wert für das Attribut Benutzername
	 */
	public MigrationDTOCredentials(final Long ID, final String Benutzername) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Benutzername == null) {
			throw new NullPointerException("Benutzername must not be null");
		}
		this.Benutzername = Benutzername;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOCredentials other = (MigrationDTOCredentials) obj;
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
		return "MigrationDTOCredentials(ID=" + this.ID + ", Benutzername=" + this.Benutzername + ", BenutzernamePseudonym=" + this.BenutzernamePseudonym + ", Initialkennwort=" + this.Initialkennwort + ", PasswordHash=" + this.PasswordHash + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}
