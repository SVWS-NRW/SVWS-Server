package de.svws_nrw.db.dto.current.schild.benutzer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzerEmail.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzerEmail")
@NamedQuery(name = "DTOBenutzerMail.all", query = "SELECT e FROM DTOBenutzerMail e")
@NamedQuery(name = "DTOBenutzerMail.benutzer_id", query = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID = :value")
@NamedQuery(name = "DTOBenutzerMail.benutzer_id.multiple", query = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID IN :value")
@NamedQuery(name = "DTOBenutzerMail.email", query = "SELECT e FROM DTOBenutzerMail e WHERE e.Email = :value")
@NamedQuery(name = "DTOBenutzerMail.email.multiple", query = "SELECT e FROM DTOBenutzerMail e WHERE e.Email IN :value")
@NamedQuery(name = "DTOBenutzerMail.emailname", query = "SELECT e FROM DTOBenutzerMail e WHERE e.EmailName = :value")
@NamedQuery(name = "DTOBenutzerMail.emailname.multiple", query = "SELECT e FROM DTOBenutzerMail e WHERE e.EmailName IN :value")
@NamedQuery(name = "DTOBenutzerMail.smtpusername", query = "SELECT e FROM DTOBenutzerMail e WHERE e.SMTPUsername = :value")
@NamedQuery(name = "DTOBenutzerMail.smtpusername.multiple", query = "SELECT e FROM DTOBenutzerMail e WHERE e.SMTPUsername IN :value")
@NamedQuery(name = "DTOBenutzerMail.smtppassword", query = "SELECT e FROM DTOBenutzerMail e WHERE e.SMTPPassword = :value")
@NamedQuery(name = "DTOBenutzerMail.smtppassword.multiple", query = "SELECT e FROM DTOBenutzerMail e WHERE e.SMTPPassword IN :value")
@NamedQuery(name = "DTOBenutzerMail.emailsignature", query = "SELECT e FROM DTOBenutzerMail e WHERE e.EMailSignature = :value")
@NamedQuery(name = "DTOBenutzerMail.emailsignature.multiple", query = "SELECT e FROM DTOBenutzerMail e WHERE e.EMailSignature IN :value")
@NamedQuery(name = "DTOBenutzerMail.heartbeatdate", query = "SELECT e FROM DTOBenutzerMail e WHERE e.HeartbeatDate = :value")
@NamedQuery(name = "DTOBenutzerMail.heartbeatdate.multiple", query = "SELECT e FROM DTOBenutzerMail e WHERE e.HeartbeatDate IN :value")
@NamedQuery(name = "DTOBenutzerMail.computername", query = "SELECT e FROM DTOBenutzerMail e WHERE e.ComputerName = :value")
@NamedQuery(name = "DTOBenutzerMail.computername.multiple", query = "SELECT e FROM DTOBenutzerMail e WHERE e.ComputerName IN :value")
@NamedQuery(name = "DTOBenutzerMail.primaryKeyQuery", query = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID = ?1")
@NamedQuery(name = "DTOBenutzerMail.all.migration", query = "SELECT e FROM DTOBenutzerMail e WHERE e.Benutzer_ID IS NOT NULL")
@JsonPropertyOrder({"Benutzer_ID", "Email", "EmailName", "SMTPUsername", "SMTPPassword", "EMailSignature", "HeartbeatDate", "ComputerName"})
public final class DTOBenutzerMail {

	/** Die ID des Benutzers, zu dem der Datensatz gehört  */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

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
	public DTOBenutzerMail(final Long Benutzer_ID, final String Email, final String EmailName) {
		if (Benutzer_ID == null) {
			throw new NullPointerException("Benutzer_ID must not be null");
		}
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
		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());
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
