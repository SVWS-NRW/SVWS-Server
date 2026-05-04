package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationDatumConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerMerkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerMerkmale")
@JsonPropertyOrder({"id", "idSchueler", "kuerzelMerkmal", "SchulnrEigner", "datumVon", "datumBis"})
public final class MigrationDTOSchuelerMerkmale {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerMerkmale e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes kuerzelMerkmal */
	public static final String QUERY_BY_KUERZELMERKMAL = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.kuerzelMerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes kuerzelMerkmal */
	public static final String QUERY_LIST_BY_KUERZELMERKMAL = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.kuerzelMerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes datumVon */
	public static final String QUERY_BY_DATUMVON = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.datumVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes datumVon */
	public static final String QUERY_LIST_BY_DATUMVON = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.datumVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes datumBis */
	public static final String QUERY_BY_DATUMBIS = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.datumBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes datumBis */
	public static final String QUERY_LIST_BY_DATUMBIS = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.datumBis IN ?1";

	/** ID des Eintrag bei besondere Merkmale zum Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** Schüler-ID des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long idSchueler;

	/** Das Kürzel des Merkmals des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Kurztext")
	@JsonProperty
	public String kuerzelMerkmal;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Datum Beginn des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumVon")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String datumVon;

	/** Datum Ende des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumBis")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String datumBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idSchueler   der Wert für das Attribut idSchueler
	 */
	public MigrationDTOSchuelerMerkmale(final Long id, final Long idSchueler) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
		this.id = id;
		if (idSchueler == null) {
			throw new NullPointerException("idSchueler must not be null");
		}
		this.idSchueler = idSchueler;
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
		MigrationDTOSchuelerMerkmale other = (MigrationDTOSchuelerMerkmale) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerMerkmale(id=" + this.id + ", idSchueler=" + this.idSchueler + ", kuerzelMerkmal=" + this.kuerzelMerkmal + ", SchulnrEigner=" + this.SchulnrEigner + ", datumVon=" + this.datumVon + ", datumBis=" + this.datumBis + ")";
	}

}
