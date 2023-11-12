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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuleCredentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuleCredentials")
@NamedQuery(name = "DTOSchuleCredentials.all", query = "SELECT e FROM DTOSchuleCredentials e")
@NamedQuery(name = "DTOSchuleCredentials.schulnummer", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer = :value")
@NamedQuery(name = "DTOSchuleCredentials.schulnummer.multiple", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer IN :value")
@NamedQuery(name = "DTOSchuleCredentials.rsapublickey", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.RSAPublicKey = :value")
@NamedQuery(name = "DTOSchuleCredentials.rsapublickey.multiple", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.RSAPublicKey IN :value")
@NamedQuery(name = "DTOSchuleCredentials.rsaprivatekey", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.RSAPrivateKey = :value")
@NamedQuery(name = "DTOSchuleCredentials.rsaprivatekey.multiple", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.RSAPrivateKey IN :value")
@NamedQuery(name = "DTOSchuleCredentials.aes", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.AES = :value")
@NamedQuery(name = "DTOSchuleCredentials.aes.multiple", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.AES IN :value")
@NamedQuery(name = "DTOSchuleCredentials.primaryKeyQuery", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer = ?1")
@NamedQuery(name = "DTOSchuleCredentials.primaryKeyQuery.multiple", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer IN ?1")
@NamedQuery(name = "DTOSchuleCredentials.all.migration", query = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer IS NOT NULL")
@JsonPropertyOrder({"Schulnummer", "RSAPublicKey", "RSAPrivateKey", "AES"})
public final class DTOSchuleCredentials {

	/** ID für den Credential-Datensatz einer Schule (also auch für den PublicKey der anderen Schulen) */
	@Id
	@Column(name = "Schulnummer")
	@JsonProperty
	public int Schulnummer;

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
	 * Erstellt ein neues Objekt der Klasse DTOSchuleCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuleCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuleCredentials ohne eine Initialisierung der Attribute.
	 * @param Schulnummer   der Wert für das Attribut Schulnummer
	 */
	public DTOSchuleCredentials(final int Schulnummer) {
		this.Schulnummer = Schulnummer;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuleCredentials other = (DTOSchuleCredentials) obj;
		return Schulnummer == other.Schulnummer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Schulnummer);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuleCredentials(Schulnummer=" + this.Schulnummer + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}
