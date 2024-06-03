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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerEinzelleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerEinzelleistungen")
@JsonPropertyOrder({"SchulnrEigner", "ID", "Datum", "Lehrer_ID", "Art_ID", "Bemerkung", "Leistung_ID", "NotenKrz"})
public final class MigrationDTOSchuelerTeilleistung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerTeilleistung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Datum */
	public static final String QUERY_BY_DATUM = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Datum */
	public static final String QUERY_LIST_BY_DATUM = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Art_ID */
	public static final String QUERY_BY_ART_ID = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Art_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Art_ID */
	public static final String QUERY_LIST_BY_ART_ID = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Art_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Leistung_ID */
	public static final String QUERY_BY_LEISTUNG_ID = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Leistung_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Leistung_ID */
	public static final String QUERY_LIST_BY_LEISTUNG_ID = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Leistung_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NotenKrz */
	public static final String QUERY_BY_NOTENKRZ = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.NotenKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NotenKrz */
	public static final String QUERY_LIST_BY_NOTENKRZ = "SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.NotenKrz IN ?1";

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** ID der Teilleistung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Datum der Teilleistung */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Datum;

	/** LehrerID der Teilleistung */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Art der Teilleistung */
	@Column(name = "Art_ID")
	@JsonProperty
	public Long Art_ID;

	/** Bemerkung zur Teilleistung */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** LeistungsdatenID der Teilleistung */
	@Column(name = "Leistung_ID")
	@JsonProperty
	public Long Leistung_ID;

	/** Notenkürzel der Teilleistung */
	@Column(name = "NotenKrz")
	@JsonProperty
	public String NotenKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerTeilleistung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerTeilleistung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerTeilleistung ohne eine Initialisierung der Attribute.
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param ID   der Wert für das Attribut ID
	 * @param Leistung_ID   der Wert für das Attribut Leistung_ID
	 */
	public MigrationDTOSchuelerTeilleistung(final Integer SchulnrEigner, final Long ID, final Long Leistung_ID) {
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Leistung_ID == null) {
			throw new NullPointerException("Leistung_ID must not be null");
		}
		this.Leistung_ID = Leistung_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerTeilleistung other = (MigrationDTOSchuelerTeilleistung) obj;
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
		return "MigrationDTOSchuelerTeilleistung(SchulnrEigner=" + this.SchulnrEigner + ", ID=" + this.ID + ", Datum=" + this.Datum + ", Lehrer_ID=" + this.Lehrer_ID + ", Art_ID=" + this.Art_ID + ", Bemerkung=" + this.Bemerkung + ", Leistung_ID=" + this.Leistung_ID + ", NotenKrz=" + this.NotenKrz + ")";
	}

}
