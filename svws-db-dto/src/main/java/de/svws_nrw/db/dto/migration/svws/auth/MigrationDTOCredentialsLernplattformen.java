package de.svws_nrw.db.dto.migration.svws.auth;

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
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.all", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.id", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.id.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.lernplattformid", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.LernplattformID = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.lernplattformid.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.LernplattformID IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.benutzername", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.Benutzername = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.benutzername.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.Benutzername IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.benutzernamepseudonym", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.BenutzernamePseudonym = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.benutzernamepseudonym.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.BenutzernamePseudonym IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.initialkennwort", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.Initialkennwort = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.initialkennwort.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.Initialkennwort IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.pashwordhash", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.PashwordHash = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.pashwordhash.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.PashwordHash IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.rsapublickey", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.RSAPublicKey = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.rsapublickey.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.RSAPublicKey IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.rsaprivatekey", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.RSAPrivateKey = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.rsaprivatekey.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.RSAPrivateKey IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.aes", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.AES = :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.aes.multiple", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.AES IN :value")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.primaryKeyQuery", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOCredentialsLernplattformen.all.migration", query = "SELECT e FROM MigrationDTOCredentialsLernplattformen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "LernplattformID", "Benutzername", "BenutzernamePseudonym", "Initialkennwort", "PashwordHash", "RSAPublicKey", "RSAPrivateKey", "AES"})
public final class MigrationDTOCredentialsLernplattformen {

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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOCredentialsLernplattformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOCredentialsLernplattformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOCredentialsLernplattformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param Benutzername   der Wert für das Attribut Benutzername
	 */
	public MigrationDTOCredentialsLernplattformen(final Long ID, final Long LernplattformID, final String Benutzername) {
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
		MigrationDTOCredentialsLernplattformen other = (MigrationDTOCredentialsLernplattformen) obj;
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
		return "MigrationDTOCredentialsLernplattformen(ID=" + this.ID + ", LernplattformID=" + this.LernplattformID + ", Benutzername=" + this.Benutzername + ", BenutzernamePseudonym=" + this.BenutzernamePseudonym + ", Initialkennwort=" + this.Initialkennwort + ", PashwordHash=" + this.PashwordHash + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}
