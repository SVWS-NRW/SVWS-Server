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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ortsteil.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ortsteil")
@JsonPropertyOrder({"id", "ortsteil", "idOrt", "sortierung", "istSichtbar", "istAenderbar", "schluesselOrtsteil"})
public final class DTOOrtsteil {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOOrtsteil e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOOrtsteil e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOOrtsteil e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOOrtsteil e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOOrtsteil e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOOrtsteil e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ortsteil */
	public static final String QUERY_BY_ORTSTEIL = "SELECT e FROM DTOOrtsteil e WHERE e.ortsteil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ortsteil */
	public static final String QUERY_LIST_BY_ORTSTEIL = "SELECT e FROM DTOOrtsteil e WHERE e.ortsteil IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idOrt */
	public static final String QUERY_BY_IDORT = "SELECT e FROM DTOOrtsteil e WHERE e.idOrt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idOrt */
	public static final String QUERY_LIST_BY_IDORT = "SELECT e FROM DTOOrtsteil e WHERE e.idOrt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOOrtsteil e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOOrtsteil e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSichtbar */
	public static final String QUERY_BY_ISTSICHTBAR = "SELECT e FROM DTOOrtsteil e WHERE e.istSichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSichtbar */
	public static final String QUERY_LIST_BY_ISTSICHTBAR = "SELECT e FROM DTOOrtsteil e WHERE e.istSichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istAenderbar */
	public static final String QUERY_BY_ISTAENDERBAR = "SELECT e FROM DTOOrtsteil e WHERE e.istAenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istAenderbar */
	public static final String QUERY_LIST_BY_ISTAENDERBAR = "SELECT e FROM DTOOrtsteil e WHERE e.istAenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schluesselOrtsteil */
	public static final String QUERY_BY_SCHLUESSELORTSTEIL = "SELECT e FROM DTOOrtsteil e WHERE e.schluesselOrtsteil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schluesselOrtsteil */
	public static final String QUERY_LIST_BY_SCHLUESSELORTSTEIL = "SELECT e FROM DTOOrtsteil e WHERE e.schluesselOrtsteil IN ?1";

	/** ID des Ortsteils */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** Bezeichnung des Ortsteils */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String ortsteil;

	/** Fremdschlüssel auf den Ort, dem der Ortsteil zugeordnet ist */
	@Column(name = "Ort_ID")
	@JsonProperty
	public Long idOrt;

	/** Sortierung des Ortsteils */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** Sichbarkeit des Ortsteils */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSichtbar;

	/** Änderbarkeit des Ortsteils */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istAenderbar;

	/** Schlüssel des Ortsteils (Regional?) */
	@Column(name = "OrtsteilSchluessel")
	@JsonProperty
	public String schluesselOrtsteil;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOrtsteil ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOOrtsteil() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOrtsteil ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param ortsteil   der Wert für das Attribut ortsteil
	 */
	public DTOOrtsteil(final long id, final String ortsteil) {
		this.id = id;
		if (ortsteil == null) {
			throw new NullPointerException("ortsteil must not be null");
		}
		this.ortsteil = ortsteil;
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
		DTOOrtsteil other = (DTOOrtsteil) obj;
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
		return "DTOOrtsteil(id=" + this.id + ", ortsteil=" + this.ortsteil + ", idOrt=" + this.idOrt + ", sortierung=" + this.sortierung + ", istSichtbar=" + this.istSichtbar + ", istAenderbar=" + this.istAenderbar + ", schluesselOrtsteil=" + this.schluesselOrtsteil + ")";
	}

}
