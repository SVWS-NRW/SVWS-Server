package de.svws_nrw.db.dto.current.svws.timestamps;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TimestampsSchuelerZuweisungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOTimestampsSchuelerZuweisungenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TimestampsSchuelerZuweisungen")
@JsonPropertyOrder({"Abschnitt_ID", "Fach_ID", "tsKursart"})
public final class DTOTimestampsSchuelerZuweisungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e WHERE e.Abschnitt_ID = ?1 AND e.Fach_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e WHERE e.Abschnitt_ID IS NOT NULL AND e.Fach_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsKursart */
	public static final String QUERY_BY_TSKURSART = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e WHERE e.tsKursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsKursart */
	public static final String QUERY_LIST_BY_TSKURSART = "SELECT e FROM DTOTimestampsSchuelerZuweisungen e WHERE e.tsKursart IN ?1";

	/** LernabschnittsID der Zuweisung (E G Kurse GE und PS SK) */
	@Id
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** FachID der Zuweisung */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Der Zeitstempel (UTC) der letzten Änderung an der Kursart-Zuweisung. */
	@Column(name = "tsKursart")
	@JsonProperty
	public String tsKursart;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerZuweisungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTimestampsSchuelerZuweisungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerZuweisungen ohne eine Initialisierung der Attribute.
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param tsKursart   der Wert für das Attribut tsKursart
	 */
	public DTOTimestampsSchuelerZuweisungen(final long Abschnitt_ID, final long Fach_ID, final String tsKursart) {
		this.Abschnitt_ID = Abschnitt_ID;
		this.Fach_ID = Fach_ID;
		if (tsKursart == null) {
			throw new NullPointerException("tsKursart must not be null");
		}
		this.tsKursart = tsKursart;
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
		DTOTimestampsSchuelerZuweisungen other = (DTOTimestampsSchuelerZuweisungen) obj;
		if (Abschnitt_ID != other.Abschnitt_ID) {
			return false;
		}
		return Fach_ID == other.Fach_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Abschnitt_ID);

		result = prime * result + Long.hashCode(Fach_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOTimestampsSchuelerZuweisungen(Abschnitt_ID=" + this.Abschnitt_ID + ", Fach_ID=" + this.Fach_ID + ", tsKursart=" + this.tsKursart + ")";
	}

}
