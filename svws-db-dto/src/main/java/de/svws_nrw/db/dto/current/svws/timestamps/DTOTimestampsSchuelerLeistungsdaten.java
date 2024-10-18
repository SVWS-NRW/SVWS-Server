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
 * Diese Klasse dient als DTO für die Datenbanktabelle TimestampsSchuelerLeistungsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TimestampsSchuelerLeistungsdaten")
@JsonPropertyOrder({"ID", "tsNotenKrz", "tsNotenKrzQuartal", "tsFehlStd", "tsuFehlStd", "tsLernentw", "tsWarnung"})
public final class DTOTimestampsSchuelerLeistungsdaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsNotenKrz */
	public static final String QUERY_BY_TSNOTENKRZ = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsNotenKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsNotenKrz */
	public static final String QUERY_LIST_BY_TSNOTENKRZ = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsNotenKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsNotenKrzQuartal */
	public static final String QUERY_BY_TSNOTENKRZQUARTAL = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsNotenKrzQuartal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsNotenKrzQuartal */
	public static final String QUERY_LIST_BY_TSNOTENKRZQUARTAL = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsNotenKrzQuartal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsFehlStd */
	public static final String QUERY_BY_TSFEHLSTD = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsFehlStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsFehlStd */
	public static final String QUERY_LIST_BY_TSFEHLSTD = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsFehlStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsuFehlStd */
	public static final String QUERY_BY_TSUFEHLSTD = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsuFehlStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsuFehlStd */
	public static final String QUERY_LIST_BY_TSUFEHLSTD = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsuFehlStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsLernentw */
	public static final String QUERY_BY_TSLERNENTW = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsLernentw = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsLernentw */
	public static final String QUERY_LIST_BY_TSLERNENTW = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsLernentw IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsWarnung */
	public static final String QUERY_BY_TSWARNUNG = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsWarnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsWarnung */
	public static final String QUERY_LIST_BY_TSWARNUNG = "SELECT e FROM DTOTimestampsSchuelerLeistungsdaten e WHERE e.tsWarnung IN ?1";

	/** ID der Leistungsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Zeitstempel der letzten Änderung an der Note. */
	@Column(name = "tsNotenKrz")
	@JsonProperty
	public String tsNotenKrz;

	/** Der Zeitstempel der letzten Änderung an der Note. */
	@Column(name = "tsNotenKrzQuartal")
	@JsonProperty
	public String tsNotenKrzQuartal;

	/** Der Zeitstempel der letzten Änderung an den Fehlstunden. */
	@Column(name = "tsFehlStd")
	@JsonProperty
	public String tsFehlStd;

	/** Der Zeitstempel der letzten Änderung an den unentschuldigten Fehlstunden. */
	@Column(name = "tsuFehlStd")
	@JsonProperty
	public String tsuFehlStd;

	/** Der Zeitstempel der letzten Änderung an den fachbezogenen Bemerkungen. */
	@Column(name = "tsLernentw")
	@JsonProperty
	public String tsLernentw;

	/** Der Zeitstempel der letzten Änderung, ob gemahnt wird. */
	@Column(name = "tsWarnung")
	@JsonProperty
	public String tsWarnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTimestampsSchuelerLeistungsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsNotenKrz   der Wert für das Attribut tsNotenKrz
	 * @param tsNotenKrzQuartal   der Wert für das Attribut tsNotenKrzQuartal
	 * @param tsFehlStd   der Wert für das Attribut tsFehlStd
	 * @param tsuFehlStd   der Wert für das Attribut tsuFehlStd
	 * @param tsLernentw   der Wert für das Attribut tsLernentw
	 * @param tsWarnung   der Wert für das Attribut tsWarnung
	 */
	public DTOTimestampsSchuelerLeistungsdaten(final long ID, final String tsNotenKrz, final String tsNotenKrzQuartal, final String tsFehlStd, final String tsuFehlStd, final String tsLernentw, final String tsWarnung) {
		this.ID = ID;
		if (tsNotenKrz == null) {
			throw new NullPointerException("tsNotenKrz must not be null");
		}
		this.tsNotenKrz = tsNotenKrz;
		if (tsNotenKrzQuartal == null) {
			throw new NullPointerException("tsNotenKrzQuartal must not be null");
		}
		this.tsNotenKrzQuartal = tsNotenKrzQuartal;
		if (tsFehlStd == null) {
			throw new NullPointerException("tsFehlStd must not be null");
		}
		this.tsFehlStd = tsFehlStd;
		if (tsuFehlStd == null) {
			throw new NullPointerException("tsuFehlStd must not be null");
		}
		this.tsuFehlStd = tsuFehlStd;
		if (tsLernentw == null) {
			throw new NullPointerException("tsLernentw must not be null");
		}
		this.tsLernentw = tsLernentw;
		if (tsWarnung == null) {
			throw new NullPointerException("tsWarnung must not be null");
		}
		this.tsWarnung = tsWarnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOTimestampsSchuelerLeistungsdaten other = (DTOTimestampsSchuelerLeistungsdaten) obj;
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
		return "DTOTimestampsSchuelerLeistungsdaten(ID=" + this.ID + ", tsNotenKrz=" + this.tsNotenKrz + ", tsNotenKrzQuartal=" + this.tsNotenKrzQuartal + ", tsFehlStd=" + this.tsFehlStd + ", tsuFehlStd=" + this.tsuFehlStd + ", tsLernentw=" + this.tsLernentw + ", tsWarnung=" + this.tsWarnung + ")";
	}

}
