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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Pausenzeit_Klassenzuordnung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Pausenzeit_Klassenzuordnung")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.all", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.id", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.id.multiple", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.pausenzeit_id", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.Pausenzeit_ID = :value")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.pausenzeit_id.multiple", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.Pausenzeit_ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.klassen_id", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.Klassen_ID = :value")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.klassen_id.multiple", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.Klassen_ID IN :value")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.primaryKeyQuery.multiple", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOStundenplanPausenzeitKlassenzuordnung.all.migration", query = "SELECT e FROM DTOStundenplanPausenzeitKlassenzuordnung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Pausenzeit_ID", "Klassen_ID"})
public final class DTOStundenplanPausenzeitKlassenzuordnung {

	/** Die eindeutige ID für die Zuordnung einer Klasse zu einer Pausenzeit */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Pausenzeit-Eintrages im Stundenplan */
	@Column(name = "Pausenzeit_ID")
	@JsonProperty
	public long Pausenzeit_ID;

	/** Die ID der zugeordneten Klasse. */
	@Column(name = "Klassen_ID")
	@JsonProperty
	public long Klassen_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenzeitKlassenzuordnung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanPausenzeitKlassenzuordnung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenzeitKlassenzuordnung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Pausenzeit_ID   der Wert für das Attribut Pausenzeit_ID
	 * @param Klassen_ID   der Wert für das Attribut Klassen_ID
	 */
	public DTOStundenplanPausenzeitKlassenzuordnung(final long ID, final long Pausenzeit_ID, final long Klassen_ID) {
		this.ID = ID;
		this.Pausenzeit_ID = Pausenzeit_ID;
		this.Klassen_ID = Klassen_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanPausenzeitKlassenzuordnung other = (DTOStundenplanPausenzeitKlassenzuordnung) obj;
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
		return "DTOStundenplanPausenzeitKlassenzuordnung(ID=" + this.ID + ", Pausenzeit_ID=" + this.Pausenzeit_ID + ", Klassen_ID=" + this.Klassen_ID + ")";
	}

}
