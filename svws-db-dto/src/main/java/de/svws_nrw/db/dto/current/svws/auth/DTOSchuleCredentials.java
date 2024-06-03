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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuleCredentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuleCredentials")
@JsonPropertyOrder({"Schulnummer", "RSAPublicKey", "RSAPrivateKey", "AES"})
public final class DTOSchuleCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuleCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulnummer */
	public static final String QUERY_BY_SCHULNUMMER = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulnummer */
	public static final String QUERY_LIST_BY_SCHULNUMMER = "SELECT e FROM DTOSchuleCredentials e WHERE e.Schulnummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSAPublicKey */
	public static final String QUERY_BY_RSAPUBLICKEY = "SELECT e FROM DTOSchuleCredentials e WHERE e.RSAPublicKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSAPublicKey */
	public static final String QUERY_LIST_BY_RSAPUBLICKEY = "SELECT e FROM DTOSchuleCredentials e WHERE e.RSAPublicKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSAPrivateKey */
	public static final String QUERY_BY_RSAPRIVATEKEY = "SELECT e FROM DTOSchuleCredentials e WHERE e.RSAPrivateKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSAPrivateKey */
	public static final String QUERY_LIST_BY_RSAPRIVATEKEY = "SELECT e FROM DTOSchuleCredentials e WHERE e.RSAPrivateKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AES */
	public static final String QUERY_BY_AES = "SELECT e FROM DTOSchuleCredentials e WHERE e.AES = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AES */
	public static final String QUERY_LIST_BY_AES = "SELECT e FROM DTOSchuleCredentials e WHERE e.AES IN ?1";

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
