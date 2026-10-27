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
@JsonPropertyOrder({"id", "bezeichnung", "benutzernameSuffixLehrer", "benutzernameSuffixErzieher", "benutzernameSuffixSchueler", "konfiguration"})
public final class DTOLernplattformen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLernplattformen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLernplattformen e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLernplattformen e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLernplattformen e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLernplattformen e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLernplattformen e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOLernplattformen e WHERE e.bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOLernplattformen e WHERE e.bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes benutzernameSuffixLehrer */
	public static final String QUERY_BY_BENUTZERNAMESUFFIXLEHRER = "SELECT e FROM DTOLernplattformen e WHERE e.benutzernameSuffixLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes benutzernameSuffixLehrer */
	public static final String QUERY_LIST_BY_BENUTZERNAMESUFFIXLEHRER = "SELECT e FROM DTOLernplattformen e WHERE e.benutzernameSuffixLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes benutzernameSuffixErzieher */
	public static final String QUERY_BY_BENUTZERNAMESUFFIXERZIEHER = "SELECT e FROM DTOLernplattformen e WHERE e.benutzernameSuffixErzieher = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes benutzernameSuffixErzieher */
	public static final String QUERY_LIST_BY_BENUTZERNAMESUFFIXERZIEHER = "SELECT e FROM DTOLernplattformen e WHERE e.benutzernameSuffixErzieher IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes benutzernameSuffixSchueler */
	public static final String QUERY_BY_BENUTZERNAMESUFFIXSCHUELER = "SELECT e FROM DTOLernplattformen e WHERE e.benutzernameSuffixSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes benutzernameSuffixSchueler */
	public static final String QUERY_LIST_BY_BENUTZERNAMESUFFIXSCHUELER = "SELECT e FROM DTOLernplattformen e WHERE e.benutzernameSuffixSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes konfiguration */
	public static final String QUERY_BY_KONFIGURATION = "SELECT e FROM DTOLernplattformen e WHERE e.konfiguration = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes konfiguration */
	public static final String QUERY_LIST_BY_KONFIGURATION = "SELECT e FROM DTOLernplattformen e WHERE e.konfiguration IN ?1";

	/** ID des Datensatzes für die verwendete Lernplattform */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** Bezeichnung der Lernplattform */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String bezeichnung;

	/** Suffix für den Benutzernamen bei den Lehrern */
	@Column(name = "BenutzernameSuffixLehrer")
	@JsonProperty
	public String benutzernameSuffixLehrer;

	/** Suffix für den Benutzernamen bei den Erziehern */
	@Column(name = "BenutzernameSuffixErzieher")
	@JsonProperty
	public String benutzernameSuffixErzieher;

	/** Suffix für den Benutzernamen bei den Schülern */
	@Column(name = "BenutzernameSuffixSchueler")
	@JsonProperty
	public String benutzernameSuffixSchueler;

	/** Json-Objekt mit den Konfigurationseinstellungen der Accounterstellung für die Lernplattform */
	@Column(name = "Konfiguration")
	@JsonProperty
	public String konfiguration;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLernplattformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLernplattformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLernplattformen ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param bezeichnung   der Wert für das Attribut bezeichnung
	 */
	public DTOLernplattformen(final long id, final String bezeichnung) {
		this.id = id;
		if (bezeichnung == null) {
			throw new NullPointerException("bezeichnung must not be null");
		}
		this.bezeichnung = bezeichnung;
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
		DTOLernplattformen other = (DTOLernplattformen) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLernplattformen(id=" + this.id + ", bezeichnung=" + this.bezeichnung + ", benutzernameSuffixLehrer=" + this.benutzernameSuffixLehrer + ", benutzernameSuffixErzieher=" + this.benutzernameSuffixErzieher + ", benutzernameSuffixSchueler=" + this.benutzernameSuffixSchueler + ", konfiguration=" + this.konfiguration + ")";
	}

}
