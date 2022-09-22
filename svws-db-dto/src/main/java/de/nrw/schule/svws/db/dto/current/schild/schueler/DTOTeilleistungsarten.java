package de.nrw.schule.svws.db.dto.current.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Einzelleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Einzelleistungen")
@NamedQuery(name="DTOTeilleistungsarten.all", query="SELECT e FROM DTOTeilleistungsarten e")
@NamedQuery(name="DTOTeilleistungsarten.id", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.ID = :value")
@NamedQuery(name="DTOTeilleistungsarten.id.multiple", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.ID IN :value")
@NamedQuery(name="DTOTeilleistungsarten.bezeichnung", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOTeilleistungsarten.bezeichnung.multiple", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOTeilleistungsarten.sortierung", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOTeilleistungsarten.sortierung.multiple", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOTeilleistungsarten.sichtbar", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.Sichtbar = :value")
@NamedQuery(name="DTOTeilleistungsarten.sichtbar.multiple", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DTOTeilleistungsarten.gewichtung", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.Gewichtung = :value")
@NamedQuery(name="DTOTeilleistungsarten.gewichtung.multiple", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.Gewichtung IN :value")
@NamedQuery(name="DTOTeilleistungsarten.primaryKeyQuery", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.ID = ?1")
@NamedQuery(name="DTOTeilleistungsarten.all.migration", query="SELECT e FROM DTOTeilleistungsarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Gewichtung"})
public class DTOTeilleistungsarten {

	/** ID der Teilleistung zu den Leistungsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Teilleistung zu den Leistungsdaten */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Teilleistung zu den Leistungsdaten */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Teilleistung zu den Leistungsdaten */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gewichtung der Teilleistung zu den Leistungsdaten */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Double Gewichtung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTeilleistungsarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTeilleistungsarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTeilleistungsarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOTeilleistungsarten(final Long ID) {
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
		DTOTeilleistungsarten other = (DTOTeilleistungsarten) obj;
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
		return "DTOTeilleistungsarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Gewichtung=" + this.Gewichtung + ")";
	}

}