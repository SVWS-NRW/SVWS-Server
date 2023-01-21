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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Schulfunktionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Schulfunktionen")
@NamedQuery(name="DevDTOSchulfunktion.all", query="SELECT e FROM DevDTOSchulfunktion e")
@NamedQuery(name="DevDTOSchulfunktion.id", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.ID = :value")
@NamedQuery(name="DevDTOSchulfunktion.id.multiple", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOSchulfunktion.bezeichnung", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOSchulfunktion.bezeichnung.multiple", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOSchulfunktion.sortierung", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOSchulfunktion.sortierung.multiple", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOSchulfunktion.sichtbar", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOSchulfunktion.sichtbar.multiple", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOSchulfunktion.primaryKeyQuery", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOSchulfunktion.all.migration", query="SELECT e FROM DevDTOSchulfunktion e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar"})
public class DevDTOSchulfunktion {

	/** ID der schulinternen Funktion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der schulinternen Funktion */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der schulinternen Funktion */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der schulinternen Funktion */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchulfunktion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchulfunktion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchulfunktion ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DevDTOSchulfunktion(final Long ID) {
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
		DevDTOSchulfunktion other = (DevDTOSchulfunktion) obj;
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
		return "DevDTOSchulfunktion(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ")";
	}

}