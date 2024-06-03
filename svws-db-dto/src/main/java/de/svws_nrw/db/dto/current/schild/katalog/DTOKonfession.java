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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Religion.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Religion")
@JsonPropertyOrder({"ID", "Bezeichnung", "StatistikKrz", "Sortierung", "Sichtbar", "Aenderbar", "ExportBez", "ZeugnisBezeichnung"})
public final class DTOKonfession {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKonfession e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKonfession e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOKonfession e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKonfession e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOKonfession e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOKonfession e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOKonfession e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOKonfession e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StatistikKrz */
	public static final String QUERY_BY_STATISTIKKRZ = "SELECT e FROM DTOKonfession e WHERE e.StatistikKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StatistikKrz */
	public static final String QUERY_LIST_BY_STATISTIKKRZ = "SELECT e FROM DTOKonfession e WHERE e.StatistikKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOKonfession e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOKonfession e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOKonfession e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOKonfession e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM DTOKonfession e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM DTOKonfession e WHERE e.Aenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ExportBez */
	public static final String QUERY_BY_EXPORTBEZ = "SELECT e FROM DTOKonfession e WHERE e.ExportBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ExportBez */
	public static final String QUERY_LIST_BY_EXPORTBEZ = "SELECT e FROM DTOKonfession e WHERE e.ExportBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeugnisBezeichnung */
	public static final String QUERY_BY_ZEUGNISBEZEICHNUNG = "SELECT e FROM DTOKonfession e WHERE e.ZeugnisBezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeugnisBezeichnung */
	public static final String QUERY_LIST_BY_ZEUGNISBEZEICHNUNG = "SELECT e FROM DTOKonfession e WHERE e.ZeugnisBezeichnung IN ?1";

	/** ID der Religion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung der Religion */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Statistikkürzel der Religion */
	@Column(name = "StatistikKrz")
	@JsonProperty
	public String StatistikKrz;

	/** Sortierung der Religion */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Religion */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Religion */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Exportbezeichnung der Religion */
	@Column(name = "ExportBez")
	@JsonProperty
	public String ExportBez;

	/** Zeugnisbezeichnung der Religion */
	@Column(name = "ZeugnisBezeichnung")
	@JsonProperty
	public String ZeugnisBezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKonfession ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKonfession() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKonfession ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOKonfession(final long ID, final String Bezeichnung) {
		this.ID = ID;
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
		DTOKonfession other = (DTOKonfession) obj;
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
		return "DTOKonfession(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", StatistikKrz=" + this.StatistikKrz + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", ExportBez=" + this.ExportBez + ", ZeugnisBezeichnung=" + this.ZeugnisBezeichnung + ")";
	}

}
