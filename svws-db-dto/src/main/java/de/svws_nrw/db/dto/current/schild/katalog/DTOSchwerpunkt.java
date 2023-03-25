package de.svws_nrw.db.dto.current.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Schwerpunkt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Schwerpunkt")
@NamedQuery(name="DTOSchwerpunkt.all", query="SELECT e FROM DTOSchwerpunkt e")
@NamedQuery(name="DTOSchwerpunkt.id", query="SELECT e FROM DTOSchwerpunkt e WHERE e.ID = :value")
@NamedQuery(name="DTOSchwerpunkt.id.multiple", query="SELECT e FROM DTOSchwerpunkt e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchwerpunkt.bezeichnung", query="SELECT e FROM DTOSchwerpunkt e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOSchwerpunkt.bezeichnung.multiple", query="SELECT e FROM DTOSchwerpunkt e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOSchwerpunkt.sortierung", query="SELECT e FROM DTOSchwerpunkt e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOSchwerpunkt.sortierung.multiple", query="SELECT e FROM DTOSchwerpunkt e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOSchwerpunkt.sichtbar", query="SELECT e FROM DTOSchwerpunkt e WHERE e.Sichtbar = :value")
@NamedQuery(name="DTOSchwerpunkt.sichtbar.multiple", query="SELECT e FROM DTOSchwerpunkt e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DTOSchwerpunkt.aenderbar", query="SELECT e FROM DTOSchwerpunkt e WHERE e.Aenderbar = :value")
@NamedQuery(name="DTOSchwerpunkt.aenderbar.multiple", query="SELECT e FROM DTOSchwerpunkt e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DTOSchwerpunkt.primaryKeyQuery", query="SELECT e FROM DTOSchwerpunkt e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchwerpunkt.all.migration", query="SELECT e FROM DTOSchwerpunkt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar"})
public class DTOSchwerpunkt {

	/** ID des Schwerpunktes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung des Schwerpunktes */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung des Schwerpunktes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit des Schwerpunktes */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Schwerpunktes */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchwerpunkt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchwerpunkt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchwerpunkt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOSchwerpunkt(final Long ID, final String Bezeichnung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchwerpunkt other = (DTOSchwerpunkt) obj;
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
		return "DTOSchwerpunkt(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}