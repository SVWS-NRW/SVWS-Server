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
 * Diese Klasse dient als DTO für die Datenbanktabelle Lernplattformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Lernplattformen")
@JsonPropertyOrder({"ID", "Bezeichnung", "BenutzernameSuffixLehrer", "BenutzernameSuffixErzieher", "BenutzernameSuffixSchueler", "Konfiguration"})
public final class DTOLernplattformen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLernplattformen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLernplattformen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLernplattformen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLernplattformen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLernplattformen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLernplattformen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOLernplattformen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOLernplattformen e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BenutzernameSuffixLehrer */
	public static final String QUERY_BY_BENUTZERNAMESUFFIXLEHRER = "SELECT e FROM DTOLernplattformen e WHERE e.BenutzernameSuffixLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BenutzernameSuffixLehrer */
	public static final String QUERY_LIST_BY_BENUTZERNAMESUFFIXLEHRER = "SELECT e FROM DTOLernplattformen e WHERE e.BenutzernameSuffixLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BenutzernameSuffixErzieher */
	public static final String QUERY_BY_BENUTZERNAMESUFFIXERZIEHER = "SELECT e FROM DTOLernplattformen e WHERE e.BenutzernameSuffixErzieher = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BenutzernameSuffixErzieher */
	public static final String QUERY_LIST_BY_BENUTZERNAMESUFFIXERZIEHER = "SELECT e FROM DTOLernplattformen e WHERE e.BenutzernameSuffixErzieher IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BenutzernameSuffixSchueler */
	public static final String QUERY_BY_BENUTZERNAMESUFFIXSCHUELER = "SELECT e FROM DTOLernplattformen e WHERE e.BenutzernameSuffixSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BenutzernameSuffixSchueler */
	public static final String QUERY_LIST_BY_BENUTZERNAMESUFFIXSCHUELER = "SELECT e FROM DTOLernplattformen e WHERE e.BenutzernameSuffixSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Konfiguration */
	public static final String QUERY_BY_KONFIGURATION = "SELECT e FROM DTOLernplattformen e WHERE e.Konfiguration = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Konfiguration */
	public static final String QUERY_LIST_BY_KONFIGURATION = "SELECT e FROM DTOLernplattformen e WHERE e.Konfiguration IN ?1";

	/** ID des Datensatzes für die verwendete Lernplattform */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung der Lernplattform */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Suffix für den Benutzernamen bei den Lehrern */
	@Column(name = "BenutzernameSuffixLehrer")
	@JsonProperty
	public String BenutzernameSuffixLehrer;

	/** Suffix für den Benutzernamen bei den Erziehern */
	@Column(name = "BenutzernameSuffixErzieher")
	@JsonProperty
	public String BenutzernameSuffixErzieher;

	/** Suffix für den Benutzernamen bei den Schülern */
	@Column(name = "BenutzernameSuffixSchueler")
	@JsonProperty
	public String BenutzernameSuffixSchueler;

	/** Json-Objekt mit den Konfigurationseinstellungen der Accounterstellung für die Lernplattform */
	@Column(name = "Konfiguration")
	@JsonProperty
	public String Konfiguration;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLernplattformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLernplattformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLernplattformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOLernplattformen(final long ID, final String Bezeichnung) {
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLernplattformen other = (DTOLernplattformen) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLernplattformen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", BenutzernameSuffixLehrer=" + this.BenutzernameSuffixLehrer + ", BenutzernameSuffixErzieher=" + this.BenutzernameSuffixErzieher + ", BenutzernameSuffixSchueler=" + this.BenutzernameSuffixSchueler + ", Konfiguration=" + this.Konfiguration + ")";
	}

}
