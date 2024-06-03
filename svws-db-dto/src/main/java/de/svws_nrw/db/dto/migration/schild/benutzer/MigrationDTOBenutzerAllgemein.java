package de.svws_nrw.db.dto.migration.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzerAllgemein.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzerAllgemein")
@JsonPropertyOrder({"ID", "AnzeigeName", "CredentialID"})
public final class MigrationDTOBenutzerAllgemein {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOBenutzerAllgemein e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzeigeName */
	public static final String QUERY_BY_ANZEIGENAME = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.AnzeigeName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzeigeName */
	public static final String QUERY_LIST_BY_ANZEIGENAME = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.AnzeigeName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes CredentialID */
	public static final String QUERY_BY_CREDENTIALID = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.CredentialID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes CredentialID */
	public static final String QUERY_LIST_BY_CREDENTIALID = "SELECT e FROM MigrationDTOBenutzerAllgemein e WHERE e.CredentialID IN ?1";

	/** Die ID des allgemeinen Benutzers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Anzeigename für den allgemeinen Benutzer */
	@Column(name = "AnzeigeName")
	@JsonProperty
	public String AnzeigeName;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzerAllgemein ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBenutzerAllgemein() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzerAllgemein ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOBenutzerAllgemein(final Long ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBenutzerAllgemein other = (MigrationDTOBenutzerAllgemein) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOBenutzerAllgemein(ID=" + this.ID + ", AnzeigeName=" + this.AnzeigeName + ", CredentialID=" + this.CredentialID + ")";
	}

}
