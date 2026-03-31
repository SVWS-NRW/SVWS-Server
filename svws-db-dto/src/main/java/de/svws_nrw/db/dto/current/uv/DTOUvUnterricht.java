package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Unterrichte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Unterrichte")
@JsonPropertyOrder({"ID", "Planungsabschnitt_ID", "ZeitrasterEintrag_ID", "Lerngruppe_ID"})
public final class DTOUvUnterricht {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvUnterricht e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvUnterricht e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvUnterricht e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvUnterricht e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvUnterricht e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvUnterricht e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvUnterricht e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvUnterricht e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeitrasterEintrag_ID */
	public static final String QUERY_BY_ZEITRASTEREINTRAG_ID = "SELECT e FROM DTOUvUnterricht e WHERE e.ZeitrasterEintrag_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeitrasterEintrag_ID */
	public static final String QUERY_LIST_BY_ZEITRASTEREINTRAG_ID = "SELECT e FROM DTOUvUnterricht e WHERE e.ZeitrasterEintrag_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lerngruppe_ID */
	public static final String QUERY_BY_LERNGRUPPE_ID = "SELECT e FROM DTOUvUnterricht e WHERE e.Lerngruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lerngruppe_ID */
	public static final String QUERY_LIST_BY_LERNGRUPPE_ID = "SELECT e FROM DTOUvUnterricht e WHERE e.Lerngruppe_ID IN ?1";

	/** Eindeutige ID der Unterrichtseinheit (automatisch generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/** Fremdschlüssel auf den Zeitrastereintrag (Tabelle UV_Zeitraster_Eintraege) */
	@Column(name = "ZeitrasterEintrag_ID")
	@JsonProperty
	public Long ZeitrasterEintrag_ID;

	/** Fremdschlüssel auf die Lerngruppe (Tabelle UV_Lerngruppen) */
	@Column(name = "Lerngruppe_ID")
	@JsonProperty
	public long Lerngruppe_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvUnterricht ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvUnterricht() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvUnterricht ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Lerngruppe_ID   der Wert für das Attribut Lerngruppe_ID
	 */
	public DTOUvUnterricht(final long ID, final long Planungsabschnitt_ID, final long Lerngruppe_ID) {
		this.ID = ID;
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Lerngruppe_ID = Lerngruppe_ID;
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
		DTOUvUnterricht other = (DTOUvUnterricht) obj;
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
		return "DTOUvUnterricht(ID=" + this.ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ", ZeitrasterEintrag_ID=" + this.ZeitrasterEintrag_ID + ", Lerngruppe_ID=" + this.Lerngruppe_ID + ")";
	}

}
