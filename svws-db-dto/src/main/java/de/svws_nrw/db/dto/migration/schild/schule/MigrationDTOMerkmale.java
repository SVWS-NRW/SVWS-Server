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
@JsonPropertyOrder({"ID", "Schule", "Schueler", "Kurztext", "Langtext", "SchulnrEigner"})
public final class MigrationDTOMerkmale {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOMerkmale e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOMerkmale e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOMerkmale e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOMerkmale e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOMerkmale e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOMerkmale e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schule */
	public static final String QUERY_BY_SCHULE = "SELECT e FROM MigrationDTOMerkmale e WHERE e.Schule = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schule */
	public static final String QUERY_LIST_BY_SCHULE = "SELECT e FROM MigrationDTOMerkmale e WHERE e.Schule IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler */
	public static final String QUERY_BY_SCHUELER = "SELECT e FROM MigrationDTOMerkmale e WHERE e.Schueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler */
	public static final String QUERY_LIST_BY_SCHUELER = "SELECT e FROM MigrationDTOMerkmale e WHERE e.Schueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurztext */
	public static final String QUERY_BY_KURZTEXT = "SELECT e FROM MigrationDTOMerkmale e WHERE e.Kurztext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurztext */
	public static final String QUERY_LIST_BY_KURZTEXT = "SELECT e FROM MigrationDTOMerkmale e WHERE e.Kurztext IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Langtext */
	public static final String QUERY_BY_LANGTEXT = "SELECT e FROM MigrationDTOMerkmale e WHERE e.Langtext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Langtext */
	public static final String QUERY_LIST_BY_LANGTEXT = "SELECT e FROM MigrationDTOMerkmale e WHERE e.Langtext IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOMerkmale e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOMerkmale e WHERE e.SchulnrEigner IN ?1";

	/** ID des Merkmals das an der Schule vorhanden ist */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Merkmal kann der Schule zugewiesen werden */
	@Column(name = "Schule")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Schule;

	/** Merkmal kann auch einem einzelnen Schüler auf Individualdaten II zugewiesen werden */
	@Column(name = "Schueler")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Schueler;

	/** Kurztext des Merkmals zB OGS */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Langtext des Merkmal zB offener Ganztag */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

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
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOMerkmale(final Long ID) {
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
		MigrationDTOMerkmale other = (MigrationDTOMerkmale) obj;
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
		return "MigrationDTOMerkmale(ID=" + this.ID + ", Schule=" + this.Schule + ", Schueler=" + this.Schueler + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
