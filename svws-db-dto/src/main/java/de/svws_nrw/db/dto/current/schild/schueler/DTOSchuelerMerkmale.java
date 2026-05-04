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
@JsonPropertyOrder({"id", "idSchueler", "kuerzelMerkmal", "datumVon", "datumBis"})
public final class DTOSchuelerMerkmale {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerMerkmale e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes kuerzelMerkmal */
	public static final String QUERY_BY_KUERZELMERKMAL = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.kuerzelMerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes kuerzelMerkmal */
	public static final String QUERY_LIST_BY_KUERZELMERKMAL = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.kuerzelMerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes datumVon */
	public static final String QUERY_BY_DATUMVON = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.datumVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes datumVon */
	public static final String QUERY_LIST_BY_DATUMVON = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.datumVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes datumBis */
	public static final String QUERY_BY_DATUMBIS = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.datumBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes datumBis */
	public static final String QUERY_LIST_BY_DATUMBIS = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.datumBis IN ?1";

	/** ID des Eintrag bei besondere Merkmale zum Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** Schüler-ID des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long idSchueler;

	/** Das Kürzel des Merkmals des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Kurztext")
	@JsonProperty
	public String kuerzelMerkmal;

	/** Datum Beginn des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String datumVon;

	/** Datum Ende des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String datumBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idSchueler   der Wert für das Attribut idSchueler
	 */
	public DTOSchuelerMerkmale(final long id, final long idSchueler) {
		this.id = id;
		this.idSchueler = idSchueler;
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
		DTOSchuelerMerkmale other = (DTOSchuelerMerkmale) obj;
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
		return "DTOSchuelerMerkmale(id=" + this.id + ", idSchueler=" + this.idSchueler + ", kuerzelMerkmal=" + this.kuerzelMerkmal + ", datumVon=" + this.datumVon + ", datumBis=" + this.datumBis + ")";
	}

}
