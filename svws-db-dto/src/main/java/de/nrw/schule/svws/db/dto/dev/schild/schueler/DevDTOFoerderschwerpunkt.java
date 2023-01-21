package de.nrw.schule.svws.db.dto.dev.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Foerderschwerpunkt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Foerderschwerpunkt")
@NamedQuery(name="DevDTOFoerderschwerpunkt.all", query="SELECT e FROM DevDTOFoerderschwerpunkt e")
@NamedQuery(name="DevDTOFoerderschwerpunkt.id", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.ID = :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.id.multiple", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.bezeichnung", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.bezeichnung.multiple", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.statistikkrz", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.StatistikKrz = :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.statistikkrz.multiple", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.StatistikKrz IN :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.sortierung", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.sortierung.multiple", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.sichtbar", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.sichtbar.multiple", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.aenderbar", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.Aenderbar = :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.aenderbar.multiple", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DevDTOFoerderschwerpunkt.primaryKeyQuery", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOFoerderschwerpunkt.all.migration", query="SELECT e FROM DevDTOFoerderschwerpunkt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","StatistikKrz","Sortierung","Sichtbar","Aenderbar"})
public class DevDTOFoerderschwerpunkt {

	/** ID des Förderschwerpunktes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung des Förderschwerpunktes */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Statistikkürzel des Förderschwerpunktes */
	@Column(name = "StatistikKrz")
	@JsonProperty
	public String StatistikKrz;

	/** Sortierung des Förderschwerpunktes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit des Förderschwerpunktes */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Förderschwerpunktes */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOFoerderschwerpunkt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOFoerderschwerpunkt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOFoerderschwerpunkt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOFoerderschwerpunkt(final Long ID, final String Bezeichnung) {
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
		DevDTOFoerderschwerpunkt other = (DevDTOFoerderschwerpunkt) obj;
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
		return "DevDTOFoerderschwerpunkt(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", StatistikKrz=" + this.StatistikKrz + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}