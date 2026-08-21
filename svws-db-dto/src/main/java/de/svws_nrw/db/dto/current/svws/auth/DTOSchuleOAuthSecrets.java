package de.svws_nrw.db.dto.current.svws.auth;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.OAuthServiceDomainConverter;

import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;


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
import de.svws_nrw.csv.converter.current.OAuthServiceDomainConverterSerializer;
import de.svws_nrw.csv.converter.current.OAuthServiceDomainConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuleOAuthSecrets.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuleOAuthSecrets")
@JsonPropertyOrder({"id", "authServerUrl", "clientId", "clientSecret", "tokenType", "tokenTimestamp", "tokenExpiresIn", "tokenScope", "token", "tlsCert", "tlsCertIsKnown", "tlsCertIsTrusted", "serviceDomain", "requestedScope"})
public final class DTOSchuleOAuthSecrets {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuleOAuthSecrets e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes authServerUrl */
	public static final String QUERY_BY_AUTHSERVERURL = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.authServerUrl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes authServerUrl */
	public static final String QUERY_LIST_BY_AUTHSERVERURL = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.authServerUrl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes clientId */
	public static final String QUERY_BY_CLIENTID = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.clientId = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes clientId */
	public static final String QUERY_LIST_BY_CLIENTID = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.clientId IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes clientSecret */
	public static final String QUERY_BY_CLIENTSECRET = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.clientSecret = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes clientSecret */
	public static final String QUERY_LIST_BY_CLIENTSECRET = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.clientSecret IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tokenType */
	public static final String QUERY_BY_TOKENTYPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tokenType = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tokenType */
	public static final String QUERY_LIST_BY_TOKENTYPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tokenType IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tokenTimestamp */
	public static final String QUERY_BY_TOKENTIMESTAMP = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tokenTimestamp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tokenTimestamp */
	public static final String QUERY_LIST_BY_TOKENTIMESTAMP = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tokenTimestamp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tokenExpiresIn */
	public static final String QUERY_BY_TOKENEXPIRESIN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tokenExpiresIn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tokenExpiresIn */
	public static final String QUERY_LIST_BY_TOKENEXPIRESIN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tokenExpiresIn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tokenScope */
	public static final String QUERY_BY_TOKENSCOPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tokenScope = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tokenScope */
	public static final String QUERY_LIST_BY_TOKENSCOPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tokenScope IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes token */
	public static final String QUERY_BY_TOKEN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.token = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes token */
	public static final String QUERY_LIST_BY_TOKEN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.token IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tlsCert */
	public static final String QUERY_BY_TLSCERT = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tlsCert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tlsCert */
	public static final String QUERY_LIST_BY_TLSCERT = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tlsCert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tlsCertIsKnown */
	public static final String QUERY_BY_TLSCERTISKNOWN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tlsCertIsKnown = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tlsCertIsKnown */
	public static final String QUERY_LIST_BY_TLSCERTISKNOWN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tlsCertIsKnown IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tlsCertIsTrusted */
	public static final String QUERY_BY_TLSCERTISTRUSTED = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tlsCertIsTrusted = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tlsCertIsTrusted */
	public static final String QUERY_LIST_BY_TLSCERTISTRUSTED = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.tlsCertIsTrusted IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes serviceDomain */
	public static final String QUERY_BY_SERVICEDOMAIN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.serviceDomain = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes serviceDomain */
	public static final String QUERY_LIST_BY_SERVICEDOMAIN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.serviceDomain IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes requestedScope */
	public static final String QUERY_BY_REQUESTEDSCOPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.requestedScope = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes requestedScope */
	public static final String QUERY_LIST_BY_REQUESTEDSCOPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.requestedScope IN ?1";

	/** ID des OAuth-Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** Der Authorization Server */
	@Column(name = "AuthServer")
	@JsonProperty
	public String authServerUrl;

	/** Die ID des Clients */
	@Column(name = "ClientID")
	@JsonProperty
	public String clientId;

	/** Das Secret des Clients */
	@Column(name = "ClientSecret")
	@JsonProperty
	public String clientSecret;

	/** Der Typ des Tokens */
	@Column(name = "TokenType")
	@JsonProperty
	public String tokenType;

	/** Ankunftzeitpunkt des Tokens als Zeitstempel in Millisekungen */
	@Column(name = "TokenTimestamp")
	@JsonProperty
	public Long tokenTimestamp;

	/** Lebensdauer des Tokens in Sekunden */
	@Column(name = "TokenExpiresIn")
	@JsonProperty
	public Long tokenExpiresIn;

	/** Der Gültigkeitsbereich des Tokens */
	@Column(name = "TokenScope")
	@JsonProperty
	public String tokenScope;

	/** Das Token */
	@Column(name = "Token")
	@JsonProperty
	public String token;

	/** Das TLS Zertifikat des OAuth2-Servers */
	@Column(name = "TLSCert")
	@JsonProperty
	public String tlsCert;

	/** Gibt an, ob das Zertifikat über den Keystore validiert werden kann. */
	@Column(name = "TLSCertIsKnown")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean tlsCertIsKnown;

	/** Gibt an, ob dem TLS-Zertifikat vertraut werden darf, entweder weil es bekannt ist oder weil der Benutzer zugestimmt hat. */
	@Column(name = "TLSCertIsTrusted")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean tlsCertIsTrusted;

	/** Die zugehörige fachliche/technische Service Domäne */
	@Column(name = "ServiceDomain")
	@JsonProperty
	@Convert(converter = OAuthServiceDomainConverter.class)
	@JsonSerialize(using = OAuthServiceDomainConverterSerializer.class)
	@JsonDeserialize(using = OAuthServiceDomainConverterDeserializer.class)
	public OAuthServiceDomain serviceDomain;

	/** Das angeforderte Scope */
	@Column(name = "RequestedScope")
	@JsonProperty
	public String requestedScope;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuleOAuthSecrets ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuleOAuthSecrets() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuleOAuthSecrets ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param authServerUrl   der Wert für das Attribut authServerUrl
	 * @param clientId   der Wert für das Attribut clientId
	 * @param clientSecret   der Wert für das Attribut clientSecret
	 */
	public DTOSchuleOAuthSecrets(final long id, final String authServerUrl, final String clientId, final String clientSecret) {
		this.id = id;
		if (authServerUrl == null) {
			throw new NullPointerException("authServerUrl must not be null");
		}
		this.authServerUrl = authServerUrl;
		if (clientId == null) {
			throw new NullPointerException("clientId must not be null");
		}
		this.clientId = clientId;
		if (clientSecret == null) {
			throw new NullPointerException("clientSecret must not be null");
		}
		this.clientSecret = clientSecret;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOSchuleOAuthSecrets other = (DTOSchuleOAuthSecrets) obj;
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
		return "DTOSchuleOAuthSecrets(id=" + this.id + ", authServerUrl=" + this.authServerUrl + ", clientId=" + this.clientId + ", clientSecret=" + this.clientSecret + ", tokenType=" + this.tokenType + ", tokenTimestamp=" + this.tokenTimestamp + ", tokenExpiresIn=" + this.tokenExpiresIn + ", tokenScope=" + this.tokenScope + ", token=" + this.token + ", tlsCert=" + this.tlsCert + ", tlsCertIsKnown=" + this.tlsCertIsKnown + ", tlsCertIsTrusted=" + this.tlsCertIsTrusted + ", serviceDomain=" + this.serviceDomain + ", requestedScope=" + this.requestedScope + ")";
	}

}
