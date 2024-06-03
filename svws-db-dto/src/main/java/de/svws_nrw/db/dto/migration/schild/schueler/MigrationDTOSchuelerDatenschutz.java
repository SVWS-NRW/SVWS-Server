package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOSchuelerDatenschutzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerDatenschutz")
@JsonPropertyOrder({"Schueler_ID", "Datenschutz_ID", "Status", "SchulnrEigner", "Abgefragt"})
public final class MigrationDTOSchuelerDatenschutz {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerDatenschutz e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Schueler_ID = ?1 AND e.Datenschutz_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Schueler_ID IS NOT NULL AND e.Datenschutz_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Datenschutz_ID */
	public static final String QUERY_BY_DATENSCHUTZ_ID = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Datenschutz_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Datenschutz_ID */
	public static final String QUERY_LIST_BY_DATENSCHUTZ_ID = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Datenschutz_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Status */
	public static final String QUERY_BY_STATUS = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Status = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Status */
	public static final String QUERY_LIST_BY_STATUS = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Status IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abgefragt */
	public static final String QUERY_BY_ABGEFRAGT = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Abgefragt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abgefragt */
	public static final String QUERY_LIST_BY_ABGEFRAGT = "SELECT e FROM MigrationDTOSchuelerDatenschutz e WHERE e.Abgefragt IN ?1";

	/** Fremdschlüssel auf Tabelle Schueler */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Fremdschlüssel auf den Katalog der DSGVO-Merkmale */
	@Id
	@Column(name = "Datenschutz_ID")
	@JsonProperty
	public Long Datenschutz_ID;

	/** Gibt an ob eine Zustimmung zum Merkmal vorliegt. */
	@Column(name = "Status")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Status;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Status der Abfrage Datenschutz-Eintrags (true/false) */
	@Column(name = "Abgefragt")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean Abgefragt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerDatenschutz ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Datenschutz_ID   der Wert für das Attribut Datenschutz_ID
	 * @param Status   der Wert für das Attribut Status
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Abgefragt   der Wert für das Attribut Abgefragt
	 */
	public MigrationDTOSchuelerDatenschutz(final Long Schueler_ID, final Long Datenschutz_ID, final Boolean Status, final Integer SchulnrEigner, final Boolean Abgefragt) {
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Datenschutz_ID == null) {
			throw new NullPointerException("Datenschutz_ID must not be null");
		}
		this.Datenschutz_ID = Datenschutz_ID;
		if (Status == null) {
			throw new NullPointerException("Status must not be null");
		}
		this.Status = Status;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Abgefragt == null) {
			throw new NullPointerException("Abgefragt must not be null");
		}
		this.Abgefragt = Abgefragt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerDatenschutz other = (MigrationDTOSchuelerDatenschutz) obj;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;
		if (Datenschutz_ID == null) {
			if (other.Datenschutz_ID != null)
				return false;
		} else if (!Datenschutz_ID.equals(other.Datenschutz_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());

		result = prime * result + ((Datenschutz_ID == null) ? 0 : Datenschutz_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerDatenschutz(Schueler_ID=" + this.Schueler_ID + ", Datenschutz_ID=" + this.Datenschutz_ID + ", Status=" + this.Status + ", SchulnrEigner=" + this.SchulnrEigner + ", Abgefragt=" + this.Abgefragt + ")";
	}

}
