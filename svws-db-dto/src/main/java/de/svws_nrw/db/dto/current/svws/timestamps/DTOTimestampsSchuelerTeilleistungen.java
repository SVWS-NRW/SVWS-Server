package de.svws_nrw.db.dto.current.svws.timestamps;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TimestampsSchuelerTeilleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TimestampsSchuelerTeilleistungen")
@JsonPropertyOrder({"ID", "tsDatum", "tsLehrer_ID", "tsArt_ID", "tsBemerkung", "tsNotenKrz"})
public final class DTOTimestampsSchuelerTeilleistungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsDatum */
	public static final String QUERY_BY_TSDATUM = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsDatum */
	public static final String QUERY_LIST_BY_TSDATUM = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsLehrer_ID */
	public static final String QUERY_BY_TSLEHRER_ID = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsLehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsLehrer_ID */
	public static final String QUERY_LIST_BY_TSLEHRER_ID = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsLehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsArt_ID */
	public static final String QUERY_BY_TSART_ID = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsArt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsArt_ID */
	public static final String QUERY_LIST_BY_TSART_ID = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsArt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsBemerkung */
	public static final String QUERY_BY_TSBEMERKUNG = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsBemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsBemerkung */
	public static final String QUERY_LIST_BY_TSBEMERKUNG = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsBemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsNotenKrz */
	public static final String QUERY_BY_TSNOTENKRZ = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsNotenKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsNotenKrz */
	public static final String QUERY_LIST_BY_TSNOTENKRZ = "SELECT e FROM DTOTimestampsSchuelerTeilleistungen e WHERE e.tsNotenKrz IN ?1";

	/** ID der Teilleistungen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Zeitstempel der letzten Änderung an dem Datum der Teilleistung, wann diese erbracht wurde. */
	@Column(name = "tsDatum")
	@JsonProperty
	public String tsDatum;

	/** Der Zeitstempel der letzten Änderung an der Lehrer-ID. */
	@Column(name = "tsLehrer_ID")
	@JsonProperty
	public String tsLehrer_ID;

	/** Der Zeitstempel der letzten Änderung der Teilleistungsart. */
	@Column(name = "tsArt_ID")
	@JsonProperty
	public String tsArt_ID;

	/** Der Zeitstempel der letzten Änderung an der Bemerkung. */
	@Column(name = "tsBemerkung")
	@JsonProperty
	public String tsBemerkung;

	/** Der Zeitstempel der letzten Änderung an der Note. */
	@Column(name = "tsNotenKrz")
	@JsonProperty
	public String tsNotenKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerTeilleistungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTimestampsSchuelerTeilleistungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerTeilleistungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsDatum   der Wert für das Attribut tsDatum
	 * @param tsLehrer_ID   der Wert für das Attribut tsLehrer_ID
	 * @param tsArt_ID   der Wert für das Attribut tsArt_ID
	 * @param tsBemerkung   der Wert für das Attribut tsBemerkung
	 * @param tsNotenKrz   der Wert für das Attribut tsNotenKrz
	 */
	public DTOTimestampsSchuelerTeilleistungen(final long ID, final String tsDatum, final String tsLehrer_ID, final String tsArt_ID, final String tsBemerkung, final String tsNotenKrz) {
		this.ID = ID;
		if (tsDatum == null) {
			throw new NullPointerException("tsDatum must not be null");
		}
		this.tsDatum = tsDatum;
		if (tsLehrer_ID == null) {
			throw new NullPointerException("tsLehrer_ID must not be null");
		}
		this.tsLehrer_ID = tsLehrer_ID;
		if (tsArt_ID == null) {
			throw new NullPointerException("tsArt_ID must not be null");
		}
		this.tsArt_ID = tsArt_ID;
		if (tsBemerkung == null) {
			throw new NullPointerException("tsBemerkung must not be null");
		}
		this.tsBemerkung = tsBemerkung;
		if (tsNotenKrz == null) {
			throw new NullPointerException("tsNotenKrz must not be null");
		}
		this.tsNotenKrz = tsNotenKrz;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOTimestampsSchuelerTeilleistungen other = (DTOTimestampsSchuelerTeilleistungen) obj;
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
		return "DTOTimestampsSchuelerTeilleistungen(ID=" + this.ID + ", tsDatum=" + this.tsDatum + ", tsLehrer_ID=" + this.tsLehrer_ID + ", tsArt_ID=" + this.tsArt_ID + ", tsBemerkung=" + this.tsBemerkung + ", tsNotenKrz=" + this.tsNotenKrz + ")";
	}

}
