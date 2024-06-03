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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Kalenderwochen_Zuordnung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Kalenderwochen_Zuordnung")
@JsonPropertyOrder({"ID", "Stundenplan_ID", "Jahr", "KW", "Wochentyp"})
public final class DTOStundenplanKalenderwochenZuordnung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundenplan_ID */
	public static final String QUERY_BY_STUNDENPLAN_ID = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Stundenplan_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundenplan_ID */
	public static final String QUERY_LIST_BY_STUNDENPLAN_ID = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Stundenplan_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KW */
	public static final String QUERY_BY_KW = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.KW = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KW */
	public static final String QUERY_LIST_BY_KW = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.KW IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochentyp */
	public static final String QUERY_BY_WOCHENTYP = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Wochentyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochentyp */
	public static final String QUERY_LIST_BY_WOCHENTYP = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Wochentyp IN ?1";

	/** Eine ID, die einen Eintrag für die Kalenderwochen-Zuordnung eindeutig identifiziert. */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Stundenplans, dem die Kalenderwochenzuordnung zugeordnet ist */
	@Column(name = "Stundenplan_ID")
	@JsonProperty
	public long Stundenplan_ID;

	/** Das Jahr (nicht Schuljahr) zu dem die Kalenderwoche gehört. */
	@Column(name = "Jahr")
	@JsonProperty
	public int Jahr;

	/** Die Kalenderwoche im Jahr. */
	@Column(name = "KW")
	@JsonProperty
	public int KW;

	/** Gibt den Wochentyp an, der der Kalenderwoche zugeordnet ist (z.B. eine A- bzw. B-Wochen, d.h. 1 bzw. 2) */
	@Column(name = "Wochentyp")
	@JsonProperty
	public int Wochentyp;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanKalenderwochenZuordnung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanKalenderwochenZuordnung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanKalenderwochenZuordnung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundenplan_ID   der Wert für das Attribut Stundenplan_ID
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param KW   der Wert für das Attribut KW
	 * @param Wochentyp   der Wert für das Attribut Wochentyp
	 */
	public DTOStundenplanKalenderwochenZuordnung(final long ID, final long Stundenplan_ID, final int Jahr, final int KW, final int Wochentyp) {
		this.ID = ID;
		this.Stundenplan_ID = Stundenplan_ID;
		this.Jahr = Jahr;
		this.KW = KW;
		this.Wochentyp = Wochentyp;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanKalenderwochenZuordnung other = (DTOStundenplanKalenderwochenZuordnung) obj;
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
		return "DTOStundenplanKalenderwochenZuordnung(ID=" + this.ID + ", Stundenplan_ID=" + this.Stundenplan_ID + ", Jahr=" + this.Jahr + ", KW=" + this.KW + ", Wochentyp=" + this.Wochentyp + ")";
	}

}
