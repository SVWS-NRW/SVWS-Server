package de.svws_nrw.db.dto.current.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzerEmail.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzerEmail")
@JsonPropertyOrder({"Benutzer_ID", "Email", "EmailName", "SMTPUsername", "SMTPPassword", "EMailSignature", "HeartbeatDate", "ComputerName"})
public final class DTOBenutzerMail {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOBenutzerMail e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzer_ID */
	public static final String QUERY_BY_BENUTZER_ID = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzer_ID */
	public static final String QUERY_LIST_BY_BENUTZER_ID = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM DTOBenutzerMail e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM DTOBenutzerMail e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EmailName */
	public static final String QUERY_BY_EMAILNAME = "SELECT e FROM DTOBenutzerMail e WHERE e.EmailName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EmailName */
	public static final String QUERY_LIST_BY_EMAILNAME = "SELECT e FROM DTOBenutzerMail e WHERE e.EmailName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPUsername */
	public static final String QUERY_BY_SMTPUSERNAME = "SELECT e FROM DTOBenutzerMail e WHERE e.SMTPUsername = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPUsername */
	public static final String QUERY_LIST_BY_SMTPUSERNAME = "SELECT e FROM DTOBenutzerMail e WHERE e.SMTPUsername IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPPassword */
	public static final String QUERY_BY_SMTPPASSWORD = "SELECT e FROM DTOBenutzerMail e WHERE e.SMTPPassword = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPPassword */
	public static final String QUERY_LIST_BY_SMTPPASSWORD = "SELECT e FROM DTOBenutzerMail e WHERE e.SMTPPassword IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EMailSignature */
	public static final String QUERY_BY_EMAILSIGNATURE = "SELECT e FROM DTOBenutzerMail e WHERE e.EMailSignature = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EMailSignature */
	public static final String QUERY_LIST_BY_EMAILSIGNATURE = "SELECT e FROM DTOBenutzerMail e WHERE e.EMailSignature IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HeartbeatDate */
	public static final String QUERY_BY_HEARTBEATDATE = "SELECT e FROM DTOBenutzerMail e WHERE e.HeartbeatDate = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HeartbeatDate */
	public static final String QUERY_LIST_BY_HEARTBEATDATE = "SELECT e FROM DTOBenutzerMail e WHERE e.HeartbeatDate IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ComputerName */
	public static final String QUERY_BY_COMPUTERNAME = "SELECT e FROM DTOBenutzerMail e WHERE e.ComputerName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ComputerName */
	public static final String QUERY_LIST_BY_COMPUTERNAME = "SELECT e FROM DTOBenutzerMail e WHERE e.ComputerName IN ?1";

	/** Die ID des Benutzers, zu dem der Datensatz gehört  */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public long Benutzer_ID;

	/** Die EMail-Adresse des Benutzers, zu dem der Datensatz gehört  */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** ???  */
	@Column(name = "EmailName")
	@JsonProperty
	public String EmailName;

	/** ???  */
	@Column(name = "SMTPUsername")
	@JsonProperty
	public String SMTPUsername;

	/** ???  */
	@Column(name = "SMTPPassword")
	@JsonProperty
	public String SMTPPassword;

	/** ???  */
	@Column(name = "EMailSignature")
	@JsonProperty
	public String EMailSignature;

	/** ???  */
	@Column(name = "HeartbeatDate")
	@JsonProperty
	public Long HeartbeatDate;

	/** ???  */
	@Column(name = "ComputerName")
	@JsonProperty
	public String ComputerName;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerMail ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzerMail() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerMail ohne eine Initialisierung der Attribute.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param Email   der Wert für das Attribut Email
	 * @param EmailName   der Wert für das Attribut EmailName
	 */
	public DTOBenutzerMail(final long Benutzer_ID, final String Email, final String EmailName) {
		this.Benutzer_ID = Benutzer_ID;
		if (Email == null) {
			throw new NullPointerException("Email must not be null");
		}
		this.Email = Email;
		if (EmailName == null) {
			throw new NullPointerException("EmailName must not be null");
		}
		this.EmailName = EmailName;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBenutzerMail other = (DTOBenutzerMail) obj;
		return Benutzer_ID == other.Benutzer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Benutzer_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOBenutzerMail(Benutzer_ID=" + this.Benutzer_ID + ", Email=" + this.Email + ", EmailName=" + this.EmailName + ", SMTPUsername=" + this.SMTPUsername + ", SMTPPassword=" + this.SMTPPassword + ", EMailSignature=" + this.EMailSignature + ", HeartbeatDate=" + this.HeartbeatDate + ", ComputerName=" + this.ComputerName + ")";
	}

}
