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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuleCredentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuleCredentials")
@NamedQuery(name="Rev8DTOSchuleCredentials.all", query="SELECT e FROM Rev8DTOSchuleCredentials e")
@NamedQuery(name="Rev8DTOSchuleCredentials.schulnummer", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.Schulnummer = :value")
@NamedQuery(name="Rev8DTOSchuleCredentials.schulnummer.multiple", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.Schulnummer IN :value")
@NamedQuery(name="Rev8DTOSchuleCredentials.rsapublickey", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.RSAPublicKey = :value")
@NamedQuery(name="Rev8DTOSchuleCredentials.rsapublickey.multiple", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.RSAPublicKey IN :value")
@NamedQuery(name="Rev8DTOSchuleCredentials.rsaprivatekey", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.RSAPrivateKey = :value")
@NamedQuery(name="Rev8DTOSchuleCredentials.rsaprivatekey.multiple", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.RSAPrivateKey IN :value")
@NamedQuery(name="Rev8DTOSchuleCredentials.aes", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.AES = :value")
@NamedQuery(name="Rev8DTOSchuleCredentials.aes.multiple", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.AES IN :value")
@NamedQuery(name="Rev8DTOSchuleCredentials.primaryKeyQuery", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.Schulnummer = ?1")
@NamedQuery(name="Rev8DTOSchuleCredentials.all.migration", query="SELECT e FROM Rev8DTOSchuleCredentials e WHERE e.Schulnummer IS NOT NULL")
@JsonPropertyOrder({"Schulnummer","RSAPublicKey","RSAPrivateKey","AES"})
public class Rev8DTOSchuleCredentials {

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuleCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOSchuleCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuleCredentials ohne eine Initialisierung der Attribute.
	 * @param Schulnummer   der Wert für das Attribut Schulnummer
	 */
	public Rev8DTOSchuleCredentials(final Integer Schulnummer) {
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
		Rev8DTOSchuleCredentials other = (Rev8DTOSchuleCredentials) obj;
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
		return "Rev8DTOSchuleCredentials(Schulnummer=" + this.Schulnummer + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}