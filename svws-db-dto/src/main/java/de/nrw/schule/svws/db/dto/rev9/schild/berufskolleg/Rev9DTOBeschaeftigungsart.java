package de.nrw.schule.svws.db.dto.rev9.schild.berufskolleg;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_BeschaeftigungsArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_BeschaeftigungsArt")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.all", query="SELECT e FROM Rev9DTOBeschaeftigungsart e")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.id", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.id.multiple", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.bezeichnung", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.bezeichnung.multiple", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.sortierung", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.sortierung.multiple", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.sichtbar", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.sichtbar.multiple", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.aenderbar", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.Aenderbar = :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.aenderbar.multiple", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.Aenderbar IN :value")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.primaryKeyQuery", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOBeschaeftigungsart.all.migration", query="SELECT e FROM Rev9DTOBeschaeftigungsart e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar"})
public class Rev9DTOBeschaeftigungsart {

	/** ID der Beschäftigungsart unter weitere Adressen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Beschäftigungsart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Beschäftigungsart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Beschäftigungsart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Beschäftigungsart ist änderbar Ja Nein */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOBeschaeftigungsart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOBeschaeftigungsart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOBeschaeftigungsart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev9DTOBeschaeftigungsart(final Long ID) {
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
		Rev9DTOBeschaeftigungsart other = (Rev9DTOBeschaeftigungsart) obj;
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
		return "Rev9DTOBeschaeftigungsart(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}