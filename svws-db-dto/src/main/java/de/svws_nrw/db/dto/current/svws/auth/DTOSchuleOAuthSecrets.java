package de.svws_nrw.db.dto.current.svws.auth;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuleOAuthSecrets.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuleOAuthSecrets")
@NamedQuery(name = "DTOSchuleOAuthSecrets.all", query = "SELECT e FROM DTOSchuleOAuthSecrets e")
@NamedQuery(name = "DTOSchuleOAuthSecrets.id", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.id.multiple", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.authserver", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.AuthServer = :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.authserver.multiple", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.AuthServer IN :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.clientid", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ClientID = :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.clientid.multiple", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ClientID IN :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.clientsecret", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ClientSecret = :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.clientsecret.multiple", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ClientSecret IN :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.primaryKeyQuery", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuleOAuthSecrets.primaryKeyQuery.multiple", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuleOAuthSecrets.all.migration", query = "SELECT e FROM DTOSchuleOAuthSecrets e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "AuthServer", "ClientID", "ClientSecret"})
public final class DTOSchuleOAuthSecrets {

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
