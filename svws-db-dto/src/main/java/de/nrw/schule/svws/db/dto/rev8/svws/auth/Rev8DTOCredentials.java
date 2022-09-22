package de.nrw.schule.svws.db.dto.rev8.svws.auth;

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
@NamedQuery(name="Rev8DTOCredentials.all", query="SELECT e FROM Rev8DTOCredentials e")
@NamedQuery(name="Rev8DTOCredentials.id", query="SELECT e FROM Rev8DTOCredentials e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOCredentials.id.multiple", query="SELECT e FROM Rev8DTOCredentials e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOCredentials.benutzername", query="SELECT e FROM Rev8DTOCredentials e WHERE e.Benutzername = :value")
@NamedQuery(name="Rev8DTOCredentials.benutzername.multiple", query="SELECT e FROM Rev8DTOCredentials e WHERE e.Benutzername IN :value")
@NamedQuery(name="Rev8DTOCredentials.benutzernamepseudonym", query="SELECT e FROM Rev8DTOCredentials e WHERE e.BenutzernamePseudonym = :value")
@NamedQuery(name="Rev8DTOCredentials.benutzernamepseudonym.multiple", query="SELECT e FROM Rev8DTOCredentials e WHERE e.BenutzernamePseudonym IN :value")
@NamedQuery(name="Rev8DTOCredentials.initialkennwort", query="SELECT e FROM Rev8DTOCredentials e WHERE e.Initialkennwort = :value")
@NamedQuery(name="Rev8DTOCredentials.initialkennwort.multiple", query="SELECT e FROM Rev8DTOCredentials e WHERE e.Initialkennwort IN :value")
@NamedQuery(name="Rev8DTOCredentials.passwordhash", query="SELECT e FROM Rev8DTOCredentials e WHERE e.PasswordHash = :value")
@NamedQuery(name="Rev8DTOCredentials.passwordhash.multiple", query="SELECT e FROM Rev8DTOCredentials e WHERE e.PasswordHash IN :value")
@NamedQuery(name="Rev8DTOCredentials.rsapublickey", query="SELECT e FROM Rev8DTOCredentials e WHERE e.RSAPublicKey = :value")
@NamedQuery(name="Rev8DTOCredentials.rsapublickey.multiple", query="SELECT e FROM Rev8DTOCredentials e WHERE e.RSAPublicKey IN :value")
@NamedQuery(name="Rev8DTOCredentials.rsaprivatekey", query="SELECT e FROM Rev8DTOCredentials e WHERE e.RSAPrivateKey = :value")
@NamedQuery(name="Rev8DTOCredentials.rsaprivatekey.multiple", query="SELECT e FROM Rev8DTOCredentials e WHERE e.RSAPrivateKey IN :value")
@NamedQuery(name="Rev8DTOCredentials.aes", query="SELECT e FROM Rev8DTOCredentials e WHERE e.AES = :value")
@NamedQuery(name="Rev8DTOCredentials.aes.multiple", query="SELECT e FROM Rev8DTOCredentials e WHERE e.AES IN :value")
@NamedQuery(name="Rev8DTOCredentials.primaryKeyQuery", query="SELECT e FROM Rev8DTOCredentials e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOCredentials.all.migration", query="SELECT e FROM Rev8DTOCredentials e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Benutzername","BenutzernamePseudonym","Initialkennwort","PasswordHash","RSAPublicKey","RSAPrivateKey","AES"})
public class Rev8DTOCredentials {

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOCredentials ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Benutzername   der Wert für das Attribut Benutzername
	 */
	public Rev8DTOCredentials(final Long ID, final String Benutzername) {
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
		Rev8DTOCredentials other = (Rev8DTOCredentials) obj;
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
		return "Rev8DTOCredentials(ID=" + this.ID + ", Benutzername=" + this.Benutzername + ", BenutzernamePseudonym=" + this.BenutzernamePseudonym + ", Initialkennwort=" + this.Initialkennwort + ", PasswordHash=" + this.PasswordHash + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}