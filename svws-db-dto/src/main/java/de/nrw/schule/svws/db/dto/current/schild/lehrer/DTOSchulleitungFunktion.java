package de.nrw.schule.svws.db.dto.current.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchulleitungFunktion.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchulleitungFunktion")
@NamedQuery(name="DTOSchulleitungFunktion.all", query="SELECT e FROM DTOSchulleitungFunktion e")
@NamedQuery(name="DTOSchulleitungFunktion.id", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.ID = :value")
@NamedQuery(name="DTOSchulleitungFunktion.id.multiple", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchulleitungFunktion.funktionstext", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.Funktionstext = :value")
@NamedQuery(name="DTOSchulleitungFunktion.funktionstext.multiple", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.Funktionstext IN :value")
@NamedQuery(name="DTOSchulleitungFunktion.abschuljahr", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.AbSchuljahr = :value")
@NamedQuery(name="DTOSchulleitungFunktion.abschuljahr.multiple", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.AbSchuljahr IN :value")
@NamedQuery(name="DTOSchulleitungFunktion.bisschuljahr", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.BisSchuljahr = :value")
@NamedQuery(name="DTOSchulleitungFunktion.bisschuljahr.multiple", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.BisSchuljahr IN :value")
@NamedQuery(name="DTOSchulleitungFunktion.primaryKeyQuery", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchulleitungFunktion.all.migration", query="SELECT e FROM DTOSchulleitungFunktion e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Funktionstext","AbSchuljahr","BisSchuljahr"})
public class DTOSchulleitungFunktion {

	/** ID der Schulleitungsfunktion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Beschreibung der Funktion */
	@Column(name = "Funktionstext")
	@JsonProperty
	public String Funktionstext;

	/** Schulleitungsfunktion ist gültig ab Schuljahr */
	@Column(name = "AbSchuljahr")
	@JsonProperty
	public Integer AbSchuljahr;

	/** Schulleitungsfunktion ist gültig bis Schuljahr */
	@Column(name = "BisSchuljahr")
	@JsonProperty
	public Integer BisSchuljahr;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulleitungFunktion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulleitungFunktion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulleitungFunktion ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Funktionstext   der Wert für das Attribut Funktionstext
	 */
	public DTOSchulleitungFunktion(final Long ID, final String Funktionstext) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Funktionstext == null) { 
			throw new NullPointerException("Funktionstext must not be null");
		}
		this.Funktionstext = Funktionstext;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchulleitungFunktion other = (DTOSchulleitungFunktion) obj;
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
		return "DTOSchulleitungFunktion(ID=" + this.ID + ", Funktionstext=" + this.Funktionstext + ", AbSchuljahr=" + this.AbSchuljahr + ", BisSchuljahr=" + this.BisSchuljahr + ")";
	}

}