package de.svws_nrw.db.dto.current.svws.auth;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.OAuthResourceTypeConverter;
import de.svws_nrw.db.converter.current.OAuthServerModeConverter;
import de.svws_nrw.db.converter.current.OAuthServiceDomainConverter;

import de.svws_nrw.core.types.oauth2.OAuthResourceType;
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
import de.svws_nrw.csv.converter.current.OAuthResourceTypeConverterSerializer;
import de.svws_nrw.csv.converter.current.OAuthResourceTypeConverterDeserializer;
import de.svws_nrw.csv.converter.current.OAuthServerModeConverterSerializer;
import de.svws_nrw.csv.converter.current.OAuthServerModeConverterDeserializer;
import de.svws_nrw.csv.converter.current.OAuthServiceDomainConverterSerializer;
import de.svws_nrw.csv.converter.current.OAuthServiceDomainConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle OAuth_Resource_Server_Config.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "OAuth_Resource_Server_Config")
@JsonPropertyOrder({"id", "url", "domain", "serverMode", "resourceType", "requestScope"})
public final class DTOOAuthResourceServerConfig {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOOAuthResourceServerConfig e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes url */
	public static final String QUERY_BY_URL = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.url = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes url */
	public static final String QUERY_LIST_BY_URL = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.url IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes domain */
	public static final String QUERY_BY_DOMAIN = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.domain = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes domain */
	public static final String QUERY_LIST_BY_DOMAIN = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.domain IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes serverMode */
	public static final String QUERY_BY_SERVERMODE = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.serverMode = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes serverMode */
	public static final String QUERY_LIST_BY_SERVERMODE = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.serverMode IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes resourceType */
	public static final String QUERY_BY_RESOURCETYPE = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.resourceType = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes resourceType */
	public static final String QUERY_LIST_BY_RESOURCETYPE = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.resourceType IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes requestScope */
	public static final String QUERY_BY_REQUESTSCOPE = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.requestScope = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes requestScope */
	public static final String QUERY_LIST_BY_REQUESTSCOPE = "SELECT e FROM DTOOAuthResourceServerConfig e WHERE e.requestScope IN ?1";

	/** ID der Konfiguration des Resource Servers */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Die vollständige URL des Resource Servers */
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

	/** Die Art des Resource Servers, z.B. SIGNIERDIENST */
	@Column(name = "resource_type")
	@JsonProperty
	@Convert(converter = OAuthResourceTypeConverter.class)
	@JsonSerialize(using = OAuthResourceTypeConverterSerializer.class)
	@JsonDeserialize(using = OAuthResourceTypeConverterDeserializer.class)
	public OAuthResourceType resourceType;

	/** Der beim Token-Request anzufordernde Scope. Er ist die einzige Quelle für den Scope. */
	@Column(name = "request_scope")
	@JsonProperty
	public String requestScope;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOAuthResourceServerConfig ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOOAuthResourceServerConfig() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOAuthResourceServerConfig ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param url   der Wert für das Attribut url
	 * @param domain   der Wert für das Attribut domain
	 * @param serverMode   der Wert für das Attribut serverMode
	 * @param resourceType   der Wert für das Attribut resourceType
	 * @param requestScope   der Wert für das Attribut requestScope
	 */
	public DTOOAuthResourceServerConfig(final long id, final String url, final OAuthServiceDomain domain, final OAuthServerMode serverMode, final OAuthResourceType resourceType, final String requestScope) {
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
		if (resourceType == null) {
			throw new NullPointerException("resourceType must not be null");
		}
		this.resourceType = resourceType;
		if (requestScope == null) {
			throw new NullPointerException("requestScope must not be null");
		}
		this.requestScope = requestScope;
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
		DTOOAuthResourceServerConfig other = (DTOOAuthResourceServerConfig) obj;
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
		return "DTOOAuthResourceServerConfig(id=" + this.id + ", url=" + this.url + ", domain=" + this.domain + ", serverMode=" + this.serverMode + ", resourceType=" + this.resourceType + ", requestScope=" + this.requestScope + ")";
	}

}
