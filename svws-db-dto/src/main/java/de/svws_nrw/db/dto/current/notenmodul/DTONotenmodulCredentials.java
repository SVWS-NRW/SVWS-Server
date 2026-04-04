package de.svws_nrw.db.dto.current.notenmodul;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Notenmodul_Credentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Notenmodul_Credentials")
@JsonPropertyOrder({"idLehrer", "initialkennwort", "passwordHash", "art2FA", "totpSecret", "istErstanmeldung"})
public final class DTONotenmodulCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTONotenmodulCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLehrer */
	public static final String QUERY_BY_IDLEHRER = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLehrer */
	public static final String QUERY_LIST_BY_IDLEHRER = "SELECT e FROM DTONotenmodulCredentials e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes initialkennwort */
	public static final String QUERY_BY_INITIALKENNWORT = "SELECT e FROM DTONotenmodulCredentials e WHERE e.initialkennwort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes initialkennwort */
	public static final String QUERY_LIST_BY_INITIALKENNWORT = "SELECT e FROM DTONotenmodulCredentials e WHERE e.initialkennwort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes passwordHash */
	public static final String QUERY_BY_PASSWORDHASH = "SELECT e FROM DTONotenmodulCredentials e WHERE e.passwordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes passwordHash */
	public static final String QUERY_LIST_BY_PASSWORDHASH = "SELECT e FROM DTONotenmodulCredentials e WHERE e.passwordHash IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes art2FA */
	public static final String QUERY_BY_ART2FA = "SELECT e FROM DTONotenmodulCredentials e WHERE e.art2FA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes art2FA */
	public static final String QUERY_LIST_BY_ART2FA = "SELECT e FROM DTONotenmodulCredentials e WHERE e.art2FA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes totpSecret */
	public static final String QUERY_BY_TOTPSECRET = "SELECT e FROM DTONotenmodulCredentials e WHERE e.totpSecret = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes totpSecret */
	public static final String QUERY_LIST_BY_TOTPSECRET = "SELECT e FROM DTONotenmodulCredentials e WHERE e.totpSecret IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istErstanmeldung */
	public static final String QUERY_BY_ISTERSTANMELDUNG = "SELECT e FROM DTONotenmodulCredentials e WHERE e.istErstanmeldung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istErstanmeldung */
	public static final String QUERY_LIST_BY_ISTERSTANMELDUNG = "SELECT e FROM DTONotenmodulCredentials e WHERE e.istErstanmeldung IN ?1";

	/** Die LehrerID des Lehrers, für den die Credentials gelten */
	@Id
	@Column(name = "idLehrer")
	@JsonProperty
	public long idLehrer;

	/** Initialkennwort für den Credential-Datensatz */
	@Column(name = "initialkennwort")
	@JsonProperty
	public String initialkennwort;

	/** Passwordhash für den Credential-Datensatz */
	@Column(name = "passwordHash")
	@JsonProperty
	public String passwordHash;

	/** Gibt die Art der verwendeten Zwei-Faktor-Authentifizierung an (0 = Keine, 1 = TOTP, 2 = EMail). */
	@Column(name = "art2FA")
	@JsonProperty
	public int art2FA;

	/** Das Shared Secret für 2FA mit TOTP */
	@Column(name = "totpSecret")
	@JsonProperty
	public String totpSecret;

	/** Gibt an, ob bei der nächsten Anmeldung eines Lehrer eine Erstanmeldung vorliegt oder nicht. */
	@Column(name = "istErstanmeldung")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean istErstanmeldung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTONotenmodulCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTONotenmodulCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTONotenmodulCredentials ohne eine Initialisierung der Attribute.
	 * @param idLehrer   der Wert für das Attribut idLehrer
	 * @param initialkennwort   der Wert für das Attribut initialkennwort
	 * @param passwordHash   der Wert für das Attribut passwordHash
	 * @param art2FA   der Wert für das Attribut art2FA
	 * @param istErstanmeldung   der Wert für das Attribut istErstanmeldung
	 */
	public DTONotenmodulCredentials(final long idLehrer, final String initialkennwort, final String passwordHash, final int art2FA, final Boolean istErstanmeldung) {
		this.idLehrer = idLehrer;
		if (initialkennwort == null) {
			throw new NullPointerException("initialkennwort must not be null");
		}
		this.initialkennwort = initialkennwort;
		if (passwordHash == null) {
			throw new NullPointerException("passwordHash must not be null");
		}
		this.passwordHash = passwordHash;
		this.art2FA = art2FA;
		this.istErstanmeldung = istErstanmeldung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTONotenmodulCredentials other = (DTONotenmodulCredentials) obj;
		return idLehrer == other.idLehrer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(idLehrer);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTONotenmodulCredentials(idLehrer=" + this.idLehrer + ", initialkennwort=" + this.initialkennwort + ", passwordHash=" + this.passwordHash + ", art2FA=" + this.art2FA + ", totpSecret=" + this.totpSecret + ", istErstanmeldung=" + this.istErstanmeldung + ")";
	}

}
