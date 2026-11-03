package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle StatistikKommentar.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "StatistikKommentar")
@JsonPropertyOrder({"id", "idSchuljahresabschnitt", "kommentar"})
public final class DTOStatistikKommentar {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStatistikKommentar e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStatistikKommentar e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStatistikKommentar e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStatistikKommentar e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStatistikKommentar e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStatistikKommentar e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchuljahresabschnitt */
	public static final String QUERY_BY_IDSCHULJAHRESABSCHNITT = "SELECT e FROM DTOStatistikKommentar e WHERE e.idSchuljahresabschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchuljahresabschnitt */
	public static final String QUERY_LIST_BY_IDSCHULJAHRESABSCHNITT = "SELECT e FROM DTOStatistikKommentar e WHERE e.idSchuljahresabschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes kommentar */
	public static final String QUERY_BY_KOMMENTAR = "SELECT e FROM DTOStatistikKommentar e WHERE e.kommentar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes kommentar */
	public static final String QUERY_LIST_BY_KOMMENTAR = "SELECT e FROM DTOStatistikKommentar e WHERE e.kommentar IN ?1";

	/** Die ID des Statistik-Kommentars */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Die ID des Schuljahresabschnitts, auf den sich der Kommentar bezieht */
	@Column(name = "idSchuljahresabschnitt")
	@JsonProperty
	public long idSchuljahresabschnitt;

	/** Der Kommentar zur Statistik */
	@Column(name = "kommentar")
	@JsonProperty
	public String kommentar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatistikKommentar ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStatistikKommentar() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatistikKommentar ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idSchuljahresabschnitt   der Wert für das Attribut idSchuljahresabschnitt
	 * @param kommentar   der Wert für das Attribut kommentar
	 */
	public DTOStatistikKommentar(final long id, final long idSchuljahresabschnitt, final String kommentar) {
		this.id = id;
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		if (kommentar == null) {
			throw new NullPointerException("kommentar must not be null");
		}
		this.kommentar = kommentar;
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
		DTOStatistikKommentar other = (DTOStatistikKommentar) obj;
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
		return "DTOStatistikKommentar(id=" + this.id + ", idSchuljahresabschnitt=" + this.idSchuljahresabschnitt + ", kommentar=" + this.kommentar + ")";
	}

}
