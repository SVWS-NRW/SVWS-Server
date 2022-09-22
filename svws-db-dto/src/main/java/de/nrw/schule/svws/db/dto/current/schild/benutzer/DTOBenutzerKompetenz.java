package de.nrw.schule.svws.db.dto.current.schild.benutzer;

import de.nrw.schule.svws.db.DBEntityManager;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzerKompetenzen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOBenutzerKompetenzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzerKompetenzen")
@NamedQuery(name="DTOBenutzerKompetenz.all", query="SELECT e FROM DTOBenutzerKompetenz e")
@NamedQuery(name="DTOBenutzerKompetenz.benutzer_id", query="SELECT e FROM DTOBenutzerKompetenz e WHERE e.Benutzer_ID = :value")
@NamedQuery(name="DTOBenutzerKompetenz.benutzer_id.multiple", query="SELECT e FROM DTOBenutzerKompetenz e WHERE e.Benutzer_ID IN :value")
@NamedQuery(name="DTOBenutzerKompetenz.kompetenz_id", query="SELECT e FROM DTOBenutzerKompetenz e WHERE e.Kompetenz_ID = :value")
@NamedQuery(name="DTOBenutzerKompetenz.kompetenz_id.multiple", query="SELECT e FROM DTOBenutzerKompetenz e WHERE e.Kompetenz_ID IN :value")
@NamedQuery(name="DTOBenutzerKompetenz.primaryKeyQuery", query="SELECT e FROM DTOBenutzerKompetenz e WHERE e.Benutzer_ID = ?1 AND e.Kompetenz_ID = ?2")
@NamedQuery(name="DTOBenutzerKompetenz.all.migration", query="SELECT e FROM DTOBenutzerKompetenz e WHERE e.Benutzer_ID IS NOT NULL AND e.Kompetenz_ID IS NOT NULL")
@JsonPropertyOrder({"Benutzer_ID","Kompetenz_ID"})
public class DTOBenutzerKompetenz {

	/** Die ID des Benutzers */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

	/** Die ID der zugeordneten Kompetenz */
	@Id
	@Column(name = "Kompetenz_ID")
	@JsonProperty
	public Long Kompetenz_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerKompetenz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzerKompetenz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerKompetenz ohne eine Initialisierung der Attribute.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param Kompetenz_ID   der Wert für das Attribut Kompetenz_ID
	 */
	public DTOBenutzerKompetenz(final Long Benutzer_ID, final Long Kompetenz_ID) {
		if (Benutzer_ID == null) { 
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
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
		DTOBenutzerKompetenz other = (DTOBenutzerKompetenz) obj;
		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
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
		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());

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
		return "DTOBenutzerKompetenz(Benutzer_ID=" + this.Benutzer_ID + ", Kompetenz_ID=" + this.Kompetenz_ID + ")";
	}

}