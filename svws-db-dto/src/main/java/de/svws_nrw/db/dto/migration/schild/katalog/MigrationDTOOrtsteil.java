package de.svws_nrw.db.dto.migration.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ortsteil.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ortsteil")
@JsonPropertyOrder({"id", "ortsteil", "idOrt", "PLZ", "sortierung", "istSichtbar", "istAenderbar", "SchulnrEigner", "schluesselOrtsteil"})
public final class MigrationDTOOrtsteil {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOOrtsteil e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ortsteil */
	public static final String QUERY_BY_ORTSTEIL = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.ortsteil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ortsteil */
	public static final String QUERY_LIST_BY_ORTSTEIL = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.ortsteil IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idOrt */
	public static final String QUERY_BY_IDORT = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.idOrt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idOrt */
	public static final String QUERY_LIST_BY_IDORT = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.idOrt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PLZ */
	public static final String QUERY_BY_PLZ = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.PLZ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PLZ */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.PLZ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSichtbar */
	public static final String QUERY_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.istSichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSichtbar */
	public static final String QUERY_LIST_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.istSichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istAenderbar */
	public static final String QUERY_BY_ISTAENDERBAR = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.istAenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istAenderbar */
	public static final String QUERY_LIST_BY_ISTAENDERBAR = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.istAenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schluesselOrtsteil */
	public static final String QUERY_BY_SCHLUESSELORTSTEIL = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.schluesselOrtsteil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schluesselOrtsteil */
	public static final String QUERY_LIST_BY_SCHLUESSELORTSTEIL = "SELECT e FROM MigrationDTOOrtsteil e WHERE e.schluesselOrtsteil IN ?1";

	/** ID des Ortsteils */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** Bezeichnung des Ortsteils */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String ortsteil;

	/** Fremdschlüssel auf den Ort, dem der Ortsteil zugeordnet ist */
	@Column(name = "Ort_ID")
	@JsonProperty
	public Long idOrt;

	/** PLZ des Ortsteils */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Sortierung des Ortsteils */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** Sichbarkeit des Ortsteils */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSichtbar;

	/** Änderbarkeit des Ortsteils */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istAenderbar;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Schlüssel des Ortsteils (Regional?) */
	@Column(name = "OrtsteilSchluessel")
	@JsonProperty
	public String schluesselOrtsteil;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOOrtsteil ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOOrtsteil() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOOrtsteil ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param ortsteil   der Wert für das Attribut ortsteil
	 */
	public MigrationDTOOrtsteil(final Long id, final String ortsteil) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
		this.id = id;
		if (ortsteil == null) {
			throw new NullPointerException("ortsteil must not be null");
		}
		this.ortsteil = ortsteil;
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
		MigrationDTOOrtsteil other = (MigrationDTOOrtsteil) obj;
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
		return "MigrationDTOOrtsteil(id=" + this.id + ", ortsteil=" + this.ortsteil + ", idOrt=" + this.idOrt + ", PLZ=" + this.PLZ + ", sortierung=" + this.sortierung + ", istSichtbar=" + this.istSichtbar + ", istAenderbar=" + this.istAenderbar + ", SchulnrEigner=" + this.SchulnrEigner + ", schluesselOrtsteil=" + this.schluesselOrtsteil + ")";
	}

}
