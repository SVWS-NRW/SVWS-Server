package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.StatusSchulwechselAbgangConverter;

import de.svws_nrw.core.types.schule.StatusSchulwechselAbgang;


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
import de.svws_nrw.csv.converter.current.StatusSchulwechselAbgangConverterSerializer;
import de.svws_nrw.csv.converter.current.StatusSchulwechselAbgangConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchulwechselAbgang.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchulwechselAbgang")
@JsonPropertyOrder({"id", "idSchueler", "status", "lastModified", "idDocument", "idSchulkindSchulbewerbung"})
public final class DTOSchulwechselAbgang {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchulwechselAbgang e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes status */
	public static final String QUERY_BY_STATUS = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.status = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes status */
	public static final String QUERY_LIST_BY_STATUS = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.status IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes lastModified */
	public static final String QUERY_BY_LASTMODIFIED = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.lastModified = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes lastModified */
	public static final String QUERY_LIST_BY_LASTMODIFIED = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.lastModified IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idDocument */
	public static final String QUERY_BY_IDDOCUMENT = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.idDocument = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idDocument */
	public static final String QUERY_LIST_BY_IDDOCUMENT = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.idDocument IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchulkindSchulbewerbung */
	public static final String QUERY_BY_IDSCHULKINDSCHULBEWERBUNG = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.idSchulkindSchulbewerbung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchulkindSchulbewerbung */
	public static final String QUERY_LIST_BY_IDSCHULKINDSCHULBEWERBUNG = "SELECT e FROM DTOSchulwechselAbgang e WHERE e.idSchulkindSchulbewerbung IN ?1";

	/** Die Id des Wechselvorgangs */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Die eindeutige Id des Schülers – verweist auf den Schüler */
	@Column(name = "id_schueler")
	@JsonProperty
	public long idSchueler;

	/** Der Status des Wechselvorgangs */
	@Column(name = "status")
	@JsonProperty
	@Convert(converter = StatusSchulwechselAbgangConverter.class)
	@JsonSerialize(using = StatusSchulwechselAbgangConverterSerializer.class)
	@JsonDeserialize(using = StatusSchulwechselAbgangConverterDeserializer.class)
	public StatusSchulwechselAbgang status;

	/** Der Zeitpunkt der letzen Statusänderung (UTC) */
	@Column(name = "last_modified")
	@JsonProperty
	public String lastModified;

	/** Die eindeutige Id des Dokuments – verweist auf das Schulwechsel-Dokument */
	@Column(name = "id_document")
	@JsonProperty
	public Long idDocument;

	/** Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang */
	@Column(name = "id_schulkind_schulbewerbung")
	@JsonProperty
	public String idSchulkindSchulbewerbung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulwechselAbgang ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulwechselAbgang() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulwechselAbgang ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idSchueler   der Wert für das Attribut idSchueler
	 * @param status   der Wert für das Attribut status
	 * @param lastModified   der Wert für das Attribut lastModified
	 */
	public DTOSchulwechselAbgang(final long id, final long idSchueler, final StatusSchulwechselAbgang status, final String lastModified) {
		this.id = id;
		this.idSchueler = idSchueler;
		if (status == null) {
			throw new NullPointerException("status must not be null");
		}
		this.status = status;
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
		DTOSchulwechselAbgang other = (DTOSchulwechselAbgang) obj;
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
		return "DTOSchulwechselAbgang(id=" + this.id + ", idSchueler=" + this.idSchueler + ", status=" + this.status + ", lastModified=" + this.lastModified + ", idDocument=" + this.idDocument + ", idSchulkindSchulbewerbung=" + this.idSchulkindSchulbewerbung + ")";
	}

}
