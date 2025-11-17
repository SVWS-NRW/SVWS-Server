package de.svws_nrw.db.dto.current.notenmodul;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Notenmodul_Verbindungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Notenmodul_Verbindungen")
@JsonPropertyOrder({"id", "bezeichnung", "url", "clientID", "clientSecret", "tokenTimestamp", "tokenExpiresIn", "token", "serverTLSCert", "serverTLSCertIsKnown", "serverTLSCertIsTrusted"})
public final class DTONotenmodulVerbindungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTONotenmodulVerbindungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes url */
	public static final String QUERY_BY_URL = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.url = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes url */
	public static final String QUERY_LIST_BY_URL = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.url IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes clientID */
	public static final String QUERY_BY_CLIENTID = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.clientID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes clientID */
	public static final String QUERY_LIST_BY_CLIENTID = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.clientID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes clientSecret */
	public static final String QUERY_BY_CLIENTSECRET = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.clientSecret = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes clientSecret */
	public static final String QUERY_LIST_BY_CLIENTSECRET = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.clientSecret IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tokenTimestamp */
	public static final String QUERY_BY_TOKENTIMESTAMP = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.tokenTimestamp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tokenTimestamp */
	public static final String QUERY_LIST_BY_TOKENTIMESTAMP = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.tokenTimestamp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tokenExpiresIn */
	public static final String QUERY_BY_TOKENEXPIRESIN = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.tokenExpiresIn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tokenExpiresIn */
	public static final String QUERY_LIST_BY_TOKENEXPIRESIN = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.tokenExpiresIn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes token */
	public static final String QUERY_BY_TOKEN = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.token = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes token */
	public static final String QUERY_LIST_BY_TOKEN = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.token IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes serverTLSCert */
	public static final String QUERY_BY_SERVERTLSCERT = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.serverTLSCert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes serverTLSCert */
	public static final String QUERY_LIST_BY_SERVERTLSCERT = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.serverTLSCert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes serverTLSCertIsKnown */
	public static final String QUERY_BY_SERVERTLSCERTISKNOWN = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.serverTLSCertIsKnown = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes serverTLSCertIsKnown */
	public static final String QUERY_LIST_BY_SERVERTLSCERTISKNOWN = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.serverTLSCertIsKnown IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes serverTLSCertIsTrusted */
	public static final String QUERY_BY_SERVERTLSCERTISTRUSTED = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.serverTLSCertIsTrusted = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes serverTLSCertIsTrusted */
	public static final String QUERY_LIST_BY_SERVERTLSCERTISTRUSTED = "SELECT e FROM DTONotenmodulVerbindungen e WHERE e.serverTLSCertIsTrusted IN ?1";

	/** Die ID der Verbindung */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Die Bezeichnung der Verbindung für die Darstellung in der Verbindungsliste (null, wenn einfach die URL dargestellt werden soll) */
	@Column(name = "bezeichnung")
	@JsonProperty
	public String bezeichnung;

	/** Die URL des Servers des externen Notenmoduls */
	@Column(name = "url")
	@JsonProperty
	public String url;

	/** Die Client-ID, welche zusammen mit dem Client-Secret, zur für die OAuth2-Verbindung zu dem externen Server genutzt wird. */
	@Column(name = "clientID")
	@JsonProperty
	public String clientID;

	/** Das Client-Secret, welches zusammen mit der Client-ID, zur für die OAuth2-Verbindung zu dem externen Server genutzt wird. */
	@Column(name = "clientSecret")
	@JsonProperty
	public String clientSecret;

	/** Verbindungs-Token: Ankunftzeitpunkt des Tokens als Zeitstempel in Millisekungen */
	@Column(name = "tokenTimestamp")
	@JsonProperty
	public Long tokenTimestamp;

	/** Verbindungs-Token: Lebensdauer des Tokens in Sekunden */
	@Column(name = "tokenExpiresIn")
	@JsonProperty
	public Long tokenExpiresIn;

	/** Verbindungs-Token: Das Token */
	@Column(name = "token")
	@JsonProperty
	public String token;

	/** Externener Server: Das TLS Zertifikat des OAuth2-Servers */
	@Column(name = "serverTLSCert")
	@JsonProperty
	public String serverTLSCert;

	/** Externener Server: Gibt an, ob das Zertifikat über den Keystore validiert werden kann. */
	@Column(name = "serverTLSCertIsKnown")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean serverTLSCertIsKnown;

	/** Externener Server: Gibt an, ob dem TLS-Zertifikat vertraut werden darf, entweder weil es bekannt ist oder weil der Benutzer zugestimmt hat. */
	@Column(name = "serverTLSCertIsTrusted")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean serverTLSCertIsTrusted;

	/**
	 * Erstellt ein neues Objekt der Klasse DTONotenmodulVerbindungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTONotenmodulVerbindungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTONotenmodulVerbindungen ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param url   der Wert für das Attribut url
	 * @param clientID   der Wert für das Attribut clientID
	 * @param clientSecret   der Wert für das Attribut clientSecret
	 */
	public DTONotenmodulVerbindungen(final long id, final String url, final String clientID, final String clientSecret) {
		this.id = id;
		if (url == null) {
			throw new NullPointerException("url must not be null");
		}
		this.url = url;
		if (clientID == null) {
			throw new NullPointerException("clientID must not be null");
		}
		this.clientID = clientID;
		if (clientSecret == null) {
			throw new NullPointerException("clientSecret must not be null");
		}
		this.clientSecret = clientSecret;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTONotenmodulVerbindungen other = (DTONotenmodulVerbindungen) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTONotenmodulVerbindungen(id=" + this.id + ", bezeichnung=" + this.bezeichnung + ", url=" + this.url + ", clientID=" + this.clientID + ", clientSecret=" + this.clientSecret + ", tokenTimestamp=" + this.tokenTimestamp + ", tokenExpiresIn=" + this.tokenExpiresIn + ", token=" + this.token + ", serverTLSCert=" + this.serverTLSCert + ", serverTLSCertIsKnown=" + this.serverTLSCertIsKnown + ", serverTLSCertIsTrusted=" + this.serverTLSCertIsTrusted + ")";
	}

}
