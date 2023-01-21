package de.nrw.schule.svws.db.dto.dev.gost.klausurplanung;

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
@NamedQuery(name="DevDTOGostKlausurenTermine.all", query="SELECT e FROM DevDTOGostKlausurenTermine e")
@NamedQuery(name="DevDTOGostKlausurenTermine.id", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.ID = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.id.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.datum", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Datum = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.datum.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Datum IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.startzeit", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Startzeit = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.startzeit.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Startzeit IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.bemerkungen", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.bemerkungen.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.primaryKeyQuery", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOGostKlausurenTermine.all.migration", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Datum","Startzeit","Bemerkungen"})
public class DevDTOGostKlausurenTermine {

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
	 * Erstellt ein neues Objekt der Klasse DevDTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostKlausurenTermine() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DevDTOGostKlausurenTermine(final Long ID) {
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
		DevDTOGostKlausurenTermine other = (DevDTOGostKlausurenTermine) obj;
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
		return "DevDTOGostKlausurenTermine(ID=" + this.ID + ", Datum=" + this.Datum + ", Startzeit=" + this.Startzeit + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}