package de.nrw.schule.svws.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Foerderschwerpunkte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Foerderschwerpunkte")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.all", query="SELECT e FROM DTOAlleFoerderschwerpunkte e")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.id", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.ID = :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.id.multiple", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.ID IN :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.kuerzel", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.Kuerzel = :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.kuerzel.multiple", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.beschreibung", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.beschreibung.multiple", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.gueltigvon", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.gueltigvon.multiple", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.gueltigbis", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.gueltigbis.multiple", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.primaryKeyQuery", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.ID = ?1")
@NamedQuery(name="DTOAlleFoerderschwerpunkte.all.migration", query="SELECT e FROM DTOAlleFoerderschwerpunkte e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Beschreibung","gueltigVon","gueltigBis"})
public class DTOAlleFoerderschwerpunkte {

	/** ID des Förderschwerpunkts */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Kürzel des Förderschwerpunkts */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Beschreibung des Förderschwerpunkts */
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
	 * Erstellt ein neues Objekt der Klasse DTOAlleFoerderschwerpunkte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAlleFoerderschwerpunkte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleFoerderschwerpunkte ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public DTOAlleFoerderschwerpunkte(final Long ID, final String Kuerzel) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
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
		DTOAlleFoerderschwerpunkte other = (DTOAlleFoerderschwerpunkte) obj;
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
		return "DTOAlleFoerderschwerpunkte(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}