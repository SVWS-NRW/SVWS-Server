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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerMerkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerMerkmale")
@JsonPropertyOrder({"ID", "Schueler_ID", "Kurztext", "DatumVon", "DatumBis"})
public final class DTOSchuelerMerkmale {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerMerkmale e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurztext */
	public static final String QUERY_BY_KURZTEXT = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.Kurztext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurztext */
	public static final String QUERY_LIST_BY_KURZTEXT = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.Kurztext IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumVon */
	public static final String QUERY_BY_DATUMVON = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.DatumVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumVon */
	public static final String QUERY_LIST_BY_DATUMVON = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.DatumVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumBis */
	public static final String QUERY_BY_DATUMBIS = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.DatumBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumBis */
	public static final String QUERY_LIST_BY_DATUMBIS = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.DatumBis IN ?1";

	/** ID des Eintrag bei besondere Merkmale zum Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Schüler-ID des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Kurztext des Merkmals des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Datum Beginn des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumVon;

	/** Datum Ende des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerMerkmale(final long ID, final long Schueler_ID) {
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
		DTOSchuelerMerkmale other = (DTOSchuelerMerkmale) obj;
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
		return "DTOSchuelerMerkmale(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Kurztext=" + this.Kurztext + ", DatumVon=" + this.DatumVon + ", DatumBis=" + this.DatumBis + ")";
	}

}
