package de.nrw.schule.svws.db.dto.current.gost.klausurplanung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Raeume_Stunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Raeume_Stunden")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.all", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.id", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.ID = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.id.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.ID IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.klausurraum_id", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.Klausurraum_ID = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.klausurraum_id.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.Klausurraum_ID IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.zeitraster_id", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.Zeitraster_ID = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.zeitraster_id.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.Zeitraster_ID IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.primaryKeyQuery", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.ID = ?1")
@NamedQuery(name="DTOGostKlausurenRaeumeStunden.all.migration", query="SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Klausurraum_ID","Zeitraster_ID"})
public class DTOGostKlausurenRaeumeStunden {

	/** ID der Stunde des Klausurraums (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Klausurraums */
	@Column(name = "Klausurraum_ID")
	@JsonProperty
	public Long Klausurraum_ID;

	/** ID des Zeitrasters */
	@Column(name = "Zeitraster_ID")
	@JsonProperty
	public Long Zeitraster_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeumeStunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenRaeumeStunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeumeStunden ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Klausurraum_ID   der Wert für das Attribut Klausurraum_ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 */
	public DTOGostKlausurenRaeumeStunden(final Long ID, final Long Klausurraum_ID, final Long Zeitraster_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Klausurraum_ID == null) { 
			throw new NullPointerException("Klausurraum_ID must not be null");
		}
		this.Klausurraum_ID = Klausurraum_ID;
		if (Zeitraster_ID == null) { 
			throw new NullPointerException("Zeitraster_ID must not be null");
		}
		this.Zeitraster_ID = Zeitraster_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenRaeumeStunden other = (DTOGostKlausurenRaeumeStunden) obj;
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
		return "DTOGostKlausurenRaeumeStunden(ID=" + this.ID + ", Klausurraum_ID=" + this.Klausurraum_ID + ", Zeitraster_ID=" + this.Zeitraster_ID + ")";
	}

}