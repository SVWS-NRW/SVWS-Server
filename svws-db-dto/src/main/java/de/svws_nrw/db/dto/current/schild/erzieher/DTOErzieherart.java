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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_ErzieherArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_ErzieherArt")
@NamedQuery(name = "DTOErzieherart.all", query = "SELECT e FROM DTOErzieherart e")
@NamedQuery(name = "DTOErzieherart.id", query = "SELECT e FROM DTOErzieherart e WHERE e.ID = :value")
@NamedQuery(name = "DTOErzieherart.id.multiple", query = "SELECT e FROM DTOErzieherart e WHERE e.ID IN :value")
@NamedQuery(name = "DTOErzieherart.bezeichnung", query = "SELECT e FROM DTOErzieherart e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOErzieherart.bezeichnung.multiple", query = "SELECT e FROM DTOErzieherart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOErzieherart.sortierung", query = "SELECT e FROM DTOErzieherart e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOErzieherart.sortierung.multiple", query = "SELECT e FROM DTOErzieherart e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOErzieherart.sichtbar", query = "SELECT e FROM DTOErzieherart e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOErzieherart.sichtbar.multiple", query = "SELECT e FROM DTOErzieherart e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOErzieherart.aenderbar", query = "SELECT e FROM DTOErzieherart e WHERE e.Aenderbar = :value")
@NamedQuery(name = "DTOErzieherart.aenderbar.multiple", query = "SELECT e FROM DTOErzieherart e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "DTOErzieherart.exportbez", query = "SELECT e FROM DTOErzieherart e WHERE e.ExportBez = :value")
@NamedQuery(name = "DTOErzieherart.exportbez.multiple", query = "SELECT e FROM DTOErzieherart e WHERE e.ExportBez IN :value")
@NamedQuery(name = "DTOErzieherart.primaryKeyQuery", query = "SELECT e FROM DTOErzieherart e WHERE e.ID = ?1")
@NamedQuery(name = "DTOErzieherart.all.migration", query = "SELECT e FROM DTOErzieherart e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sortierung", "Sichtbar", "Aenderbar", "ExportBez"})
public final class DTOErzieherart {

	/** ID der Erzieherart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung der Erzieherart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Erzieherart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Erzieherart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Erzieherart */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Exportbezeichnung der Erzieherart */
	@Column(name = "ExportBez")
	@JsonProperty
	public String ExportBez;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOErzieherart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOErzieherart(final long ID, final String Bezeichnung) {
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
		DTOErzieherart other = (DTOErzieherart) obj;
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
		return "DTOErzieherart(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", ExportBez=" + this.ExportBez + ")";
	}

}
