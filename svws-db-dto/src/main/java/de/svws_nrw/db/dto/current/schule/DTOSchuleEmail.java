package de.svws_nrw.db.dto.current.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@JsonPropertyOrder({"ID", "Domain", "SMTPServer", "SMTPPort", "SMTPStartTLS", "SMTPUseTLS", "SMTPTrustTLSHost"})
public final class DTOSchuleEmail {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuleEmail e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuleEmail e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuleEmail e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuleEmail e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuleEmail e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuleEmail e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Domain */
	public static final String QUERY_BY_DOMAIN = "SELECT e FROM DTOSchuleEmail e WHERE e.Domain = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Domain */
	public static final String QUERY_LIST_BY_DOMAIN = "SELECT e FROM DTOSchuleEmail e WHERE e.Domain IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPServer */
	public static final String QUERY_BY_SMTPSERVER = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPServer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPServer */
	public static final String QUERY_LIST_BY_SMTPSERVER = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPServer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPPort */
	public static final String QUERY_BY_SMTPPORT = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPPort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPPort */
	public static final String QUERY_LIST_BY_SMTPPORT = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPPort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPStartTLS */
	public static final String QUERY_BY_SMTPSTARTTLS = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPStartTLS = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPStartTLS */
	public static final String QUERY_LIST_BY_SMTPSTARTTLS = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPStartTLS IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPUseTLS */
	public static final String QUERY_BY_SMTPUSETLS = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPUseTLS = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPUseTLS */
	public static final String QUERY_LIST_BY_SMTPUSETLS = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPUseTLS IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SMTPTrustTLSHost */
	public static final String QUERY_BY_SMTPTRUSTTLSHOST = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPTrustTLSHost = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SMTPTrustTLSHost */
	public static final String QUERY_LIST_BY_SMTPTRUSTTLSHOST = "SELECT e FROM DTOSchuleEmail e WHERE e.SMTPTrustTLSHost IN ?1";

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
