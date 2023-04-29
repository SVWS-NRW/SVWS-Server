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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_UnterrichtKlasse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_UnterrichtKlasse")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.all", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.id", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.unterricht_id", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e WHERE e.Unterricht_ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.unterricht_id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e WHERE e.Unterricht_ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.klasse_id", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e WHERE e.Klasse_ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.klasse_id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e WHERE e.Klasse_ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanUnterrichtKlasse.all.migration", query = "SELECT e FROM DTOStundenplanUnterrichtKlasse e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Unterricht_ID", "Klasse_ID"})
public final class DTOStundenplanUnterrichtKlasse {

	/** Die eindeutige ID für die Zuordnung der Klasse zum Unterricht */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Unterricht-Eintrages im Stundenplan */
	@Column(name = "Unterricht_ID")
	@JsonProperty
	public long Unterricht_ID;

	/** Die ID der zugeordneten Klasse. */
	@Column(name = "Klasse_ID")
	@JsonProperty
	public long Klasse_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterrichtKlasse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanUnterrichtKlasse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterrichtKlasse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param Klasse_ID   der Wert für das Attribut Klasse_ID
	 */
	public DTOStundenplanUnterrichtKlasse(final long ID, final long Unterricht_ID, final long Klasse_ID) {
		this.ID = ID;
		this.Unterricht_ID = Unterricht_ID;
		this.Klasse_ID = Klasse_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanUnterrichtKlasse other = (DTOStundenplanUnterrichtKlasse) obj;
		if (ID != other.ID)
			return false;
		return true;
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
		return "DTOStundenplanUnterrichtKlasse(ID=" + this.ID + ", Unterricht_ID=" + this.Unterricht_ID + ", Klasse_ID=" + this.Klasse_ID + ")";
	}

}
