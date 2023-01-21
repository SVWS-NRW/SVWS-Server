package de.nrw.schule.svws.db.dto.dev.svws.auth;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuleCredentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuleCredentials")
@NamedQuery(name="DevDTOSchuleCredentials.all", query="SELECT e FROM DevDTOSchuleCredentials e")
@NamedQuery(name="DevDTOSchuleCredentials.schulnummer", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.Schulnummer = :value")
@NamedQuery(name="DevDTOSchuleCredentials.schulnummer.multiple", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.Schulnummer IN :value")
@NamedQuery(name="DevDTOSchuleCredentials.rsapublickey", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.RSAPublicKey = :value")
@NamedQuery(name="DevDTOSchuleCredentials.rsapublickey.multiple", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.RSAPublicKey IN :value")
@NamedQuery(name="DevDTOSchuleCredentials.rsaprivatekey", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.RSAPrivateKey = :value")
@NamedQuery(name="DevDTOSchuleCredentials.rsaprivatekey.multiple", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.RSAPrivateKey IN :value")
@NamedQuery(name="DevDTOSchuleCredentials.aes", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.AES = :value")
@NamedQuery(name="DevDTOSchuleCredentials.aes.multiple", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.AES IN :value")
@NamedQuery(name="DevDTOSchuleCredentials.primaryKeyQuery", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.Schulnummer = ?1")
@NamedQuery(name="DevDTOSchuleCredentials.all.migration", query="SELECT e FROM DevDTOSchuleCredentials e WHERE e.Schulnummer IS NOT NULL")
@JsonPropertyOrder({"Schulnummer","RSAPublicKey","RSAPrivateKey","AES"})
public class DevDTOSchuleCredentials {

	/** ID für den Credential-Datensatz einer Schule (also auch für den PublicKey der anderen Schulen) */
	@Id
	@Column(name = "Schulnummer")
	@JsonProperty
	public Integer Schulnummer;

	/** RSAPublicKey für den Credential-Datensatz einer Schule */
	@Column(name = "RSAPublicKey")
	@JsonProperty
	public String RSAPublicKey;

	/** RSAPrivateKey für den Credential-Datensatz der Schule falls es die eigene ist sonst NULL */
	@Column(name = "RSAPrivateKey")
	@JsonProperty
	public String RSAPrivateKey;

	/** AES-Schlüssel für den Credential-Datensatz der Schule falls es die eigene ist sonst NULL */
	@Column(name = "AES")
	@JsonProperty
	public String AES;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuleCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchuleCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuleCredentials ohne eine Initialisierung der Attribute.
	 * @param Schulnummer   der Wert für das Attribut Schulnummer
	 */
	public DevDTOSchuleCredentials(final Integer Schulnummer) {
		if (Schulnummer == null) { 
			throw new NullPointerException("Schulnummer must not be null");
		}
		this.Schulnummer = Schulnummer;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOSchuleCredentials other = (DevDTOSchuleCredentials) obj;
		if (Schulnummer == null) {
			if (other.Schulnummer != null)
				return false;
		} else if (!Schulnummer.equals(other.Schulnummer))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schulnummer == null) ? 0 : Schulnummer.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DevDTOSchuleCredentials(Schulnummer=" + this.Schulnummer + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}