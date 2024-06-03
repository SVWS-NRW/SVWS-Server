package de.svws_nrw.db.dto.current.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Logins.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOProtokollLoginPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Logins")
@JsonPropertyOrder({"LI_UserID", "LI_LoginTime", "LI_LogoffTime"})
public final class DTOProtokollLogin {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOProtokollLogin e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOProtokollLogin e WHERE e.LI_UserID = ?1 AND e.LI_LoginTime = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOProtokollLogin e WHERE e.LI_UserID IS NOT NULL AND e.LI_LoginTime IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LI_UserID */
	public static final String QUERY_BY_LI_USERID = "SELECT e FROM DTOProtokollLogin e WHERE e.LI_UserID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LI_UserID */
	public static final String QUERY_LIST_BY_LI_USERID = "SELECT e FROM DTOProtokollLogin e WHERE e.LI_UserID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LI_LoginTime */
	public static final String QUERY_BY_LI_LOGINTIME = "SELECT e FROM DTOProtokollLogin e WHERE e.LI_LoginTime = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LI_LoginTime */
	public static final String QUERY_LIST_BY_LI_LOGINTIME = "SELECT e FROM DTOProtokollLogin e WHERE e.LI_LoginTime IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LI_LogoffTime */
	public static final String QUERY_BY_LI_LOGOFFTIME = "SELECT e FROM DTOProtokollLogin e WHERE e.LI_LogoffTime = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LI_LogoffTime */
	public static final String QUERY_LIST_BY_LI_LOGOFFTIME = "SELECT e FROM DTOProtokollLogin e WHERE e.LI_LogoffTime IN ?1";

	/** UserID des Logins */
	@Id
	@Column(name = "LI_UserID")
	@JsonProperty
	public long LI_UserID;

	/** Login Zeit */
	@Id
	@Column(name = "LI_LoginTime")
	@JsonProperty
	public String LI_LoginTime;

	/** Logoff Zeit */
	@Column(name = "LI_LogoffTime")
	@JsonProperty
	public String LI_LogoffTime;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOProtokollLogin ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOProtokollLogin() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOProtokollLogin ohne eine Initialisierung der Attribute.
	 * @param LI_UserID   der Wert für das Attribut LI_UserID
	 */
	public DTOProtokollLogin(final long LI_UserID) {
		this.LI_UserID = LI_UserID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOProtokollLogin other = (DTOProtokollLogin) obj;
		if (LI_UserID != other.LI_UserID)
			return false;
		if (LI_LoginTime == null) {
			if (other.LI_LoginTime != null)
				return false;
		} else if (!LI_LoginTime.equals(other.LI_LoginTime))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(LI_UserID);

		result = prime * result + ((LI_LoginTime == null) ? 0 : LI_LoginTime.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOProtokollLogin(LI_UserID=" + this.LI_UserID + ", LI_LoginTime=" + this.LI_LoginTime + ", LI_LogoffTime=" + this.LI_LogoffTime + ")";
	}

}
