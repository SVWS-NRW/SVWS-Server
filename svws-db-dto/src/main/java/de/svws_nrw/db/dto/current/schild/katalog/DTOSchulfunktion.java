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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Schulfunktionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Schulfunktionen")
@NamedQuery(name = "DTOSchulfunktion.all", query = "SELECT e FROM DTOSchulfunktion e")
@NamedQuery(name = "DTOSchulfunktion.id", query = "SELECT e FROM DTOSchulfunktion e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchulfunktion.id.multiple", query = "SELECT e FROM DTOSchulfunktion e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchulfunktion.bezeichnung", query = "SELECT e FROM DTOSchulfunktion e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOSchulfunktion.bezeichnung.multiple", query = "SELECT e FROM DTOSchulfunktion e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOSchulfunktion.sortierung", query = "SELECT e FROM DTOSchulfunktion e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOSchulfunktion.sortierung.multiple", query = "SELECT e FROM DTOSchulfunktion e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOSchulfunktion.sichtbar", query = "SELECT e FROM DTOSchulfunktion e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOSchulfunktion.sichtbar.multiple", query = "SELECT e FROM DTOSchulfunktion e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOSchulfunktion.primaryKeyQuery", query = "SELECT e FROM DTOSchulfunktion e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchulfunktion.all.migration", query = "SELECT e FROM DTOSchulfunktion e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sortierung", "Sichtbar"})
public final class DTOSchulfunktion {

	/** ID der schulinternen Funktion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulfunktion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulfunktion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulfunktion ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOSchulfunktion(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchulfunktion other = (DTOSchulfunktion) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchulfunktion(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ")";
	}

}
