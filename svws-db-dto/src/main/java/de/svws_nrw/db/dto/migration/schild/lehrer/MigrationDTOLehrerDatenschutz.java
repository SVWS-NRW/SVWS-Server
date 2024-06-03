package de.svws_nrw.db.dto.migration.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;


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

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOLehrerDatenschutzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerDatenschutz")
@JsonPropertyOrder({"LehrerID", "DatenschutzID", "Status", "Abgefragt"})
public final class MigrationDTOLehrerDatenschutz {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrerDatenschutz e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.LehrerID = ?1 AND e.DatenschutzID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.LehrerID IS NOT NULL AND e.DatenschutzID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrerID */
	public static final String QUERY_BY_LEHRERID = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.LehrerID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrerID */
	public static final String QUERY_LIST_BY_LEHRERID = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.LehrerID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatenschutzID */
	public static final String QUERY_BY_DATENSCHUTZID = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.DatenschutzID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatenschutzID */
	public static final String QUERY_LIST_BY_DATENSCHUTZID = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.DatenschutzID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Status */
	public static final String QUERY_BY_STATUS = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.Status = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Status */
	public static final String QUERY_LIST_BY_STATUS = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.Status IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abgefragt */
	public static final String QUERY_BY_ABGEFRAGT = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.Abgefragt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abgefragt */
	public static final String QUERY_LIST_BY_ABGEFRAGT = "SELECT e FROM MigrationDTOLehrerDatenschutz e WHERE e.Abgefragt IN ?1";

	/** LehrerID des Datenschutzeintrags */
	@Id
	@Column(name = "LehrerID")
	@JsonProperty
	public Long LehrerID;

	/** DatenschutzID des Eintrags */
	@Id
	@Column(name = "DatenschutzID")
	@JsonProperty
	public Long DatenschutzID;

	/** Status des Datenschutz-Eintrags (true/false) */
	@Column(name = "Status")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean Status;

	/** Status der Abfrage Datenschutz-Eintrags (true/false) */
	@Column(name = "Abgefragt")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean Abgefragt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerDatenschutz ohne eine Initialisierung der Attribute.
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 * @param DatenschutzID   der Wert für das Attribut DatenschutzID
	 * @param Status   der Wert für das Attribut Status
	 * @param Abgefragt   der Wert für das Attribut Abgefragt
	 */
	public MigrationDTOLehrerDatenschutz(final Long LehrerID, final Long DatenschutzID, final Boolean Status, final Boolean Abgefragt) {
		if (LehrerID == null) {
			throw new NullPointerException("LehrerID must not be null");
		}
		this.LehrerID = LehrerID;
		if (DatenschutzID == null) {
			throw new NullPointerException("DatenschutzID must not be null");
		}
		this.DatenschutzID = DatenschutzID;
		if (Status == null) {
			throw new NullPointerException("Status must not be null");
		}
		this.Status = Status;
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
		MigrationDTOLehrerDatenschutz other = (MigrationDTOLehrerDatenschutz) obj;
		if (LehrerID == null) {
			if (other.LehrerID != null)
				return false;
		} else if (!LehrerID.equals(other.LehrerID))
			return false;
		if (DatenschutzID == null) {
			if (other.DatenschutzID != null)
				return false;
		} else if (!DatenschutzID.equals(other.DatenschutzID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LehrerID == null) ? 0 : LehrerID.hashCode());

		result = prime * result + ((DatenschutzID == null) ? 0 : DatenschutzID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOLehrerDatenschutz(LehrerID=" + this.LehrerID + ", DatenschutzID=" + this.DatenschutzID + ", Status=" + this.Status + ", Abgefragt=" + this.Abgefragt + ")";
	}

}
