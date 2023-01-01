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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Termine.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Termine")
@NamedQuery(name="DTOGostKlausurenTermine.all", query="SELECT e FROM DTOGostKlausurenTermine e")
@NamedQuery(name="DTOGostKlausurenTermine.id", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID = :value")
@NamedQuery(name="DTOGostKlausurenTermine.id.multiple", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID IN :value")
@NamedQuery(name="DTOGostKlausurenTermine.datum", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.Datum = :value")
@NamedQuery(name="DTOGostKlausurenTermine.datum.multiple", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.Datum IN :value")
@NamedQuery(name="DTOGostKlausurenTermine.startzeit", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.Startzeit = :value")
@NamedQuery(name="DTOGostKlausurenTermine.startzeit.multiple", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.Startzeit IN :value")
@NamedQuery(name="DTOGostKlausurenTermine.bemerkungen", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DTOGostKlausurenTermine.bemerkungen.multiple", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DTOGostKlausurenTermine.primaryKeyQuery", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID = ?1")
@NamedQuery(name="DTOGostKlausurenTermine.all.migration", query="SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Datum","Startzeit","Bemerkungen"})
public class DTOGostKlausurenTermine {

	/** ID des Klausurtermins (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Datum des Klausurtermins */
	@Column(name = "Datum")
	@JsonProperty
	public String Datum;

	/** Die Startzeit des Klausurtermins */
	@Column(name = "Startzeit")
	@JsonProperty
	public String Startzeit;

	/** Text für Bemerkungen zur Klausurvorlage */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenTermine() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOGostKlausurenTermine(final Long ID) {
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
		DTOGostKlausurenTermine other = (DTOGostKlausurenTermine) obj;
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
		return "DTOGostKlausurenTermine(ID=" + this.ID + ", Datum=" + this.Datum + ", Startzeit=" + this.Startzeit + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}