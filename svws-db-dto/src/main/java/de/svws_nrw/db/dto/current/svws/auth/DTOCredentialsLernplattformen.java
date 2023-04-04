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
 * Diese Klasse dient als DTO für die Datenbanktabelle CredentialsLernplattformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "CredentialsLernplattformen")
@NamedQuery(name = "DTOCredentialsLernplattformen.all", query = "SELECT e FROM DTOCredentialsLernplattformen e")
@NamedQuery(name = "DTOCredentialsLernplattformen.id", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.id.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.lernplattformid", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.LernplattformID = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.lernplattformid.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.LernplattformID IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.benutzername", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.Benutzername = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.benutzername.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.Benutzername IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.benutzernamepseudonym", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.BenutzernamePseudonym = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.benutzernamepseudonym.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.BenutzernamePseudonym IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.initialkennwort", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.Initialkennwort = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.initialkennwort.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.Initialkennwort IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.pashwordhash", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.PashwordHash = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.pashwordhash.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.PashwordHash IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.rsapublickey", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.RSAPublicKey = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.rsapublickey.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.RSAPublicKey IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.rsaprivatekey", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.RSAPrivateKey = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.rsaprivatekey.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.RSAPrivateKey IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.aes", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.AES = :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.aes.multiple", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.AES IN :value")
@NamedQuery(name = "DTOCredentialsLernplattformen.primaryKeyQuery", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID = ?1")
@NamedQuery(name = "DTOCredentialsLernplattformen.all.migration", query = "SELECT e FROM DTOCredentialsLernplattformen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "LernplattformID", "Benutzername", "BenutzernamePseudonym", "Initialkennwort", "PashwordHash", "RSAPublicKey", "RSAPrivateKey", "AES"})
public final class DTOCredentialsLernplattformen {

	/** ID des Datensatzes für die externen Account-Credentials (Lernplattformen) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Lernplattform */
	@Column(name = "LernplattformID")
	@JsonProperty
	public Long LernplattformID;

	/** Benutzername für den Credential-Datensatz */
	@Column(name = "Benutzername")
	@JsonProperty
	public String Benutzername;

	/** Der pseudonymisierte Benutzername für den Credential-Datensatz */
	@Column(name = "BenutzernamePseudonym")
	@JsonProperty
	public String BenutzernamePseudonym;

	/** Initialkennwort für den Credential-Datensatz */
	@Column(name = "Initialkennwort")
	@JsonProperty
	public String Initialkennwort;

	/** Passwordhash für den Credential-Datensatz */
	@Column(name = "PashwordHash")
	@JsonProperty
	public String PashwordHash;

	/** RSAPublicKey für den Credential-Datensatz */
	@Column(name = "RSAPublicKey")
	@JsonProperty
	public String RSAPublicKey;

	/** RSAPrivateKey für den Credential-Datensatz */
	@Column(name = "RSAPrivateKey")
	@JsonProperty
	public String RSAPrivateKey;

	/** AES-Schlüssel für den Credential-Datensatz */
	@Column(name = "AES")
	@JsonProperty
	public String AES;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOCredentialsLernplattformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOCredentialsLernplattformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOCredentialsLernplattformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param Benutzername   der Wert für das Attribut Benutzername
	 */
	public DTOCredentialsLernplattformen(final Long ID, final Long LernplattformID, final String Benutzername) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (LernplattformID == null) {
			throw new NullPointerException("LernplattformID must not be null");
		}
		this.LernplattformID = LernplattformID;
		if (Benutzername == null) {
			throw new NullPointerException("Benutzername must not be null");
		}
		this.Benutzername = Benutzername;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOCredentialsLernplattformen other = (DTOCredentialsLernplattformen) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOCredentialsLernplattformen(ID=" + this.ID + ", LernplattformID=" + this.LernplattformID + ", Benutzername=" + this.Benutzername + ", BenutzernamePseudonym=" + this.BenutzernamePseudonym + ", Initialkennwort=" + this.Initialkennwort + ", PashwordHash=" + this.PashwordHash + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}
