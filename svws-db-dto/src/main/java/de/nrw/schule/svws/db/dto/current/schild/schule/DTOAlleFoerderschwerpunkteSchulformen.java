package de.nrw.schule.svws.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Foerderschwerpunkte_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOAlleFoerderschwerpunkteSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Foerderschwerpunkte_Schulformen")
@NamedQuery(name="DTOAlleFoerderschwerpunkteSchulformen.all", query="SELECT e FROM DTOAlleFoerderschwerpunkteSchulformen e")
@NamedQuery(name="DTOAlleFoerderschwerpunkteSchulformen.foerderschwerpunkt_id", query="SELECT e FROM DTOAlleFoerderschwerpunkteSchulformen e WHERE e.Foerderschwerpunkt_ID = :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkteSchulformen.foerderschwerpunkt_id.multiple", query="SELECT e FROM DTOAlleFoerderschwerpunkteSchulformen e WHERE e.Foerderschwerpunkt_ID IN :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkteSchulformen.schulform_kuerzel", query="SELECT e FROM DTOAlleFoerderschwerpunkteSchulformen e WHERE e.Schulform_Kuerzel = :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkteSchulformen.schulform_kuerzel.multiple", query="SELECT e FROM DTOAlleFoerderschwerpunkteSchulformen e WHERE e.Schulform_Kuerzel IN :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkteSchulformen.primaryKeyQuery", query="SELECT e FROM DTOAlleFoerderschwerpunkteSchulformen e WHERE e.Foerderschwerpunkt_ID = ?1 AND e.Schulform_Kuerzel = ?2")
@NamedQuery(name="DTOAlleFoerderschwerpunkteSchulformen.all.migration", query="SELECT e FROM DTOAlleFoerderschwerpunkteSchulformen e WHERE e.Foerderschwerpunkt_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Foerderschwerpunkt_ID","Schulform_Kuerzel"})
public class DTOAlleFoerderschwerpunkteSchulformen {

	/** die ID des Förderschwerpunktes */
	@Id
	@Column(name = "Foerderschwerpunkt_ID")
	@JsonProperty
	public Long Foerderschwerpunkt_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleFoerderschwerpunkteSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAlleFoerderschwerpunkteSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleFoerderschwerpunkteSchulformen ohne eine Initialisierung der Attribute.
	 * @param Foerderschwerpunkt_ID   der Wert für das Attribut Foerderschwerpunkt_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public DTOAlleFoerderschwerpunkteSchulformen(final Long Foerderschwerpunkt_ID, final String Schulform_Kuerzel) {
		if (Foerderschwerpunkt_ID == null) { 
			throw new NullPointerException("Foerderschwerpunkt_ID must not be null");
		}
		this.Foerderschwerpunkt_ID = Foerderschwerpunkt_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAlleFoerderschwerpunkteSchulformen other = (DTOAlleFoerderschwerpunkteSchulformen) obj;
		if (Foerderschwerpunkt_ID == null) {
			if (other.Foerderschwerpunkt_ID != null)
				return false;
		} else if (!Foerderschwerpunkt_ID.equals(other.Foerderschwerpunkt_ID))
			return false;

		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Foerderschwerpunkt_ID == null) ? 0 : Foerderschwerpunkt_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOAlleFoerderschwerpunkteSchulformen(Foerderschwerpunkt_ID=" + this.Foerderschwerpunkt_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ")";
	}

}