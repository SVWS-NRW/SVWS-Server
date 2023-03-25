package de.svws_nrw.db.dto.migration.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Logins.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOProtokollLoginPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Logins")
@NamedQuery(name="MigrationDTOProtokollLogin.all", query="SELECT e FROM MigrationDTOProtokollLogin e")
@NamedQuery(name="MigrationDTOProtokollLogin.li_userid", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.LI_UserID = :value")
@NamedQuery(name="MigrationDTOProtokollLogin.li_userid.multiple", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.LI_UserID IN :value")
@NamedQuery(name="MigrationDTOProtokollLogin.li_logintime", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.LI_LoginTime = :value")
@NamedQuery(name="MigrationDTOProtokollLogin.li_logintime.multiple", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.LI_LoginTime IN :value")
@NamedQuery(name="MigrationDTOProtokollLogin.li_logofftime", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.LI_LogoffTime = :value")
@NamedQuery(name="MigrationDTOProtokollLogin.li_logofftime.multiple", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.LI_LogoffTime IN :value")
@NamedQuery(name="MigrationDTOProtokollLogin.schulnreigner", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOProtokollLogin.schulnreigner.multiple", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOProtokollLogin.primaryKeyQuery", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.LI_UserID = ?1 AND e.LI_LoginTime = ?2")
@NamedQuery(name="MigrationDTOProtokollLogin.all.migration", query="SELECT e FROM MigrationDTOProtokollLogin e WHERE e.LI_UserID IS NOT NULL AND e.LI_LoginTime IS NOT NULL")
@JsonPropertyOrder({"LI_UserID","LI_LoginTime","LI_LogoffTime","SchulnrEigner"})
public class MigrationDTOProtokollLogin {

	/** UserID des Logins */
	@Id
	@Column(name = "LI_UserID")
	@JsonProperty
	public Long LI_UserID;

	/** Login Zeit */
	@Id
	@Column(name = "LI_LoginTime")
	@JsonProperty
	public String LI_LoginTime;

	/** Logoff Zeit */
	@Column(name = "LI_LogoffTime")
	@JsonProperty
	public String LI_LogoffTime;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOProtokollLogin ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOProtokollLogin() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOProtokollLogin ohne eine Initialisierung der Attribute.
	 * @param LI_UserID   der Wert für das Attribut LI_UserID
	 */
	public MigrationDTOProtokollLogin(final Long LI_UserID) {
		if (LI_UserID == null) { 
			throw new NullPointerException("LI_UserID must not be null");
		}
		this.LI_UserID = LI_UserID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOProtokollLogin other = (MigrationDTOProtokollLogin) obj;
		if (LI_UserID == null) {
			if (other.LI_UserID != null)
				return false;
		} else if (!LI_UserID.equals(other.LI_UserID))
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
		result = prime * result + ((LI_UserID == null) ? 0 : LI_UserID.hashCode());

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
		return "MigrationDTOProtokollLogin(LI_UserID=" + this.LI_UserID + ", LI_LoginTime=" + this.LI_LoginTime + ", LI_LogoffTime=" + this.LI_LogoffTime + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}