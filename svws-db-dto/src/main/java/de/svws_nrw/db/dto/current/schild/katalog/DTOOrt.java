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
@JsonPropertyOrder({"ID", "PLZ", "Bezeichnung", "Kreis", "Sortierung", "Sichtbar", "Aenderbar", "Land"})
public final class DTOOrt {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOOrt e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOOrt e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOOrt e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOOrt e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOOrt e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOOrt e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PLZ */
	public static final String QUERY_BY_PLZ = "SELECT e FROM DTOOrt e WHERE e.PLZ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PLZ */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM DTOOrt e WHERE e.PLZ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOOrt e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOOrt e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kreis */
	public static final String QUERY_BY_KREIS = "SELECT e FROM DTOOrt e WHERE e.Kreis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kreis */
	public static final String QUERY_LIST_BY_KREIS = "SELECT e FROM DTOOrt e WHERE e.Kreis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOOrt e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOOrt e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOOrt e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOOrt e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM DTOOrt e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM DTOOrt e WHERE e.Aenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Land */
	public static final String QUERY_BY_LAND = "SELECT e FROM DTOOrt e WHERE e.Land = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Land */
	public static final String QUERY_LIST_BY_LAND = "SELECT e FROM DTOOrt e WHERE e.Land IN ?1";

	/** ID des Ortes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** PLZ des Ortes */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Bezeichnung des Ortes */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Kreis des Ortes */
	@Column(name = "Kreis")
	@JsonProperty
	public String Kreis;

	/** Sortierung des Ortes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit des Ortes */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Ortes */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Land des Ortes */
	@Column(name = "Land")
	@JsonProperty
	public String Land;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOrt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOOrt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOrt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param PLZ   der Wert für das Attribut PLZ
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOOrt(final long ID, final String PLZ, final String Bezeichnung) {
		this.ID = ID;
		if (PLZ == null) {
			throw new NullPointerException("PLZ must not be null");
		}
		this.PLZ = PLZ;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOOrt other = (DTOOrt) obj;
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
		return "DTOOrt(ID=" + this.ID + ", PLZ=" + this.PLZ + ", Bezeichnung=" + this.Bezeichnung + ", Kreis=" + this.Kreis + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", Land=" + this.Land + ")";
	}

}
