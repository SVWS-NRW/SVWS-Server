package de.svws_nrw.db.dto.current.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerVermerke.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerVermerke")
@JsonPropertyOrder({"ID", "Schueler_ID", "VermerkArt_ID", "Datum", "Bemerkung", "AngelegtVon", "GeaendertVon"})
public final class DTOSchuelerVermerke {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerVermerke e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VermerkArt_ID */
	public static final String QUERY_BY_VERMERKART_ID = "SELECT e FROM DTOSchuelerVermerke e WHERE e.VermerkArt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VermerkArt_ID */
	public static final String QUERY_LIST_BY_VERMERKART_ID = "SELECT e FROM DTOSchuelerVermerke e WHERE e.VermerkArt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Datum */
	public static final String QUERY_BY_DATUM = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Datum */
	public static final String QUERY_LIST_BY_DATUM = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AngelegtVon */
	public static final String QUERY_BY_ANGELEGTVON = "SELECT e FROM DTOSchuelerVermerke e WHERE e.AngelegtVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AngelegtVon */
	public static final String QUERY_LIST_BY_ANGELEGTVON = "SELECT e FROM DTOSchuelerVermerke e WHERE e.AngelegtVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GeaendertVon */
	public static final String QUERY_BY_GEAENDERTVON = "SELECT e FROM DTOSchuelerVermerke e WHERE e.GeaendertVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GeaendertVon */
	public static final String QUERY_LIST_BY_GEAENDERTVON = "SELECT e FROM DTOSchuelerVermerke e WHERE e.GeaendertVon IN ?1";

	/** ID des Vermerkeintrages beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** SchülerID des Vermerkeintrages beim Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Art des Vermerkeintrages beim Schüler */
	@Column(name = "VermerkArt_ID")
	@JsonProperty
	public Long VermerkArt_ID;

	/** Datum des Vermerkeintrages beim Schüler */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Datum;

	/** Bemerkung des Vermerkeintrages beim Schüler */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Angelegt von User des Vermerkeintrages beim Schüler */
	@Column(name = "AngelegtVon")
	@JsonProperty
	public String AngelegtVon;

	/** Geändert von User des Vermerkeintrages beim Schüler */
	@Column(name = "GeaendertVon")
	@JsonProperty
	public String GeaendertVon;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerVermerke ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerVermerke() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerVermerke ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerVermerke(final long ID, final long Schueler_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerVermerke other = (DTOSchuelerVermerke) obj;
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
		return "DTOSchuelerVermerke(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", VermerkArt_ID=" + this.VermerkArt_ID + ", Datum=" + this.Datum + ", Bemerkung=" + this.Bemerkung + ", AngelegtVon=" + this.AngelegtVon + ", GeaendertVon=" + this.GeaendertVon + ")";
	}

}
