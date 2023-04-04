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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Pausenaufsichten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Pausenaufsichten")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.all", query = "SELECT e FROM DTOStundenplanPausenaufsichten e")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.id", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.id.multiple", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.pausenzeit_id", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.Pausenzeit_ID = :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.pausenzeit_id.multiple", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.Pausenzeit_ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.wochentyp", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.Wochentyp = :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.wochentyp.multiple", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.Wochentyp IN :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.lehrer_id", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.lehrer_id.multiple", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanPausenaufsichten.all.migration", query = "SELECT e FROM DTOStundenplanPausenaufsichten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Pausenzeit_ID", "Wochentyp", "Lehrer_ID"})
public final class DTOStundenplanPausenaufsichten {

	/** Die eindeutige ID für diese Zuordnung des Pausenaufsichts-Eintrages zu einem Stundenplan */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die ID des Pausenzeit-Eintrags */
	@Column(name = "Pausenzeit_ID")
	@JsonProperty
	public Long Pausenzeit_ID;

	/** Gibt an, ob es sich um einen Eintrag für jede Woche handelt (0) oder ob es sich um einen unterschiedlichen (!) Eintrag für eine A- bzw. B-Wochen (1 bzw. 2) handelt */
	@Column(name = "Wochentyp")
	@JsonProperty
	public Integer Wochentyp;

	/** Die ID des aufsichtsführenden Lehrers. Im Falle von mehreren Aufsichten werden für eine Pausenzeit-ID einfach mehrere Datensätze erzeugt */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenaufsichten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanPausenaufsichten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenaufsichten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Pausenzeit_ID   der Wert für das Attribut Pausenzeit_ID
	 * @param Wochentyp   der Wert für das Attribut Wochentyp
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOStundenplanPausenaufsichten(final Long ID, final Long Pausenzeit_ID, final Integer Wochentyp, final Long Lehrer_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Pausenzeit_ID == null) {
			throw new NullPointerException("Pausenzeit_ID must not be null");
		}
		this.Pausenzeit_ID = Pausenzeit_ID;
		if (Wochentyp == null) {
			throw new NullPointerException("Wochentyp must not be null");
		}
		this.Wochentyp = Wochentyp;
		if (Lehrer_ID == null) {
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanPausenaufsichten other = (DTOStundenplanPausenaufsichten) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStundenplanPausenaufsichten(ID=" + this.ID + ", Pausenzeit_ID=" + this.Pausenzeit_ID + ", Wochentyp=" + this.Wochentyp + ", Lehrer_ID=" + this.Lehrer_ID + ")";
	}

}
