package de.nrw.schule.svws.db.dto.dev.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Religionen_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Religionen_Keys")
@NamedQuery(name="DevDTOReligionKeys.all", query="SELECT e FROM DevDTOReligionKeys e")
@NamedQuery(name="DevDTOReligionKeys.kuerzel", query="SELECT e FROM DevDTOReligionKeys e WHERE e.Kuerzel = :value")
@NamedQuery(name="DevDTOReligionKeys.kuerzel.multiple", query="SELECT e FROM DevDTOReligionKeys e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DevDTOReligionKeys.primaryKeyQuery", query="SELECT e FROM DevDTOReligionKeys e WHERE e.Kuerzel = ?1")
@NamedQuery(name="DevDTOReligionKeys.all.migration", query="SELECT e FROM DevDTOReligionKeys e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel"})
public class DevDTOReligionKeys {

	/** Das Kürzel der Religion / Konfession */
	@Id
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOReligionKeys ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOReligionKeys() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOReligionKeys ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public DevDTOReligionKeys(final String Kuerzel) {
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOReligionKeys other = (DevDTOReligionKeys) obj;
		if (Kuerzel == null) {
			if (other.Kuerzel != null)
				return false;
		} else if (!Kuerzel.equals(other.Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Kuerzel == null) ? 0 : Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DevDTOReligionKeys(Kuerzel=" + this.Kuerzel + ")";
	}

}