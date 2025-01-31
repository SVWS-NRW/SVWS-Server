package de.svws_nrw.db.dto.current.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Fach_Gliederungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOFachgliederungenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Fach_Gliederungen")
@JsonPropertyOrder({"Fach_ID", "Gliederung", "Fachklasse_ID", "Faechergruppe", "GewichtungAB", "GewichtungBB", "SchriftlichAB", "SchriftlichBB", "GymOSFach", "ZeugnisBez", "Lernfelder", "Sortierung"})
public final class DTOFachgliederungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOFachgliederungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOFachgliederungen e WHERE e.Fach_ID = ?1 AND e.Gliederung = ?2 AND e.Fachklasse_ID = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOFachgliederungen e WHERE e.Fach_ID IS NOT NULL AND e.Gliederung IS NOT NULL AND e.Fachklasse_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOFachgliederungen e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOFachgliederungen e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gliederung */
	public static final String QUERY_BY_GLIEDERUNG = "SELECT e FROM DTOFachgliederungen e WHERE e.Gliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gliederung */
	public static final String QUERY_LIST_BY_GLIEDERUNG = "SELECT e FROM DTOFachgliederungen e WHERE e.Gliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachklasse_ID */
	public static final String QUERY_BY_FACHKLASSE_ID = "SELECT e FROM DTOFachgliederungen e WHERE e.Fachklasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachklasse_ID */
	public static final String QUERY_LIST_BY_FACHKLASSE_ID = "SELECT e FROM DTOFachgliederungen e WHERE e.Fachklasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Faechergruppe */
	public static final String QUERY_BY_FAECHERGRUPPE = "SELECT e FROM DTOFachgliederungen e WHERE e.Faechergruppe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Faechergruppe */
	public static final String QUERY_LIST_BY_FAECHERGRUPPE = "SELECT e FROM DTOFachgliederungen e WHERE e.Faechergruppe IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GewichtungAB */
	public static final String QUERY_BY_GEWICHTUNGAB = "SELECT e FROM DTOFachgliederungen e WHERE e.GewichtungAB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GewichtungAB */
	public static final String QUERY_LIST_BY_GEWICHTUNGAB = "SELECT e FROM DTOFachgliederungen e WHERE e.GewichtungAB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GewichtungBB */
	public static final String QUERY_BY_GEWICHTUNGBB = "SELECT e FROM DTOFachgliederungen e WHERE e.GewichtungBB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GewichtungBB */
	public static final String QUERY_LIST_BY_GEWICHTUNGBB = "SELECT e FROM DTOFachgliederungen e WHERE e.GewichtungBB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchriftlichAB */
	public static final String QUERY_BY_SCHRIFTLICHAB = "SELECT e FROM DTOFachgliederungen e WHERE e.SchriftlichAB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchriftlichAB */
	public static final String QUERY_LIST_BY_SCHRIFTLICHAB = "SELECT e FROM DTOFachgliederungen e WHERE e.SchriftlichAB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchriftlichBB */
	public static final String QUERY_BY_SCHRIFTLICHBB = "SELECT e FROM DTOFachgliederungen e WHERE e.SchriftlichBB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchriftlichBB */
	public static final String QUERY_LIST_BY_SCHRIFTLICHBB = "SELECT e FROM DTOFachgliederungen e WHERE e.SchriftlichBB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GymOSFach */
	public static final String QUERY_BY_GYMOSFACH = "SELECT e FROM DTOFachgliederungen e WHERE e.GymOSFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GymOSFach */
	public static final String QUERY_LIST_BY_GYMOSFACH = "SELECT e FROM DTOFachgliederungen e WHERE e.GymOSFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeugnisBez */
	public static final String QUERY_BY_ZEUGNISBEZ = "SELECT e FROM DTOFachgliederungen e WHERE e.ZeugnisBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeugnisBez */
	public static final String QUERY_LIST_BY_ZEUGNISBEZ = "SELECT e FROM DTOFachgliederungen e WHERE e.ZeugnisBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lernfelder */
	public static final String QUERY_BY_LERNFELDER = "SELECT e FROM DTOFachgliederungen e WHERE e.Lernfelder = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lernfelder */
	public static final String QUERY_LIST_BY_LERNFELDER = "SELECT e FROM DTOFachgliederungen e WHERE e.Lernfelder IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOFachgliederungen e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOFachgliederungen e WHERE e.Sortierung IN ?1";

	/** ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** SGL für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Id
	@Column(name = "Gliederung")
	@JsonProperty
	public String Gliederung;

	/** Fachklassen ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Id
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public long Fachklasse_ID;

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchriftlichAB;

	/** Ist schriftliches Fach Berufsbildend für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "SchriftlichBB")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchriftlichBB;

	/** Ist Fach der GymOB für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "GymOSFach")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean GymOSFach;

	/** Zeugnisbezeihnung für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "ZeugnisBez")
	@JsonProperty
	public String ZeugnisBez;

	/** Lernfelder für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Lernfelder")
	@JsonProperty
	public String Lernfelder;

	/** Sortierung dfür die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachgliederungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFachgliederungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachgliederungen ohne eine Initialisierung der Attribute.
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Gliederung   der Wert für das Attribut Gliederung
	 * @param Fachklasse_ID   der Wert für das Attribut Fachklasse_ID
	 */
	public DTOFachgliederungen(final long Fach_ID, final String Gliederung, final long Fachklasse_ID) {
		this.Fach_ID = Fach_ID;
		if (Gliederung == null) {
			throw new NullPointerException("Gliederung must not be null");
		}
		this.Gliederung = Gliederung;
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
		DTOFachgliederungen other = (DTOFachgliederungen) obj;
		if (Fach_ID != other.Fach_ID)
			return false;
		if (Gliederung == null) {
			if (other.Gliederung != null)
				return false;
		} else if (!Gliederung.equals(other.Gliederung))
			return false;
		return Fachklasse_ID == other.Fachklasse_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Fach_ID);

		result = prime * result + ((Gliederung == null) ? 0 : Gliederung.hashCode());

		result = prime * result + Long.hashCode(Fachklasse_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOFachgliederungen(Fach_ID=" + this.Fach_ID + ", Gliederung=" + this.Gliederung + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Faechergruppe=" + this.Faechergruppe + ", GewichtungAB=" + this.GewichtungAB + ", GewichtungBB=" + this.GewichtungBB + ", SchriftlichAB=" + this.SchriftlichAB + ", SchriftlichBB=" + this.SchriftlichBB + ", GymOSFach=" + this.GymOSFach + ", ZeugnisBez=" + this.ZeugnisBez + ", Lernfelder=" + this.Lernfelder + ", Sortierung=" + this.Sortierung + ")";
	}

}
