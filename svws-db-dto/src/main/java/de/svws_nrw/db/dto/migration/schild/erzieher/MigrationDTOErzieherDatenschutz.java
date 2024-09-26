package de.svws_nrw.db.dto.migration.schild.erzieher;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle ErzieherDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOErzieherDatenschutzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ErzieherDatenschutz")
@JsonPropertyOrder({"ErzieherID", "DatenschutzID", "Status", "SchulnrEigner"})
public final class MigrationDTOErzieherDatenschutz {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOErzieherDatenschutz e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.ErzieherID = ?1 AND e.DatenschutzID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.ErzieherID IS NOT NULL AND e.DatenschutzID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzieherID */
	public static final String QUERY_BY_ERZIEHERID = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.ErzieherID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzieherID */
	public static final String QUERY_LIST_BY_ERZIEHERID = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.ErzieherID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatenschutzID */
	public static final String QUERY_BY_DATENSCHUTZID = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.DatenschutzID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatenschutzID */
	public static final String QUERY_LIST_BY_DATENSCHUTZID = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.DatenschutzID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Status */
	public static final String QUERY_BY_STATUS = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.Status = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Status */
	public static final String QUERY_LIST_BY_STATUS = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.Status IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.SchulnrEigner IN ?1";

	/** ErzieherID des Datenschutzeintrags */
	@Id
	@Column(name = "ErzieherID")
	@JsonProperty
	public Long ErzieherID;

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

	/** DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOErzieherDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOErzieherDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOErzieherDatenschutz ohne eine Initialisierung der Attribute.
	 * @param ErzieherID   der Wert für das Attribut ErzieherID
	 * @param DatenschutzID   der Wert für das Attribut DatenschutzID
	 * @param Status   der Wert für das Attribut Status
	 */
	public MigrationDTOErzieherDatenschutz(final Long ErzieherID, final Long DatenschutzID, final Boolean Status) {
		if (ErzieherID == null) {
			throw new NullPointerException("ErzieherID must not be null");
		}
		this.ErzieherID = ErzieherID;
		if (DatenschutzID == null) {
			throw new NullPointerException("DatenschutzID must not be null");
		}
		this.DatenschutzID = DatenschutzID;
		if (Status == null) {
			throw new NullPointerException("Status must not be null");
		}
		this.Status = Status;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOErzieherDatenschutz other = (MigrationDTOErzieherDatenschutz) obj;
		if (ErzieherID == null) {
			if (other.ErzieherID != null)
				return false;
		} else if (!ErzieherID.equals(other.ErzieherID))
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
		result = prime * result + ((ErzieherID == null) ? 0 : ErzieherID.hashCode());

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
		return "MigrationDTOErzieherDatenschutz(ErzieherID=" + this.ErzieherID + ", DatenschutzID=" + this.DatenschutzID + ", Status=" + this.Status + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
