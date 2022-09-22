package de.nrw.schule.svws.db.dto.rev8.statkue;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_PLZOrt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_PLZOrt")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.all", query="SELECT e FROM Rev8DTOStatkuePLZOrt e")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.id", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.id.multiple", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.plz", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.PLZ = :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.plz.multiple", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.PLZ IN :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.regschl", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.RegSchl = :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.regschl.multiple", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.RegSchl IN :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.ort", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.Ort = :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.ort.multiple", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.Ort IN :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.sortierung", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.sortierung.multiple", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.gueltigvon", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.gueltigbis", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOStatkuePLZOrt.all.migration", query="SELECT e FROM Rev8DTOStatkuePLZOrt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","PLZ","RegSchl","Ort","Sortierung","gueltigVon","gueltigBis"})
public class Rev8DTOStatkuePLZOrt {

	/** Statkue Tabelle IT.NRW: ID der PLZ und ORT */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Statkue Tabelle IT.NRW: Postleitzahl der PLZ und ORT */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Statkue Tabelle IT.NRW: Regionalschlüssel der PLZ und ORT */
	@Column(name = "RegSchl")
	@JsonProperty
	public String RegSchl;

	/** Statkue Tabelle IT.NRW: Ortsbezeichnung der PLZ und ORT */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Statkue Tabelle IT.NRW: Sortierung */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkuePLZOrt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStatkuePLZOrt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkuePLZOrt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOStatkuePLZOrt(final Long ID) {
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
		Rev8DTOStatkuePLZOrt other = (Rev8DTOStatkuePLZOrt) obj;
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
		return "Rev8DTOStatkuePLZOrt(ID=" + this.ID + ", PLZ=" + this.PLZ + ", RegSchl=" + this.RegSchl + ", Ort=" + this.Ort + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}