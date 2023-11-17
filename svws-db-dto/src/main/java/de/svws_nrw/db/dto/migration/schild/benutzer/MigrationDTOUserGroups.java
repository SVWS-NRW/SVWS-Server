package de.svws_nrw.db.dto.migration.schild.benutzer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Usergroups.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Usergroups")
@NamedQuery(name = "MigrationDTOUserGroups.all", query = "SELECT e FROM MigrationDTOUserGroups e")
@NamedQuery(name = "MigrationDTOUserGroups.ug_id", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_ID = :value")
@NamedQuery(name = "MigrationDTOUserGroups.ug_id.multiple", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_ID IN :value")
@NamedQuery(name = "MigrationDTOUserGroups.ug_bezeichnung", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOUserGroups.ug_bezeichnung.multiple", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOUserGroups.ug_kompetenzen", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_Kompetenzen = :value")
@NamedQuery(name = "MigrationDTOUserGroups.ug_kompetenzen.multiple", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_Kompetenzen IN :value")
@NamedQuery(name = "MigrationDTOUserGroups.ug_nr", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_Nr = :value")
@NamedQuery(name = "MigrationDTOUserGroups.ug_nr.multiple", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_Nr IN :value")
@NamedQuery(name = "MigrationDTOUserGroups.schulnreigner", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOUserGroups.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOUserGroups.primaryKeyQuery", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_ID = ?1")
@NamedQuery(name = "MigrationDTOUserGroups.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_ID IN :value")
@NamedQuery(name = "MigrationDTOUserGroups.all.migration", query = "SELECT e FROM MigrationDTOUserGroups e WHERE e.UG_ID IS NOT NULL")
@JsonPropertyOrder({"UG_ID", "UG_Bezeichnung", "UG_Kompetenzen", "UG_Nr", "SchulnrEigner"})
public final class MigrationDTOUserGroups {

	/** ID der Benutzergruppe */
	@Id
	@Column(name = "UG_ID")
	@JsonProperty
	public Long UG_ID;

	/** Bezeichnung der Benutzergruppe */
	@Column(name = "UG_Bezeichnung")
	@JsonProperty
	public String UG_Bezeichnung;

	/** Kompetenzen der Benutzergrupppe in vorgegebenen Zahlerwerten */
	@Column(name = "UG_Kompetenzen")
	@JsonProperty
	public String UG_Kompetenzen;

	/** Nummer der Benutzergruppe ??? */
	@Column(name = "UG_Nr")
	@JsonProperty
	public Integer UG_Nr;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOUserGroups ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOUserGroups() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOUserGroups ohne eine Initialisierung der Attribute.
	 * @param UG_ID   der Wert für das Attribut UG_ID
	 */
	public MigrationDTOUserGroups(final Long UG_ID) {
		if (UG_ID == null) {
			throw new NullPointerException("UG_ID must not be null");
		}
		this.UG_ID = UG_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOUserGroups other = (MigrationDTOUserGroups) obj;
		if (UG_ID == null) {
			if (other.UG_ID != null)
				return false;
		} else if (!UG_ID.equals(other.UG_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((UG_ID == null) ? 0 : UG_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOUserGroups(UG_ID=" + this.UG_ID + ", UG_Bezeichnung=" + this.UG_Bezeichnung + ", UG_Kompetenzen=" + this.UG_Kompetenzen + ", UG_Nr=" + this.UG_Nr + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
