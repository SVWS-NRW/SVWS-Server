package de.nrw.schule.svws.db.dto.rev8.schild.stundenplan;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_PausenaufsichtenBereich.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_PausenaufsichtenBereich")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.all", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.id", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.id.multiple", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.pausenaufsicht_id", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e WHERE e.Pausenaufsicht_ID = :value")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.pausenaufsicht_id.multiple", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e WHERE e.Pausenaufsicht_ID IN :value")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.aufsichtsbereich_id", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e WHERE e.Aufsichtsbereich_ID = :value")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.aufsichtsbereich_id.multiple", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e WHERE e.Aufsichtsbereich_ID IN :value")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.primaryKeyQuery", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOStundenplanPausenaufsichtenBereiche.all.migration", query="SELECT e FROM Rev8DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Pausenaufsicht_ID","Aufsichtsbereich_ID"})
public class Rev8DTOStundenplanPausenaufsichtenBereiche {

	/** Die eindeutige ID für die Zuordnung des Aufsichtsbereichs zur Pausenaufsicht */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die ID des Pausenaufsicht-Eintrages im Stundenplan */
	@Column(name = "Pausenaufsicht_ID")
	@JsonProperty
	public Long Pausenaufsicht_ID;

	/** Die ID des zugewiesenen Aufsichtsbereichs. Sollten ggf. mehrere Aufsichtsbereiche zugwiesen werden, so müssen für eine Pausenaufsicht_ID mehrere Datensätze vorliegen */
	@Column(name = "Aufsichtsbereich_ID")
	@JsonProperty
	public Long Aufsichtsbereich_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStundenplanPausenaufsichtenBereiche ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStundenplanPausenaufsichtenBereiche() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStundenplanPausenaufsichtenBereiche ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Pausenaufsicht_ID   der Wert für das Attribut Pausenaufsicht_ID
	 * @param Aufsichtsbereich_ID   der Wert für das Attribut Aufsichtsbereich_ID
	 */
	public Rev8DTOStundenplanPausenaufsichtenBereiche(final Long ID, final Long Pausenaufsicht_ID, final Long Aufsichtsbereich_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Pausenaufsicht_ID == null) { 
			throw new NullPointerException("Pausenaufsicht_ID must not be null");
		}
		this.Pausenaufsicht_ID = Pausenaufsicht_ID;
		if (Aufsichtsbereich_ID == null) { 
			throw new NullPointerException("Aufsichtsbereich_ID must not be null");
		}
		this.Aufsichtsbereich_ID = Aufsichtsbereich_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStundenplanPausenaufsichtenBereiche other = (Rev8DTOStundenplanPausenaufsichtenBereiche) obj;
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
		return "Rev8DTOStundenplanPausenaufsichtenBereiche(ID=" + this.ID + ", Pausenaufsicht_ID=" + this.Pausenaufsicht_ID + ", Aufsichtsbereich_ID=" + this.Aufsichtsbereich_ID + ")";
	}

}