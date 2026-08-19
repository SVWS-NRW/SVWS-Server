package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerPersonaldatenLehramtFachrichtung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerPersonaldatenLehramtFachrichtung")
@JsonPropertyOrder({"id", "idLehramt", "idFachrichtung", "idAnerkennungsgrund"})
public final class DTOLehrerPersonaldatenLehramtFachrichtung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLehramt */
	public static final String QUERY_BY_IDLEHRAMT = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.idLehramt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLehramt */
	public static final String QUERY_LIST_BY_IDLEHRAMT = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.idLehramt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idFachrichtung */
	public static final String QUERY_BY_IDFACHRICHTUNG = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.idFachrichtung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idFachrichtung */
	public static final String QUERY_LIST_BY_IDFACHRICHTUNG = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.idFachrichtung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAnerkennungsgrund */
	public static final String QUERY_BY_IDANERKENNUNGSGRUND = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.idAnerkennungsgrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAnerkennungsgrund */
	public static final String QUERY_LIST_BY_IDANERKENNUNGSGRUND = "SELECT e FROM DTOLehrerPersonaldatenLehramtFachrichtung e WHERE e.idAnerkennungsgrund IN ?1";

	/** Eine eindeutige ID für den Eintrag zu der Fachrichtung zu einem Lehramt eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** Die ID des Lehramtseintrags des Lehrers zu der die Fachrichtung gehört */
	@Column(name = "Lehreramt_ID")
	@JsonProperty
	public long idLehramt;

	/** Die ID der Fachrichtung aus dem zugehörigen Statistik-Katalog */
	@Column(name = "Fachrichtung_Katalog_ID")
	@JsonProperty
	public long idFachrichtung;

	/** Die ID des Anerkennungsgrundes für die Fachrichtung des Lehrers aus dem zugehörigen Statistik-Katalog */
	@Column(name = "FachrichtungAnerkennung_Katalog_ID")
	@JsonProperty
	public Long idAnerkennungsgrund;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerPersonaldatenLehramtFachrichtung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerPersonaldatenLehramtFachrichtung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerPersonaldatenLehramtFachrichtung ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idLehramt   der Wert für das Attribut idLehramt
	 * @param idFachrichtung   der Wert für das Attribut idFachrichtung
	 */
	public DTOLehrerPersonaldatenLehramtFachrichtung(final long id, final long idLehramt, final long idFachrichtung) {
		this.id = id;
		this.idLehramt = idLehramt;
		this.idFachrichtung = idFachrichtung;
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
		DTOLehrerPersonaldatenLehramtFachrichtung other = (DTOLehrerPersonaldatenLehramtFachrichtung) obj;
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
		return "DTOLehrerPersonaldatenLehramtFachrichtung(id=" + this.id + ", idLehramt=" + this.idLehramt + ", idFachrichtung=" + this.idFachrichtung + ", idAnerkennungsgrund=" + this.idAnerkennungsgrund + ")";
	}

}
