package de.nrw.schule.svws.db.dto.rev9.schild.katalog;

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
@NamedQuery(name="Rev9DTOSchwerpunkt.all", query="SELECT e FROM Rev9DTOSchwerpunkt e")
@NamedQuery(name="Rev9DTOSchwerpunkt.id", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.id.multiple", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.bezeichnung", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.bezeichnung.multiple", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.sortierung", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.sortierung.multiple", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.sichtbar", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.sichtbar.multiple", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.aenderbar", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.Aenderbar = :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.aenderbar.multiple", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.Aenderbar IN :value")
@NamedQuery(name="Rev9DTOSchwerpunkt.primaryKeyQuery", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOSchwerpunkt.all.migration", query="SELECT e FROM Rev9DTOSchwerpunkt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar"})
public class Rev9DTOSchwerpunkt {

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
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchwerpunkt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOSchwerpunkt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchwerpunkt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public Rev9DTOSchwerpunkt(final Long ID, final String Bezeichnung) {
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
		Rev9DTOSchwerpunkt other = (Rev9DTOSchwerpunkt) obj;
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
		return "Rev9DTOSchwerpunkt(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}