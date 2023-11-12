package de.svws_nrw.db.dto.current.gost.klausurplanung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Raeume.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Raeume")
@NamedQuery(name = "DTOGostKlausurenRaeume.all", query = "SELECT e FROM DTOGostKlausurenRaeume e")
@NamedQuery(name = "DTOGostKlausurenRaeume.id", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaeume.id.multiple", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaeume.termin_id", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Termin_ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaeume.termin_id.multiple", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Termin_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaeume.stundenplan_raum_id", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Stundenplan_Raum_ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaeume.stundenplan_raum_id.multiple", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Stundenplan_Raum_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaeume.bemerkungen", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenRaeume.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenRaeume.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenRaeume.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOGostKlausurenRaeume.all.migration", query = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Termin_ID", "Stundenplan_Raum_ID", "Bemerkungen"})
public final class DTOGostKlausurenRaeume {

	/** ID des Klausurraums (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Termins */
	@Column(name = "Termin_ID")
	@JsonProperty
	public long Termin_ID;

	/** ID des Raums aus der Tabelle Stundenplan_Raeume */
	@Column(name = "Stundenplan_Raum_ID")
	@JsonProperty
	public Long Stundenplan_Raum_ID;

	/** Text für Bemerkungen zum Klausurraum */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeume ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenRaeume() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeume ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Termin_ID   der Wert für das Attribut Termin_ID
	 */
	public DTOGostKlausurenRaeume(final long ID, final long Termin_ID) {
		this.ID = ID;
		this.Termin_ID = Termin_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenRaeume other = (DTOGostKlausurenRaeume) obj;
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
		return "DTOGostKlausurenRaeume(ID=" + this.ID + ", Termin_ID=" + this.Termin_ID + ", Stundenplan_Raum_ID=" + this.Stundenplan_Raum_ID + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
