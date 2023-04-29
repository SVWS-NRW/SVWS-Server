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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_PausenaufsichtenBereich.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_PausenaufsichtenBereich")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.all", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.id", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.id.multiple", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.pausenaufsicht_id", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Pausenaufsicht_ID = :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.pausenaufsicht_id.multiple", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Pausenaufsicht_ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.aufsichtsbereich_id", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Aufsichtsbereich_ID = :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.aufsichtsbereich_id.multiple", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Aufsichtsbereich_ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanPausenaufsichtenBereiche.all.migration", query = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Pausenaufsicht_ID", "Aufsichtsbereich_ID"})
public final class DTOStundenplanPausenaufsichtenBereiche {

	/** Die eindeutige ID für die Zuordnung des Aufsichtsbereichs zur Pausenaufsicht */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Pausenaufsicht-Eintrages im Stundenplan */
	@Column(name = "Pausenaufsicht_ID")
	@JsonProperty
	public long Pausenaufsicht_ID;

	/** Die ID des zugewiesenen Aufsichtsbereichs. Sollten ggf. mehrere Aufsichtsbereiche zugwiesen werden, so müssen für eine Pausenaufsicht_ID mehrere Datensätze vorliegen */
	@Column(name = "Aufsichtsbereich_ID")
	@JsonProperty
	public long Aufsichtsbereich_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenaufsichtenBereiche ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanPausenaufsichtenBereiche() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenaufsichtenBereiche ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Pausenaufsicht_ID   der Wert für das Attribut Pausenaufsicht_ID
	 * @param Aufsichtsbereich_ID   der Wert für das Attribut Aufsichtsbereich_ID
	 */
	public DTOStundenplanPausenaufsichtenBereiche(final long ID, final long Pausenaufsicht_ID, final long Aufsichtsbereich_ID) {
		this.ID = ID;
		this.Pausenaufsicht_ID = Pausenaufsicht_ID;
		this.Aufsichtsbereich_ID = Aufsichtsbereich_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanPausenaufsichtenBereiche other = (DTOStundenplanPausenaufsichtenBereiche) obj;
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
		return "DTOStundenplanPausenaufsichtenBereiche(ID=" + this.ID + ", Pausenaufsicht_ID=" + this.Pausenaufsicht_ID + ", Aufsichtsbereich_ID=" + this.Aufsichtsbereich_ID + ")";
	}

}
