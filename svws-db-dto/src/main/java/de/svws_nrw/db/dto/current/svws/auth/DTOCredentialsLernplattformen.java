package de.svws_nrw.db.dto.current.svws.auth;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle CredentialsLernplattformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "CredentialsLernplattformen")
@JsonPropertyOrder({"ID", "LernplattformID", "Benutzername", "BenutzernamePseudonym", "Initialkennwort", "PashwordHash", "RSAPublicKey", "RSAPrivateKey", "AES"})
public final class DTOCredentialsLernplattformen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOCredentialsLernplattformen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LernplattformID */
	public static final String QUERY_BY_LERNPLATTFORMID = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.LernplattformID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LernplattformID */
	public static final String QUERY_LIST_BY_LERNPLATTFORMID = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.LernplattformID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzername */
	public static final String QUERY_BY_BENUTZERNAME = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.Benutzername = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzername */
	public static final String QUERY_LIST_BY_BENUTZERNAME = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.Benutzername IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BenutzernamePseudonym */
	public static final String QUERY_BY_BENUTZERNAMEPSEUDONYM = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.BenutzernamePseudonym = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BenutzernamePseudonym */
	public static final String QUERY_LIST_BY_BENUTZERNAMEPSEUDONYM = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.BenutzernamePseudonym IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Initialkennwort */
	public static final String QUERY_BY_INITIALKENNWORT = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.Initialkennwort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Initialkennwort */
	public static final String QUERY_LIST_BY_INITIALKENNWORT = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.Initialkennwort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PashwordHash */
	public static final String QUERY_BY_PASHWORDHASH = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.PashwordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PashwordHash */
	public static final String QUERY_LIST_BY_PASHWORDHASH = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.PashwordHash IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSAPublicKey */
	public static final String QUERY_BY_RSAPUBLICKEY = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.RSAPublicKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSAPublicKey */
	public static final String QUERY_LIST_BY_RSAPUBLICKEY = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.RSAPublicKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSAPrivateKey */
	public static final String QUERY_BY_RSAPRIVATEKEY = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.RSAPrivateKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSAPrivateKey */
	public static final String QUERY_LIST_BY_RSAPRIVATEKEY = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.RSAPrivateKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AES */
	public static final String QUERY_BY_AES = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.AES = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AES */
	public static final String QUERY_LIST_BY_AES = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.AES IN ?1";

	/** ID des Datensatzes für die externen Account-Credentials (Lernplattformen) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Lernplattform */
	@Column(name = "LernplattformID")
	@JsonProperty
	public long LernplattformID;

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
	@Column(name = "PashwordHash")
	@JsonProperty
	public String PashwordHash;

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
	 * Erstellt ein neues Objekt der Klasse DTOCredentialsLernplattformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOCredentialsLernplattformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOCredentialsLernplattformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param Benutzername   der Wert für das Attribut Benutzername
	 */
	public DTOCredentialsLernplattformen(final long ID, final long LernplattformID, final String Benutzername) {
		this.ID = ID;
		this.LernplattformID = LernplattformID;
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
		DTOCredentialsLernplattformen other = (DTOCredentialsLernplattformen) obj;
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
		return "DTOCredentialsLernplattformen(ID=" + this.ID + ", LernplattformID=" + this.LernplattformID + ", Benutzername=" + this.Benutzername + ", BenutzernamePseudonym=" + this.BenutzernamePseudonym + ", Initialkennwort=" + this.Initialkennwort + ", PashwordHash=" + this.PashwordHash + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}
