package de.svws_nrw.db.dto.current.svws.auth;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.OAuthServerModeConverter;
import de.svws_nrw.db.converter.current.OAuthServiceDomainConverter;

import de.svws_nrw.core.types.oauth2.OAuthServerMode;
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
import de.svws_nrw.csv.converter.current.OAuthServerModeConverterSerializer;
import de.svws_nrw.csv.converter.current.OAuthServerModeConverterDeserializer;
import de.svws_nrw.csv.converter.current.OAuthServiceDomainConverterSerializer;
import de.svws_nrw.csv.converter.current.OAuthServiceDomainConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle OAuth_Token_Server_Config.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "OAuth_Token_Server_Config")
@JsonPropertyOrder({"id", "url", "domain", "serverMode"})
public final class DTOOAuthTokenServerConfig {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOOAuthTokenServerConfig e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes url */
	public static final String QUERY_BY_URL = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.url = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes url */
	public static final String QUERY_LIST_BY_URL = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.url IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes domain */
	public static final String QUERY_BY_DOMAIN = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.domain = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes domain */
	public static final String QUERY_LIST_BY_DOMAIN = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.domain IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes serverMode */
	public static final String QUERY_BY_SERVERMODE = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.serverMode = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes serverMode */
	public static final String QUERY_LIST_BY_SERVERMODE = "SELECT e FROM DTOOAuthTokenServerConfig e WHERE e.serverMode IN ?1";

	/** ID der Konfiguration des Token Servers */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Die vollständige URL des Token-Endpunktes */
	@Column(name = "url")
	@JsonProperty
	public String url;

	/** Die zugehörige fachliche/technische Service Domäne */
	@Column(name = "domain")
	@JsonProperty
	@Convert(converter = OAuthServiceDomainConverter.class)
	@JsonSerialize(using = OAuthServiceDomainConverterSerializer.class)
	@JsonDeserialize(using = OAuthServiceDomainConverterDeserializer.class)
	public OAuthServiceDomain domain;

	/** Der Modus der externen Systeme (DEVELOPMENT oder PRODUCTION) */
	@Column(name = "server_mode")
	@JsonProperty
	@Convert(converter = OAuthServerModeConverter.class)
	@JsonSerialize(using = OAuthServerModeConverterSerializer.class)
	@JsonDeserialize(using = OAuthServerModeConverterDeserializer.class)
	public OAuthServerMode serverMode;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOAuthTokenServerConfig ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOOAuthTokenServerConfig() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOAuthTokenServerConfig ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param url   der Wert für das Attribut url
	 * @param domain   der Wert für das Attribut domain
	 * @param serverMode   der Wert für das Attribut serverMode
	 */
	public DTOOAuthTokenServerConfig(final long id, final String url, final OAuthServiceDomain domain, final OAuthServerMode serverMode) {
		this.id = id;
		if (url == null) {
			throw new NullPointerException("url must not be null");
		}
		this.url = url;
		if (domain == null) {
			throw new NullPointerException("domain must not be null");
		}
		this.domain = domain;
		if (serverMode == null) {
			throw new NullPointerException("serverMode must not be null");
		}
		this.serverMode = serverMode;
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
		DTOOAuthTokenServerConfig other = (DTOOAuthTokenServerConfig) obj;
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
		return "DTOOAuthTokenServerConfig(id=" + this.id + ", url=" + this.url + ", domain=" + this.domain + ", serverMode=" + this.serverMode + ")";
	}

}
