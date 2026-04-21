package de.svws_nrw.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Merkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Merkmale")
@JsonPropertyOrder({"id", "istSchulmerkmal", "istSchuelermerkmal", "kuerzel", "bezeichnung", "SchulnrEigner"})
public final class MigrationDTOMerkmale {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOMerkmale e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOMerkmale e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOMerkmale e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOMerkmale e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOMerkmale e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOMerkmale e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSchulmerkmal */
	public static final String QUERY_BY_ISTSCHULMERKMAL = "SELECT e FROM MigrationDTOMerkmale e WHERE e.istSchulmerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSchulmerkmal */
	public static final String QUERY_LIST_BY_ISTSCHULMERKMAL = "SELECT e FROM MigrationDTOMerkmale e WHERE e.istSchulmerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSchuelermerkmal */
	public static final String QUERY_BY_ISTSCHUELERMERKMAL = "SELECT e FROM MigrationDTOMerkmale e WHERE e.istSchuelermerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSchuelermerkmal */
	public static final String QUERY_LIST_BY_ISTSCHUELERMERKMAL = "SELECT e FROM MigrationDTOMerkmale e WHERE e.istSchuelermerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM MigrationDTOMerkmale e WHERE e.kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM MigrationDTOMerkmale e WHERE e.kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOMerkmale e WHERE e.bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOMerkmale e WHERE e.bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOMerkmale e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOMerkmale e WHERE e.SchulnrEigner IN ?1";

	/** ID des Merkmals das an der Schule vorhanden ist */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** Merkmal kann der Schule zugewiesen werden */
	@Column(name = "Schule")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSchulmerkmal;

	/** Merkmal kann auch einem einzelnen Schüler auf Individualdaten II zugewiesen werden */
	@Column(name = "Schueler")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSchuelermerkmal;

	/** Kurztext des Merkmals zB OGS */
	@Column(name = "Kurztext")
	@JsonProperty
	public String kuerzel;

	/** Langtext des Merkmal zB offener Ganztag */
	@Column(name = "Langtext")
	@JsonProperty
	public String bezeichnung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOMerkmale ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 */
	public MigrationDTOMerkmale(final Long id) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
		this.id = id;
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
		MigrationDTOMerkmale other = (MigrationDTOMerkmale) obj;
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
		return "MigrationDTOMerkmale(id=" + this.id + ", istSchulmerkmal=" + this.istSchulmerkmal + ", istSchuelermerkmal=" + this.istSchuelermerkmal + ", kuerzel=" + this.kuerzel + ", bezeichnung=" + this.bezeichnung + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
