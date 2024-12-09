package de.svws_nrw.db.dto.current.schild.stundenplan;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Unterricht.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Unterricht")
@JsonPropertyOrder({"ID", "Zeitraster_ID", "Wochentyp", "Kurs_ID", "Fach_ID"})
public final class DTOStundenplanUnterricht {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundenplanUnterricht e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zeitraster_ID */
	public static final String QUERY_BY_ZEITRASTER_ID = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.Zeitraster_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zeitraster_ID */
	public static final String QUERY_LIST_BY_ZEITRASTER_ID = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.Zeitraster_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochentyp */
	public static final String QUERY_BY_WOCHENTYP = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.Wochentyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochentyp */
	public static final String QUERY_LIST_BY_WOCHENTYP = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.Wochentyp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurs_ID */
	public static final String QUERY_BY_KURS_ID = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurs_ID */
	public static final String QUERY_LIST_BY_KURS_ID = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOStundenplanUnterricht e WHERE e.Fach_ID IN ?1";

	/** Die eindeutige ID für diese Zuordnung des Unterricht-Eintrages zu einem Stundenplan */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des belegten Zeitraster-Eintrags */
	@Column(name = "Zeitraster_ID")
	@JsonProperty
	public long Zeitraster_ID;

	/** Gibt an, ob es sich um einen Eintrag für jede Woche handelt (0) oder ob es sich um einen unterschiedlichen (!) Eintrag für eine A- bzw. B-Wochen (1 bzw. 2) handelt */
	@Column(name = "Wochentyp")
	@JsonProperty
	public int Wochentyp;

	/** Die ID des Kurses, falls der Unterricht nicht im Klassenverband stattfindet */
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** Die ID des Faches, in dem der Unterricht stattfindet */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterricht ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanUnterricht() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterricht ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 * @param Wochentyp   der Wert für das Attribut Wochentyp
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOStundenplanUnterricht(final long ID, final long Zeitraster_ID, final int Wochentyp, final long Fach_ID) {
		this.ID = ID;
		this.Zeitraster_ID = Zeitraster_ID;
		this.Wochentyp = Wochentyp;
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanUnterricht other = (DTOStundenplanUnterricht) obj;
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
		return "DTOStundenplanUnterricht(ID=" + this.ID + ", Zeitraster_ID=" + this.Zeitraster_ID + ", Wochentyp=" + this.Wochentyp + ", Kurs_ID=" + this.Kurs_ID + ", Fach_ID=" + this.Fach_ID + ")";
	}

}
