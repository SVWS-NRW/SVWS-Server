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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_UnterrichtRaum.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_UnterrichtRaum")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.all", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.id", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.unterricht_id", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e WHERE e.Unterricht_ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.unterricht_id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e WHERE e.Unterricht_ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.raum_id", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e WHERE e.Raum_ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.raum_id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e WHERE e.Raum_ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanUnterrichtRaum.all.migration", query = "SELECT e FROM DTOStundenplanUnterrichtRaum e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Unterricht_ID", "Raum_ID"})
public final class DTOStundenplanUnterrichtRaum {

	/** Die eindeutige ID für die Zuordnung des Raumes zum Unterricht */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Unterricht-Eintrages im Stundenplan */
	@Column(name = "Unterricht_ID")
	@JsonProperty
	public long Unterricht_ID;

	/** Die ID des zugewiesenen Raumes. Sollten ggf. mehrere Räume zugwiesen werden, so müssen für eine Unterricht-ID mehrere Datensätze vorliegen */
	@Column(name = "Raum_ID")
	@JsonProperty
	public long Raum_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterrichtRaum ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanUnterrichtRaum() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterrichtRaum ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param Raum_ID   der Wert für das Attribut Raum_ID
	 */
	public DTOStundenplanUnterrichtRaum(final long ID, final long Unterricht_ID, final long Raum_ID) {
		this.ID = ID;
		this.Unterricht_ID = Unterricht_ID;
		this.Raum_ID = Raum_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanUnterrichtRaum other = (DTOStundenplanUnterrichtRaum) obj;
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
		return "DTOStundenplanUnterrichtRaum(ID=" + this.ID + ", Unterricht_ID=" + this.Unterricht_ID + ", Raum_ID=" + this.Raum_ID + ")";
	}

}
