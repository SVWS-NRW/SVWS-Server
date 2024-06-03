package de.svws_nrw.db.dto.migration.svws.auth;

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
public final class MigrationDTOSchuleCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuleCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.Schulnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.Schulnummer IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.Schulnummer IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulnummer */
	public static final String QUERY_BY_SCHULNUMMER = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.Schulnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulnummer */
	public static final String QUERY_LIST_BY_SCHULNUMMER = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.Schulnummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSAPublicKey */
	public static final String QUERY_BY_RSAPUBLICKEY = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.RSAPublicKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSAPublicKey */
	public static final String QUERY_LIST_BY_RSAPUBLICKEY = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.RSAPublicKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSAPrivateKey */
	public static final String QUERY_BY_RSAPRIVATEKEY = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.RSAPrivateKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSAPrivateKey */
	public static final String QUERY_LIST_BY_RSAPRIVATEKEY = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.RSAPrivateKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AES */
	public static final String QUERY_BY_AES = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.AES = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AES */
	public static final String QUERY_LIST_BY_AES = "SELECT e FROM MigrationDTOSchuleCredentials e WHERE e.AES IN ?1";

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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuleCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuleCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuleCredentials ohne eine Initialisierung der Attribute.
	 * @param Schulnummer   der Wert für das Attribut Schulnummer
	 */
	public MigrationDTOSchuleCredentials(final Integer Schulnummer) {
		if (Schulnummer == null) {
			throw new NullPointerException("Schulnummer must not be null");
		}
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
		MigrationDTOSchuleCredentials other = (MigrationDTOSchuleCredentials) obj;
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
		return "MigrationDTOSchuleCredentials(Schulnummer=" + this.Schulnummer + ", RSAPublicKey=" + this.RSAPublicKey + ", RSAPrivateKey=" + this.RSAPrivateKey + ", AES=" + this.AES + ")";
	}

}
