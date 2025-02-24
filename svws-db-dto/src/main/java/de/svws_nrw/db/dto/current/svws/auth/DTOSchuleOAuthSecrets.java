package de.svws_nrw.db.dto.current.svws.auth;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuleOAuthSecrets.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuleOAuthSecrets")
@JsonPropertyOrder({"ID", "AuthServer", "ClientID", "ClientSecret", "TokenType", "TokenTimestamp", "TokenExpiresIn", "TokenScope", "Token", "TLSCert", "TLSCertIsKnown", "TLSCertIsTrusted"})
public final class DTOSchuleOAuthSecrets {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuleOAuthSecrets e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AuthServer */
	public static final String QUERY_BY_AUTHSERVER = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.AuthServer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AuthServer */
	public static final String QUERY_LIST_BY_AUTHSERVER = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.AuthServer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ClientID */
	public static final String QUERY_BY_CLIENTID = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ClientID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ClientID */
	public static final String QUERY_LIST_BY_CLIENTID = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ClientID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ClientSecret */
	public static final String QUERY_BY_CLIENTSECRET = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ClientSecret = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ClientSecret */
	public static final String QUERY_LIST_BY_CLIENTSECRET = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ClientSecret IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TokenType */
	public static final String QUERY_BY_TOKENTYPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TokenType = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TokenType */
	public static final String QUERY_LIST_BY_TOKENTYPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TokenType IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TokenTimestamp */
	public static final String QUERY_BY_TOKENTIMESTAMP = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TokenTimestamp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TokenTimestamp */
	public static final String QUERY_LIST_BY_TOKENTIMESTAMP = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TokenTimestamp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TokenExpiresIn */
	public static final String QUERY_BY_TOKENEXPIRESIN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TokenExpiresIn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TokenExpiresIn */
	public static final String QUERY_LIST_BY_TOKENEXPIRESIN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TokenExpiresIn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TokenScope */
	public static final String QUERY_BY_TOKENSCOPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TokenScope = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TokenScope */
	public static final String QUERY_LIST_BY_TOKENSCOPE = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TokenScope IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Token */
	public static final String QUERY_BY_TOKEN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.Token = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Token */
	public static final String QUERY_LIST_BY_TOKEN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.Token IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TLSCert */
	public static final String QUERY_BY_TLSCERT = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TLSCert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TLSCert */
	public static final String QUERY_LIST_BY_TLSCERT = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TLSCert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TLSCertIsKnown */
	public static final String QUERY_BY_TLSCERTISKNOWN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TLSCertIsKnown = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TLSCertIsKnown */
	public static final String QUERY_LIST_BY_TLSCERTISKNOWN = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TLSCertIsKnown IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TLSCertIsTrusted */
	public static final String QUERY_BY_TLSCERTISTRUSTED = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TLSCertIsTrusted = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TLSCertIsTrusted */
	public static final String QUERY_LIST_BY_TLSCERTISTRUSTED = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.TLSCertIsTrusted IN ?1";

	/** ID des OAuth-Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Authorization Server */
	@Column(name = "AuthServer")
	@JsonProperty
	public String AuthServer;

	/** Die ID des Clients */
	@Column(name = "ClientID")
	@JsonProperty
	public String ClientID;

	/** Das Secret des Clients */
	@Column(name = "ClientSecret")
	@JsonProperty
	public String ClientSecret;

	/** Der Typ des Tokens */
	@Column(name = "TokenType")
	@JsonProperty
	public String TokenType;

	/** Ankunftzeitpunkt des Tokens als Zeitstempel in Millisekungen */
	@Column(name = "TokenTimestamp")
	@JsonProperty
	public Long TokenTimestamp;

	/** Lebensdauer des Tokens in Sekunden */
	@Column(name = "TokenExpiresIn")
	@JsonProperty
	public Long TokenExpiresIn;

	/** Der Gültigkeitsbereich des Tokens */
	@Column(name = "TokenScope")
	@JsonProperty
	public String TokenScope;

	/** Das Token */
	@Column(name = "Token")
	@JsonProperty
	public String Token;

	/** Das TLS Zertifikat des OAuth2-Servers */
	@Column(name = "TLSCert")
	@JsonProperty
	public String TLSCert;

	/** Gibt an, ob das Zertifikat über den Keystore validiert werden kann. */
	@Column(name = "TLSCertIsKnown")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean TLSCertIsKnown;

	/** Gibt an, ob dem TLS-Zertifikat vertraut werden darf, entweder weil es bekannt ist oder weil der Benutzer zugestimmt hat. */
	@Column(name = "TLSCertIsTrusted")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean TLSCertIsTrusted;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuleOAuthSecrets ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuleOAuthSecrets() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuleOAuthSecrets ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param AuthServer   der Wert für das Attribut AuthServer
	 * @param ClientID   der Wert für das Attribut ClientID
	 * @param ClientSecret   der Wert für das Attribut ClientSecret
	 */
	public DTOSchuleOAuthSecrets(final long ID, final String AuthServer, final String ClientID, final String ClientSecret) {
		this.ID = ID;
		if (AuthServer == null) {
			throw new NullPointerException("AuthServer must not be null");
		}
		this.AuthServer = AuthServer;
		if (ClientID == null) {
			throw new NullPointerException("ClientID must not be null");
		}
		this.ClientID = ClientID;
		if (ClientSecret == null) {
			throw new NullPointerException("ClientSecret must not be null");
		}
		this.ClientSecret = ClientSecret;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuleOAuthSecrets other = (DTOSchuleOAuthSecrets) obj;
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
		return "DTOSchuleOAuthSecrets(ID=" + this.ID + ", AuthServer=" + this.AuthServer + ", ClientID=" + this.ClientID + ", ClientSecret=" + this.ClientSecret + ", TokenType=" + this.TokenType + ", TokenTimestamp=" + this.TokenTimestamp + ", TokenExpiresIn=" + this.TokenExpiresIn + ", TokenScope=" + this.TokenScope + ", Token=" + this.Token + ", TLSCert=" + this.TLSCert + ", TLSCertIsKnown=" + this.TLSCertIsKnown + ", TLSCertIsTrusted=" + this.TLSCertIsTrusted + ")";
	}

}
