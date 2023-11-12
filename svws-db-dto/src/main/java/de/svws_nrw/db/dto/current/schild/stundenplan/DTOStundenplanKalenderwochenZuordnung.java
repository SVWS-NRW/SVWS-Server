package de.svws_nrw.db.dto.current.schild.stundenplan;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.all", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.id", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.id.multiple", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.stundenplan_id", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Stundenplan_ID = :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.stundenplan_id.multiple", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Stundenplan_ID IN :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.jahr", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Jahr = :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.jahr.multiple", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Jahr IN :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.kw", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.KW = :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.kw.multiple", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.KW IN :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.wochentyp", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Wochentyp = :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.wochentyp.multiple", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Wochentyp IN :value")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.primaryKeyQuery.multiple", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOStundenplanKalenderwochenZuordnung.all.migration", query = "SELECT e FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Stundenplan_ID", "Jahr", "KW", "Wochentyp"})
public final class DTOStundenplanKalenderwochenZuordnung {

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
