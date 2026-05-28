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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Lerngruppen_Lehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Lerngruppen_Lehrer")
@JsonPropertyOrder({"ID", "Planungsabschnitt_ID", "Lerngruppe_ID", "Lehrer_ID", "Reihenfolge", "Wochenstunden", "WochenstundenAngerechnet"})
public final class DTOUvLerngruppenLehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvLerngruppenLehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lerngruppe_ID */
	public static final String QUERY_BY_LERNGRUPPE_ID = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Lerngruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lerngruppe_ID */
	public static final String QUERY_LIST_BY_LERNGRUPPE_ID = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Lerngruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Reihenfolge */
	public static final String QUERY_BY_REIHENFOLGE = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Reihenfolge = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Reihenfolge */
	public static final String QUERY_LIST_BY_REIHENFOLGE = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Reihenfolge IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochenstunden */
	public static final String QUERY_BY_WOCHENSTUNDEN = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochenstunden */
	public static final String QUERY_LIST_BY_WOCHENSTUNDEN = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.Wochenstunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenstundenAngerechnet */
	public static final String QUERY_BY_WOCHENSTUNDENANGERECHNET = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.WochenstundenAngerechnet = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenstundenAngerechnet */
	public static final String QUERY_LIST_BY_WOCHENSTUNDENANGERECHNET = "SELECT e FROM DTOUvLerngruppenLehrer e WHERE e.WochenstundenAngerechnet IN ?1";

	/** ID des Lerngruppen-Lehrers (generiert, planungsspezifisch) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/** ID der UV_Lerngruppe */
	@Column(name = "Lerngruppe_ID")
	@JsonProperty
	public long Lerngruppe_ID;

	/** ID des Lehrers, welcher der Lerngruppe zugeordnet ist */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Eine Reihenfolge für die Lehrer, z.B. zur Unterscheidung der eigentlichen Hauptlehrkraft (z.B. 1) und einer Zusatzkraft (z.B. 2) */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public int Reihenfolge;

	/** Die Anzahl der Wochenstunden, für die der Lehrer in der Lerngruppe eingesetzt wird */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public double Wochenstunden;

	/** Die Anzahl der Wochenstunden, die deputatswirksam sind */
	@Column(name = "WochenstundenAngerechnet")
	@JsonProperty
	public double WochenstundenAngerechnet;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvLerngruppenLehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvLerngruppenLehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvLerngruppenLehrer ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Lerngruppe_ID   der Wert für das Attribut Lerngruppe_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Reihenfolge   der Wert für das Attribut Reihenfolge
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 * @param WochenstundenAngerechnet   der Wert für das Attribut WochenstundenAngerechnet
	 */
	public DTOUvLerngruppenLehrer(final long ID, final long Planungsabschnitt_ID, final long Lerngruppe_ID, final long Lehrer_ID, final int Reihenfolge, final double Wochenstunden, final double WochenstundenAngerechnet) {
		this.ID = ID;
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Lerngruppe_ID = Lerngruppe_ID;
		this.Lehrer_ID = Lehrer_ID;
		this.Reihenfolge = Reihenfolge;
		this.Wochenstunden = Wochenstunden;
		this.WochenstundenAngerechnet = WochenstundenAngerechnet;
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
		DTOUvLerngruppenLehrer other = (DTOUvLerngruppenLehrer) obj;
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
		return "DTOUvLerngruppenLehrer(ID=" + this.ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ", Lerngruppe_ID=" + this.Lerngruppe_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Reihenfolge=" + this.Reihenfolge + ", Wochenstunden=" + this.Wochenstunden + ", WochenstundenAngerechnet=" + this.WochenstundenAngerechnet + ")";
	}

}
