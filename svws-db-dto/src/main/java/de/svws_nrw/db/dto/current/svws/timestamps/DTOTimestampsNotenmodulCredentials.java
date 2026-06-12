package de.svws_nrw.db.dto.current.svws.timestamps;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TimestampsNotenmodulCredentials.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TimestampsNotenmodulCredentials")
@JsonPropertyOrder({"idLehrer", "tsPasswordHash", "tsArt2FA", "tsTotpSecret", "tsIstErstanmeldung"})
public final class DTOTimestampsNotenmodulCredentials {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTimestampsNotenmodulCredentials e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLehrer */
	public static final String QUERY_BY_IDLEHRER = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLehrer */
	public static final String QUERY_LIST_BY_IDLEHRER = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsPasswordHash */
	public static final String QUERY_BY_TSPASSWORDHASH = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsPasswordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsPasswordHash */
	public static final String QUERY_LIST_BY_TSPASSWORDHASH = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsPasswordHash IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsArt2FA */
	public static final String QUERY_BY_TSART2FA = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsArt2FA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsArt2FA */
	public static final String QUERY_LIST_BY_TSART2FA = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsArt2FA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsTotpSecret */
	public static final String QUERY_BY_TSTOTPSECRET = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsTotpSecret = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsTotpSecret */
	public static final String QUERY_LIST_BY_TSTOTPSECRET = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsTotpSecret IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsIstErstanmeldung */
	public static final String QUERY_BY_TSISTERSTANMELDUNG = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsIstErstanmeldung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsIstErstanmeldung */
	public static final String QUERY_LIST_BY_TSISTERSTANMELDUNG = "SELECT e FROM DTOTimestampsNotenmodulCredentials e WHERE e.tsIstErstanmeldung IN ?1";

	/** die ID des Lehrers */
	@Id
	@Column(name = "idLehrer")
	@JsonProperty
	public long idLehrer;

	/** Der Zeitstempel (UTC) der letzten Änderung an dem Password-Hash der Notenmodul-Credentials. */
	@Column(name = "tsPasswordHash")
	@JsonProperty
	public String tsPasswordHash;

	/** Der Zeitstempel (UTC) der letzten Änderung an der Art der Zwei-Faktor-Authentifizierung. */
	@Column(name = "tsArt2FA")
	@JsonProperty
	public String tsArt2FA;

	/** Der Zeitstempel (UTC) der letzten Änderung an dem Shared-Secret für TOTP. */
	@Column(name = "tsTotpSecret")
	@JsonProperty
	public String tsTotpSecret;

	/** Der Zeitstempel (UTC) der letzten Änderung an der Information, ob es sich bei der nächsten Anmeldung um eine erstanmeldung handelt oder nicht. */
	@Column(name = "tsIstErstanmeldung")
	@JsonProperty
	public String tsIstErstanmeldung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsNotenmodulCredentials ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTimestampsNotenmodulCredentials() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsNotenmodulCredentials ohne eine Initialisierung der Attribute.
	 * @param idLehrer   der Wert für das Attribut idLehrer
	 * @param tsPasswordHash   der Wert für das Attribut tsPasswordHash
	 * @param tsArt2FA   der Wert für das Attribut tsArt2FA
	 * @param tsTotpSecret   der Wert für das Attribut tsTotpSecret
	 * @param tsIstErstanmeldung   der Wert für das Attribut tsIstErstanmeldung
	 */
	public DTOTimestampsNotenmodulCredentials(final long idLehrer, final String tsPasswordHash, final String tsArt2FA, final String tsTotpSecret, final String tsIstErstanmeldung) {
		this.idLehrer = idLehrer;
		if (tsPasswordHash == null) {
			throw new NullPointerException("tsPasswordHash must not be null");
		}
		this.tsPasswordHash = tsPasswordHash;
		if (tsArt2FA == null) {
			throw new NullPointerException("tsArt2FA must not be null");
		}
		this.tsArt2FA = tsArt2FA;
		if (tsTotpSecret == null) {
			throw new NullPointerException("tsTotpSecret must not be null");
		}
		this.tsTotpSecret = tsTotpSecret;
		if (tsIstErstanmeldung == null) {
			throw new NullPointerException("tsIstErstanmeldung must not be null");
		}
		this.tsIstErstanmeldung = tsIstErstanmeldung;
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
		DTOTimestampsNotenmodulCredentials other = (DTOTimestampsNotenmodulCredentials) obj;
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
		return "DTOTimestampsNotenmodulCredentials(idLehrer=" + this.idLehrer + ", tsPasswordHash=" + this.tsPasswordHash + ", tsArt2FA=" + this.tsArt2FA + ", tsTotpSecret=" + this.tsTotpSecret + ", tsIstErstanmeldung=" + this.tsIstErstanmeldung + ")";
	}

}
