package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchulwechselDokument.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchulwechselDokument")
@JsonPropertyOrder({"id", "fileName", "xmlDocument", "createdAt", "lastModified"})
public final class DTOSchulwechselDokument {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchulwechselDokument e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchulwechselDokument e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchulwechselDokument e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchulwechselDokument e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchulwechselDokument e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchulwechselDokument e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes fileName */
	public static final String QUERY_BY_FILENAME = "SELECT e FROM DTOSchulwechselDokument e WHERE e.fileName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes fileName */
	public static final String QUERY_LIST_BY_FILENAME = "SELECT e FROM DTOSchulwechselDokument e WHERE e.fileName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes xmlDocument */
	public static final String QUERY_BY_XMLDOCUMENT = "SELECT e FROM DTOSchulwechselDokument e WHERE e.xmlDocument = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes xmlDocument */
	public static final String QUERY_LIST_BY_XMLDOCUMENT = "SELECT e FROM DTOSchulwechselDokument e WHERE e.xmlDocument IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes createdAt */
	public static final String QUERY_BY_CREATEDAT = "SELECT e FROM DTOSchulwechselDokument e WHERE e.createdAt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes createdAt */
	public static final String QUERY_LIST_BY_CREATEDAT = "SELECT e FROM DTOSchulwechselDokument e WHERE e.createdAt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes lastModified */
	public static final String QUERY_BY_LASTMODIFIED = "SELECT e FROM DTOSchulwechselDokument e WHERE e.lastModified = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes lastModified */
	public static final String QUERY_LIST_BY_LASTMODIFIED = "SELECT e FROM DTOSchulwechselDokument e WHERE e.lastModified IN ?1";

	/** Die Id des Dokuments */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Der Dateiname des Dokuments */
	@Column(name = "file_name")
	@JsonProperty
	public String fileName;

	/** Das Dokument selbst */
	@Column(name = "xml_document")
	@JsonProperty
	public String xmlDocument;

	/** Der Zeitpunkt der Erstellung des Dokuments (UTC) */
	@Column(name = "created_at")
	@JsonProperty
	public String createdAt;

	/** Der Zeitpunkt der letzen Änderung des Dokuments (UTC) */
	@Column(name = "last_modified")
	@JsonProperty
	public String lastModified;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulwechselDokument ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulwechselDokument() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulwechselDokument ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param fileName   der Wert für das Attribut fileName
	 * @param xmlDocument   der Wert für das Attribut xmlDocument
	 * @param createdAt   der Wert für das Attribut createdAt
	 * @param lastModified   der Wert für das Attribut lastModified
	 */
	public DTOSchulwechselDokument(final long id, final String fileName, final String xmlDocument, final String createdAt, final String lastModified) {
		this.id = id;
		if (fileName == null) {
			throw new NullPointerException("fileName must not be null");
		}
		this.fileName = fileName;
		if (xmlDocument == null) {
			throw new NullPointerException("xmlDocument must not be null");
		}
		this.xmlDocument = xmlDocument;
		if (createdAt == null) {
			throw new NullPointerException("createdAt must not be null");
		}
		this.createdAt = createdAt;
		if (lastModified == null) {
			throw new NullPointerException("lastModified must not be null");
		}
		this.lastModified = lastModified;
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
		DTOSchulwechselDokument other = (DTOSchulwechselDokument) obj;
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
		return "DTOSchulwechselDokument(id=" + this.id + ", fileName=" + this.fileName + ", xmlDocument=" + this.xmlDocument + ", createdAt=" + this.createdAt + ", lastModified=" + this.lastModified + ")";
	}

}
