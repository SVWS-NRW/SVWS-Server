package de.svws_nrw.db.dto.migration.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Users.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Users")
@JsonPropertyOrder({"ID", "US_Name", "US_LoginName", "US_Password", "US_UserGroups", "US_Privileges", "SchulnrEigner", "Email", "EmailName", "SMTPUsername", "SMTPPassword", "EmailSignature", "HeartbeatDate", "ComputerName", "US_PasswordHash"})
public final class MigrationDTOUsers {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOUsers e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOUsers e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOUsers e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOUsers e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOUsers e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOUsers e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes US_Name */
	public static final String QUERY_BY_US_NAME = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Name = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes US_Name */
	public static final String QUERY_LIST_BY_US_NAME = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Name IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes US_LoginName */
	public static final String QUERY_BY_US_LOGINNAME = "SELECT e FROM MigrationDTOUsers e WHERE e.US_LoginName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes US_LoginName */
	public static final String QUERY_LIST_BY_US_LOGINNAME = "SELECT e FROM MigrationDTOUsers e WHERE e.US_LoginName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes US_Password */
	public static final String QUERY_BY_US_PASSWORD = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Password = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes US_Password */
	public static final String QUERY_LIST_BY_US_PASSWORD = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Password IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes US_UserGroups */
	public static final String QUERY_BY_US_USERGROUPS = "SELECT e FROM MigrationDTOUsers e WHERE e.US_UserGroups = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes US_UserGroups */
	public static final String QUERY_LIST_BY_US_USERGROUPS = "SELECT e FROM MigrationDTOUsers e WHERE e.US_UserGroups IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes US_Privileges */
	public static final String QUERY_BY_US_PRIVILEGES = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Privileges = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes US_Privileges */
	public static final String QUERY_LIST_BY_US_PRIVILEGES = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Privileges IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOUsers e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOUsers e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM MigrationDTOUsers e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM MigrationDTOUsers e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EmailName */
	public static final String QUERY_BY_EMAILNAME = "SELECT e FROM MigrationDTOUsers e WHERE e.EmailName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EmailName */
	public static final String QUERY_LIST_BY_EMAILNAME = "SELECT e FROM MigrationDTOUsers e WHERE e.EmailName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPUsername */
	public static final String QUERY_BY_SMTPUSERNAME = "SELECT e FROM MigrationDTOUsers e WHERE e.SMTPUsername = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPUsername */
	public static final String QUERY_LIST_BY_SMTPUSERNAME = "SELECT e FROM MigrationDTOUsers e WHERE e.SMTPUsername IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPPassword */
	public static final String QUERY_BY_SMTPPASSWORD = "SELECT e FROM MigrationDTOUsers e WHERE e.SMTPPassword = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPPassword */
	public static final String QUERY_LIST_BY_SMTPPASSWORD = "SELECT e FROM MigrationDTOUsers e WHERE e.SMTPPassword IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EmailSignature */
	public static final String QUERY_BY_EMAILSIGNATURE = "SELECT e FROM MigrationDTOUsers e WHERE e.EmailSignature = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EmailSignature */
	public static final String QUERY_LIST_BY_EMAILSIGNATURE = "SELECT e FROM MigrationDTOUsers e WHERE e.EmailSignature IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HeartbeatDate */
	public static final String QUERY_BY_HEARTBEATDATE = "SELECT e FROM MigrationDTOUsers e WHERE e.HeartbeatDate = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HeartbeatDate */
	public static final String QUERY_LIST_BY_HEARTBEATDATE = "SELECT e FROM MigrationDTOUsers e WHERE e.HeartbeatDate IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ComputerName */
	public static final String QUERY_BY_COMPUTERNAME = "SELECT e FROM MigrationDTOUsers e WHERE e.ComputerName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ComputerName */
	public static final String QUERY_LIST_BY_COMPUTERNAME = "SELECT e FROM MigrationDTOUsers e WHERE e.ComputerName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes US_PasswordHash */
	public static final String QUERY_BY_US_PASSWORDHASH = "SELECT e FROM MigrationDTOUsers e WHERE e.US_PasswordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes US_PasswordHash */
	public static final String QUERY_LIST_BY_US_PASSWORDHASH = "SELECT e FROM MigrationDTOUsers e WHERE e.US_PasswordHash IN ?1";

	/** ID des Datenbankbenutzers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Name des Datenbankbenutzers  */
	@Column(name = "US_Name")
	@JsonProperty
	public String US_Name;

	/** LoginName des Datenbankbenutzers */
	@Column(name = "US_LoginName")
	@JsonProperty
	public String US_LoginName;

	/** DEPRECATED: nicht mehr genutzt altes Passwort des Datenbankbenutzers */
	@Column(name = "US_Password")
	@JsonProperty
	public String US_Password;

	/** Zugehörigkeit zu Usergruops des Datenbankbenutzers */
	@Column(name = "US_UserGroups")
	@JsonProperty
	public String US_UserGroups;

	/** Berechtigungen des Datenbankbenutzers */
	@Column(name = "US_Privileges")
	@JsonProperty
	public String US_Privileges;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** E-Mail-Adresse des Datenbankbenutzers für des Versand aus Schild-NRW */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Name des Datenbankbenutzers für den Mailversand aus Schild-NRW */
	@Column(name = "EmailName")
	@JsonProperty
	public String EmailName;

	/** SMTP Username des Datenbankbenutzers */
	@Column(name = "SMTPUsername")
	@JsonProperty
	public String SMTPUsername;

	/** SMTP Passwort des Datenbankbenutzers */
	@Column(name = "SMTPPassword")
	@JsonProperty
	public String SMTPPassword;

	/** E-Mail-Signatur des Datenbankbenutzers */
	@Column(name = "EmailSignature")
	@JsonProperty
	public String EmailSignature;

	/** Datum des Heartbeats bei einigen Datenbank verwende um Sleepmodus zu verhindern */
	@Column(name = "HeartbeatDate")
	@JsonProperty
	public Integer HeartbeatDate;

	/** Name des Computers an dem der Benutzer zuletzt angemeldet war */
	@Column(name = "ComputerName")
	@JsonProperty
	public String ComputerName;

	/** PasswortHash des Users mit BeCrypt generiert */
	@Column(name = "US_PasswordHash")
	@JsonProperty
	public String US_PasswordHash;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOUsers ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOUsers() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOUsers ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param US_Name   der Wert für das Attribut US_Name
	 * @param US_LoginName   der Wert für das Attribut US_LoginName
	 */
	public MigrationDTOUsers(final Long ID, final String US_Name, final String US_LoginName) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (US_Name == null) {
			throw new NullPointerException("US_Name must not be null");
		}
		this.US_Name = US_Name;
		if (US_LoginName == null) {
			throw new NullPointerException("US_LoginName must not be null");
		}
		this.US_LoginName = US_LoginName;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOUsers other = (MigrationDTOUsers) obj;
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
		return "MigrationDTOUsers(ID=" + this.ID + ", US_Name=" + this.US_Name + ", US_LoginName=" + this.US_LoginName + ", US_Password=" + this.US_Password + ", US_UserGroups=" + this.US_UserGroups + ", US_Privileges=" + this.US_Privileges + ", SchulnrEigner=" + this.SchulnrEigner + ", Email=" + this.Email + ", EmailName=" + this.EmailName + ", SMTPUsername=" + this.SMTPUsername + ", SMTPPassword=" + this.SMTPPassword + ", EmailSignature=" + this.EmailSignature + ", HeartbeatDate=" + this.HeartbeatDate + ", ComputerName=" + this.ComputerName + ", US_PasswordHash=" + this.US_PasswordHash + ")";
	}

}
