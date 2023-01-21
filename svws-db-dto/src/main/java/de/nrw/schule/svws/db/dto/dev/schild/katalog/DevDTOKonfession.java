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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Religion.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Religion")
@NamedQuery(name="DevDTOKonfession.all", query="SELECT e FROM DevDTOKonfession e")
@NamedQuery(name="DevDTOKonfession.id", query="SELECT e FROM DevDTOKonfession e WHERE e.ID = :value")
@NamedQuery(name="DevDTOKonfession.id.multiple", query="SELECT e FROM DevDTOKonfession e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOKonfession.bezeichnung", query="SELECT e FROM DevDTOKonfession e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOKonfession.bezeichnung.multiple", query="SELECT e FROM DevDTOKonfession e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOKonfession.statistikkrz", query="SELECT e FROM DevDTOKonfession e WHERE e.StatistikKrz = :value")
@NamedQuery(name="DevDTOKonfession.statistikkrz.multiple", query="SELECT e FROM DevDTOKonfession e WHERE e.StatistikKrz IN :value")
@NamedQuery(name="DevDTOKonfession.sortierung", query="SELECT e FROM DevDTOKonfession e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOKonfession.sortierung.multiple", query="SELECT e FROM DevDTOKonfession e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOKonfession.sichtbar", query="SELECT e FROM DevDTOKonfession e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOKonfession.sichtbar.multiple", query="SELECT e FROM DevDTOKonfession e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOKonfession.aenderbar", query="SELECT e FROM DevDTOKonfession e WHERE e.Aenderbar = :value")
@NamedQuery(name="DevDTOKonfession.aenderbar.multiple", query="SELECT e FROM DevDTOKonfession e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DevDTOKonfession.exportbez", query="SELECT e FROM DevDTOKonfession e WHERE e.ExportBez = :value")
@NamedQuery(name="DevDTOKonfession.exportbez.multiple", query="SELECT e FROM DevDTOKonfession e WHERE e.ExportBez IN :value")
@NamedQuery(name="DevDTOKonfession.zeugnisbezeichnung", query="SELECT e FROM DevDTOKonfession e WHERE e.ZeugnisBezeichnung = :value")
@NamedQuery(name="DevDTOKonfession.zeugnisbezeichnung.multiple", query="SELECT e FROM DevDTOKonfession e WHERE e.ZeugnisBezeichnung IN :value")
@NamedQuery(name="DevDTOKonfession.primaryKeyQuery", query="SELECT e FROM DevDTOKonfession e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOKonfession.all.migration", query="SELECT e FROM DevDTOKonfession e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","StatistikKrz","Sortierung","Sichtbar","Aenderbar","ExportBez","ZeugnisBezeichnung"})
public class DevDTOKonfession {

	/** ID der Religion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Religion */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Statistikkürzel der Religion */
	@Column(name = "StatistikKrz")
	@JsonProperty
	public String StatistikKrz;

	/** Sortierung der Religion */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Religion */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Religion */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Exportbezeichnung der Religion */
	@Column(name = "ExportBez")
	@JsonProperty
	public String ExportBez;

	/** Zeugnisbezeichnung der Religion */
	@Column(name = "ZeugnisBezeichnung")
	@JsonProperty
	public String ZeugnisBezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKonfession ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOKonfession() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKonfession ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOKonfession(final Long ID, final String Bezeichnung) {
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
		DevDTOKonfession other = (DevDTOKonfession) obj;
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
		return "DevDTOKonfession(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", StatistikKrz=" + this.StatistikKrz + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", ExportBez=" + this.ExportBez + ", ZeugnisBezeichnung=" + this.ZeugnisBezeichnung + ")";
	}

}