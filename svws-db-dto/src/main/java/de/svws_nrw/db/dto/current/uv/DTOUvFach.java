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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Faecher")
@JsonPropertyOrder({"ID", "Fach_ID", "GueltigVon", "GueltigBis"})
public final class DTOUvFach {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvFach e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvFach e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOUvFach e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOUvFach e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM DTOUvFach e WHERE e.GueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM DTOUvFach e WHERE e.GueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM DTOUvFach e WHERE e.GueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM DTOUvFach e WHERE e.GueltigBis IN ?1";

	/** ID des UV-Faches (generiert, planungsspezifisch) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Faches als Fremdschlüssel auf die Tabelle EigeneSchule_Faecher */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Das Datum, ab dem der Raum gültig ist */
	@Column(name = "GueltigVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String GueltigVon;

	/** Datum, bis zu dem das Fach gültig ist. Ist kein Datum gesetzt, gilt das Fach unbegrenzt weiter. */
	@Column(name = "GueltigBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String GueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param GueltigVon   der Wert für das Attribut GueltigVon
	 */
	public DTOUvFach(final long ID, final long Fach_ID, final String GueltigVon) {
		this.ID = ID;
		this.Fach_ID = Fach_ID;
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
		DTOUvFach other = (DTOUvFach) obj;
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
		return "DTOUvFach(ID=" + this.ID + ", Fach_ID=" + this.Fach_ID + ", GueltigVon=" + this.GueltigVon + ", GueltigBis=" + this.GueltigBis + ")";
	}

}
