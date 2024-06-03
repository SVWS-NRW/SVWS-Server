package de.svws_nrw.db.dto.current.svws.auth;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuleOAuthSecrets.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuleOAuthSecrets")
@JsonPropertyOrder({"ID", "AuthServer", "ClientID", "ClientSecret"})
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
		return "DTOSchuleOAuthSecrets(ID=" + this.ID + ", AuthServer=" + this.AuthServer + ", ClientID=" + this.ClientID + ", ClientSecret=" + this.ClientSecret + ")";
	}

}
