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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Kursart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Kursart")
@NamedQuery(name="DevDTOKursarten.all", query="SELECT e FROM DevDTOKursarten e")
@NamedQuery(name="DevDTOKursarten.id", query="SELECT e FROM DevDTOKursarten e WHERE e.ID = :value")
@NamedQuery(name="DevDTOKursarten.id.multiple", query="SELECT e FROM DevDTOKursarten e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOKursarten.bezeichnung", query="SELECT e FROM DevDTOKursarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOKursarten.bezeichnung.multiple", query="SELECT e FROM DevDTOKursarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOKursarten.internbez", query="SELECT e FROM DevDTOKursarten e WHERE e.InternBez = :value")
@NamedQuery(name="DevDTOKursarten.internbez.multiple", query="SELECT e FROM DevDTOKursarten e WHERE e.InternBez IN :value")
@NamedQuery(name="DevDTOKursarten.kursart", query="SELECT e FROM DevDTOKursarten e WHERE e.Kursart = :value")
@NamedQuery(name="DevDTOKursarten.kursart.multiple", query="SELECT e FROM DevDTOKursarten e WHERE e.Kursart IN :value")
@NamedQuery(name="DevDTOKursarten.kursartallg", query="SELECT e FROM DevDTOKursarten e WHERE e.KursartAllg = :value")
@NamedQuery(name="DevDTOKursarten.kursartallg.multiple", query="SELECT e FROM DevDTOKursarten e WHERE e.KursartAllg IN :value")
@NamedQuery(name="DevDTOKursarten.sortierung", query="SELECT e FROM DevDTOKursarten e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOKursarten.sortierung.multiple", query="SELECT e FROM DevDTOKursarten e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOKursarten.sichtbar", query="SELECT e FROM DevDTOKursarten e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOKursarten.sichtbar.multiple", query="SELECT e FROM DevDTOKursarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOKursarten.aenderbar", query="SELECT e FROM DevDTOKursarten e WHERE e.Aenderbar = :value")
@NamedQuery(name="DevDTOKursarten.aenderbar.multiple", query="SELECT e FROM DevDTOKursarten e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DevDTOKursarten.primaryKeyQuery", query="SELECT e FROM DevDTOKursarten e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOKursarten.all.migration", query="SELECT e FROM DevDTOKursarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","InternBez","Kursart","KursartAllg","Sortierung","Sichtbar","Aenderbar"})
public class DevDTOKursarten {

	/** ID des Kursarteneintrag */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung des Kursarteneintrags IT.NRW */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Interne Bezeichnung Kursarteneintrag */
	@Column(name = "InternBez")
	@JsonProperty
	public String InternBez;

	/** Kürzel Kursart */
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Allgemeine Bezeichnung Kursart (zB GK bei GKM) */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Sortierung der Kursart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Kursart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Kursart änderbar Ja Nein */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKursarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOKursarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKursarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DevDTOKursarten(final Long ID) {
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
		DevDTOKursarten other = (DevDTOKursarten) obj;
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
		return "DevDTOKursarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", InternBez=" + this.InternBez + ", Kursart=" + this.Kursart + ", KursartAllg=" + this.KursartAllg + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}