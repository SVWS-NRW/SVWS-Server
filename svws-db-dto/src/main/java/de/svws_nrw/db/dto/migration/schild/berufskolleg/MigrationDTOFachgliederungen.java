package de.svws_nrw.db.dto.migration.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Fach_Gliederungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOFachgliederungenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Fach_Gliederungen")
@JsonPropertyOrder({"Fach_ID", "Gliederung", "SchulnrEigner", "Faechergruppe", "GewichtungAB", "GewichtungBB", "SchriftlichAB", "SchriftlichBB", "GymOSFach", "ZeugnisBez", "Lernfelder", "Fachklasse_ID", "Sortierung"})
public final class MigrationDTOFachgliederungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOFachgliederungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Fach_ID = ?1 AND e.Fachklasse_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Fach_ID IS NOT NULL AND e.Fachklasse_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gliederung */
	public static final String QUERY_BY_GLIEDERUNG = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Gliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gliederung */
	public static final String QUERY_LIST_BY_GLIEDERUNG = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Gliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Faechergruppe */
	public static final String QUERY_BY_FAECHERGRUPPE = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Faechergruppe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Faechergruppe */
	public static final String QUERY_LIST_BY_FAECHERGRUPPE = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Faechergruppe IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GewichtungAB */
	public static final String QUERY_BY_GEWICHTUNGAB = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.GewichtungAB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GewichtungAB */
	public static final String QUERY_LIST_BY_GEWICHTUNGAB = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.GewichtungAB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GewichtungBB */
	public static final String QUERY_BY_GEWICHTUNGBB = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.GewichtungBB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GewichtungBB */
	public static final String QUERY_LIST_BY_GEWICHTUNGBB = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.GewichtungBB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchriftlichAB */
	public static final String QUERY_BY_SCHRIFTLICHAB = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.SchriftlichAB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchriftlichAB */
	public static final String QUERY_LIST_BY_SCHRIFTLICHAB = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.SchriftlichAB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchriftlichBB */
	public static final String QUERY_BY_SCHRIFTLICHBB = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.SchriftlichBB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchriftlichBB */
	public static final String QUERY_LIST_BY_SCHRIFTLICHBB = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.SchriftlichBB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GymOSFach */
	public static final String QUERY_BY_GYMOSFACH = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.GymOSFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GymOSFach */
	public static final String QUERY_LIST_BY_GYMOSFACH = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.GymOSFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeugnisBez */
	public static final String QUERY_BY_ZEUGNISBEZ = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.ZeugnisBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeugnisBez */
	public static final String QUERY_LIST_BY_ZEUGNISBEZ = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.ZeugnisBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lernfelder */
	public static final String QUERY_BY_LERNFELDER = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Lernfelder = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lernfelder */
	public static final String QUERY_LIST_BY_LERNFELDER = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Lernfelder IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachklasse_ID */
	public static final String QUERY_BY_FACHKLASSE_ID = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Fachklasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachklasse_ID */
	public static final String QUERY_LIST_BY_FACHKLASSE_ID = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Fachklasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOFachgliederungen e WHERE e.Sortierung IN ?1";

	/** ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** SGL für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Gliederung")
	@JsonProperty
	public String Gliederung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Fächergruppe für gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Faechergruppe")
	@JsonProperty
	public Integer Faechergruppe;

	/** Gewichtung Allgemeinbildend für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "GewichtungAB")
	@JsonProperty
	public Integer GewichtungAB;

	/** Gewichtung Berufsbezogen für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "GewichtungBB")
	@JsonProperty
	public Integer GewichtungBB;

	/** Ist schriftliches Fach Allgemeinbildend für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "SchriftlichAB")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchriftlichAB;

	/** Ist schriftliches Fach Berufsbildend für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "SchriftlichBB")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchriftlichBB;

	/** Ist Fach der GymOB für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "GymOSFach")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean GymOSFach;

	/** Zeugnisbezeihnung für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "ZeugnisBez")
	@JsonProperty
	public String ZeugnisBez;

	/** Lernfelder für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Lernfelder")
	@JsonProperty
	public String Lernfelder;

	/** Fachklassen ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Id
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Sortierung dfür die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachgliederungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFachgliederungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachgliederungen ohne eine Initialisierung der Attribute.
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Gliederung   der Wert für das Attribut Gliederung
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Fachklasse_ID   der Wert für das Attribut Fachklasse_ID
	 */
	public MigrationDTOFachgliederungen(final Long Fach_ID, final String Gliederung, final Integer SchulnrEigner, final Long Fachklasse_ID) {
		if (Fach_ID == null) {
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
		if (Gliederung == null) {
			throw new NullPointerException("Gliederung must not be null");
		}
		this.Gliederung = Gliederung;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Fachklasse_ID == null) {
			throw new NullPointerException("Fachklasse_ID must not be null");
		}
		this.Fachklasse_ID = Fachklasse_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFachgliederungen other = (MigrationDTOFachgliederungen) obj;
		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;
		if (Fachklasse_ID == null) {
			if (other.Fachklasse_ID != null)
				return false;
		} else if (!Fachklasse_ID.equals(other.Fachklasse_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());

		result = prime * result + ((Fachklasse_ID == null) ? 0 : Fachklasse_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOFachgliederungen(Fach_ID=" + this.Fach_ID + ", Gliederung=" + this.Gliederung + ", SchulnrEigner=" + this.SchulnrEigner + ", Faechergruppe=" + this.Faechergruppe + ", GewichtungAB=" + this.GewichtungAB + ", GewichtungBB=" + this.GewichtungBB + ", SchriftlichAB=" + this.SchriftlichAB + ", SchriftlichBB=" + this.SchriftlichBB + ", GymOSFach=" + this.GymOSFach + ", ZeugnisBez=" + this.ZeugnisBez + ", Lernfelder=" + this.Lernfelder + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Sortierung=" + this.Sortierung + ")";
	}

}
