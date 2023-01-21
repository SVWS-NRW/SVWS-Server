package de.nrw.schule.svws.db.dto.dev.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Schulformen")
@NamedQuery(name="DevDTOSchulformen.all", query="SELECT e FROM DevDTOSchulformen e")
@NamedQuery(name="DevDTOSchulformen.id", query="SELECT e FROM DevDTOSchulformen e WHERE e.ID = :value")
@NamedQuery(name="DevDTOSchulformen.id.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOSchulformen.sgl", query="SELECT e FROM DevDTOSchulformen e WHERE e.SGL = :value")
@NamedQuery(name="DevDTOSchulformen.sgl.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.SGL IN :value")
@NamedQuery(name="DevDTOSchulformen.sf_sgl", query="SELECT e FROM DevDTOSchulformen e WHERE e.SF_SGL = :value")
@NamedQuery(name="DevDTOSchulformen.sf_sgl.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.SF_SGL IN :value")
@NamedQuery(name="DevDTOSchulformen.schulform", query="SELECT e FROM DevDTOSchulformen e WHERE e.Schulform = :value")
@NamedQuery(name="DevDTOSchulformen.schulform.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.Schulform IN :value")
@NamedQuery(name="DevDTOSchulformen.doppelqualifikation", query="SELECT e FROM DevDTOSchulformen e WHERE e.DoppelQualifikation = :value")
@NamedQuery(name="DevDTOSchulformen.doppelqualifikation.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.DoppelQualifikation IN :value")
@NamedQuery(name="DevDTOSchulformen.sortierung", query="SELECT e FROM DevDTOSchulformen e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOSchulformen.sortierung.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOSchulformen.sichtbar", query="SELECT e FROM DevDTOSchulformen e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOSchulformen.sichtbar.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOSchulformen.bkindex", query="SELECT e FROM DevDTOSchulformen e WHERE e.BKIndex = :value")
@NamedQuery(name="DevDTOSchulformen.bkindex.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.BKIndex IN :value")
@NamedQuery(name="DevDTOSchulformen.schulform2", query="SELECT e FROM DevDTOSchulformen e WHERE e.Schulform2 = :value")
@NamedQuery(name="DevDTOSchulformen.schulform2.multiple", query="SELECT e FROM DevDTOSchulformen e WHERE e.Schulform2 IN :value")
@NamedQuery(name="DevDTOSchulformen.primaryKeyQuery", query="SELECT e FROM DevDTOSchulformen e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOSchulformen.all.migration", query="SELECT e FROM DevDTOSchulformen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","SGL","SF_SGL","Schulform","DoppelQualifikation","Sortierung","Sichtbar","BKIndex","Schulform2"})
public class DevDTOSchulformen {

	/** ID der Schulgliederung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schulgliedererung die an der Schule vorkommt */
	@Column(name = "SGL")
	@JsonProperty
	public String SGL;

	/** Statistikkürzel SchulformSchulgliederung */
	@Column(name = "SF_SGL")
	@JsonProperty
	public String SF_SGL;

	/** Schulform der SGL */
	@Column(name = "Schulform")
	@JsonProperty
	public String Schulform;

	/** Gibt an, ob am Berufskolleg die SGL mit Doppelqualifikation abgeschlossen werden kann */
	@Column(name = "DoppelQualifikation")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean DoppelQualifikation;

	/** Sortierung der SGL */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der SGL */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** BKIndex zur SGL (IT.NRW) */
	@Column(name = "BKIndex")
	@JsonProperty
	public Integer BKIndex;

	/** Schulform2 zur SGL */
	@Column(name = "Schulform2")
	@JsonProperty
	public String Schulform2;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchulformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DevDTOSchulformen(final Long ID) {
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
		DevDTOSchulformen other = (DevDTOSchulformen) obj;
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
		return "DevDTOSchulformen(ID=" + this.ID + ", SGL=" + this.SGL + ", SF_SGL=" + this.SF_SGL + ", Schulform=" + this.Schulform + ", DoppelQualifikation=" + this.DoppelQualifikation + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", BKIndex=" + this.BKIndex + ", Schulform2=" + this.Schulform2 + ")";
	}

}