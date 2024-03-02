package de.svws_nrw.db.dto.current.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Email.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Email")
@NamedQuery(name = "DTOSchuleEmail.all", query = "SELECT e FROM DTOSchuleEmail e")
@NamedQuery(name = "DTOSchuleEmail.id", query = "SELECT e FROM DTOSchuleEmail e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuleEmail.id.multiple", query = "SELECT e FROM DTOSchuleEmail e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuleEmail.domain", query = "SELECT e FROM DTOSchuleEmail e WHERE e.Domain = :value")
@NamedQuery(name = "DTOSchuleEmail.domain.multiple", query = "SELECT e FROM DTOSchuleEmail e WHERE e.Domain IN :value")
@NamedQuery(name = "DTOSchuleEmail.smtpserver", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPServer = :value")
@NamedQuery(name = "DTOSchuleEmail.smtpserver.multiple", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPServer IN :value")
@NamedQuery(name = "DTOSchuleEmail.smtpport", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPPort = :value")
@NamedQuery(name = "DTOSchuleEmail.smtpport.multiple", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPPort IN :value")
@NamedQuery(name = "DTOSchuleEmail.smtpstarttls", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPStartTLS = :value")
@NamedQuery(name = "DTOSchuleEmail.smtpstarttls.multiple", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPStartTLS IN :value")
@NamedQuery(name = "DTOSchuleEmail.smtpusetls", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPUseTLS = :value")
@NamedQuery(name = "DTOSchuleEmail.smtpusetls.multiple", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPUseTLS IN :value")
@NamedQuery(name = "DTOSchuleEmail.smtptrusttlshost", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPTrustTLSHost = :value")
@NamedQuery(name = "DTOSchuleEmail.smtptrusttlshost.multiple", query = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPTrustTLSHost IN :value")
@NamedQuery(name = "DTOSchuleEmail.primaryKeyQuery", query = "SELECT e FROM DTOSchuleEmail e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuleEmail.primaryKeyQuery.multiple", query = "SELECT e FROM DTOSchuleEmail e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuleEmail.all.migration", query = "SELECT e FROM DTOSchuleEmail e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Domain", "SMTPServer", "SMTPPort", "SMTPStartTLS", "SMTPUseTLS", "SMTPTrustTLSHost"})
public final class DTOSchuleEmail {

	/** Die ID des Eintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die Default Email-Domain der Schule */
	@Column(name = "Domain")
	@JsonProperty
	public String Domain;

	/** Die Adresse des SMTP-Servers  */
	@Column(name = "SMTPServer")
	@JsonProperty
	public String SMTPServer;

	/** Die Port-Adresse des SMTP-Servers */
	@Column(name = "SMTPPort")
	@JsonProperty
	public int SMTPPort;

	/** Gibt an, ob StartTLS für die SMTP-Verbindung genutzt wird (1) oder nicht (0) */
	@Column(name = "SMTPStartTLS")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean SMTPStartTLS;

	/** Gibt an, ob TLS für die SMTP-Verbindung genutzt wird (1) oder nicht (0). Ist TLS gesetzt, so wird entweder ein Zertifikat im Key-Store des Servers benötigt oder es muss einem Host vertraut werden (siehe Spalte SMTPTrustTLSHost) */
	@Column(name = "SMTPUseTLS")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean SMTPUseTLS;

	/** Gibt an, falls gesetzt, welchem Host - unabhängig von Zertifikaten - vertraut werden kann, '*' für jeden beliebigen Host */
	@Column(name = "SMTPTrustTLSHost")
	@JsonProperty
	public String SMTPTrustTLSHost;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuleEmail ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuleEmail() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuleEmail ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SMTPServer   der Wert für das Attribut SMTPServer
	 * @param SMTPPort   der Wert für das Attribut SMTPPort
	 * @param SMTPStartTLS   der Wert für das Attribut SMTPStartTLS
	 * @param SMTPUseTLS   der Wert für das Attribut SMTPUseTLS
	 */
	public DTOSchuleEmail(final long ID, final String SMTPServer, final int SMTPPort, final Boolean SMTPStartTLS, final Boolean SMTPUseTLS) {
		this.ID = ID;
		if (SMTPServer == null) {
			throw new NullPointerException("SMTPServer must not be null");
		}
		this.SMTPServer = SMTPServer;
		this.SMTPPort = SMTPPort;
		this.SMTPStartTLS = SMTPStartTLS;
		this.SMTPUseTLS = SMTPUseTLS;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuleEmail other = (DTOSchuleEmail) obj;
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
		return "DTOSchuleEmail(ID=" + this.ID + ", Domain=" + this.Domain + ", SMTPServer=" + this.SMTPServer + ", SMTPPort=" + this.SMTPPort + ", SMTPStartTLS=" + this.SMTPStartTLS + ", SMTPUseTLS=" + this.SMTPUseTLS + ", SMTPTrustTLSHost=" + this.SMTPTrustTLSHost + ")";
	}

}
