package de.svws_nrw.db.dto.migration.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = "MigrationDTOUsers.all", query = "SELECT e FROM MigrationDTOUsers e")
@NamedQuery(name = "MigrationDTOUsers.id", query = "SELECT e FROM MigrationDTOUsers e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOUsers.id.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOUsers.us_name", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Name = :value")
@NamedQuery(name = "MigrationDTOUsers.us_name.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Name IN :value")
@NamedQuery(name = "MigrationDTOUsers.us_loginname", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_LoginName = :value")
@NamedQuery(name = "MigrationDTOUsers.us_loginname.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_LoginName IN :value")
@NamedQuery(name = "MigrationDTOUsers.us_password", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Password = :value")
@NamedQuery(name = "MigrationDTOUsers.us_password.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Password IN :value")
@NamedQuery(name = "MigrationDTOUsers.us_usergroups", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_UserGroups = :value")
@NamedQuery(name = "MigrationDTOUsers.us_usergroups.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_UserGroups IN :value")
@NamedQuery(name = "MigrationDTOUsers.us_privileges", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Privileges = :value")
@NamedQuery(name = "MigrationDTOUsers.us_privileges.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_Privileges IN :value")
@NamedQuery(name = "MigrationDTOUsers.schulnreigner", query = "SELECT e FROM MigrationDTOUsers e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOUsers.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOUsers.email", query = "SELECT e FROM MigrationDTOUsers e WHERE e.Email = :value")
@NamedQuery(name = "MigrationDTOUsers.email.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.Email IN :value")
@NamedQuery(name = "MigrationDTOUsers.emailname", query = "SELECT e FROM MigrationDTOUsers e WHERE e.EmailName = :value")
@NamedQuery(name = "MigrationDTOUsers.emailname.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.EmailName IN :value")
@NamedQuery(name = "MigrationDTOUsers.smtpusername", query = "SELECT e FROM MigrationDTOUsers e WHERE e.SMTPUsername = :value")
@NamedQuery(name = "MigrationDTOUsers.smtpusername.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.SMTPUsername IN :value")
@NamedQuery(name = "MigrationDTOUsers.smtppassword", query = "SELECT e FROM MigrationDTOUsers e WHERE e.SMTPPassword = :value")
@NamedQuery(name = "MigrationDTOUsers.smtppassword.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.SMTPPassword IN :value")
@NamedQuery(name = "MigrationDTOUsers.emailsignature", query = "SELECT e FROM MigrationDTOUsers e WHERE e.EmailSignature = :value")
@NamedQuery(name = "MigrationDTOUsers.emailsignature.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.EmailSignature IN :value")
@NamedQuery(name = "MigrationDTOUsers.heartbeatdate", query = "SELECT e FROM MigrationDTOUsers e WHERE e.HeartbeatDate = :value")
@NamedQuery(name = "MigrationDTOUsers.heartbeatdate.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.HeartbeatDate IN :value")
@NamedQuery(name = "MigrationDTOUsers.computername", query = "SELECT e FROM MigrationDTOUsers e WHERE e.ComputerName = :value")
@NamedQuery(name = "MigrationDTOUsers.computername.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.ComputerName IN :value")
@NamedQuery(name = "MigrationDTOUsers.us_passwordhash", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_PasswordHash = :value")
@NamedQuery(name = "MigrationDTOUsers.us_passwordhash.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.US_PasswordHash IN :value")
@NamedQuery(name = "MigrationDTOUsers.primaryKeyQuery", query = "SELECT e FROM MigrationDTOUsers e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOUsers.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOUsers e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOUsers.all.migration", query = "SELECT e FROM MigrationDTOUsers e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "US_Name", "US_LoginName", "US_Password", "US_UserGroups", "US_Privileges", "SchulnrEigner", "Email", "EmailName", "SMTPUsername", "SMTPPassword", "EmailSignature", "HeartbeatDate", "ComputerName", "US_PasswordHash"})
public final class MigrationDTOUsers {

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
