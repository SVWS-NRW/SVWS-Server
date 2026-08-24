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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Religion.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Religion")
@JsonPropertyOrder({"id", "bezeichnung", "schluesselReligion", "sortierung", "istSichtbar", "istAenderbar", "bezeichnungExport", "bezeichnungZeugnis", "SchulnrEigner"})
public final class MigrationDTOReligion {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOReligion e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOReligion e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOReligion e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOReligion e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOReligion e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOReligion e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOReligion e WHERE e.bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOReligion e WHERE e.bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schluesselReligion */
	public static final String QUERY_BY_SCHLUESSELRELIGION = "SELECT e FROM MigrationDTOReligion e WHERE e.schluesselReligion = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schluesselReligion */
	public static final String QUERY_LIST_BY_SCHLUESSELRELIGION = "SELECT e FROM MigrationDTOReligion e WHERE e.schluesselReligion IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOReligion e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOReligion e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSichtbar */
	public static final String QUERY_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOReligion e WHERE e.istSichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSichtbar */
	public static final String QUERY_LIST_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOReligion e WHERE e.istSichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istAenderbar */
	public static final String QUERY_BY_ISTAENDERBAR = "SELECT e FROM MigrationDTOReligion e WHERE e.istAenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istAenderbar */
	public static final String QUERY_LIST_BY_ISTAENDERBAR = "SELECT e FROM MigrationDTOReligion e WHERE e.istAenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnungExport */
	public static final String QUERY_BY_BEZEICHNUNGEXPORT = "SELECT e FROM MigrationDTOReligion e WHERE e.bezeichnungExport = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnungExport */
	public static final String QUERY_LIST_BY_BEZEICHNUNGEXPORT = "SELECT e FROM MigrationDTOReligion e WHERE e.bezeichnungExport IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnungZeugnis */
	public static final String QUERY_BY_BEZEICHNUNGZEUGNIS = "SELECT e FROM MigrationDTOReligion e WHERE e.bezeichnungZeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnungZeugnis */
	public static final String QUERY_LIST_BY_BEZEICHNUNGZEUGNIS = "SELECT e FROM MigrationDTOReligion e WHERE e.bezeichnungZeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOReligion e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOReligion e WHERE e.SchulnrEigner IN ?1";

	/** ID der Religion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** Bezeichnung der Religion */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String bezeichnung;

	/** Statistikkürzel der Religion */
	@Column(name = "StatistikKrz")
	@JsonProperty
	public String schluesselReligion;

	/** Sortierung der Religion */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** Sichbarkeit der Religion */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSichtbar;

	/** Änderbarkeit der Religion */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istAenderbar;

	/** Exportbezeichnung der Religion */
	@Column(name = "ExportBez")
	@JsonProperty
	public String bezeichnungExport;

	/** Zeugnisbezeichnung der Religion */
	@Column(name = "ZeugnisBezeichnung")
	@JsonProperty
	public String bezeichnungZeugnis;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOReligion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOReligion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOReligion ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param bezeichnung   der Wert für das Attribut bezeichnung
	 */
	public MigrationDTOReligion(final Long id, final String bezeichnung) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
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
		MigrationDTOReligion other = (MigrationDTOReligion) obj;
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
		return "MigrationDTOReligion(id=" + this.id + ", bezeichnung=" + this.bezeichnung + ", schluesselReligion=" + this.schluesselReligion + ", sortierung=" + this.sortierung + ", istSichtbar=" + this.istSichtbar + ", istAenderbar=" + this.istAenderbar + ", bezeichnungExport=" + this.bezeichnungExport + ", bezeichnungZeugnis=" + this.bezeichnungZeugnis + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
