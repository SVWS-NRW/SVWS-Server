package de.nrw.schule.svws.db.dto.dev.gost.kursblockung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Schienen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Schienen")
@NamedQuery(name="DevDTOGostBlockungSchiene.all", query="SELECT e FROM DevDTOGostBlockungSchiene e")
@NamedQuery(name="DevDTOGostBlockungSchiene.id", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.ID = :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.id.multiple", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.blockung_id", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.Blockung_ID = :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.blockung_id.multiple", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.Blockung_ID IN :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.nummer", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.Nummer = :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.nummer.multiple", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.Nummer IN :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.bezeichnung", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.bezeichnung.multiple", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.wochenstunden", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.Wochenstunden = :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.wochenstunden.multiple", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.Wochenstunden IN :value")
@NamedQuery(name="DevDTOGostBlockungSchiene.primaryKeyQuery", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOGostBlockungSchiene.all.migration", query="SELECT e FROM DevDTOGostBlockungSchiene e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Blockung_ID","Nummer","Bezeichnung","Wochenstunden"})
public class DevDTOGostBlockungSchiene {

	/** ID der Schiene in der Blockung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Blockung */
	@Column(name = "Blockung_ID")
	@JsonProperty
	public Long Blockung_ID;

	/** Die Nummer der Schiene, beginnend bei 1 */
	@Column(name = "Nummer")
	@JsonProperty
	public Integer Nummer;

	/** Bezeichnung der Schiene (z.B. LK Schiene 1) */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Anzahl der Wochenstunden, welche der Schiene zugeordnet sind */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public Integer Wochenstunden;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostBlockungSchiene ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostBlockungSchiene() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostBlockungSchiene ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Blockung_ID   der Wert für das Attribut Blockung_ID
	 * @param Nummer   der Wert für das Attribut Nummer
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 */
	public DevDTOGostBlockungSchiene(final Long ID, final Long Blockung_ID, final Integer Nummer, final String Bezeichnung, final Integer Wochenstunden) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Blockung_ID == null) { 
			throw new NullPointerException("Blockung_ID must not be null");
		}
		this.Blockung_ID = Blockung_ID;
		if (Nummer == null) { 
			throw new NullPointerException("Nummer must not be null");
		}
		this.Nummer = Nummer;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Wochenstunden == null) { 
			throw new NullPointerException("Wochenstunden must not be null");
		}
		this.Wochenstunden = Wochenstunden;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOGostBlockungSchiene other = (DevDTOGostBlockungSchiene) obj;
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
		return "DevDTOGostBlockungSchiene(ID=" + this.ID + ", Blockung_ID=" + this.Blockung_ID + ", Nummer=" + this.Nummer + ", Bezeichnung=" + this.Bezeichnung + ", Wochenstunden=" + this.Wochenstunden + ")";
	}

}