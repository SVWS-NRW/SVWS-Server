package de.nrw.schule.svws.db.dto.dev.schild.erzieher;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_ErzieherArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_ErzieherArt")
@NamedQuery(name="DevDTOErzieherart.all", query="SELECT e FROM DevDTOErzieherart e")
@NamedQuery(name="DevDTOErzieherart.id", query="SELECT e FROM DevDTOErzieherart e WHERE e.ID = :value")
@NamedQuery(name="DevDTOErzieherart.id.multiple", query="SELECT e FROM DevDTOErzieherart e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOErzieherart.bezeichnung", query="SELECT e FROM DevDTOErzieherart e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOErzieherart.bezeichnung.multiple", query="SELECT e FROM DevDTOErzieherart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOErzieherart.sortierung", query="SELECT e FROM DevDTOErzieherart e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOErzieherart.sortierung.multiple", query="SELECT e FROM DevDTOErzieherart e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOErzieherart.sichtbar", query="SELECT e FROM DevDTOErzieherart e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOErzieherart.sichtbar.multiple", query="SELECT e FROM DevDTOErzieherart e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOErzieherart.aenderbar", query="SELECT e FROM DevDTOErzieherart e WHERE e.Aenderbar = :value")
@NamedQuery(name="DevDTOErzieherart.aenderbar.multiple", query="SELECT e FROM DevDTOErzieherart e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DevDTOErzieherart.exportbez", query="SELECT e FROM DevDTOErzieherart e WHERE e.ExportBez = :value")
@NamedQuery(name="DevDTOErzieherart.exportbez.multiple", query="SELECT e FROM DevDTOErzieherart e WHERE e.ExportBez IN :value")
@NamedQuery(name="DevDTOErzieherart.primaryKeyQuery", query="SELECT e FROM DevDTOErzieherart e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOErzieherart.all.migration", query="SELECT e FROM DevDTOErzieherart e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar","ExportBez"})
public class DevDTOErzieherart {

	/** ID der Erzieherart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Erzieherart */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Exportbezeichnung der Erzieherart */
	@Column(name = "ExportBez")
	@JsonProperty
	public String ExportBez;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOErzieherart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOErzieherart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOErzieherart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOErzieherart(final Long ID, final String Bezeichnung) {
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
		DevDTOErzieherart other = (DevDTOErzieherart) obj;
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
		return "DevDTOErzieherart(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", ExportBez=" + this.ExportBez + ")";
	}

}