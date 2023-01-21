package de.nrw.schule.svws.db.dto.dev.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunft.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunft")
@NamedQuery(name="DevDTOHerkunft.all", query="SELECT e FROM DevDTOHerkunft e")
@NamedQuery(name="DevDTOHerkunft.id", query="SELECT e FROM DevDTOHerkunft e WHERE e.ID = :value")
@NamedQuery(name="DevDTOHerkunft.id.multiple", query="SELECT e FROM DevDTOHerkunft e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOHerkunft.kuerzel", query="SELECT e FROM DevDTOHerkunft e WHERE e.Kuerzel = :value")
@NamedQuery(name="DevDTOHerkunft.kuerzel.multiple", query="SELECT e FROM DevDTOHerkunft e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DevDTOHerkunft.beschreibung", query="SELECT e FROM DevDTOHerkunft e WHERE e.Beschreibung = :value")
@NamedQuery(name="DevDTOHerkunft.beschreibung.multiple", query="SELECT e FROM DevDTOHerkunft e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DevDTOHerkunft.gueltigvon", query="SELECT e FROM DevDTOHerkunft e WHERE e.gueltigVon = :value")
@NamedQuery(name="DevDTOHerkunft.gueltigvon.multiple", query="SELECT e FROM DevDTOHerkunft e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DevDTOHerkunft.gueltigbis", query="SELECT e FROM DevDTOHerkunft e WHERE e.gueltigBis = :value")
@NamedQuery(name="DevDTOHerkunft.gueltigbis.multiple", query="SELECT e FROM DevDTOHerkunft e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DevDTOHerkunft.primaryKeyQuery", query="SELECT e FROM DevDTOHerkunft e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOHerkunft.all.migration", query="SELECT e FROM DevDTOHerkunft e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Beschreibung","gueltigVon","gueltigBis"})
public class DevDTOHerkunft {

	/** Die ID der Herkunft */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Statistikkürzel der Herkunft */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Die textuelle Beschreibung der Herkunft */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOHerkunft ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOHerkunft() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOHerkunft ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public DevDTOHerkunft(final Long ID, final String Kuerzel, final String Beschreibung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Beschreibung == null) { 
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOHerkunft other = (DevDTOHerkunft) obj;
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
		return "DevDTOHerkunft(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}