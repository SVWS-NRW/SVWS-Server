package de.svws_nrw.db.dto.current.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzergruppenKompetenzen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOBenutzergruppenKompetenzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzergruppenKompetenzen")
@NamedQuery(name="DTOBenutzergruppenKompetenz.all", query="SELECT e FROM DTOBenutzergruppenKompetenz e")
@NamedQuery(name="DTOBenutzergruppenKompetenz.gruppe_id", query="SELECT e FROM DTOBenutzergruppenKompetenz e WHERE e.Gruppe_ID = :value")
@NamedQuery(name="DTOBenutzergruppenKompetenz.gruppe_id.multiple", query="SELECT e FROM DTOBenutzergruppenKompetenz e WHERE e.Gruppe_ID IN :value")
@NamedQuery(name="DTOBenutzergruppenKompetenz.kompetenz_id", query="SELECT e FROM DTOBenutzergruppenKompetenz e WHERE e.Kompetenz_ID = :value")
@NamedQuery(name="DTOBenutzergruppenKompetenz.kompetenz_id.multiple", query="SELECT e FROM DTOBenutzergruppenKompetenz e WHERE e.Kompetenz_ID IN :value")
@NamedQuery(name="DTOBenutzergruppenKompetenz.primaryKeyQuery", query="SELECT e FROM DTOBenutzergruppenKompetenz e WHERE e.Gruppe_ID = ?1 AND e.Kompetenz_ID = ?2")
@NamedQuery(name="DTOBenutzergruppenKompetenz.all.migration", query="SELECT e FROM DTOBenutzergruppenKompetenz e WHERE e.Gruppe_ID IS NOT NULL AND e.Kompetenz_ID IS NOT NULL")
@JsonPropertyOrder({"Gruppe_ID","Kompetenz_ID"})
public class DTOBenutzergruppenKompetenz {

	/** Die ID der Benutzergruppe */
	@Id
	@Column(name = "Gruppe_ID")
	@JsonProperty
	public Long Gruppe_ID;

	/** Die ID der zugeordneten Kompetenz */
	@Id
	@Column(name = "Kompetenz_ID")
	@JsonProperty
	public Long Kompetenz_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzergruppenKompetenz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzergruppenKompetenz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzergruppenKompetenz ohne eine Initialisierung der Attribute.
	 * @param Gruppe_ID   der Wert für das Attribut Gruppe_ID
	 * @param Kompetenz_ID   der Wert für das Attribut Kompetenz_ID
	 */
	public DTOBenutzergruppenKompetenz(final Long Gruppe_ID, final Long Kompetenz_ID) {
		if (Gruppe_ID == null) { 
			throw new NullPointerException("Gruppe_ID must not be null");
		}
		this.Gruppe_ID = Gruppe_ID;
		if (Kompetenz_ID == null) { 
			throw new NullPointerException("Kompetenz_ID must not be null");
		}
		this.Kompetenz_ID = Kompetenz_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBenutzergruppenKompetenz other = (DTOBenutzergruppenKompetenz) obj;
		if (Gruppe_ID == null) {
			if (other.Gruppe_ID != null)
				return false;
		} else if (!Gruppe_ID.equals(other.Gruppe_ID))
			return false;

		if (Kompetenz_ID == null) {
			if (other.Kompetenz_ID != null)
				return false;
		} else if (!Kompetenz_ID.equals(other.Kompetenz_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Gruppe_ID == null) ? 0 : Gruppe_ID.hashCode());

		result = prime * result + ((Kompetenz_ID == null) ? 0 : Kompetenz_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOBenutzergruppenKompetenz(Gruppe_ID=" + this.Gruppe_ID + ", Kompetenz_ID=" + this.Kompetenz_ID + ")";
	}

}