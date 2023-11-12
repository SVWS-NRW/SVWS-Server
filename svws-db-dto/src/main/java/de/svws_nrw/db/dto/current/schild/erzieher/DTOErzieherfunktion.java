package de.svws_nrw.db.dto.current.schild.erzieher;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_ErzieherFunktion.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_ErzieherFunktion")
@NamedQuery(name = "DTOErzieherfunktion.all", query = "SELECT e FROM DTOErzieherfunktion e")
@NamedQuery(name = "DTOErzieherfunktion.id", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.ID = :value")
@NamedQuery(name = "DTOErzieherfunktion.id.multiple", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.ID IN :value")
@NamedQuery(name = "DTOErzieherfunktion.bezeichnung", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOErzieherfunktion.bezeichnung.multiple", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOErzieherfunktion.sortierung", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOErzieherfunktion.sortierung.multiple", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOErzieherfunktion.sichtbar", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOErzieherfunktion.sichtbar.multiple", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOErzieherfunktion.aenderbar", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.Aenderbar = :value")
@NamedQuery(name = "DTOErzieherfunktion.aenderbar.multiple", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "DTOErzieherfunktion.primaryKeyQuery", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.ID = ?1")
@NamedQuery(name = "DTOErzieherfunktion.primaryKeyQuery.multiple", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.ID IN :value")
@NamedQuery(name = "DTOErzieherfunktion.all.migration", query = "SELECT e FROM DTOErzieherfunktion e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sortierung", "Sichtbar", "Aenderbar"})
public final class DTOErzieherfunktion {

	/** DEPRECATED: ID der Ezieherfunktion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** DEPRECATED: Bezeichnung der Ezieherfunktion */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** DEPRECATED: Sortierung der Ezieherfunktion */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** SDEPRECATED: Sichbarkeit der Ezieherfunktion */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** DEPRECATED: Änderbarkeit der Ezieherfunktion */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherfunktion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOErzieherfunktion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherfunktion ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOErzieherfunktion(final long ID, final String Bezeichnung) {
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOErzieherfunktion other = (DTOErzieherfunktion) obj;
		return ID == other.ID;
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
		return "DTOErzieherfunktion(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}
