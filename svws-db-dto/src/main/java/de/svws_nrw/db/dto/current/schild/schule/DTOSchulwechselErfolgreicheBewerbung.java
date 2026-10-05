package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.StatusSchulwechselErfolgreicheBewerbungConverter;

import de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung;


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
import de.svws_nrw.csv.converter.current.StatusSchulwechselErfolgreicheBewerbungConverterSerializer;
import de.svws_nrw.csv.converter.current.StatusSchulwechselErfolgreicheBewerbungConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchulwechselErfolgreicheBewerbung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchulwechselErfolgreicheBewerbung")
@JsonPropertyOrder({"id", "idSchueler", "status", "lastModified", "idDocument", "idSchulkindSchulbewerbung"})
public final class DTOSchulwechselErfolgreicheBewerbung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes status */
	public static final String QUERY_BY_STATUS = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.status = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes status */
	public static final String QUERY_LIST_BY_STATUS = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.status IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes lastModified */
	public static final String QUERY_BY_LASTMODIFIED = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.lastModified = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes lastModified */
	public static final String QUERY_LIST_BY_LASTMODIFIED = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.lastModified IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idDocument */
	public static final String QUERY_BY_IDDOCUMENT = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.idDocument = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idDocument */
	public static final String QUERY_LIST_BY_IDDOCUMENT = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.idDocument IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchulkindSchulbewerbung */
	public static final String QUERY_BY_IDSCHULKINDSCHULBEWERBUNG = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.idSchulkindSchulbewerbung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchulkindSchulbewerbung */
	public static final String QUERY_LIST_BY_IDSCHULKINDSCHULBEWERBUNG = "SELECT e FROM DTOSchulwechselErfolgreicheBewerbung e WHERE e.idSchulkindSchulbewerbung IN ?1";

	/** Die Id des Wechselvorgangs */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Die eindeutige Id des Schülers – verweist auf den Schüler */
	@Column(name = "id_schueler")
	@JsonProperty
	public Long idSchueler;

	/** Der Status des Wechselvorgangs */
	@Column(name = "status")
	@JsonProperty
	@Convert(converter = StatusSchulwechselErfolgreicheBewerbungConverter.class)
	@JsonSerialize(using = StatusSchulwechselErfolgreicheBewerbungConverterSerializer.class)
	@JsonDeserialize(using = StatusSchulwechselErfolgreicheBewerbungConverterDeserializer.class)
	public StatusSchulwechselErfolgreicheBewerbung status;

	/** Der Zeitpunkt der letzen Statusänderung (UTC) */
	@Column(name = "last_modified")
	@JsonProperty
	public String lastModified;

	/** Die eindeutige Id des Dokuments – verweist auf das Schulwechsel-Dokument */
	@Column(name = "id_document")
	@JsonProperty
	public long idDocument;

	/** Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang */
	@Column(name = "id_schulkind_schulbewerbung")
	@JsonProperty
	public String idSchulkindSchulbewerbung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulwechselErfolgreicheBewerbung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulwechselErfolgreicheBewerbung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulwechselErfolgreicheBewerbung ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param status   der Wert für das Attribut status
	 * @param lastModified   der Wert für das Attribut lastModified
	 * @param idDocument   der Wert für das Attribut idDocument
	 * @param idSchulkindSchulbewerbung   der Wert für das Attribut idSchulkindSchulbewerbung
	 */
	public DTOSchulwechselErfolgreicheBewerbung(final long id, final StatusSchulwechselErfolgreicheBewerbung status, final String lastModified, final long idDocument, final String idSchulkindSchulbewerbung) {
		this.id = id;
		if (status == null) {
			throw new NullPointerException("status must not be null");
		}
		this.status = status;
		if (lastModified == null) {
			throw new NullPointerException("lastModified must not be null");
		}
		this.lastModified = lastModified;
		this.idDocument = idDocument;
		if (idSchulkindSchulbewerbung == null) {
			throw new NullPointerException("idSchulkindSchulbewerbung must not be null");
		}
		this.idSchulkindSchulbewerbung = idSchulkindSchulbewerbung;
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
		DTOSchulwechselErfolgreicheBewerbung other = (DTOSchulwechselErfolgreicheBewerbung) obj;
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
		return "DTOSchulwechselErfolgreicheBewerbung(id=" + this.id + ", idSchueler=" + this.idSchueler + ", status=" + this.status + ", lastModified=" + this.lastModified + ", idDocument=" + this.idDocument + ", idSchulkindSchulbewerbung=" + this.idSchulkindSchulbewerbung + ")";
	}

}
