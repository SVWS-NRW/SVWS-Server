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
 * Diese Klasse dient als DTO für die Datenbanktabelle TimestampsSchuelerZP10.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TimestampsSchuelerZP10")
@JsonPropertyOrder({"ID", "tsVornote", "tsNoteSchriftlichePruefung", "tsMuendlichePruefung", "tsMuendlichePruefungFreiwillig", "tsNoteMuendlichePruefung", "tsAbschlussnote"})
public final class DTOTimestampsSchuelerZP10 {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTimestampsSchuelerZP10 e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsVornote */
	public static final String QUERY_BY_TSVORNOTE = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsVornote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsVornote */
	public static final String QUERY_LIST_BY_TSVORNOTE = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsVornote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsNoteSchriftlichePruefung */
	public static final String QUERY_BY_TSNOTESCHRIFTLICHEPRUEFUNG = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsNoteSchriftlichePruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsNoteSchriftlichePruefung */
	public static final String QUERY_LIST_BY_TSNOTESCHRIFTLICHEPRUEFUNG = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsNoteSchriftlichePruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsMuendlichePruefung */
	public static final String QUERY_BY_TSMUENDLICHEPRUEFUNG = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsMuendlichePruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsMuendlichePruefung */
	public static final String QUERY_LIST_BY_TSMUENDLICHEPRUEFUNG = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsMuendlichePruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsMuendlichePruefungFreiwillig */
	public static final String QUERY_BY_TSMUENDLICHEPRUEFUNGFREIWILLIG = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsMuendlichePruefungFreiwillig = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsMuendlichePruefungFreiwillig */
	public static final String QUERY_LIST_BY_TSMUENDLICHEPRUEFUNGFREIWILLIG = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsMuendlichePruefungFreiwillig IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsNoteMuendlichePruefung */
	public static final String QUERY_BY_TSNOTEMUENDLICHEPRUEFUNG = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsNoteMuendlichePruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsNoteMuendlichePruefung */
	public static final String QUERY_LIST_BY_TSNOTEMUENDLICHEPRUEFUNG = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsNoteMuendlichePruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsAbschlussnote */
	public static final String QUERY_BY_TSABSCHLUSSNOTE = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsAbschlussnote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsAbschlussnote */
	public static final String QUERY_LIST_BY_TSABSCHLUSSNOTE = "SELECT e FROM DTOTimestampsSchuelerZP10 e WHERE e.tsAbschlussnote IN ?1";

	/** ID des Facheintrags für den ZP10 Abschluss */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Zeitstempel der letzten Änderung an der Vornote. */
	@Column(name = "tsVornote")
	@JsonProperty
	public String tsVornote;

	/** Der Zeitstempel der letzten Änderung an der Note zur schriftlichen Prüfung. */
	@Column(name = "tsNoteSchriftlichePruefung")
	@JsonProperty
	public String tsNoteSchriftlichePruefung;

	/** Der Zeitstempel der letzten Änderung zu der Information, ob eine mündliche Prüfung nötig ist oder nicht. */
	@Column(name = "tsMuendlichePruefung")
	@JsonProperty
	public String tsMuendlichePruefung;

	/** Der Zeitstempel der letzten Änderung zu der Information, ob eine freiwillige mündliche Prüfung gewählt wurde oder nicht. */
	@Column(name = "tsMuendlichePruefungFreiwillig")
	@JsonProperty
	public String tsMuendlichePruefungFreiwillig;

	/** Der Zeitstempel der letzten Änderung an der Note zur mündlichen Prüfung. */
	@Column(name = "tsNoteMuendlichePruefung")
	@JsonProperty
	public String tsNoteMuendlichePruefung;

	/** Der Zeitstempel der letzten Änderung an der Abschlussnote. */
	@Column(name = "tsAbschlussnote")
	@JsonProperty
	public String tsAbschlussnote;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerZP10 ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTimestampsSchuelerZP10() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerZP10 ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsVornote   der Wert für das Attribut tsVornote
	 * @param tsNoteSchriftlichePruefung   der Wert für das Attribut tsNoteSchriftlichePruefung
	 * @param tsMuendlichePruefung   der Wert für das Attribut tsMuendlichePruefung
	 * @param tsMuendlichePruefungFreiwillig   der Wert für das Attribut tsMuendlichePruefungFreiwillig
	 * @param tsNoteMuendlichePruefung   der Wert für das Attribut tsNoteMuendlichePruefung
	 * @param tsAbschlussnote   der Wert für das Attribut tsAbschlussnote
	 */
	public DTOTimestampsSchuelerZP10(final long ID, final String tsVornote, final String tsNoteSchriftlichePruefung, final String tsMuendlichePruefung, final String tsMuendlichePruefungFreiwillig, final String tsNoteMuendlichePruefung, final String tsAbschlussnote) {
		this.ID = ID;
		if (tsVornote == null) {
			throw new NullPointerException("tsVornote must not be null");
		}
		this.tsVornote = tsVornote;
		if (tsNoteSchriftlichePruefung == null) {
			throw new NullPointerException("tsNoteSchriftlichePruefung must not be null");
		}
		this.tsNoteSchriftlichePruefung = tsNoteSchriftlichePruefung;
		if (tsMuendlichePruefung == null) {
			throw new NullPointerException("tsMuendlichePruefung must not be null");
		}
		this.tsMuendlichePruefung = tsMuendlichePruefung;
		if (tsMuendlichePruefungFreiwillig == null) {
			throw new NullPointerException("tsMuendlichePruefungFreiwillig must not be null");
		}
		this.tsMuendlichePruefungFreiwillig = tsMuendlichePruefungFreiwillig;
		if (tsNoteMuendlichePruefung == null) {
			throw new NullPointerException("tsNoteMuendlichePruefung must not be null");
		}
		this.tsNoteMuendlichePruefung = tsNoteMuendlichePruefung;
		if (tsAbschlussnote == null) {
			throw new NullPointerException("tsAbschlussnote must not be null");
		}
		this.tsAbschlussnote = tsAbschlussnote;
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
		DTOTimestampsSchuelerZP10 other = (DTOTimestampsSchuelerZP10) obj;
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
		return "DTOTimestampsSchuelerZP10(ID=" + this.ID + ", tsVornote=" + this.tsVornote + ", tsNoteSchriftlichePruefung=" + this.tsNoteSchriftlichePruefung + ", tsMuendlichePruefung=" + this.tsMuendlichePruefung + ", tsMuendlichePruefungFreiwillig=" + this.tsMuendlichePruefungFreiwillig + ", tsNoteMuendlichePruefung=" + this.tsNoteMuendlichePruefung + ", tsAbschlussnote=" + this.tsAbschlussnote + ")";
	}

}
