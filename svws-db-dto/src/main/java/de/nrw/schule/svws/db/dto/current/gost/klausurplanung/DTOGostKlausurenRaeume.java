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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Raeume.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Raeume")
@NamedQuery(name="DTOGostKlausurenRaeume.all", query="SELECT e FROM DTOGostKlausurenRaeume e")
@NamedQuery(name="DTOGostKlausurenRaeume.id", query="SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID = :value")
@NamedQuery(name="DTOGostKlausurenRaeume.id.multiple", query="SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID IN :value")
@NamedQuery(name="DTOGostKlausurenRaeume.katalog_raum_id", query="SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Katalog_Raum_ID = :value")
@NamedQuery(name="DTOGostKlausurenRaeume.katalog_raum_id.multiple", query="SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Katalog_Raum_ID IN :value")
@NamedQuery(name="DTOGostKlausurenRaeume.primaryKeyQuery", query="SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID = ?1")
@NamedQuery(name="DTOGostKlausurenRaeume.all.migration", query="SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Katalog_Raum_ID"})
public class DTOGostKlausurenRaeume {

	/** ID des Klausurraums (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Raums aus der Tabelle Katalog_Raeume */
	@Column(name = "Katalog_Raum_ID")
	@JsonProperty
	public Long Katalog_Raum_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeume ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenRaeume() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeume ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOGostKlausurenRaeume(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenRaeume other = (DTOGostKlausurenRaeume) obj;
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
		return "DTOGostKlausurenRaeume(ID=" + this.ID + ", Katalog_Raum_ID=" + this.Katalog_Raum_ID + ")";
	}

}