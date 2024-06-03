package de.svws_nrw.db.dto.current.svws.dav;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumUhrzeitConverter;


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
import de.svws_nrw.csv.converter.current.DatumUhrzeitConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumUhrzeitConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle DavRessources.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "DavRessources")
@JsonPropertyOrder({"ID", "DavRessourceCollection_ID", "UID", "lastModified", "KalenderTyp", "KalenderStart", "KalenderEnde", "ressource", "geloeschtam"})
public final class DTODavRessource {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTODavRessource e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTODavRessource e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTODavRessource e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTODavRessource e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTODavRessource e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTODavRessource e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DavRessourceCollection_ID */
	public static final String QUERY_BY_DAVRESSOURCECOLLECTION_ID = "SELECT e FROM DTODavRessource e WHERE e.DavRessourceCollection_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DavRessourceCollection_ID */
	public static final String QUERY_LIST_BY_DAVRESSOURCECOLLECTION_ID = "SELECT e FROM DTODavRessource e WHERE e.DavRessourceCollection_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes UID */
	public static final String QUERY_BY_UID = "SELECT e FROM DTODavRessource e WHERE e.UID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes UID */
	public static final String QUERY_LIST_BY_UID = "SELECT e FROM DTODavRessource e WHERE e.UID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes lastModified */
	public static final String QUERY_BY_LASTMODIFIED = "SELECT e FROM DTODavRessource e WHERE e.lastModified = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes lastModified */
	public static final String QUERY_LIST_BY_LASTMODIFIED = "SELECT e FROM DTODavRessource e WHERE e.lastModified IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KalenderTyp */
	public static final String QUERY_BY_KALENDERTYP = "SELECT e FROM DTODavRessource e WHERE e.KalenderTyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KalenderTyp */
	public static final String QUERY_LIST_BY_KALENDERTYP = "SELECT e FROM DTODavRessource e WHERE e.KalenderTyp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KalenderStart */
	public static final String QUERY_BY_KALENDERSTART = "SELECT e FROM DTODavRessource e WHERE e.KalenderStart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KalenderStart */
	public static final String QUERY_LIST_BY_KALENDERSTART = "SELECT e FROM DTODavRessource e WHERE e.KalenderStart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KalenderEnde */
	public static final String QUERY_BY_KALENDERENDE = "SELECT e FROM DTODavRessource e WHERE e.KalenderEnde = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KalenderEnde */
	public static final String QUERY_LIST_BY_KALENDERENDE = "SELECT e FROM DTODavRessource e WHERE e.KalenderEnde IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ressource */
	public static final String QUERY_BY_RESSOURCE = "SELECT e FROM DTODavRessource e WHERE e.ressource = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ressource */
	public static final String QUERY_LIST_BY_RESSOURCE = "SELECT e FROM DTODavRessource e WHERE e.ressource IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes geloeschtam */
	public static final String QUERY_BY_GELOESCHTAM = "SELECT e FROM DTODavRessource e WHERE e.geloeschtam = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes geloeschtam */
	public static final String QUERY_LIST_BY_GELOESCHTAM = "SELECT e FROM DTODavRessource e WHERE e.geloeschtam IN ?1";

	/** ID der Dav Ressource */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Id der Ressourcensammlung, deren Teil diese Ressource ist */
	@Column(name = "DavRessourceCollection_ID")
	@JsonProperty
	public long DavRessourceCollection_ID;

	/** Die UID der Ressource */
	@Column(name = "UID")
	@JsonProperty
	public String UID;

	/** Das Datum an dem die Ressource zuletzt geändert wurde, als Synctoken einsetzbar */
	@Column(name = "lastModified")
	@JsonProperty
	@Convert(converter = DatumUhrzeitConverter.class)
	@JsonSerialize(using = DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using = DatumUhrzeitConverterDeserializer.class)
	public String lastModified;

	/** Die Art der Kalenderressource, wenn es eine Kalenderressource ist */
	@Column(name = "KalenderTyp")
	@JsonProperty
	public String KalenderTyp;

	/** Der Start der Kalenderressource, wenn es eine Kalenderressource ist */
	@Column(name = "KalenderStart")
	@JsonProperty
	@Convert(converter = DatumUhrzeitConverter.class)
	@JsonSerialize(using = DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using = DatumUhrzeitConverterDeserializer.class)
	public String KalenderStart;

	/** Das Ende der Kalenderressource, wenn es eines Kalenderressource ist */
	@Column(name = "KalenderEnde")
	@JsonProperty
	@Convert(converter = DatumUhrzeitConverter.class)
	@JsonSerialize(using = DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using = DatumUhrzeitConverterDeserializer.class)
	public String KalenderEnde;

	/** Die Daten der Ressource */
	@Column(name = "ressource")
	@JsonProperty
	public byte[] ressource;

	/** Der Zeitpunkt, an dem diese ggf. Ressource gelöscht wurde. */
	@Column(name = "geloeschtam")
	@JsonProperty
	@Convert(converter = DatumUhrzeitConverter.class)
	@JsonSerialize(using = DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using = DatumUhrzeitConverterDeserializer.class)
	public String geloeschtam;

	/**
	 * Erstellt ein neues Objekt der Klasse DTODavRessource ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTODavRessource() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTODavRessource ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param DavRessourceCollection_ID   der Wert für das Attribut DavRessourceCollection_ID
	 * @param UID   der Wert für das Attribut UID
	 * @param lastModified   der Wert für das Attribut lastModified
	 * @param KalenderTyp   der Wert für das Attribut KalenderTyp
	 * @param KalenderStart   der Wert für das Attribut KalenderStart
	 * @param KalenderEnde   der Wert für das Attribut KalenderEnde
	 * @param ressource   der Wert für das Attribut ressource
	 */
	public DTODavRessource(final long ID, final long DavRessourceCollection_ID, final String UID, final String lastModified, final String KalenderTyp, final String KalenderStart, final String KalenderEnde, final byte[] ressource) {
		this.ID = ID;
		this.DavRessourceCollection_ID = DavRessourceCollection_ID;
		if (UID == null) {
			throw new NullPointerException("UID must not be null");
		}
		this.UID = UID;
		if (lastModified == null) {
			throw new NullPointerException("lastModified must not be null");
		}
		this.lastModified = lastModified;
		if (KalenderTyp == null) {
			throw new NullPointerException("KalenderTyp must not be null");
		}
		this.KalenderTyp = KalenderTyp;
		if (KalenderStart == null) {
			throw new NullPointerException("KalenderStart must not be null");
		}
		this.KalenderStart = KalenderStart;
		if (KalenderEnde == null) {
			throw new NullPointerException("KalenderEnde must not be null");
		}
		this.KalenderEnde = KalenderEnde;
		if (ressource == null) {
			throw new NullPointerException("ressource must not be null");
		}
		this.ressource = ressource;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTODavRessource other = (DTODavRessource) obj;
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
		return "DTODavRessource(ID=" + this.ID + ", DavRessourceCollection_ID=" + this.DavRessourceCollection_ID + ", UID=" + this.UID + ", lastModified=" + this.lastModified + ", KalenderTyp=" + this.KalenderTyp + ", KalenderStart=" + this.KalenderStart + ", KalenderEnde=" + this.KalenderEnde + ", ressource=" + this.ressource + ", geloeschtam=" + this.geloeschtam + ")";
	}

}
