package de.nrw.schule.svws.db.dto.dev.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Schwerpunkt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Schwerpunkt")
@NamedQuery(name="DevDTOSchwerpunkt.all", query="SELECT e FROM DevDTOSchwerpunkt e")
@NamedQuery(name="DevDTOSchwerpunkt.id", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.ID = :value")
@NamedQuery(name="DevDTOSchwerpunkt.id.multiple", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOSchwerpunkt.bezeichnung", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOSchwerpunkt.bezeichnung.multiple", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOSchwerpunkt.sortierung", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOSchwerpunkt.sortierung.multiple", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOSchwerpunkt.sichtbar", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOSchwerpunkt.sichtbar.multiple", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOSchwerpunkt.aenderbar", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.Aenderbar = :value")
@NamedQuery(name="DevDTOSchwerpunkt.aenderbar.multiple", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DevDTOSchwerpunkt.primaryKeyQuery", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOSchwerpunkt.all.migration", query="SELECT e FROM DevDTOSchwerpunkt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar"})
public class DevDTOSchwerpunkt {

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
	 * Erstellt ein neues Objekt der Klasse DevDTOSchwerpunkt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchwerpunkt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchwerpunkt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOSchwerpunkt(final Long ID, final String Bezeichnung) {
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
		DevDTOSchwerpunkt other = (DevDTOSchwerpunkt) obj;
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
		return "DevDTOSchwerpunkt(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}