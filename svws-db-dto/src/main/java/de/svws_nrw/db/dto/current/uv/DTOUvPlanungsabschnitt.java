package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;
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
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Planungsabschnitte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Planungsabschnitte")
@JsonPropertyOrder({"ID", "Schuljahr", "Aktiv", "GueltigVon", "GueltigBis", "Beschreibung"})
public final class DTOUvPlanungsabschnitt {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvPlanungsabschnitt e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahr */
	public static final String QUERY_BY_SCHULJAHR = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.Schuljahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahr */
	public static final String QUERY_LIST_BY_SCHULJAHR = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.Schuljahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aktiv */
	public static final String QUERY_BY_AKTIV = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.Aktiv = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aktiv */
	public static final String QUERY_LIST_BY_AKTIV = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.Aktiv IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.GueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.GueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.GueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.GueltigBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.Beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM DTOUvPlanungsabschnitt e WHERE e.Beschreibung IN ?1";

	/** ID des Planungsabschnitts (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Schuljahr des Planungsabschnitts (z.B. 2012 für 2012/13) */
	@Column(name = "Schuljahr")
	@JsonProperty
	public int Schuljahr;

	/** Gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein */
	@Column(name = "Aktiv")
	@JsonProperty
	public boolean Aktiv;

	/** Das Datum, ab dem der Raum gültig ist */
	@Column(name = "GueltigVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String GueltigVon;

	/** Datum, bis zu dem der Planungsabschnitt gültig ist. Wenn null gesetzt ist, gilt der Planungsabschnitt unbegrenzt weiter */
	@Column(name = "GueltigBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String GueltigBis;

	/** Optionale Beschreibung oder Kommentar zum Planungsabschnitt */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvPlanungsabschnitt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvPlanungsabschnitt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvPlanungsabschnitt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahr   der Wert für das Attribut Schuljahr
	 * @param Aktiv   der Wert für das Attribut Aktiv
	 * @param GueltigVon   der Wert für das Attribut GueltigVon
	 */
	public DTOUvPlanungsabschnitt(final long ID, final int Schuljahr, final boolean Aktiv, final String GueltigVon) {
		this.ID = ID;
		this.Schuljahr = Schuljahr;
		this.Aktiv = Aktiv;
		if (GueltigVon == null) {
			throw new NullPointerException("GueltigVon must not be null");
		}
		this.GueltigVon = GueltigVon;
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
		DTOUvPlanungsabschnitt other = (DTOUvPlanungsabschnitt) obj;
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
		return "DTOUvPlanungsabschnitt(ID=" + this.ID + ", Schuljahr=" + this.Schuljahr + ", Aktiv=" + this.Aktiv + ", GueltigVon=" + this.GueltigVon + ", GueltigBis=" + this.GueltigBis + ", Beschreibung=" + this.Beschreibung + ")";
	}

}
