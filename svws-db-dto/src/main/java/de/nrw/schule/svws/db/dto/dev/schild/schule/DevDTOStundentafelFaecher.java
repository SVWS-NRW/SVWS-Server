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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundentafel_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundentafel_Faecher")
@NamedQuery(name="DevDTOStundentafelFaecher.all", query="SELECT e FROM DevDTOStundentafelFaecher e")
@NamedQuery(name="DevDTOStundentafelFaecher.id", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.ID = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.id.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.stundentafel_id", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Stundentafel_ID = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.stundentafel_id.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Stundentafel_ID IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.fach_id", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Fach_ID = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.fach_id.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.kursartallg", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.KursartAllg = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.kursartallg.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.KursartAllg IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.wochenstd", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.WochenStd = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.wochenstd.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.WochenStd IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.lehrer_id", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.lehrer_id.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.epochenunterricht", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.EpochenUnterricht = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.epochenunterricht.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.EpochenUnterricht IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.sortierung", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.sortierung.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.sichtbar", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.sichtbar.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.gewichtung", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Gewichtung = :value")
@NamedQuery(name="DevDTOStundentafelFaecher.gewichtung.multiple", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.Gewichtung IN :value")
@NamedQuery(name="DevDTOStundentafelFaecher.primaryKeyQuery", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOStundentafelFaecher.all.migration", query="SELECT e FROM DevDTOStundentafelFaecher e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Stundentafel_ID","Fach_ID","KursartAllg","WochenStd","Lehrer_ID","EpochenUnterricht","Sortierung","Sichtbar","Gewichtung"})
public class DevDTOStundentafelFaecher {

	/** ID des Facheintrags für die Stundentafel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der zugehörigen Stundentafel */
	@Column(name = "Stundentafel_ID")
	@JsonProperty
	public Long Stundentafel_ID;

	/** FachID das in der Stundentafel ist */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Kursart des Faches in der Stundentafel */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Wochenstunde des Faches in der Stundentafel */
	@Column(name = "WochenStd")
	@JsonProperty
	public Integer WochenStd;

	/** Lehrer-ID des unterrichtenden Lehrers für das Fach der Stundentafel */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Merkmal Epochenunterricht des Faches in der Stundentafel */
	@Column(name = "EpochenUnterricht")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean EpochenUnterricht;

	/** Sortierung des Faches in der Stundentafel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit des Faches in der Stundentafel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** ??? entweder BB auch oder weg ??? Gewichtung allgemeinbidend BK  des Faches in der Stundentafel */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOStundentafelFaecher ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOStundentafelFaecher() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOStundentafelFaecher ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundentafel_ID   der Wert für das Attribut Stundentafel_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DevDTOStundentafelFaecher(final Long ID, final Long Stundentafel_ID, final Long Fach_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Stundentafel_ID == null) { 
			throw new NullPointerException("Stundentafel_ID must not be null");
		}
		this.Stundentafel_ID = Stundentafel_ID;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOStundentafelFaecher other = (DevDTOStundentafelFaecher) obj;
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
		return "DevDTOStundentafelFaecher(ID=" + this.ID + ", Stundentafel_ID=" + this.Stundentafel_ID + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", WochenStd=" + this.WochenStd + ", Lehrer_ID=" + this.Lehrer_ID + ", EpochenUnterricht=" + this.EpochenUnterricht + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Gewichtung=" + this.Gewichtung + ")";
	}

}