package de.svws_nrw.db.dto.current.schild;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchildFilter.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchildFilter")
@JsonPropertyOrder({"ID", "Art", "Name", "Beschreibung", "Tabellen", "ZusatzTabellen", "Bedingung", "BedingungKlartext"})
public final class DTOSchildAuswahlFilter {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchildAuswahlFilter e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Art */
	public static final String QUERY_BY_ART = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Art = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Art */
	public static final String QUERY_LIST_BY_ART = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Art IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Name */
	public static final String QUERY_BY_NAME = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Name = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Name */
	public static final String QUERY_LIST_BY_NAME = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Name IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Beschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Tabellen */
	public static final String QUERY_BY_TABELLEN = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Tabellen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Tabellen */
	public static final String QUERY_LIST_BY_TABELLEN = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Tabellen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZusatzTabellen */
	public static final String QUERY_BY_ZUSATZTABELLEN = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.ZusatzTabellen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZusatzTabellen */
	public static final String QUERY_LIST_BY_ZUSATZTABELLEN = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.ZusatzTabellen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bedingung */
	public static final String QUERY_BY_BEDINGUNG = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Bedingung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bedingung */
	public static final String QUERY_LIST_BY_BEDINGUNG = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.Bedingung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BedingungKlartext */
	public static final String QUERY_BY_BEDINGUNGKLARTEXT = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.BedingungKlartext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BedingungKlartext */
	public static final String QUERY_LIST_BY_BEDINGUNGKLARTEXT = "SELECT e FROM DTOSchildAuswahlFilter e WHERE e.BedingungKlartext IN ?1";

	/** ID des gespeicherten Filters */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Art des Filters */
	@Column(name = "Art")
	@JsonProperty
	public String Art;

	/** Bezeichnender Kurztext des Filters */
	@Column(name = "Name")
	@JsonProperty
	public String Name;

	/** Beschreibung zum Filter */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Tabellen die im Filter vorkommen */
	@Column(name = "Tabellen")
	@JsonProperty
	public String Tabellen;

	/** Zusätzliche Tabellen die im Filtervorkommen */
	@Column(name = "ZusatzTabellen")
	@JsonProperty
	public String ZusatzTabellen;

	/** SQL-Text des Filters */
	@Column(name = "Bedingung")
	@JsonProperty
	public String Bedingung;

	/** Klartext der bedingung */
	@Column(name = "BedingungKlartext")
	@JsonProperty
	public String BedingungKlartext;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchildAuswahlFilter ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchildAuswahlFilter() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchildAuswahlFilter ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Name   der Wert für das Attribut Name
	 */
	public DTOSchildAuswahlFilter(final long ID, final String Name) {
		this.ID = ID;
		if (Name == null) {
			throw new NullPointerException("Name must not be null");
		}
		this.Name = Name;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchildAuswahlFilter other = (DTOSchildAuswahlFilter) obj;
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
		return "DTOSchildAuswahlFilter(ID=" + this.ID + ", Art=" + this.Art + ", Name=" + this.Name + ", Beschreibung=" + this.Beschreibung + ", Tabellen=" + this.Tabellen + ", ZusatzTabellen=" + this.ZusatzTabellen + ", Bedingung=" + this.Bedingung + ", BedingungKlartext=" + this.BedingungKlartext + ")";
	}

}
