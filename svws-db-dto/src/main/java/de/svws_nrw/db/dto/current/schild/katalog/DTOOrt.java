package de.svws_nrw.db.dto.current.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ort.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ort")
@JsonPropertyOrder({"id", "plz", "ortsname", "kreis", "sortierung", "istSichtbar", "istAenderbar", "schluesselBundesland"})
public final class DTOOrt {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOOrt e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOOrt e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOOrt e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOOrt e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOOrt e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOOrt e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes plz */
	public static final String QUERY_BY_PLZ = "SELECT e FROM DTOOrt e WHERE e.plz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes plz */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM DTOOrt e WHERE e.plz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ortsname */
	public static final String QUERY_BY_ORTSNAME = "SELECT e FROM DTOOrt e WHERE e.ortsname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ortsname */
	public static final String QUERY_LIST_BY_ORTSNAME = "SELECT e FROM DTOOrt e WHERE e.ortsname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes kreis */
	public static final String QUERY_BY_KREIS = "SELECT e FROM DTOOrt e WHERE e.kreis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes kreis */
	public static final String QUERY_LIST_BY_KREIS = "SELECT e FROM DTOOrt e WHERE e.kreis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOOrt e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOOrt e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSichtbar */
	public static final String QUERY_BY_ISTSICHTBAR = "SELECT e FROM DTOOrt e WHERE e.istSichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSichtbar */
	public static final String QUERY_LIST_BY_ISTSICHTBAR = "SELECT e FROM DTOOrt e WHERE e.istSichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istAenderbar */
	public static final String QUERY_BY_ISTAENDERBAR = "SELECT e FROM DTOOrt e WHERE e.istAenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istAenderbar */
	public static final String QUERY_LIST_BY_ISTAENDERBAR = "SELECT e FROM DTOOrt e WHERE e.istAenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schluesselBundesland */
	public static final String QUERY_BY_SCHLUESSELBUNDESLAND = "SELECT e FROM DTOOrt e WHERE e.schluesselBundesland = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schluesselBundesland */
	public static final String QUERY_LIST_BY_SCHLUESSELBUNDESLAND = "SELECT e FROM DTOOrt e WHERE e.schluesselBundesland IN ?1";

	/** ID des Ortes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** PLZ des Ortes */
	@Column(name = "PLZ")
	@JsonProperty
	public String plz;

	/** Bezeichnung des Ortes */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String ortsname;

	/** Kreis des Ortes */
	@Column(name = "Kreis")
	@JsonProperty
	public String kreis;

	/** Sortierung des Ortes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** Sichbarkeit des Ortes */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSichtbar;

	/** Änderbarkeit des Ortes */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istAenderbar;

	/** Land des Ortes */
	@Column(name = "Land")
	@JsonProperty
	public String schluesselBundesland;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOrt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOOrt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOrt ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param plz   der Wert für das Attribut plz
	 * @param ortsname   der Wert für das Attribut ortsname
	 */
	public DTOOrt(final long id, final String plz, final String ortsname) {
		this.id = id;
		if (plz == null) {
			throw new NullPointerException("plz must not be null");
		}
		this.plz = plz;
		if (ortsname == null) {
			throw new NullPointerException("ortsname must not be null");
		}
		this.ortsname = ortsname;
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
		DTOOrt other = (DTOOrt) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOOrt(id=" + this.id + ", plz=" + this.plz + ", ortsname=" + this.ortsname + ", kreis=" + this.kreis + ", sortierung=" + this.sortierung + ", istSichtbar=" + this.istSichtbar + ", istAenderbar=" + this.istAenderbar + ", schluesselBundesland=" + this.schluesselBundesland + ")";
	}

}
