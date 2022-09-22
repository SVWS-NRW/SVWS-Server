package de.nrw.schule.svws.db.dto.rev8.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Haltestelle.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Haltestelle")
@NamedQuery(name="Rev8DTOHaltestellen.all", query="SELECT e FROM Rev8DTOHaltestellen e")
@NamedQuery(name="Rev8DTOHaltestellen.id", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOHaltestellen.id.multiple", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOHaltestellen.bezeichnung", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOHaltestellen.bezeichnung.multiple", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOHaltestellen.sortierung", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOHaltestellen.sortierung.multiple", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOHaltestellen.sichtbar", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev8DTOHaltestellen.sichtbar.multiple", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev8DTOHaltestellen.aenderbar", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.Aenderbar = :value")
@NamedQuery(name="Rev8DTOHaltestellen.aenderbar.multiple", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.Aenderbar IN :value")
@NamedQuery(name="Rev8DTOHaltestellen.entfernungschule", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.EntfernungSchule = :value")
@NamedQuery(name="Rev8DTOHaltestellen.entfernungschule.multiple", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.EntfernungSchule IN :value")
@NamedQuery(name="Rev8DTOHaltestellen.primaryKeyQuery", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOHaltestellen.all.migration", query="SELECT e FROM Rev8DTOHaltestellen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar","EntfernungSchule"})
public class Rev8DTOHaltestellen {

	/** ID der Haltestelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Haltestelle */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Haltestelle */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Haltestelle */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Haltestelle */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Enterfung zur Schule von der Haltestelle */
	@Column(name = "EntfernungSchule")
	@JsonProperty
	public Double EntfernungSchule;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOHaltestellen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOHaltestellen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOHaltestellen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public Rev8DTOHaltestellen(final Long ID, final String Bezeichnung) {
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
		Rev8DTOHaltestellen other = (Rev8DTOHaltestellen) obj;
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
		return "Rev8DTOHaltestellen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", EntfernungSchule=" + this.EntfernungSchule + ")";
	}

}