package de.nrw.schule.svws.db.dto.rev9.svws.auth;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Credentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Credentials")
@NamedQuery(name="Rev9DTOCredentials.all", query="SELECT e FROM Rev9DTOCredentials e")
@NamedQuery(name="Rev9DTOCredentials.id", query="SELECT e FROM Rev9DTOCredentials e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOCredentials.id.multiple", query="SELECT e FROM Rev9DTOCredentials e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOCredentials.benutzername", query="SELECT e FROM Rev9DTOCredentials e WHERE e.Benutzername = :value")
@NamedQuery(name="Rev9DTOCredentials.benutzername.multiple", query="SELECT e FROM Rev9DTOCredentials e WHERE e.Benutzername IN :value")
@NamedQuery(name="Rev9DTOCredentials.benutzernamepseudonym", query="SELECT e FROM Rev9DTOCredentials e WHERE e.BenutzernamePseudonym = :value")
@NamedQuery(name="Rev9DTOCredentials.benutzernamepseudonym.multiple", query="SELECT e FROM Rev9DTOCredentials e WHERE e.BenutzernamePseudonym IN :value")
@NamedQuery(name="Rev9DTOCredentials.initialkennwort", query="SELECT e FROM Rev9DTOCredentials e WHERE e.Initialkennwort = :value")
@NamedQuery(name="Rev9DTOCredentials.initialkennwort.multiple", query="SELECT e FROM Rev9DTOCredentials e WHERE e.Initialkennwort IN :value")
@NamedQuery(name="Rev9DTOCredentials.passwordhash", query="SELECT e FROM Rev9DTOCredentials e WHERE e.PasswordHash = :value")
@NamedQuery(name="Rev9DTOCredentials.passwordhash.multiple", query="SELECT e FROM Rev9DTOCredentials e WHERE e.PasswordHash IN :value")
@NamedQuery(name="Rev9DTOCredentials.rsapublickey", query="SELECT e FROM Rev9DTOCredentials e WHERE e.RSAPublicKey = :value")
@NamedQuery(name="Rev9DTOCredentials.rsapublickey.multiple", query="SELECT e FROM Rev9DTOCredentials e WHERE e.RSAPublicKey IN :value")
@NamedQuery(name="Rev9DTOCredentials.rsaprivatekey", query="SELECT e FROM Rev9DTOCredentials e WHERE e.RSAPrivateKey = :value")
@NamedQuery(name="Rev9DTOCredentials.rsaprivatekey.multiple", query="SELECT e FROM Rev9DTOCredentials e WHERE e.RSAPrivateKey IN :value")
@NamedQuery(name="Rev9DTOCredentials.aes", query="SELECT e FROM Rev9DTOCredentials e WHERE e.AES = :value")
@NamedQuery(name="Rev9DTOCredentials.aes.multiple", query="SELECT e FROM Rev9DTOCredentials e WHERE e.AES IN :value")
@NamedQuery(name="Rev9DTOCredentials.primaryKeyQuery", query="SELECT e FROM Rev9DTOCredentials e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOCredentials.all.migration", query="SELECT e FROM Rev9DTOCredentials e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Benutzername","BenutzernamePseudonym","Initialkennwort","PasswordHash","RSAPublicKey","RSAPrivateKey","AES"})
public class Rev9DTOCredentials {

	/** ID des Datensatzes für die SVWS internen Account-Credentials */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

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
	@Column(name = "PasswordHash")
	@JsonProperty
	public String PasswordHash;

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
	 * Erstellt ein neues Objekt der Klasse Rev9DTOCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOCredentials ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Benutzername   der Wert für das Attribut Benutzername
	 */
	public Rev9DTOCredentials(final Long ID, final String Benutzername) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Benutzername == null) { 
			throw new NullPointerException("Benutzername must not be null");
		}
		this.Benutzername = Benutzername;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOCredentials other = (Rev9DTOCredentials) obj;
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
		return "Rev9DTOCredentials(ID=" + this.ID + ", Benutzername=" + this.Benutzername + ", BenutzernamePseudonym=" + this.BenutzernamePseudonym + ", Initialkennwort=" + this.Initialkennwort + ", PasswordHash=" + this.PasswordHash + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}