package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFehlstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFehlstunden")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "Datum", "Fach_ID", "FehlStd", "VonStd", "BisStd", "Entschuldigt", "Lehrer_ID"})
public final class DTOSchuelerFehlstunden {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerFehlstunden e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Datum */
	public static final String QUERY_BY_DATUM = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Datum */
	public static final String QUERY_LIST_BY_DATUM = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FehlStd */
	public static final String QUERY_BY_FEHLSTD = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.FehlStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FehlStd */
	public static final String QUERY_LIST_BY_FEHLSTD = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.FehlStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VonStd */
	public static final String QUERY_BY_VONSTD = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.VonStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VonStd */
	public static final String QUERY_LIST_BY_VONSTD = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.VonStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BisStd */
	public static final String QUERY_BY_BISSTD = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.BisStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BisStd */
	public static final String QUERY_LIST_BY_BISSTD = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.BisStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entschuldigt */
	public static final String QUERY_BY_ENTSCHULDIGT = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Entschuldigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entschuldigt */
	public static final String QUERY_LIST_BY_ENTSCHULDIGT = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Entschuldigt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Lehrer_ID IN ?1";

	/** ID des Fehlstundeneintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** AbschnittsID des zugehörigen Lernabschnitts */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** Datum der Fehlzeit */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Datum;

	/** FachID der Fehlzeit */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Anzahl der Fehlstunden */
	@Column(name = "FehlStd")
	@JsonProperty
	public double FehlStd;

	/** Beginn Stunde Fehlzeit */
	@Column(name = "VonStd")
	@JsonProperty
	public Integer VonStd;

	/** Ende Stunde Fehlzeit */
	@Column(name = "BisStd")
	@JsonProperty
	public Integer BisStd;

	/** Entschuldigt Ja Nein */
	@Column(name = "Entschuldigt")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Entschuldigt;

	/** LehrerID der Fehlzeit */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFehlstunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerFehlstunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFehlstunden ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Datum   der Wert für das Attribut Datum
	 * @param FehlStd   der Wert für das Attribut FehlStd
	 */
	public DTOSchuelerFehlstunden(final long ID, final long Abschnitt_ID, final String Datum, final double FehlStd) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
		if (Datum == null) {
			throw new NullPointerException("Datum must not be null");
		}
		this.Datum = Datum;
		this.FehlStd = FehlStd;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerFehlstunden other = (DTOSchuelerFehlstunden) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerFehlstunden(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Datum=" + this.Datum + ", Fach_ID=" + this.Fach_ID + ", FehlStd=" + this.FehlStd + ", VonStd=" + this.VonStd + ", BisStd=" + this.BisStd + ", Entschuldigt=" + this.Entschuldigt + ", Lehrer_ID=" + this.Lehrer_ID + ")";
	}

}
