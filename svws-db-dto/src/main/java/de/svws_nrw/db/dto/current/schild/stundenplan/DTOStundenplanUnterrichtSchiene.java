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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_UnterrichtSchiene.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_UnterrichtSchiene")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.all", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.id", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.unterricht_id", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.Unterricht_ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.unterricht_id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.Unterricht_ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.schiene_id", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.Schiene_ID = :value")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.schiene_id.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.Schiene_ID IN :value")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.primaryKeyQuery.multiple", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOStundenplanUnterrichtSchiene.all.migration", query = "SELECT e FROM DTOStundenplanUnterrichtSchiene e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Unterricht_ID", "Schiene_ID"})
public final class DTOStundenplanUnterrichtSchiene {

	/** Die eindeutige ID für die Zuordnung der Schiene zum Unterricht */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Unterricht-Eintrages im Stundenplan */
	@Column(name = "Unterricht_ID")
	@JsonProperty
	public long Unterricht_ID;

	/** Die ID der zugewiesenen Schiene. Sollten ggf. mehrere Schienen zugewiesen werden, so müssen für eine Unterricht-ID mehrere Datensätze vorliegen */
	@Column(name = "Schiene_ID")
	@JsonProperty
	public long Schiene_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterrichtSchiene ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanUnterrichtSchiene() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterrichtSchiene ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param Schiene_ID   der Wert für das Attribut Schiene_ID
	 */
	public DTOStundenplanUnterrichtSchiene(final long ID, final long Unterricht_ID, final long Schiene_ID) {
		this.ID = ID;
		this.Unterricht_ID = Unterricht_ID;
		this.Schiene_ID = Schiene_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanUnterrichtSchiene other = (DTOStundenplanUnterrichtSchiene) obj;
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
		return "DTOStundenplanUnterrichtSchiene(ID=" + this.ID + ", Unterricht_ID=" + this.Unterricht_ID + ", Schiene_ID=" + this.Schiene_ID + ")";
	}

}
