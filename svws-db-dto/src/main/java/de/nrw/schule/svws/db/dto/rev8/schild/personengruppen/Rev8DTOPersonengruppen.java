package de.nrw.schule.svws.db.dto.rev8.schild.personengruppen;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Personengruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Personengruppen")
@NamedQuery(name="Rev8DTOPersonengruppen.all", query="SELECT e FROM Rev8DTOPersonengruppen e")
@NamedQuery(name="Rev8DTOPersonengruppen.id", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOPersonengruppen.id.multiple", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOPersonengruppen.gruppenname", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.Gruppenname = :value")
@NamedQuery(name="Rev8DTOPersonengruppen.gruppenname.multiple", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.Gruppenname IN :value")
@NamedQuery(name="Rev8DTOPersonengruppen.zusatzinfo", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.Zusatzinfo = :value")
@NamedQuery(name="Rev8DTOPersonengruppen.zusatzinfo.multiple", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.Zusatzinfo IN :value")
@NamedQuery(name="Rev8DTOPersonengruppen.sammelemail", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.SammelEmail = :value")
@NamedQuery(name="Rev8DTOPersonengruppen.sammelemail.multiple", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.SammelEmail IN :value")
@NamedQuery(name="Rev8DTOPersonengruppen.gruppenart", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.GruppenArt = :value")
@NamedQuery(name="Rev8DTOPersonengruppen.gruppenart.multiple", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.GruppenArt IN :value")
@NamedQuery(name="Rev8DTOPersonengruppen.xmlexport", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.XMLExport = :value")
@NamedQuery(name="Rev8DTOPersonengruppen.xmlexport.multiple", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.XMLExport IN :value")
@NamedQuery(name="Rev8DTOPersonengruppen.sortierung", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOPersonengruppen.sortierung.multiple", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOPersonengruppen.sichtbar", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev8DTOPersonengruppen.sichtbar.multiple", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev8DTOPersonengruppen.primaryKeyQuery", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOPersonengruppen.all.migration", query="SELECT e FROM Rev8DTOPersonengruppen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Gruppenname","Zusatzinfo","SammelEmail","GruppenArt","XMLExport","Sortierung","Sichtbar"})
public class Rev8DTOPersonengruppen {

	/** ID der Personengruppe */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Gruppenname der Personengruppe */
	@Column(name = "Gruppenname")
	@JsonProperty
	public String Gruppenname;

	/** Zusatzinfo der Personengruppe */
	@Column(name = "Zusatzinfo")
	@JsonProperty
	public String Zusatzinfo;

	/** Sammel-E-Mail-Adresse der Personengruppe */
	@Column(name = "SammelEmail")
	@JsonProperty
	public String SammelEmail;

	/** Gruppenart  der Personengruppe */
	@Column(name = "GruppenArt")
	@JsonProperty
	public String GruppenArt;

	/** Steuert den LogineoXML-Export */
	@Column(name = "XMLExport")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean XMLExport;

	/** Sortierung der Personengruppe */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Personengruppe */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOPersonengruppen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOPersonengruppen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOPersonengruppen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Gruppenname   der Wert für das Attribut Gruppenname
	 */
	public Rev8DTOPersonengruppen(final Long ID, final String Gruppenname) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Gruppenname == null) { 
			throw new NullPointerException("Gruppenname must not be null");
		}
		this.Gruppenname = Gruppenname;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOPersonengruppen other = (Rev8DTOPersonengruppen) obj;
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
		return "Rev8DTOPersonengruppen(ID=" + this.ID + ", Gruppenname=" + this.Gruppenname + ", Zusatzinfo=" + this.Zusatzinfo + ", SammelEmail=" + this.SammelEmail + ", GruppenArt=" + this.GruppenArt + ", XMLExport=" + this.XMLExport + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ")";
	}

}