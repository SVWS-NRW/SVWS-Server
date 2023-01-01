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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Vermerkart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Vermerkart")
@NamedQuery(name="Rev9DTOVermerkArt.all", query="SELECT e FROM Rev9DTOVermerkArt e")
@NamedQuery(name="Rev9DTOVermerkArt.id", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOVermerkArt.id.multiple", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOVermerkArt.bezeichnung", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev9DTOVermerkArt.bezeichnung.multiple", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev9DTOVermerkArt.sortierung", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev9DTOVermerkArt.sortierung.multiple", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev9DTOVermerkArt.sichtbar", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev9DTOVermerkArt.sichtbar.multiple", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev9DTOVermerkArt.aenderbar", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.Aenderbar = :value")
@NamedQuery(name="Rev9DTOVermerkArt.aenderbar.multiple", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.Aenderbar IN :value")
@NamedQuery(name="Rev9DTOVermerkArt.primaryKeyQuery", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOVermerkArt.all.migration", query="SELECT e FROM Rev9DTOVermerkArt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar"})
public class Rev9DTOVermerkArt {

	/** ID der Vermerkart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Vermerkart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Vermerkart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Vermerkart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Vermerkart */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOVermerkArt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOVermerkArt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOVermerkArt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public Rev9DTOVermerkArt(final Long ID, final String Bezeichnung) {
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
		Rev9DTOVermerkArt other = (Rev9DTOVermerkArt) obj;
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
		return "Rev9DTOVermerkArt(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}